/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policygenerator.forms.element;

/**
 *
 * @author vasilije
 */
public class IntegerInput extends FormElement {

    private Integer value;

    public IntegerInput(Panel panel, String id, boolean mandatory, String label, String conditionId) {
        super(panel, Type.INTEGER, id, mandatory, label, conditionId);
        this.value = null;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public void set(String value) {
        try {
            this.value = Integer.parseInt(value);
        } catch (Exception ex) {
            this.value = null;
        }
    }

    @Override
    public boolean isEmpty() {
        return (value == null);
    }

    @Override
    public boolean isRegexValid() {
        //TODO: validacija nad toString()
        return true;
    }

    @Override
    public boolean match(String value) {
        boolean match;
        try {
            match = (this.value == Integer.parseInt(value));
        } catch (Exception ex) {
            match = false;
        }
        return match;
    }

    @Override
    public void clear() {
        this.value = 0;
    }

    @Override
    public void remove(String value) {
    }

    @Override
    public void sync(FormElement element) {
        switch (element.getType()) {
            case INTEGER:
                this.value = ((IntegerInput) element).getValue();
                break;
            case DOUBLE:
                this.value = (int) Math.round(((DoubleInput) element).getValue());
                break;
            case ONELINE:
                set(((OneLine) element).getValue());
                break;
            case TEXT:
                set(((Text) element).getValue());
                break;
            case SELECTONE:
                set(((SelectOne) element).getValue());
                break;
        }
    }

    @Override
    public String getXml() {
        return "<field type=\"integer\" id=\"" + getId() + "\"><value>" + value + "</value></field>";
    }

}
