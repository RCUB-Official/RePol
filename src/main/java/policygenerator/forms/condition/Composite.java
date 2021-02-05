/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policygenerator.forms.condition;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import policygenerator.forms.Form;
import policygenerator.forms.condition.exceptions.UnknownOperatorException;
import policygenerator.forms.element.exceptions.ElementNotFoundException;

/**
 *
 * @author vasilije
 */
public class Composite extends Condition {

    public enum Operator {
        AND,
        OR
    }

    private final Operator operator;
    private final List<Condition> subConditions;

    public Composite(Form myForm, String id, Node node) throws UnknownOperatorException, ElementNotFoundException {
        super(myForm, id, node);

        String nodeName = node.getNodeName();
        switch (nodeName) {
            case "condition":
                String parsedCondition;
                try {
                    parsedCondition = node.getAttributes().getNamedItem("operator").getTextContent();
                } catch (Exception ex) {
                    parsedCondition = null;
                }
                if ("and".equals(parsedCondition)) {
                    this.operator = Operator.AND;
                } else {
                    this.operator = Operator.OR;
                }
                break;
            case "and":
                this.operator = Operator.AND;
                break;
            case "or":
                this.operator = Operator.OR;
                break;
            default:
                throw new UnknownOperatorException(nodeName);
        }

        subConditions = new LinkedList<Condition>();

        NodeList subNodes = node.getChildNodes();
        for (int i = 0; i < subNodes.getLength(); i++) {
            if (subNodes.item(i).getNodeName().equals("match")) {
                subConditions.add(new Match(myForm, subNodes.item(i)));
            } else if (subNodes.item(i).getNodeName().equals("empty")) {
                subConditions.add(new Empty(myForm, subNodes.item(i)));
            } else if (subNodes.item(i).getNodeName().equals("and") || subNodes.item(i).getNodeName().equals("or")) {
                subConditions.add(new Composite(myForm, null, subNodes.item(i)));
            }
        }
    }

    @Override
    public boolean evaluate() {
        boolean value;
        switch (operator) {
            case AND:
                value = true;
                for (Condition sub : subConditions) {
                    value = value && sub.evaluate();
                }
                break;
            case OR:
                value = false;
                for (Condition sub : subConditions) {
                    value = value || sub.evaluate();
                }
                break;
            default:
                value = false;
                Logger.getLogger(Composite.class.getName()).log(Level.WARNING, "Condition has no operator defined.");
        }
        if (!inverted) {
            return value;
        } else {
            return !value;
        }
    }

    @Override
    public String toString() {
        String expression = "";
        for (int i = 0; i < subConditions.size(); i++) {
            if (i > 0) {
                if (this.operator == Operator.AND) {
                    expression += " AND ";
                } else if (this.operator == Operator.OR) {
                    expression += " OR ";
                }
            }
            if (subConditions.get(i) instanceof Composite && !subConditions.get(i).inverted && subConditions.size() > 1) {
                expression += "(" + subConditions.get(i) + ")";
            } else {
                expression += subConditions.get(i);
            }
        }
        if (inverted) {
            return "NOT(" + expression + ")";
        } else {
            return expression;
        }
    }
}
