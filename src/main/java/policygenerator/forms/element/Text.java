/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policygenerator.forms.element;

import framework.utilities.Utilities;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author vasilije
 */
public class Text extends FormElement {

    private String value;
    private String validationRegex;

    public Text(Panel panel, String id, boolean mandatory, String label, String conditionId) {
        super(panel, Type.TEXT, id, mandatory, label, conditionId);
        this.value = "";
        this.validationRegex = null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void setDefaultValue(String defaultValue) {
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        touch();
    }

    @Override
    public boolean isEmpty() {
        return (value == null || "".equals(value));
    }

    @Override
    public void setValidationRegex(String validationRegex) {
        this.validationRegex = validationRegex;
    }

    @Override
    public boolean isValid() {
        boolean valueValid = true;
        if (value != null && validationRegex != null) {
            Pattern pattern = Pattern.compile(validationRegex);
            Matcher matcher = pattern.matcher(value);
            valueValid = matcher.find();
        }
        return valueValid && !(isEmpty() && mandatory);
    }

    @Override
    public void setByTrigger(String value) {
        this.value = value;
        push();
    }

    @Override
    public boolean match(String value) {
        return (this.value.equals(value));
    }

    @Override
    public void clear() {
        this.value = "";
    }

    @Override
    public void remove(String value) {
        if (this.value.equals(value)) {
            this.value = "";
        }
    }

    @Override
    public void sync(FormElement element) {
        switch (element.getType()) {
            case TEXT:
                this.value = ((Text) element).getValue();
                break;
            case ONELINE:
                this.value = ((OneLine) element).getValue();
                break;
            case INTEGER:
                this.value = ((IntegerInput) element).getValue() + "";
                break;
            case DOUBLE:
                this.value = ((DoubleInput) element).getValue() + "";
                break;
            case DATE:
                this.value = ((DateInput) element).getValue() + "";
                break;
        }
    }

    @Override
    public String getXml() {
        return "<field type=\"text\" id=\"" + getId() + "\"><value>" + Utilities.xmlEscape(value) + "</value></field>";
    }

}
