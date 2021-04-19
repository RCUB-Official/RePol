package policygenerator.form.element.input;

import framework.settings.RepolSettings;
import policygenerator.form.element.Panel;

public final class IntegerInput extends FormElement {

    private Integer value;

    protected IntegerInput(Panel panel, String id, boolean mandatory, String label, String conditionId) {
        super(panel, Type.INTEGER, id, mandatory, label, conditionId, null, null);  // TODO: regex validation
        this.value = null;
    }

    @Override
    public Integer getValue() {
        if (value == null && RepolSettings.getInstance().isUseSafeDefaults()) {
            return 0;
        } else {
            return value;
        }
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
        // TODO: regex validation toString()
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
