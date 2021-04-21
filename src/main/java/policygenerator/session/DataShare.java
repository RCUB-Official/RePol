package policygenerator.session;

import framework.utilities.HttpUtilities;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import policygenerator.form.element.input.FormElement;
import policygenerator.form.element.input.FormElementFactory;

public final class DataShare {

    private static final Logger LOG = Logger.getLogger(DataShare.class.getName());

    private final Map<String, FormElement> latestValues = new HashMap<>();

    DataShare() {
    }

    public void reset() {
        latestValues.clear();
    }

    public synchronized void push(FormElement element) {
        FormElement old = latestValues.get(element.getId());
        latestValues.put(element.getId(), element);

        if (element != old && old != null) {
            old.syncElement(element);
        }

    }

    public synchronized void touch(FormElement element) {
        if (!latestValues.containsKey(element.getId())) {
            latestValues.put(element.getId(), element);
        }
    }

    public synchronized void requestSync(FormElement element) {
        if (latestValues.containsKey(element.getId())) {
            if (element != latestValues.get(element.getId())) {
                element.syncElement(latestValues.get(element.getId()));
            }
        }
    }

    public void downloadData() throws IOException {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n";
        xml += "<embedded-data>";

        Iterator<String> iterator = latestValues.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            xml += "\n\t" + latestValues.get(key).getXml(true);
        }
        xml += "\n</embedded-data>";

        HttpUtilities.sendFileToClient(xml, "PolGen-Export-" + System.currentTimeMillis() + ".xml");
    }

    public List<String> processUpload(FileUploadEvent event) {

        List<String> affectedFormIds = new LinkedList<>();

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

            try {
                String formId = ed.getAttributes().getNamedItem("form").getTextContent();
                affectedFormIds.add(formId);   // Uploaded generated document
            } catch (Exception ex) {    // In case there is no form id specified
            }

            Set<String> detectedForms = new HashSet<>();

            NodeList fieldNodes = document.getElementsByTagName("field");
            for (int i = 0; i < fieldNodes.getLength(); i++) {
                Node fNode = fieldNodes.item(i);

                String type = fNode.getAttributes().getNamedItem("type").getTextContent();
                String id = fNode.getAttributes().getNamedItem("id").getTextContent();

                try {
                    String formId = fNode.getAttributes().getNamedItem("form").getTextContent();
                    detectedForms.add(formId);
                } catch (Exception ex) {
                }

                FormElement element = FormElementFactory.getDummyElement(type, id);
                if (element != null) {

                    // Iterating through values of the read element from the uploaded file
                    NodeList valueNodes = fNode.getChildNodes();
                    for (int k = 0; k < valueNodes.getLength(); k++) {
                        if (valueNodes.item(k).getNodeName().equals("value")) {
                            element.setByUpload(valueNodes.item(k).getTextContent());
                        }
                    }

                    if (latestValues.containsKey(id)) { // If it already exists, just sync it with the dummy
                        latestValues.get(id).sync(element);
                    } else {    // if not, put the dummy into the map
                        latestValues.put(id, element);
                    }
                }
            }

            affectedFormIds.addAll(detectedForms);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        return affectedFormIds;
    }

}
