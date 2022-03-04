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
        System.out.println("JOVANA DataShare.push " + element.getId() + " " + element.getValue() + ": " + Arrays.toString(element.getIdAliases().toArray()));
//        if (element.isIdAliased()) {
            for (String alias : element.getIdAliases()) {
                FormElement oldAliased = latestValues.get(alias);
                latestValues.put(alias, element);

                if (element != oldAliased && oldAliased != null) {
                    oldAliased.syncElement(element);
                }

            }
//        }

        FormElement old = latestValues.get(element.getId());
        latestValues.put(element.getId(), element);

        if (element != old && old != null) {
            old.syncElement(element);
        }
        System.out.println("JOVANA DataShare.push kraj" + element.getId() + " " + element.getValue() + ": " + Arrays.toString(element.getIdAliases().toArray()));

    }

    public synchronized void push(String aliasId, FormElement element) {
        System.out.println("JOVANA DataShare.push " + aliasId + " (id " + element.getId() + ") " + element.getValue() + ": " + Arrays.toString(element.getIdAliases().toArray()));
//        if (element.isIdAliased()) {
        for (String alias : element.getIdAliases()) {
            FormElement oldAliased = latestValues.get(alias);
            latestValues.put(alias, element);

            if (element != oldAliased && oldAliased != null) {
                oldAliased.syncElement(element);
            }

        }
//        }

        FormElement old = latestValues.get(aliasId);
        latestValues.put(aliasId, element);

        if (element != old && old != null) {
            old.syncElement(element);
        }

    }

    public synchronized void touch(FormElement element) {
        for (String alias : element.getIdAliases()) {
            if (!latestValues.containsKey(alias)) {
                latestValues.put(alias, element);
            }
        }
        if (!latestValues.containsKey(element.getId())) {
            latestValues.put(element.getId(), element);
        }
    }

    public synchronized void requestSync(FormElement element) {
        for (String alias : element.getIdAliases()) {
            if (latestValues.containsKey(alias)) {
                // JOVANA: bitan uslov koliko god delovao besmisleno
                if (element != latestValues.get(alias)) {
                    element.syncElement(latestValues.get(alias));
                }
            }
        }

        if (latestValues.containsKey(element.getId())) {
            // JOVANA: bitan uslov koliko god delovao besmisleno
            if (element != latestValues.get(element.getId())) {
                element.syncElement(latestValues.get(element.getId()));
            }
        }
    }

    public void downloadData() throws IOException {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n";
        xml += "<embedded-data>";

        Iterator<String> iterator = latestValues.keySet().iterator();
        Set<String> writtenIds = new HashSet<>();
        while (iterator.hasNext()) {
            String key = iterator.next();
            FormElement formElement = latestValues.get(key);
            if (!writtenIds.contains(formElement.getId())) {
                xml += "\n\t" + formElement.getXml();
                writtenIds.add(formElement.getId());
            }
            for (String xmlForAlias : formElement.getXmlForAliases(writtenIds)) {
                xml += "\n\t" + xmlForAlias;
            }
            writtenIds.addAll(formElement.getIdAliases());
        }
        xml += "\n</embedded-data>";

        HttpUtilities.sendFileToClient(xml, "PolGen-Export-" + System.currentTimeMillis() + ".xml");
    }

    public Set<String> processUpload(FileUploadEvent event) {

        Set<String> affectedFormIds = new HashSet<>();

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

//            Set<String> detectedForms = new HashSet<>();
//            Set<String> detectedRealElementIds = new HashSet<>();

            NodeList fieldNodes = document.getElementsByTagName("field");
//            System.out.println("JOVANA: Number of uploaded nodes " + fieldNodes.getLength());
            for (int i = 0; i < fieldNodes.getLength(); i++) {
                Node fNode = fieldNodes.item(i);

                String type = fNode.getAttributes().getNamedItem("type").getTextContent();
                String id = fNode.getAttributes().getNamedItem("id").getTextContent();

//                NE POSTOJI OVDE: ovo je upload-ovan (XML) dokument
//                String aliases = Objects.nonNull(fNode.getAttributes().getNamedItem("aliases")) ? fNode.getAttributes().getNamedItem("aliases").getTextContent() : null;
//                List<String> aliasIds = new ArrayList<>();
//                if (Objects.nonNull(aliases)) {
//                    aliasIds.addAll(Arrays.asList(aliases.split(FormElementFactory.ALIAS_DELIMITER)));
//                }

                try {
                    if (Objects.nonNull(fNode.getAttributes().getNamedItem("form"))) {
                        String formId = fNode.getAttributes().getNamedItem("form").getTextContent();
                        if (Objects.nonNull(formId) && !formId.isEmpty()) {
                            affectedFormIds.add(formId);
                        }
                    }

                    Set<String> formIdsForAlias = FormFactory.getInstance().getFormIdsForAttribute(id);
                    affectedFormIds.addAll(formIdsForAlias);
//                    System.out.print("Form ids for for attribute " + id + " size " + formIdsForAlias.size());
//                    for (String formIdForAlias : formIdsForAlias) {
//                        System.out.print(" " + formIdForAlias);
//                    }
//                    System.out.println();
                Set<String> elementAliases = FormFactory.getInstance().getAliasesForElementId(id);

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
                    // sync aliases
                    for (String alias : element.getIdAliases()) {
                        if (latestValues.containsKey(alias)) { // If it already exists, just sync it with the dummy
                            latestValues.get(alias).sync(element);
                        } else {    // if not, put the dummy into the map
                            latestValues.put(alias, element);
                        }
                    }
                }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }


            }

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        return affectedFormIds;
    }

}
