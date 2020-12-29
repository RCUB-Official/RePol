/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policygenerator.forms.condition;

import org.w3c.dom.Node;
import policygenerator.forms.Form;

/**
 *
 * @author vasilije
 */
public abstract class Condition {

    protected final Form myForm;
    protected final String id;
    protected final boolean inverted;

    public Condition(Form myForm, String id, Node node) {
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
