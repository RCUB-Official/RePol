/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policygenerator.forms;

import framework.utilities.HttpUtilities;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.Part;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import policygenerator.forms.element.AddList;
import policygenerator.forms.element.BooleanCheckbox;
import policygenerator.forms.element.DateInput;
import policygenerator.forms.element.DoubleInput;
import policygenerator.forms.element.FormElement;
import policygenerator.forms.element.IntegerInput;
import policygenerator.forms.element.OneLine;
import policygenerator.forms.element.PoolPicker;
import policygenerator.forms.element.SelectMany;
import policygenerator.forms.element.SelectOne;
import policygenerator.forms.element.SelectionElement;
import policygenerator.forms.element.Text;

/**
 *
 * @author vasilije
 */
@SessionScoped
@ManagedBean(name = "dataShare", eager = true)
public class DataShare implements Serializable {

    private final Map<String, FormElement> latestValues;
    private final Map<String, List<String>> mandatoryFieldIds;
    private Part rawfile;

    private boolean used;

    public DataShare() {
        latestValues = new HashMap<String, FormElement>();
        mandatoryFieldIds = new HashMap<String, List<String>>();
    }

    @PostConstruct
    public void init() {
        latestValues.clear();
        mandatoryFieldIds.clear();
        for (FormHeader fh : FormFactory.getInstance().getFormHeaders()) {
            mandatoryFieldIds.put(fh.getFormId(), fh.getMandatoryFieldIds());
        }

        used = false;
    }

    public void reset() {
        init();
        ActivityLogger.getActivityLogger().resetValues();
    }

    public synchronized void push(FormElement element) {
        latestValues.put(element.getId(), element);
        used = true;
    }

    public synchronized void touch(FormElement element) {
        if (!latestValues.containsKey(element.getId())) {
            latestValues.put(element.getId(), element);
        }
        used = true;
    }

    public synchronized void requestSync(FormElement element) {
        if (latestValues.containsKey(element.getId())) {
            if (element != latestValues.get(element.getId())) {
                element.syncElement(latestValues.get(element.getId()));
            }
        }
        used = true;
    }

    public void downloadStandalone() throws IOException {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n";
        xml += "<embedded-data>";

        Iterator<String> iterator = latestValues.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            xml += "\n\t" + latestValues.get(key).getXml();
        }
        xml += "\n</embedded-data>";

        HttpUtilities.sendFileToClient(xml, "PolGen-Export-" + System.currentTimeMillis() + ".xml");

        ActivityLogger.getActivityLogger().downloadedStandalone();
    }

    public void uploadFile() {
        try {
            String fileContent = new Scanner(rawfile.getInputStream()).useDelimiter("\\A").next();
            String xml;

            String beginSeparator = "<!--EMBEDDED_BEGIN\n";
            String endSeparator = "\nEMBEDDED_END-->";
            if (fileContent.contains(beginSeparator) && fileContent.contains(endSeparator)) {
                xml = fileContent.substring(fileContent.indexOf(beginSeparator) + beginSeparator.length());
                xml = xml.substring(0, xml.indexOf(endSeparator));

            } else {
                xml = fileContent;
            }

            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
            NodeList fieldNodes = document.getElementsByTagName("field");
            for (int i = 0; i < fieldNodes.getLength(); i++) {
                Node fNode = fieldNodes.item(i);

                String type = fNode.getAttributes().getNamedItem("type").getTextContent();
                String id = fNode.getAttributes().getNamedItem("id").getTextContent();

                FormElement element;

                switch (type) {
                    case "oneline":
                        element = new OneLine(null, id, false, null, null);
                        break;
                    case "text":
                        element = new Text(null, id, false, null, null);
                        break;
                    case "boolean":
                        element = new BooleanCheckbox(null, id, false, null, null);
                        break;
                    case "integer":
                        element = new IntegerInput(null, id, false, null, null);
                        break;
                    case "double":
                        element = new DoubleInput(null, id, false, null, null);
                        break;
                    case "date":
                        element = new DateInput(null, id, false, null, null);
                        break;
                    case "addlist":
                        element = new AddList(null, id, false, null, null);
                        break;
                    case "selectone":
                        element = new SelectOne(null, id, false, null, null);
                        break;
                    case "selectmany":
                        element = new SelectMany(null, id, false, null, null);
                        break;
                    case "poolpicker":
                        element = new PoolPicker(null, id, false, null, null);
                        break;
                    default:
                        element = null;
                        break;
                }

                if (element != null) {
                    List<SelectionElement> list = new LinkedList<SelectionElement>();

                    NodeList valueNodes = fNode.getChildNodes();
                    for (int k = 0; k < valueNodes.getLength(); k++) {
                        if (valueNodes.item(k).getNodeName().equals("value")) {
                            if (type.equals("selectone") || type.equals("selectmany")) { // Form the list first
                                SelectionElement se = new SelectionElement(element, null, valueNodes.item(k).getTextContent());
                                list.add(se);
                            } else { // Set directly
                                element.setByTrigger(valueNodes.item(k).getTextContent());
                            }
                        }
                    }

                    // Handling SELECTONE and SELECTMANY
                    if (type.equals("selectone") || type.equals("selectmany")) {
                        if (type.equals("selectone")) {
                            ((SelectOne) element).setAvailableValues(list);
                        }
                        if (type.equals("selectmany")) {
                            ((SelectMany) element).setAvailableValues(list);
                        }
                        for (int k = 0; k < valueNodes.getLength(); k++) {
                            if (valueNodes.item(k).getNodeName().equals("value")) {
                                element.setByTrigger(valueNodes.item(k).getTextContent());
                            }
                        }
                    }

                    latestValues.put(id, element);
                }
            }

            used = true;
            ActivityLogger.getActivityLogger().uploadedDocument();
        } catch (Exception ex) {
            Logger.getLogger(DataShare.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Part getRawfile() {
        return rawfile;
    }

    public void setRawfile(Part rawfile) {
        this.rawfile = rawfile;
    }

    public int getProgress(String formId) {
        if (mandatoryFieldIds.containsKey(formId)) {
            int completed = 0;

            for (String mfid : mandatoryFieldIds.get(formId)) {
                if (latestValues.containsKey(mfid)) {
                    if (!latestValues.get(mfid).isEmpty()) {
                        completed++;
                    }
                }
            }

            int total = mandatoryFieldIds.get(formId).size();
            if (total == 0) {
                return 100;
            } else {
                return 100 * completed / total;
            }
        } else {
            return 0;
        }
    }

    public boolean isUsed() {
        return used;
    }

}
