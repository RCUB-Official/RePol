package policygenerator.forms;

import framework.utilities.HttpUtilities;
import framework.utilities.Utilities;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.xml.parsers.DocumentBuilderFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
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
import policygenerator.forms.element.Text;

@SessionScoped
@ManagedBean(name = "dataShare", eager = true)
public class DataShare implements Serializable {

    private final Map<String, FormElement> latestValues;
    private final Map<String, List<String>> mandatoryFieldIds;

    private boolean used;

    public DataShare() {
        latestValues = new HashMap<>();
        mandatoryFieldIds = new HashMap<>();
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

    public static DataShare getDataShare() {
        return (DataShare) Utilities.getObject("#{dataShare}");
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

    public void uploadFile(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        try {
            String fileContent = new Scanner(file.getInputStream()).useDelimiter("\\A").next();
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

            Node ed = document.getElementsByTagName("embedded-data").item(0);
            String formId;
            try {
                formId = ed.getAttributes().getNamedItem("form").getTextContent();
            } catch (Exception ex) {
                formId = null;
            }

            NodeList fieldNodes = document.getElementsByTagName("field");
            for (int i = 0; i < fieldNodes.getLength(); i++) {
                Node fNode = fieldNodes.item(i);

                String type = fNode.getAttributes().getNamedItem("type").getTextContent();
                String id = fNode.getAttributes().getNamedItem("id").getTextContent();

                FormElement element;

                switch (type) {
                    case "oneline":
                        element = new OneLine(null, id, false, null, null, null, null);
                        break;
                    case "text":
                        element = new Text(null, id, false, null, null, null, null);
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
                        element = new AddList(null, id, false, null, null, null, null);
                        break;
                    case "selectone":   // AddList can serve as a dummy for any list-like FormElement
                        element = new AddList(null, id, false, null, null, null, null);
                        break;
                    case "selectmany":
                        element = new AddList(null, id, false, null, null, null, null);
                        break;
                    case "poolpicker":
                        element = new AddList(null, id, false, null, null, null, null);
                        break;
                    default:
                        element = null;
                        break;
                }

                if (element != null) {

                    NodeList valueNodes = fNode.getChildNodes();
                    for (int k = 0; k < valueNodes.getLength(); k++) {
                        if (valueNodes.item(k).getNodeName().equals("value")) {
                            element.setByUpload(valueNodes.item(k).getTextContent());
                        }
                    }

                    latestValues.put(id, element);
                }
            }

            used = true;
            ActivityLogger.getActivityLogger().uploadedDocument();

            if (formId != null) {
                if (FormFactory.getInstance().validateFormId(formId)) {
                    ActivityLogger.getActivityLogger().setLastRequestedFormId(formId);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(DataShare.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public int getMandatorySetCount(String formId) {
        int count = 0;
        if (mandatoryFieldIds.containsKey(formId)) {
            for (String mfid : mandatoryFieldIds.get(formId)) {
                if (latestValues.containsKey(mfid)) {
                    if (!latestValues.get(mfid).isEmpty()) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public int getMandatoryTotalCount(String formId) {
        int count = 0;
        if (mandatoryFieldIds.containsKey(formId)) {
            for (String mfid : mandatoryFieldIds.get(formId)) {
                count++;
            }
        }
        return count;
    }

    public boolean isUsed() {
        return used;
    }

}
