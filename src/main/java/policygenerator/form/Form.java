package policygenerator.form;

import policygenerator.form.element.input.*;
import policygenerator.form.element.Panel;
import framework.EventHandler;
import framework.cache.Cacheable;
import framework.settings.RepolSettings;
import framework.utilities.HttpUtilities;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import policygenerator.form.trigger.Trigger.Operation;
import policygenerator.form.condition.Condition;
import policygenerator.form.element.input.FormElement.Type;
import policygenerator.form.condition.exceptions.ConditionNotFoundException;
import policygenerator.form.element.ListFactory;
import policygenerator.form.element.exceptions.ElementNotFoundException;
import policygenerator.form.element.exceptions.IdentifierCollision;
import policygenerator.freemarker.FMHandler;
import policygenerator.session.SessionController;

public final class Form implements Cacheable {

    private static final Logger LOG = Logger.getLogger(Form.class.getName());

    private final SessionController mySessionController;

    private final String id;
    private String label;
    private String description;

    private final List<Panel> panels;
    private final Map<String, FormElement> elementMap;
    private final Map<String, String> elementIdAliases;

    private final List<Condition> conditions;
    private final Map<String, Condition> conditionMap;

    private final List<FormElement> needValidation;

    public Form(SessionController sessionController, String id) {

        this.mySessionController = sessionController;
        this.id = id;

        this.label = null;
        this.description = null;

        this.panels = new LinkedList<>();
        this.elementMap = new HashMap<>();
        this.elementIdAliases = new HashMap<>();

        this.conditions = new LinkedList<>();
        this.conditionMap = new HashMap<>();

        needValidation = new LinkedList<>();
    }

    public SessionController getSessionController() {
        return mySessionController;
    }

    @Override
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
        List<Panel> list = new LinkedList<>();
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

                    for (String idAlias : fe.getIdAliases()) {
                        this.elementIdAliases.put(fe.getId(), idAlias);
                    }

                    if (fe.isMandatory() || fe.getValidationRegex() != null) {
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
                fe.clearByTrigger();
                break;
            case RESET:
                fe.resetToDefault();
                break;
        }
    }

    void test() throws ConditionNotFoundException {
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

    public List<Condition> getConditions() {    // Needed for form_specs.xhtml page
        return conditions;
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

    private String getXml() throws ConditionNotFoundException {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n";
        xml += "<embedded-data form=\"" + id + "\">";
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

        List<FormElement> elements = new LinkedList<>();
        for (Panel p : panels) {
            elements.addAll(p.getElements());
        }

        boolean valid = true;

        for (FormElement fe : elements) {
            if (fe.getType() != Type.SEPARATOR) {
                if (fe.isValid()) {
                    if (settings.isUseSafeDefaults()) { // Making sure no null values are passed to FreeMarker
                        Object safeValue = fe.getSafeValue();
                        model.put(fe.getId(), safeValue);
                        for (String aliasId : fe.getIdAliases()) {
                            model.put(aliasId, safeValue);
                        }
                    } else {
                        Object value = fe.getValue();
                        model.put(fe.getId(), value);
                        for (String aliasId : fe.getIdAliases()) {
                            model.put(aliasId, value);
                        }
                    }
                } else {
                    EventHandler.alertUserError("Invalid input", fe.getLabel());
                    valid = false;
                    break;
                }
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
            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "policy.html" + "\""); // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.

            try (OutputStream os = ec.getResponseOutputStream();
                    Writer out = new OutputStreamWriter(os)) {
                template.process(model, out);
                fc.responseComplete();
            }

            mySessionController.getActivityLogger().documentGenerated(id);
        }
    }

    public void sync() throws ConditionNotFoundException {
        for (FormElement fe : elementMap.values()) {
//            System.out.println("Form syncing element " + fe.getId() + " with aliases " + Arrays.toString(fe.getIdAliases().toArray()));
            mySessionController.requestSync(fe);
        }
    }

    public void downloadTemplate() {
        try (InputStream is = FMHandler.getInstance().getInputStream(id)) {
            HttpUtilities.sendFileToClient(is, "text/plain", id + ".ftlh");

            mySessionController.getActivityLogger().downloadedTemplate(id);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    public void downloadConfig() {
        try (InputStream is = FormFactory.getInstance().getStream()) {
            HttpUtilities.sendFileToClient(is, "application/xml", "template-forms.xml");

            mySessionController.getActivityLogger().downloadedConfig();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    public void downloadLists() {
        try (InputStream is = ListFactory.getInstance().getStream()) {
            HttpUtilities.sendFileToClient(is, "application/xml", "selection-lists.xml");

            mySessionController.getActivityLogger().downloadedLists();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    // FORM STATS
    // Completion
    public boolean isComplete() throws ConditionNotFoundException {
        boolean complete = true;
        for (FormElement fe : needValidation) {
            if (!fe.isValid()) {
                complete = false;
                break;
            }
        }
        return complete;
    }

    // Mandatory
    public int getMandatoryFieldCount() throws ConditionNotFoundException {
        int count = 0;
        for (FormElement fe : needValidation) {
            if (fe.isMandatory() && fe.isRendered()) {
                count++;
            }
        }
        return count;
    }

    public int getMandatoryFieldSetCount() throws ConditionNotFoundException {
        int count = 0;
        for (FormElement fe : needValidation) {
            if (fe.isValid() && fe.isMandatory()) {
                count++;
            }
        }
        return count;
    }

    // Relevant
    public int getRelevantFieldCount() throws ConditionNotFoundException {
        int count = 0;
        for (FormElement fe : elementMap.values()) {
            if (fe.getType() != Type.SEPARATOR && fe.isRendered()) {
                count++;
            }
        }
        return count;
    }

    public int getUserSetFieldCount() throws ConditionNotFoundException {
        int count = 0;
        for (FormElement fe : elementMap.values()) {
            if (fe.getType() != Type.SEPARATOR && fe.isRendered() && fe.isUserSet()) {
                count++;
            }
        }
        return count;
    }

    public void userSetAll() throws ConditionNotFoundException {  // When user moves forward ahead of the form
        for (FormElement fe : elementMap.values()) {
            if (fe.isRendered()) {
                fe.setUserSet();
            }
        }
    }

    public boolean isHasConditions() {
        return !conditionMap.isEmpty();
    }

    public boolean isHasTriggers() {
        boolean hasTriggers = false;
        for (FormElement fe : elementMap.values()) {
            if (!fe.getTriggers().isEmpty()) {
                hasTriggers = true;
                break;
            }
        }
        return hasTriggers;
    }

    public String getFormElementRealId(Type aliasType, String alias) {
//        System.out.println("String alias " + alias + " " + " aliasType " + aliasType);
        for (Panel panel : this.panels) {

            for (FormElement element : panel.getElements()) {
//                System.out.println("\t element " + element.getId() + " aliases " + Arrays.toString(element.getIdAliases().toArray(new String[0])));
                if (element.getType().name().equals(aliasType.name()) && element.hasAlias(alias)) {
                    return element.getId();
                }
            }
        }
        return alias;
    }
}
