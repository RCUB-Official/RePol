package policygenerator.form.element.input;

import framework.settings.RepolSettings;
import policygenerator.form.element.Panel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateInput extends FormElement {

    private Date value;

    protected DateInput(Panel panel, String id, boolean mandatory, String label, String conditionId) {
        super(panel, Type.DATE, id, mandatory, label, conditionId, null, null); // TODO: regex validation
        this.value = new Date(System.currentTimeMillis());
    }

    @Override
    public Date getValue() {
        if (value == null && RepolSettings.getInstance().isUseSafeDefaults()) {
            return new Date(0);
        } else {
            return value;
        }
    }

    public void setValue(Date value) {
        this.value = value;
    }

    @Override
    public void set(String value) {
        try {
            if ("current_date".equals(value)) {
                this.value = new Date(System.currentTimeMillis());
            } else {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                this.value = format.parse(value);
            }
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(this.value).contains(value);
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
            case DATE:
                this.value = ((DateInput) element).getValue();
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
    public String getXml(boolean includeFormId) {
        String formId;
        if (getForm() != null && includeFormId) {
            formId = " form=\"" + getForm().getId() + "\"";
        } else {
            formId = "";
        }

        return "<field type=\"date\" id=\"" + getId() + "\"" + formId + "><value>" + value + "</value></field>";
    }

}
