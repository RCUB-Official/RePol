package policygenerator.form;

import framework.utilities.xml.XMLHandler;
import policygenerator.form.element.Panel;
import framework.utilities.xml.XMLUtilities;
import policygenerator.form.element.exceptions.UnknownTypeInputException;
import policygenerator.form.element.exceptions.MisconfiguredSelectionList;

import java.util.*;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import policygenerator.form.condition.Composite;
import policygenerator.form.condition.exceptions.ConditionNotFoundException;
import framework.utilities.xml.MissingAttributeException;
import policygenerator.form.condition.exceptions.UnknownOperatorException;
import policygenerator.form.element.PanelFactory;
import policygenerator.form.element.exceptions.ElementNotFoundException;
import policygenerator.form.element.exceptions.IdentifierCollision;
import policygenerator.form.element.exceptions.UnknownTriggerOperation;
import policygenerator.session.SessionController;

import static policygenerator.form.element.input.FormElementFactory.ALIAS_DELIMITER;

public class FormFactory extends XMLHandler {

    private static final FormFactory INSTANCE = new FormFactory();

    private List<FormHeader> headers = null;

    private FormFactory() {
        super(true, "Form Factory", "/config/template-forms.xml");
    }

    public static FormFactory getInstance() {
        return INSTANCE;
    }

    @Override
    protected void initializeProcedure() throws MissingAttributeException {
//        System.out.println("FormFactory: initialize procedure");
        headers = new LinkedList<>();

        NodeList forms = xmlDoc.getElementsByTagName("form");
        for (int i = 0; i < forms.getLength(); i++) {
            String formId = XMLUtilities.getRequiredAttributeValue(forms.item(i), "id");
            String label = XMLUtilities.getAttributeValue(forms.item(i), "label");
            String description = null;
            Map<String, List<String>> aliasesByElementId = new HashMap<>();

            NodeList subnodes = forms.item(i).getChildNodes();
//            System.out.println("\tchildNodes.size " + subnodes.getLength());

            for (int k = 0; k < subnodes.getLength(); k++) {
                Node panelOrConditionNOde = subnodes.item(k);
//                System.out.println("\t\txmlNode name " + xmlNode.getNodeName());
                if (panelOrConditionNOde.getNodeName().equals("description")) {
                    description = XMLUtilities.innerXml(subnodes.item(k));
                } else if (panelOrConditionNOde.getNodeName().equals("panel")) {
//                    System.out.println("JOVANA: form " + formId + " panel ");
                    NodeList panelSubnodes = panelOrConditionNOde.getChildNodes();
                    for (int l = 0; l < panelSubnodes.getLength(); l++) {
                        Node xmlNode = panelSubnodes.item(l);
                        if (xmlNode.getNodeName().equals("input")) {
                            String id = xmlNode.getAttributes().getNamedItem("id").getTextContent();
                            List<String> aliasIds = new ArrayList<>();
//                            System.out.print("\t" + id + " aliases ");

                            Node aliasesNode = xmlNode.getAttributes().getNamedItem("aliases");
//                            System.out.println("Form " + formId + " aliases for id " + id + " " + (Objects.nonNull(aliasesNode) ? aliasesNode.getTextContent() : "null"));
                            if (Objects.nonNull(aliasesNode)) {
                                String[] aliases = aliasesNode.getTextContent().split(ALIAS_DELIMITER);
                                aliasIds.addAll(Arrays.asList(aliases));
//                                System.out.print(Arrays.toString(aliases));
                            }
//                            System.out.println();
                            aliasesByElementId.put(id, aliasIds);
                        }
                    }
                }
            }

            headers.add(new FormHeader(formId, label, description, aliasesByElementId));
        }
//        System.out.println("\theaders number " + this.headers.size());
    }

    public Set<String> getFormIdsForAttribute(String attributeId) {
//        System.out.println("finding form ids for attribute " + attributeId);
        Set<String> formIds = new HashSet<>();
        for (FormHeader formHeader : this.headers) {
//            System.out.print("\ttrying " + formHeader.getFormId());
            if (formHeader.hasAtrribute(attributeId)) {
                formIds.add(formHeader.getFormId());
//                System.out.print(" has");
            }
//            System.out.println();
        }
        return formIds;
    }

    @Override
    protected void shutdownProcedure() {
        headers = null;
    }

    public List<FormHeader> getFormHeaders() {
//        System.out.println("FormFactory.getFormHeaders " + headers.size());
        return headers;
    }

    public boolean validateFormId(String formId) {
        boolean valid = false;
        for (FormHeader fh : headers) {
            if (fh.getFormId().equals(formId)) {
                valid = true;
                break;
            }
        }
        return valid;
    }

    public Form getForm(SessionController sessionController, String id) throws UnknownTypeInputException, MisconfiguredSelectionList,
            UnknownOperatorException, ElementNotFoundException, IdentifierCollision, ConditionNotFoundException,
            UnknownTriggerOperation, MissingAttributeException, FormNotFoundException {

        Form form = null;

        Node formNode = getNode("form", id);
        if (formNode != null) {
            String label = XMLUtilities.getAttributeValue(formNode, "label");

            form = new Form(sessionController, id);

            form.setLabel(label);

            List<Panel> panels = new LinkedList<>();
            List<Node> conditionNodes = new LinkedList<>();

            String description = null;

            NodeList subElements = formNode.getChildNodes();
            for (int i = 0; i < subElements.getLength(); i++) {

                Node elementNode = subElements.item(i);
                String nodeName = elementNode.getNodeName();
                switch (nodeName) {
                    case "description":
                        description = XMLUtilities.innerXml(elementNode);
                        break;
                    case "panel":
                        panels.add(PanelFactory.parsePanel(form, elementNode));
                        break;
                    case "condition":
                        conditionNodes.add(subElements.item(i)); // Parsing later
                        break;
                }
            }

            form.addPanels(panels);
            form.setDescription(description);

            //Parsing Conditions
            for (Node cn : conditionNodes) {
                String conditionId = XMLUtilities.getRequiredAttributeValue(cn, "id");
                form.addCondition(new Composite(form, conditionId, cn));
            }
        } else {
            throw new FormNotFoundException(id);
        }

        form.test();    // Factory tested

        return form;
    }

    public Set<String> getAliasesForElementId(String id) {
        Set<String> elementIds = new HashSet<>();
        for (FormHeader form : this.headers) {
            Set<String> aliasedElementIds = form.getAliasesForElementId(id);
            elementIds.addAll(aliasedElementIds);
            if (form.hasAttributeId(id)) {
                elementIds.add(id);
            }
        }
        return elementIds;
    }
}
