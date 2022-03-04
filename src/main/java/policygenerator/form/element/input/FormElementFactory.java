package policygenerator.form.element.input;

import policygenerator.form.element.Panel;
import framework.utilities.xml.XMLUtilities;
import framework.utilities.xml.MissingAttributeException;

import java.util.*;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import policygenerator.form.trigger.Trigger;
import policygenerator.form.element.exceptions.MisconfiguredSelectionList;
import policygenerator.form.element.exceptions.UnknownTriggerOperation;
import policygenerator.form.element.exceptions.UnknownTypeInputException;

public class FormElementFactory {
    public static final String ALIAS_DELIMITER = ";";

    public static FormElement parseInputElement(Panel panel, Node node) throws UnknownTypeInputException, MisconfiguredSelectionList, UnknownTriggerOperation, MissingAttributeException {
        String type = XMLUtilities.getRequiredAttributeValue(node, "type");
        String id = XMLUtilities.getRequiredAttributeValue(node, "id");

        String elementLabel = XMLUtilities.getAttributeValue(node, "label");
        String tooltip = XMLUtilities.getAttributeValue(node, "tooltip");
        String conditionId = XMLUtilities.getAttributeValue(node, "rendered");

        List<String> defaultValues = new LinkedList<>();

        String attributeDefaultValue = XMLUtilities.getAttributeValue(node, "default"); // Legacy support for single default value declared as default attribute
        if (attributeDefaultValue != null) {
//            System.out.println(id + " " + type + " parsed default " + attributeDefaultValue);
            defaultValues.add(attributeDefaultValue);
        }

        String attributeIdAliases = XMLUtilities.getAttributeValue(node, "aliases"); // Support for id aliases
        Set<String> idAliases = new HashSet<>();
        if (Objects.nonNull(attributeIdAliases)) {
//            System.out.println("Parsing input element with aliases " + attributeIdAliases + " for node" + node.getTextContent());
            for (String alias : attributeIdAliases.split(ALIAS_DELIMITER)) {
                idAliases.add(alias);
            }
        }

        String validationRegex = XMLUtilities.getAttributeValue(node, "validation-regex");
        String validationMessage = XMLUtilities.getAttributeValue(node, "validation-message");

        boolean mandatory = "true".equals(XMLUtilities.getAttributeValue(node, "mandatory"));

        String listId = XMLUtilities.getAttributeValue(node, "list-id");
        FormElement element;

        switch (type) {
            case "oneline":
                element = new OneLine(panel, id, idAliases, mandatory, elementLabel, conditionId, validationRegex, validationMessage);
                break;
            case "text":
                element = new Text(panel, id, idAliases, mandatory, elementLabel, conditionId, validationRegex, validationMessage);
                break;
            case "boolean":
                element = new BooleanCheckbox(panel, id, idAliases, mandatory, elementLabel, conditionId);
                break;
            case "integer":
                element = new IntegerInput(panel, id, idAliases, mandatory, elementLabel, conditionId);
                break;
            case "double":
                element = new DoubleInput(panel, id, idAliases, mandatory, elementLabel, conditionId);
                break;
            case "date":
                element = new DateInput(panel, id, idAliases, mandatory, elementLabel, conditionId);
                break;
            case "addlist":
                element = new AddList(panel, id, idAliases, mandatory, elementLabel, conditionId, validationRegex, validationMessage);
                break;
            case "selectone":
                element = new SelectOne(panel, id, idAliases, mandatory, elementLabel, conditionId, listId);
                break;
            case "selectmany":
                element = new SelectMany(panel, id, idAliases, mandatory, elementLabel, conditionId, listId);
                break;
            case "poolpicker":
                element = new PoolPicker(panel, id, idAliases, mandatory, elementLabel, conditionId, validationRegex, validationMessage, listId);
                break;
            default:
                throw new UnknownTypeInputException(type);
        }

        element.setTooltip(tooltip);

        //Parsing Triggers and descriptions
        NodeList subElementNodes = node.getChildNodes();

        for (int i = 0; i < subElementNodes.getLength(); i++) {
            Node sen = subElementNodes.item(i);
            String nodeName = sen.getNodeName();

            switch (nodeName) {
                case "description":
                    element.setDescription(XMLUtilities.innerXml(sen));
                    break;
                case "default":
//                    System.out.println("Subnode " + sen.getNodeName() + " " + type + " parsed default " + sen.getTextContent());
                    defaultValues.add(sen.getTextContent());
                    break;
                case "trigger":
                    String triggerConditionId = XMLUtilities.getRequiredAttributeValue(sen, "condition");
                    String target = XMLUtilities.getRequiredAttributeValue(sen, "target");
                    String parsedOperation = XMLUtilities.getRequiredAttributeValue(sen, "operation");

                    String value = XMLUtilities.getAttributeValue(sen, "value");

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
                    break;
            }
        }

        for (String dv : defaultValues) {
            element.setDefaultValue(dv);
        }

        return element;
    }

    public static FormElement getDummyElement(String type, String id, Set<String> aliasIds) {
        FormElement element;

        switch (type) {
            case "oneline":
                element = new OneLine(null, id, aliasIds, false, null, null, null, null);
                break;
            case "text":
                element = new Text(null, id, aliasIds, false, null, null, null, null);
                break;
            case "boolean":
                element = new BooleanCheckbox(null, id, aliasIds, false, null, null);
                break;
            case "integer":
                element = new IntegerInput(null, id, aliasIds, false, null, null);
                break;
            case "double":
                element = new DoubleInput(null, id, aliasIds, false, null, null);
                break;
            case "date":
                element = new DateInput(null, id, aliasIds, false, null, null);
                break;
            case "addlist": // AddList can serve as a dummy for any list-like FormElement
            case "selectone":
            case "selectmany":
            case "poolpicker":
                element = new AddList(null, id, aliasIds, false, null, null, null, null);
                break;
            default:
                element = null;
                break;
        }

        return element;
    }
}
