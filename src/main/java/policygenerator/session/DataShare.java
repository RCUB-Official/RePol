package policygenerator.session;

import framework.utilities.HttpUtilities;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import policygenerator.form.FormFactory;
import policygenerator.form.FormHeader;
import policygenerator.form.element.input.FormElement;
import policygenerator.form.element.input.FormElementFactory;

/**
 * Map populated with user provided values of <code>FormElements</code>, i. e. free-marker data-model as map.
 *
 * @author Vasilije
 */
public final class DataShare {

    private static final Logger LOG = Logger.getLogger(DataShare.class.getName());

    private final Map<String, FormElement> latestValues = new HashMap<>();

    DataShare() {
    }

    public void reset() {
        latestValues.clear();
    }

    public synchronized void push(FormElement element) {
        if (element.isIdAliased()) {
            for (String alias : element.getIdAliases()) {
                FormElement oldAliased = latestValues.get(alias);
                latestValues.put(alias, element);

                if (element != oldAliased && oldAliased != null) {
                    oldAliased.syncElement(element);
                }

            }
        }

        FormElement old = latestValues.get(element.getId());
        latestValues.put(element.getId(), element);

        if (element != old && old != null) {
            old.syncElement(element);
        }

    }

    public synchronized void touch(FormElement element) {
        if (element.isIdAliased()) {
            for (String alias : element.getIdAliases()) {
                if (!latestValues.containsKey(alias)) {
                    latestValues.put(alias, element);
                }
            }
        }
        if (!latestValues.containsKey(element.getId())) {
            latestValues.put(element.getId(), element);
        }
    }

    public synchronized void requestSync(FormElement element) {
        if (element.isIdAliased()) {
            for (String alias : element.getIdAliases()) {
                if (latestValues.containsKey(alias)) {
                    if (element != latestValues.get(alias)) {
                        element.syncElement(latestValues.get(alias));
                    }
                }
            }
        }
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
            FormElement formElement = latestValues.get(key);
            xml += "\n\t" + formElement.getXml(true);
            if (formElement.isIdAliased()) {
                for (String xmlForAlias : formElement.getXmlForAliases()) {
                    xml += "\n\t" + xmlForAlias;
                }
            }
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
            Set<String> detectedRealElementIds = new HashSet<>();

            NodeList fieldNodes = document.getElementsByTagName("field");
            for (int i = 0; i < fieldNodes.getLength(); i++) {
                Node fNode = fieldNodes.item(i);

                String type = fNode.getAttributes().getNamedItem("type").getTextContent();
                String id = fNode.getAttributes().getNamedItem("id").getTextContent();

//                NE POSTOJI OVDE
//                String aliases = Objects.nonNull(fNode.getAttributes().getNamedItem("aliases")) ? fNode.getAttributes().getNamedItem("aliases").getTextContent() : null;
//                List<String> aliasIds = new ArrayList<>();
//                if (Objects.nonNull(aliases)) {
//                    aliasIds.addAll(Arrays.asList(aliases.split(FormElementFactory.ALIAS_DELIMITER)));
//                }

                try {
                    String formId = fNode.getAttributes().getNamedItem("form").getTextContent();
                    detectedForms.add(formId);
                    Set<String> formIdsForAlias = FormFactory.getInstance().getFormIdsForAttribute(id);
                    detectedForms.addAll(formIdsForAlias);
                    System.out.print("Form ids for for attribute " + id + " size " + formIdsForAlias.size());
                    for (String formIdForAlias : formIdsForAlias) {
                        System.out.print(" " + formIdForAlias);
                    }
                    System.out.println();
                } catch (Exception ex) {
                }

                Set<String> elementAliases = FormFactory.getInstance().getElementIdsForAlias(id);

                FormElement element = FormElementFactory.getDummyElement(type, id, elementAliases);
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
