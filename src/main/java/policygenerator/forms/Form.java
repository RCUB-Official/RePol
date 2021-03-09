/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policygenerator.forms;

import framework.EventHandler;
import framework.settings.RepolSettings;
import framework.utilities.HttpUtilities;
import framework.utilities.Utilities;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import policygenerator.forms.Trigger.Operation;
import policygenerator.forms.condition.Condition;
import policygenerator.forms.element.FormElement.Type;
import policygenerator.forms.element.*;
import policygenerator.forms.condition.exceptions.ConditionNotFoundException;
import policygenerator.forms.element.exceptions.ElementNotFoundException;
import policygenerator.forms.element.exceptions.IdentifierCollision;
import policygenerator.freemarker.FMHandler;

/**
 *
 * @author vasilije
 */
public final class Form {

    private final String id;
    private String label;
    private String description;

    private final List<Panel> panels;
    private final Map<String, FormElement> elementMap;

    private final List<Condition> conditions;
    private final Map<String, Condition> conditionMap;

    private final List<FormElement> needValidation;

    public Form(String id) {
        this.id = id;
        this.label = null;
        this.description = null;

        this.panels = new LinkedList<Panel>();
        this.elementMap = new HashMap<String, FormElement>();

        this.conditions = new LinkedList<Condition>();
        this.conditionMap = new HashMap<String, Condition>();

        needValidation = new LinkedList<FormElement>();

        Logger.getLogger(Form.class.getName()).log(Level.INFO, FacesContext.getCurrentInstance().getExternalContext().getSessionId(true) + " ("
                + ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr() + ")" + " opened form \"" + id + "\"");
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        if (label != null) {
            return label;
        } else {
            return id;
        }
    }

    void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public List<Panel> getPanels() throws ConditionNotFoundException {
        List<Panel> list = new LinkedList<Panel>();
        list.addAll(panels);
        return list;
    }

    public boolean isValidTemplate() {
        return FMHandler.getInstance().templateExists(id + ".ftlh");
    }

    synchronized void addPanels(List<Panel> panels) throws IdentifierCollision, ConditionNotFoundException {
        this.panels.clear();
        this.elementMap.clear();

        this.panels.addAll(panels);
        for (Panel p : this.panels) {
            for (FormElement fe : p.getElements()) {
                if (fe.getType() != Type.SEPARATOR) {
                    String elementId = fe.getId();
                    if (elementMap.containsKey(elementId)) {
                        throw new IdentifierCollision(elementId, "FormElement");
                    }
                    if (conditionMap.containsKey(elementId)) {
                        throw new IdentifierCollision(elementId, "Condition");
                    }
                    elementMap.put(elementId, fe);

                    if (fe.isMandatory()) {
                        needValidation.add(fe);
                    }
                }
            }
        }
    }

    synchronized void addCondition(Condition condition) throws IdentifierCollision {
        String conditionId = condition.getId();
        if (elementMap.containsKey(conditionId)) {
            throw new IdentifierCollision(conditionId, "FormElement");
        }
        if (conditionMap.containsKey(conditionId)) {
            throw new IdentifierCollision(conditionId, "Condition");
        }
        conditionMap.put(conditionId, condition);
        conditions.add(condition);
    }

    synchronized void clearConditions() {
        conditionMap.clear();
        conditions.clear();
    }

    public void processTrigger(String targetId, Operation operation, String targetValue) throws ElementNotFoundException {
        FormElement fe = getElement(targetId);
        switch (operation) {
            case SET:
                fe.setByTrigger(targetValue);
                break;
            case REMOVE:
                fe.remove(targetValue);
                break;
            case CLEAR:
                fe.clear();
                break;
        }
    }

    public void test() throws ConditionNotFoundException {
        for (Panel p : panels) {
            p.isRendered();
            for (FormElement fe : p.getElements()) {
                for (String cid : fe.getTriggerConditionIds()) {
                    getCondition(cid);
                }
            }
        }
    }

    public FormElement getElement(String id) throws ElementNotFoundException {
        if (!elementMap.containsKey(id)) {
            throw new ElementNotFoundException(id);
        } else {
            return elementMap.get(id);
        }
    }

    public Condition getCondition(String id) throws ConditionNotFoundException {
        if (!conditionMap.containsKey(id)) {
            throw new ConditionNotFoundException(id);
        } else {
            return conditionMap.get(id);
        }
    }

