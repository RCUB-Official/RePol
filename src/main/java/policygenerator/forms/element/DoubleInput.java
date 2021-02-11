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
public class DoubleInput extends FormElement {

    private Double value;

    public DoubleInput(Panel panel, String id, boolean mandatory, String label, String conditionId) {
        super(panel, Type.DOUBLE, id, mandatory, label, conditionId);
        this.value = null;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public void setDefaultValue(String defaultValue) {
        try {
            this.value = Double.parseDouble(defaultValue);
            this.defaultValue = defaultValue;
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
    public void setValidationRegex(String validationRegex) {
    }

    @Override
    public boolean isValid() {
        return true;    //Always valid except maybe in NAN case
    }

    @Override
    public void setByTrigger(String value) {
        //TODO: Handle exception
        this.value = Double.parseDouble(value);
        push();
    }

    @Override
    public boolean match(String value) {
        boolean trigger;
        try {
            trigger = (this.value == Double.parseDouble(value));
        } catch (Exception ex) {
            trigger = false;
        }
        return trigger;
    }

    @Override
    public void clear() {
        this.value = null;
    }

    @Override
    public void remove(String value) {
    }

    @Override
    public void sync(FormElement element) {
        switch (element.getType()) {
            case DOUBLE:
                this.value = ((DoubleInput) element).getValue();
                break;
            case INTEGER:
                this.value = (double) ((IntegerInput) element).getValue();
                break;
        }
    }

    @Override
    public String getXml() {
        return "<field type=\"double\" id=\"" + getId() + "\"><value>" + value + "</value></field>";
    }

}
