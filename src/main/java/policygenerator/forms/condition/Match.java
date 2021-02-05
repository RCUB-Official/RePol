/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policygenerator.forms.condition;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import policygenerator.forms.Form;
import policygenerator.forms.element.FormElement;
import policygenerator.forms.element.exceptions.ElementNotFoundException;

/**
 *
 * @author vasilije
 */
public class Match extends Condition {

    private final FormElement element;
    private final String matchValue;

    public Match(Form myForm, Node node) throws ElementNotFoundException {
        super(myForm, null, node);
        this.matchValue = node.getAttributes().getNamedItem("value").getTextContent();

        String elementId;
        try {
            elementId = node.getAttributes().getNamedItem("id").getTextContent();
        } catch (DOMException ex) {
            throw new ElementNotFoundException("FormElement ID not specified!");
        }
        this.element = myForm.getElement(elementId);
    }

    @Override
    public boolean evaluate() {
        boolean value = element.match(matchValue);
        if (!inverted) {
            return value;
        } else {
            return !value;
        }
    }

    @Override
    public String toString() {
        return element.getId() + (inverted ? " != \"" : " == \"") + matchValue + "\"";
    }

}
