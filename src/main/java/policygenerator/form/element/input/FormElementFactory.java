package policygenerator.form.element.input;

import policygenerator.form.element.SelectionElement;
import policygenerator.form.element.Panel;
import framework.utilities.xml.XMLUtilities;
import framework.utilities.xml.MissingAttributeException;
import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import policygenerator.form.trigger.Trigger;
import policygenerator.form.element.exceptions.MisconfiguredSelectionList;
import policygenerator.form.element.exceptions.UnknownTriggerOperation;
import policygenerator.form.element.exceptions.UnknownTypeInputException;

public class FormElementFactory {

    public static FormElement parseInputElement(Panel panel, Node node) throws UnknownTypeInputException, MisconfiguredSelectionList, UnknownTriggerOperation, MissingAttributeException {
        String type = XMLUtilities.getRequiredAttributeValue(node, "type");
        String id = XMLUtilities.getRequiredAttributeValue(node, "id");

        String elementLabel = XMLUtilities.getAttributeValue(node, "label");
        String tooltip = XMLUtilities.getAttributeValue(node, "tooltip");
        String defaultValue = XMLUtilities.getAttributeValue(node, "default");
        String conditionId = XMLUtilities.getAttributeValue(node, "rendered");

        String validationRegex = XMLUtilities.getAttributeValue(node, "validation-regex");
        String validationMessage = XMLUtilities.getAttributeValue(node, "validation-message");

        boolean mandatory = "true".equals(XMLUtilities.getAttributeValue(node, "mandatory"));

        String listId = XMLUtilities.getAttributeValue(node, "list-id");
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
                String triggerConditionId = XMLUtilities.getRequiredAttributeValue(tn, "condition");
                String target = XMLUtilities.getRequiredAttributeValue(tn, "target");
                String parsedOperation = XMLUtilities.getRequiredAttributeValue(tn, "operation");

                String value = XMLUtilities.getAttributeValue(tn, "value");

                Trigger.Operation operation;
                switch (parsedOperation) {
                    case "set":
                        operation = Trigger.Operation.SET;
                        break;
                    case "clear":
                        operation = Trigger.Operation.CLEAR;
                        break;
                    case "remove":
                        operation = Trigger.Operation.REMOVE;
                        break;
                    case "reset":
                        operation = Trigger.Operation.RESET;
                        break;
                    default:
                        throw new UnknownTriggerOperation(parsedOperation);
                }

                element.addTrigger(new Trigger(triggerConditionId, target, operation, value));
            }
            if (tn.getNodeName().equals("description")) {
                element.setDescription(XMLUtilities.innerXml(tn));
            }
        }

        if (defaultValue != null) {
            element.setDefaultValue(defaultValue);
        }

        return element;
    }

    public static FormElement getDummyElement(String type, String id) {
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
            case "addlist": // AddList can serve as a dummy for any list-like FormElement
            case "selectone":
            case "selectmany":
            case "poolpicker":
                element = new AddList(null, id, false, null, null, null, null);
                break;
            default:
                element = null;
                break;
        }

        return element;
    }
}
