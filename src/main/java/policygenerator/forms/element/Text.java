package policygenerator.forms.element;

import framework.settings.RepolSettings;
import framework.utilities.Utilities;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Text extends FormElement {

    private String value;

    public Text(Panel panel, String id, boolean mandatory, String label, String conditionId, String validationRegex, String validationMessage) {
        super(panel, Type.TEXT, id, mandatory, label, conditionId, validationRegex, validationMessage);
        this.value = "";
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void set(String value) {
        this.value = value;
    }

    @Override
    public boolean isEmpty() {
        return (value == null || "".equals(value));
    }

    @Override
    public boolean isRegexValid() {
        boolean valueValid = true;
        if (value != null && validationRegex != null) {
            Pattern pattern = Pattern.compile(validationRegex);
            Matcher matcher = pattern.matcher(value);
            valueValid = matcher.find();
        }
        return valueValid;
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

        String listDelimiter = RepolSettings.getInstance().getListDelimiter();
        String merged = "";

        switch (element.getType()) {
            case TEXT:
                set(((Text) element).getValue());
                break;
            case ONELINE:
                set(((OneLine) element).getValue());
                break;
            case INTEGER:
                set(((IntegerInput) element).getValue() + "");
                break;
            case DOUBLE:
                set(((DoubleInput) element).getValue() + "");
                break;
            case DATE:
                set(((DateInput) element).getValue() + "");
                break;
            case BOOLEAN:
                set(((BooleanCheckbox) element).isValue() ? "true" : "false");
                break;
            case SELECTONE:
                set(((SelectOne) element).getValue());
                break;
            case ADDLIST:
                for (String v : ((AddList) element).getValues()) {
                    merged += ((!merged.equals("")) ? listDelimiter : "") + v;
                }
                set(merged);
                break;
            case SELECTMANY:
                for (String v : ((SelectMany) element).getValues()) {
                    merged += ((!merged.equals("")) ? listDelimiter : "") + v;
                }
                set(merged);
                break;
            case POOLPICKER:
                for (String v : ((PoolPicker) element).getValues()) {
                    merged += ((!merged.equals("")) ? listDelimiter : "") + v;
                }
                set(merged);
                break;
        }
    }

    @Override
    public String getXml() {
        return "<field type=\"text\" id=\"" + getId() + "\"><value>" + Utilities.xmlEscape(value) + "</value></field>";
    }

}