    public Timestamp getCurrentTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    public String getXml() throws ConditionNotFoundException {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n";
        xml += "<embedded-data>";
        for (Panel p : panels) {
            for (FormElement fe : p.getElements()) {
                xml += "\n\t" + fe.getXml();
            }
        }
        xml += "\n</embedded-data>";
        return xml;
    }

    public void generateDocument() throws IOException, TemplateException, ConditionNotFoundException {

        RepolSettings settings = RepolSettings.getInstance();

        Map model = new HashMap();

        model.put("current_time", getCurrentTime());    // Can be overrided by a form element.
        model.put("repol_version", settings.getRepolVersion());
        model.put("repol_url", settings.getRepolUrl());
        model.put("embedded_data", "<!--EMBEDDED_BEGIN\n" + getXml() + "\nEMBEDDED_END-->");

        List<FormElement> elements = new LinkedList<FormElement>();
        for (Panel p : panels) {
            elements.addAll(p.getElements());
        }

        boolean valid = true;

        for (FormElement fe : elements) {
            if (fe.isValid()) {
                Type elementType = fe.getType();
                switch (elementType) {
                    case ONELINE:
                        model.put(fe.getId(), ((OneLine) fe).getValue());
                        break;
                    case TEXT:
                        model.put(fe.getId(), ((Text) fe).getValue());
                        break;
                    case BOOLEAN:
                        model.put(fe.getId(), ((BooleanCheckbox) fe).isValue());
                        break;
                    case INTEGER:
                        model.put(fe.getId(), ((IntegerInput) fe).getValue());
                        break;
                    case DOUBLE:
                        model.put(fe.getId(), ((DoubleInput) fe).getValue());
                        break;
                    case DATE:
                        model.put(fe.getId(), ((DateInput) fe).getValue());
                        break;
                    case ADDLIST:
                        model.put(fe.getId(), ((AddList) fe).getValues());
                        break;
                    case SELECTONE:
                        model.put(fe.getId(), ((SelectOne) fe).getValue());
                        break;
                    case SELECTMANY:
                        model.put(fe.getId(), ((SelectMany) fe).getValues());
                        break;
                    case POOLPICKER:
                        model.put(fe.getId(), ((PoolPicker) fe).getValues());
                        break;
                }
            } else {
                EventHandler.alertUserError("Invalid input", fe.getLabel());
                valid = false;
                break;
            }
        }

        for (Condition c : conditions) {
            model.put(c.getId(), c.evaluate());
        }

        if (valid) {
            Template template = FMHandler.getInstance().getConfiguration().getTemplate(id + ".ftlh");
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
            ec.setResponseContentType("text/html"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "fm.html" + "\""); // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.

            try (OutputStream os = ec.getResponseOutputStream();
                    Writer out = new OutputStreamWriter(os)) {
                template.process(model, out);
                fc.responseComplete();
            }

            Logger.getLogger(Form.class.getName()).log(Level.INFO, FacesContext.getCurrentInstance().getExternalContext().getSessionId(true) + " ("
                    + ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr() + ")" + " generated document");
        }
    }

    public void sync() throws ConditionNotFoundException {
        DataShare myShare = (DataShare) Utilities.getObject("#{dataShare}");
        for (Panel p : panels) {
            for (FormElement fe : p.getElements()) {
                myShare.sync(fe);
            }
        }
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void downloadTemplate() {
        try (InputStream is = FMHandler.getInstance().getInputStream(id)) {
            HttpUtilities.sendFileToClient(is, "text/plain", id + ".ftlh");
            Logger.getLogger(Form.class.getName()).log(Level.INFO, FacesContext.getCurrentInstance().getExternalContext().getSessionId(true) + " ("
                    + ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr() + ")" + " downloaded template");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Form.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Form.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void downloadConfig() {
        try (InputStream is = FormFactory.getInstance().getStream()) {
            HttpUtilities.sendFileToClient(is, "application/xml", "template-forms.xml");
            Logger.getLogger(Form.class.getName()).log(Level.INFO, FacesContext.getCurrentInstance().getExternalContext().getSessionId(true) + " ("
                    + ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr() + ")" + " downloaded config");
        } catch (IOException ex) {
            Logger.getLogger(Form.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
