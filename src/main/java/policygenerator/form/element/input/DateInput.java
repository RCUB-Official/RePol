package policygenerator.form.element.input;

import framework.utilities.xml.XMLUtilities;
import policygenerator.form.element.Panel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public final class DateInput extends FormElement {

    private Date value;

    DateInput(Panel panel, String id, Set<String> idAliases, boolean mandatory, String label, String conditionId) {
        super(panel, Type.DATE, id, idAliases, mandatory, label, conditionId, null, null); // TODO: regex validation
        this.value = new Date(System.currentTimeMillis());
    }

    // Getter and setter for UI interaction
    @Override   // Also for FreeMarker
    public Date getValue() {
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }

    // Safe value
    @Override
    public Date getSafeValue() {    // returns 1970-01-01 if (value == null)
        if (value == null) {
            return new Date(0);
        } else {
            return value;
        }
    }

    @Override
    public void set(String value) {
//        System.out.println("'" + value + "'");
            if ("current_date".equals(value)) {
                this.value = new Date(System.currentTimeMillis());
            } else {
                try {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    this.value = format.parse(value);
//                    System.out.println("yyyy-MM-dd");
                } catch (ParseException e1) {
                    try {
                        // Thu Dec 02 00:00:00 CET 2021
                        DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
                        this.value = format.parse(value);
//                        System.out.println("EEE MMM dd HH:mm:ss Z yyyy");
                    } catch (ParseException e2) {
                        this.value = null;
                    }
                }
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
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String stringValue = Objects.nonNull(this.value) ? dateFormat.format(value) : "null";
        return "<field type=\"date\" id=\"" + getId() + "\"" + formId + "><value>" + stringValue + "</value></field>";
    }

    @Override
    public Set<String> getXmlForAliases(Set<String> skipIds) {
        Set<String> aliases = this.getIdAliases();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String stringValue = Objects.nonNull(this.value) ? dateFormat.format(value) : "null";
        Set<String> xmlForAliases = new HashSet<String>();
        for (String alias : aliases) {
            if (!skipIds.contains(alias) && !getId().equals(alias)) {
                xmlForAliases.add("\n\t<field type=\"date\" id=\"" + alias + "\"><value>" + stringValue + "</value></field>");
            }
        }
        return xmlForAliases;
    }

}
