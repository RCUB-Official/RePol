/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policygenerator.forms.element;

import framework.utilities.Utilities;
import java.util.List;

/**
 *
 * @author vasilije
 */
public class SelectOne extends FormElement {

    private List<SelectionElement> availableValues;
    private String value;

    public SelectOne(Panel panel, String id, boolean mandatory, String label, String conditionId) {
        super(panel, Type.SELECTONE, id, mandatory, label, conditionId);
        this.availableValues = null;
        this.value = null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setAvailableValues(List<SelectionElement> availableValues) {
        this.availableValues = availableValues;
    }

    public List<SelectionElement> getAvailableValues() {
        return availableValues;
    }

    @Override
    public void setDefaultValue(String defaultValue) {
        for (SelectionElement se : availableValues) {
            if (se.getValue().equals(defaultValue)) {
                this.value = defaultValue;
                this.defaultValue = defaultValue;
                break;
            }
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
        return true;
    }

    @Override
    public void setByTrigger(String value) {
        for (SelectionElement se : availableValues) {
            if (se.getValue().equals(value)) {
                this.value = value;
                break;
            }
        }
        push();
    }

    @Override
    public boolean match(String value) {
        if (this.value == null) {
            return false;
        } else {
            return (this.value.equals(value));
        }
    }

    @Override
    public void clear() {
    }

    @Override
    public void remove(String value) {
    }

    @Override
    public void sync(FormElement element) {
        switch (element.getType()) {
            case SELECTONE:
                setByTrigger(((SelectOne) element).getValue());
                break;
            case TEXT:
                setByTrigger(((Text) element).getValue());
                break;
            case ONELINE:
                setByTrigger(((OneLine) element).getValue());
                break;
            case INTEGER:
                setByTrigger(((IntegerInput) element).getValue() + "");
                break;
            case DOUBLE:
                setByTrigger(((DoubleInput) element).getValue() + "");
                break;
            case DATE:
                setByTrigger(((DateInput) element).getValue() + "");
                break;
        }
    }

    @Override
    public String getXml() {
        if (value != null) {
            return "<field type=\"oneline\" id=\"" + getId() + "\"><value>" + Utilities.xmlEscape(value) + "</value></field>";
        } else {
            return "";
        }
    }

}
