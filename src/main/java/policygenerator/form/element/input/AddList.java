package policygenerator.form.element.input;

import policygenerator.form.element.SelectionElement;
import policygenerator.form.element.Panel;
import framework.settings.RepolSettings;
import framework.utilities.xml.XMLUtilities;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import policygenerator.form.condition.exceptions.ConditionNotFoundException;
import policygenerator.form.element.exceptions.ElementNotFoundException;

public final class AddList extends FormElement {

    private final List<String> values = new LinkedList<>();
    private String toBeAdded;

    AddList(Panel panel, String id, boolean mandatory, String label, String conditionId, String validationRegex, String validationMessage) {
        super(panel, Type.ADDLIST, id, mandatory, label, conditionId, validationRegex, validationMessage);
        this.toBeAdded = "";
    }

    public void add() throws ElementNotFoundException, ConditionNotFoundException {
        // TODO: if regex valid
        set(toBeAdded);
        toBeAdded = "";
        processTriggers();
    }

    public List<String> getValues() {
        return values;
    }

    @Override
    public List<String> getValue() {
        return values;
    }

    @Override
    public List<String> getSafeValue() {    // Empty list is never null
        return values;
    }

    public String getToBeAdded() {
        return toBeAdded;
    }

    public void setToBeAdded(String toBeAdded) {
        this.toBeAdded = toBeAdded;
    }

    @Override
    public void set(String value) {
        if (!"".equals(value)) {
            boolean exists = false;
            for (String v : values) {
                if (v.equals(value)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                values.add(value);
            }
        }
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public boolean isRegexValid() {
        boolean allValuesValid = true;
        if (validationRegex != null & !isEmpty()) {
            Pattern pattern = Pattern.compile(validationRegex);
            for (String value : values) {
                Matcher matcher = pattern.matcher(value);
                if (!matcher.find()) {
                    allValuesValid = false;
                    break;
                }
            }
        }
        return allValuesValid;
    }

    @Override
    public boolean match(String value) {
        boolean trigger = false;
        for (String v : values) {
            if (v.equals(value)) {
                trigger = true;
                break;
            }
        }
        return trigger;
    }

    @Override
    public void clear() {
        values.clear();
    }

    @Override
    public void remove(String value) {
        for (String v : values) {
            if (v.equals(value)) {
                values.remove(v);
            }
        }
    }

    @Override
    public void sync(FormElement element) {
        clear();

        String listDelimiter = RepolSettings.getInstance().getListDelimiter();
        String obtainedValue;

        switch (element.getType()) {
            case ADDLIST:
                for (String s : ((AddList) element).getValues()) {
                    set(s);
                }
                break;
            case POOLPICKER:
                for (SelectionElement se : ((PoolPicker) element).getSelectedValues()) {
                    set(se.getValue());
                }
                break;
            case SELECTMANY:
                for (SelectionElement se : ((PoolPicker) element).getSelectedValues()) {
                    set(se.getValue());
                }
                break;
            case SELECTONE:
                set(((SelectOne) element).getValue());
                break;
            case ONELINE:
                obtainedValue = ((OneLine) element).getValue();
                if (obtainedValue.contains(listDelimiter)) {
                    for (String val : obtainedValue.split(listDelimiter)) {
                        set(val);
                    }
                } else {
                    set(obtainedValue);
                }
                break;
            case TEXT:
                obtainedValue = ((Text) element).getValue();
                if (obtainedValue.contains(listDelimiter)) {
                    for (String val : obtainedValue.split(listDelimiter)) {
                        set(val);
                    }
                } else {
                    set(obtainedValue);
                }
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
        }
    }

    @Override
    public String getXml(boolean includeFormId) {
        String xml;

        String formId;
        if (getForm() != null && includeFormId) {
            formId = " form=\"" + getForm().getId() + "\"";
        } else {
            formId = "";
        }

        if (!values.isEmpty()) {
            xml = "<field type=\"addlist\" id=\"" + getId() + "\"" + formId + ">";
            for (String value : values) {
                xml += "<value>" + XMLUtilities.xmlEscape(value) + "</value>";
            }
            xml += "</field>";
        } else {
            xml = "";
        }
        return xml;
    }

}
