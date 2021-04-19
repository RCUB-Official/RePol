package policygenerator.form.condition;

import org.w3c.dom.Node;
import policygenerator.form.Form;

public abstract class Condition {

    protected final Form myForm;
    protected final String id;
    protected final boolean inverted;

    protected Condition(Form myForm, String id, Node node) {
        this.myForm = myForm;
        this.id = id;

        boolean parsed;
        try {
            parsed = ("true".equals(node.getAttributes().getNamedItem("inverted").getTextContent()));
        } catch (Exception ex) {
            parsed = false;
        }
        this.inverted = parsed;
    }

    public String getId() {
        return id;
    }

    public abstract boolean evaluate();

}
