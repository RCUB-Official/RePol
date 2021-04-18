package policygenerator.forms;

import policygenerator.forms.element.exceptions.UnknownTypeInputException;
import policygenerator.forms.element.exceptions.MisconfiguredSelectionList;
import java.util.LinkedList;
import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import policygenerator.forms.Trigger.Operation;
import policygenerator.forms.condition.Composite;
import policygenerator.forms.condition.exceptions.ConditionNotFoundException;
import policygenerator.forms.condition.exceptions.MissingAttributeException;
import policygenerator.forms.condition.exceptions.UnknownOperatorException;
import policygenerator.forms.element.*;
import policygenerator.forms.element.exceptions.ElementNotFoundException;
import policygenerator.forms.element.exceptions.IdentifierCollision;
import policygenerator.forms.element.exceptions.UnknownTriggerOperation;

public class FormFactory extends XMLHandler {

    private static final FormFactory INSTANCE = new FormFactory();

    private List<FormHeader> headers = null;

    private FormFactory() {
        super(true, "Form Factory", "/config/template-forms.xml");
    }

    public static FormFactory getInstance() {
        return INSTANCE;
    }

    private static String innerXml(Node node) {
        DOMImplementationLS lsImpl = (DOMImplementationLS) node.getOwnerDocument().getImplementation().getFeature("LS", "3.0");
        LSSerializer lsSerializer = lsImpl.createLSSerializer();
        NodeList childNodes = node.getChildNodes();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < childNodes.getLength(); i++) {
            sb.append(lsSerializer.writeToString(childNodes.item(i)));
        }
        return sb.toString().replace("<?xml version=\"1.0\" encoding=\"UTF-16\"?>", "");
    }

    private static String getAttributeValue(Node node, String name) {   // Returns null if the attribute does not exist
        String attribute;
        try {
            attribute = node.getAttributes().getNamedItem(name).getTextContent();
        } catch (Exception ex) {
            attribute = null;
        }
        return attribute;
    }

    private static String getRequiredAttributeValue(Node node, String name) throws MissingAttributeException { // Throws an exception if the attribute does not exist.
        String attribute;
        try {
            attribute = node.getAttributes().getNamedItem(name).getTextContent();
        } catch (Exception ex) {
            throw new MissingAttributeException(name);
        }
        return attribute;
    }

    @Override
    protected void initializeProcedure() throws MissingAttributeException {
        headers = new LinkedList<>();

        NodeList forms = xmlDoc.getElementsByTagName("form");
        for (int i = 0; i < forms.getLength(); i++) {
            String formId = getRequiredAttributeValue(forms.item(i), "id");
            String label = getAttributeValue(forms.item(i), "label");
            String description = null;

            NodeList subnodes = forms.item(i).getChildNodes();

            List<String> mandatoryFieldIds = new LinkedList<>();

            for (int k = 0; k < subnodes.getLength(); k++) {
                if (subnodes.item(k).getNodeName().equals("description")) {
                    description = subnodes.item(k).getTextContent();
                } else if (subnodes.item(k).getNodeName().equals("panel")) {
                    NodeList elementNodes = subnodes.item(k).getChildNodes();
                    for (int j = 0; j < elementNodes.getLength(); j++) {
                        Node elementNode = elementNodes.item(j);
                        if (elementNode.getNodeName().equals("input")) {
                            if ("true".equals(getAttributeValue(elementNode, "mandatory"))) {
                                mandatoryFieldIds.add(getRequiredAttributeValue(elementNode, "id"));
                            }
                        }
                    }
                }
            }
            headers.add(new FormHeader(formId, label, description, mandatoryFieldIds));
        }
    }

    @Override
    protected void shutdownProcedure() {
        headers = null;
    }

    public List<FormHeader> getFormHeaders() {
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

    private static FormElement parseInputElement(Panel panel, Node node) throws UnknownTypeInputException, MisconfiguredSelectionList, UnknownTriggerOperation, MissingAttributeException {
        String type = getRequiredAttributeValue(node, "type");
        String id = getRequiredAttributeValue(node, "id");

        String elementLabel = getAttributeValue(node, "label");
        String tooltip = getAttributeValue(node, "tooltip");
        String defaultValue = getAttributeValue(node, "default");
        String conditionId = getAttributeValue(node, "rendered");

        String validationRegex = getAttributeValue(node, "validation-regex");
        String validationMessage = getAttributeValue(node, "validation-message");

        boolean mandatory = "true".equals(getAttributeValue(node, "mandatory"));

        String listId = getAttributeValue(node, "list-id");
        List<SelectionElement> selectionList;

        FormElement element;
        switch (type) {
            case "oneline":
                element = new OneLine(panel, id, mandatory, elementLabel, conditionId, validationRegex, validationMessage);
                break;
            case "text":
                element = new Text(panel, id, mandatory, elementLabel, conditionId, validationRegex, validationMessage);
                break;
            case "boolean":
                element = new BooleanCheckbox(panel, id, mandatory, elementLabel, conditionId);
                break;
            case "integer":
                element = new IntegerInput(panel, id, mandatory, elementLabel, conditionId);
                break;
            case "double":
                element = new DoubleInput(panel, id, mandatory, elementLabel, conditionId);
                break;
            case "date":
                element = new DateInput(panel, id, mandatory, elementLabel, conditionId);
                break;
            case "addlist":
                element = new AddList(panel, id, mandatory, elementLabel, conditionId, validationRegex, validationMessage);
                break;
            case "selectone":
                element = new SelectOne(panel, id, mandatory, elementLabel, conditionId, listId);
                break;
            case "selectmany":
                element = new SelectMany(panel, id, mandatory, elementLabel, conditionId, listId);
                break;
            case "poolpicker":
                element = new PoolPicker(panel, id, mandatory, elementLabel, conditionId, validationRegex, validationMessage, listId);
                break;
            default:
                throw new UnknownTypeInputException(type);
        }
        element.setTooltip(tooltip);

        //Parsing Triggers and descriptions
        NodeList triggerNodes = node.getChildNodes();

        for (int i = 0; i < triggerNodes.getLength(); i++) {
            Node tn = triggerNodes.item(i);
            if (tn.getNodeName().equals("trigger")) {
                String triggerConditionId = getRequiredAttributeValue(tn, "condition");
                String target = getRequiredAttributeValue(tn, "target");
                String parsedOperation = getRequiredAttributeValue(tn, "operation");

                String value = getAttributeValue(tn, "value");

                Operation operation;
                switch (parsedOperation) {
                    case "set":
                        operation = Operation.SET;
                        break;
                    case "clear":
                        operation = Operation.CLEAR;
                        break;
                    case "remove":
                        operation = Operation.REMOVE;
                        break;
                    case "reset":
                        operation = Operation.RESET;
                        break;
                    default:
                        throw new UnknownTriggerOperation(parsedOperation);
                }

                element.addTrigger(new Trigger(triggerConditionId, target, operation, value));
            }
            if (tn.getNodeName().equals("description")) {
                element.setDescription(innerXml(tn));
            }
        }

        if (defaultValue != null) {
            element.setDefaultValue(defaultValue);
        }

        return element;
    }

    private static Panel parsePanel(Form form, Node panelNode) throws UnknownTypeInputException, MisconfiguredSelectionList, UnknownTriggerOperation, MissingAttributeException {
        String label = getAttributeValue(panelNode, "label");
        String conditionId = getAttributeValue(panelNode, "rendered");

        Panel panel = new Panel(form, label, conditionId);

        List<FormElement> elements = new LinkedList<>();

        NodeList subElements = panelNode.getChildNodes();
        for (int i = 0; i < subElements.getLength(); i++) {
            Node elementNode = subElements.item(i);
            String nodeName = elementNode.getNodeName();

            switch (nodeName) {
                case "input":
                    elements.add(parseInputElement(panel, elementNode));
                    break;
                case "separator":
                    elements.add(new Separator());
                    break;
            }
        }

        panel.addElements(elements);
        return panel;
    }

    protected Form getForm(FormController controller, String id) throws UnknownTypeInputException, MisconfiguredSelectionList, UnknownOperatorException, ElementNotFoundException, IdentifierCollision, ConditionNotFoundException, UnknownTriggerOperation, MissingAttributeException {
        Form form = null;

        Node formNode = getNode("form", id);
        if (formNode != null) {
            String label = getAttributeValue(formNode, "label");

            form = new Form(controller, id);

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
                        description = innerXml(elementNode);
                        break;
                    case "panel":
                        panels.add(parsePanel(form, elementNode));
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
                String conditionId = getRequiredAttributeValue(cn, "id");
                form.addCondition(new Composite(form, conditionId, cn));
            }
        }

        return form;
    }
}
