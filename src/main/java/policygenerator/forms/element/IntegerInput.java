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
    public void setDefaultValue(String defaultValue) {
        try {
            this.value = Integer.parseInt(defaultValue);
        } catch (Exception ex) {
            this.value = null;
        }
        touch();
    }

    @Override
    public boolean isEmpty() {
        return (value == null);
    }

    @Override
    public void setValidationRegex(String validationRegex) { // Maybe validate as a string?
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void setByTrigger(String value) {
        //TODO: Handle exception
        this.value = Integer.parseInt(value);
        push();
    }

    @Override
    public boolean match(String value) {
        boolean trigger;
        try {
            trigger = (this.value == Integer.parseInt(value));
        } catch (Exception ex) {
            trigger = false;
        }
        return trigger;
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
        }
    }

    @Override
    public String getXml() {
        return "<field type=\"integer\" id=\"" + getId() + "\"><value>" + value + "</value></field>";
    }

}
