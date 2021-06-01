package policygenerator.form.condition;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import policygenerator.form.Form;
import policygenerator.form.element.input.FormElement;
import policygenerator.form.element.exceptions.ElementNotFoundException;
import policygenerator.form.element.input.AddList;
import policygenerator.form.element.input.PoolPicker;
import policygenerator.form.element.input.SelectMany;

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
        boolean listElement = element instanceof PoolPicker || element instanceof AddList || element instanceof SelectMany;
        return element.getId() + (listElement ? (inverted ? " !contains \"" : " contains \"") : (inverted ? " != \"" : " == \"")) + matchValue + "\"";
    }

}
