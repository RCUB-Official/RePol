/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import policygenerator.forms.condition.exceptions.UnknownOperatorException;
import policygenerator.forms.element.*;
import policygenerator.forms.element.exceptions.ElementNotFoundException;
import policygenerator.forms.element.exceptions.IdentifierCollision;
import policygenerator.forms.element.exceptions.UnknownTriggerOperation;

/**
 *
 * @author vasilije
 */
public class FormFactory extends XMLHandler {

    private static final FormFactory INSTANCE = new FormFactory();

    private FormFactory() {
        super(true, "Form Factory", "/config/template-forms.xml");
    }

    public static FormFactory getInstance() {
        return INSTANCE;
    }

    public List<FormHeader> getFormHeaders() {
        List<FormHeader> headers = new LinkedList<FormHeader>();

        NodeList forms = xmlDoc.getElementsByTagName("form");
        for (int i = 0; i < forms.getLength(); i++) {
            String formId = forms.item(i).getAttributes().getNamedItem("id").getTextContent();
            String label;
            try {
                label = forms.item(i).getAttributes().getNamedItem("label").getTextContent();
            } catch (Exception ex) {
                label = null;
            }
            String description = null;
            NodeList subnodes = forms.item(i).getChildNodes();

            List<String> mandatoryFieldIds = new LinkedList<String>();

            for (int k = 0; k < subnodes.getLength(); k++) {
                if (subnodes.item(k).getNodeName().equals("description")) {
                    description = subnodes.item(k).getTextContent();
                } else if (subnodes.item(k).getNodeName().equals("panel")) {
                    NodeList elementNodes = subnodes.item(k).getChildNodes();
                    for (int j = 0; j < elementNodes.getLength(); j++) {
                        Node elementNode = elementNodes.item(j);
                        if (elementNode.getNodeName().equals("input")) {
                            try {
                                if ("true".equals(elementNode.getAttributes().getNamedItem("mandatory").getTextContent())) {
                                    mandatoryFieldIds.add(elementNode.getAttributes().getNamedItem("id").getTextContent());
                                }
                            } catch (Exception ex) {
                            }
                        }
                    }
                }
            }

            headers.add(new FormHeader(formId, label, description, mandatoryFieldIds));
        }

        return headers;
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

    private static FormElement parseInputElement(Panel panel, Node node) throws UnknownTypeInputException, MisconfiguredSelectionList, UnknownTriggerOperation {
        String type = node.getAttributes().getNamedItem("type").getTextContent();
        String id = node.getAttributes().getNamedItem("id").getTextContent();

        String elementLabel;
        try {
            elementLabel = node.getAttributes().getNamedItem("label").getTextContent();
        } catch (Exception ex) {
            elementLabel = null;
        }

        String tooltip;
        try {
            tooltip = node.getAttributes().getNamedItem("tooltip").getTextContent();
        } catch (Exception ex) {
            tooltip = null;
        }

        String defaultValue;
        try {
            defaultValue = node.getAttributes().getNamedItem("default").getTextContent();
        } catch (Exception ex) {
            defaultValue = null;
        }

        boolean mandatory;
        try {
            mandatory = "true".equals(node.getAttributes().getNamedItem("mandatory").getTextContent());
        } catch (Exception ex) {
            mandatory = false;
        }

        String validationRegex;
        try {
            validationRegex = node.getAttributes().getNamedItem("validation-regex").getTextContent();
        } catch (Exception ex) {
            validationRegex = null;
        }

        String conditionId;
        try {
            conditionId = node.getAttributes().getNamedItem("rendered").getTextContent();
        } catch (Exception ex) {
            conditionId = null;
        }

        String listId;
        List<SelectionElement> selectionList;
        try {
            listId = node.getAttributes().getNamedItem("list-id").getTextContent();
        } catch (Exception ex) {
            listId = null;
            selectionList = null;
        }

        FormElement element;
        switch (type) {
            case "oneline":
                element = new OneLine(panel, id, mandatory, elementLabel, conditionId);
                if (validationRegex != null) {
                    ((OneLine) element).setValidationRegex(validationRegex);
                }
                break;
            case "text":
                element = new Text(panel, id, mandatory, elementLabel, conditionId);
                if (validationRegex != null) {
                    ((Text) element).setValidationRegex(validationRegex);
                }
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
                element = new AddList(panel, id, mandatory, elementLabel, conditionId);
                break;
            case "selectone":
                element = new SelectOne(panel, id, mandatory, elementLabel, conditionId);
                selectionList = ListFactory.getInstance().getSelectionList(element, listId);
                if (selectionList != null) {
                    ((SelectOne) element).setAvailableValues(selectionList);
                } else {
                    throw new MisconfiguredSelectionList(listId);
                }
                break;
            case "selectmany":
                element = new SelectMany(panel, id, mandatory, elementLabel, conditionId);
                selectionList = ListFactory.getInstance().getSelectionList(element, listId);
                if (selectionList != null) {
                    ((SelectMany) element).setAvailableValues(selectionList);
                } else {
                    throw new MisconfiguredSelectionList(listId);
                }
                break;
            case "poolpicker":
                element = new PoolPicker(panel, id, mandatory, elementLabel, conditionId);
                selectionList = ListFactory.getInstance().getSelectionList(element, listId);
                if (selectionList != null) {
                    ((PoolPicker) element).setAvailableValues(selectionList);
                } else {
                    throw new MisconfiguredSelectionList(listId);
                }
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
                String triggerConditionId = tn.getAttributes().getNamedItem("condition").getTextContent();
                String target = tn.getAttributes().getNamedItem("target").getTextContent();
                String parsedOperation = tn.getAttributes().getNamedItem("operation").getTextContent();

                String value;
                try {
                    value = tn.getAttributes().getNamedItem("value").getTextContent();
                } catch (Exception ex) {
                    value = null;
                }

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

    private static Panel parsePanel(Form form, Node panelNode) throws UnknownTypeInputException, MisconfiguredSelectionList, UnknownTriggerOperation {
        //Parsing Label
        String label;
        try {
            label = panelNode.getAttributes().getNamedItem("label").getTextContent();
        } catch (Exception ex) {
            label = null;
        }

        //Parsing Condition Id
        String conditionId;
        try {
            conditionId = panelNode.getAttributes().getNamedItem("rendered").getTextContent();
        } catch (Exception ex) {
            conditionId = null;
        }

        Panel panel = new Panel(form, label, conditionId);

        List<FormElement> elements = new LinkedList<FormElement>();

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

    public Form getForm(String id) throws UnknownTypeInputException, MisconfiguredSelectionList, UnknownOperatorException, ElementNotFoundException, IdentifierCollision, ConditionNotFoundException, UnknownTriggerOperation {
        Form form = null;

        Node formNode = getNode("form", id);
        if (formNode != null) {
            String label;
            try {
                label = formNode.getAttributes().getNamedItem("label").getTextContent();
            } catch (Exception ex) {
                label = null;
            }

            form = new Form(id);

            form.setLabel(label);

            List<Panel> panels = new LinkedList<Panel>();
            List<Node> conditionNodes = new LinkedList<Node>();

            NodeList panelElements = formNode.getChildNodes();
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
                String conditionId = cn.getAttributes().getNamedItem("id").getTextContent();
                form.addCondition(new Composite(form, conditionId, cn));
            }

        }

        return form;
    }

}
