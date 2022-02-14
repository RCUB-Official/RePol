package policygenerator.form.element.input;

import framework.utilities.xml.XMLUtilities;
import policygenerator.form.element.Panel;

import java.util.HashSet;
import java.util.Set;

public final class BooleanCheckbox extends FormElement {

    private boolean value;

    BooleanCheckbox(Panel panel, String id, Set<String> idAliases, boolean mandatory, String label, String conditionId) {
        super(panel, Type.BOOLEAN, id, idAliases, mandatory, label, conditionId, null, null);
        this.value = false;
        defaultValues.add("false");
    }

    // Getter and setter for UI interaction
    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    // getValue() and getSafeValue() for FreeMarker
    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public Boolean getSafeValue() {     // Primitive boolean type is never null
        return value;
    }

    @Override
    public void set(String value) {
        this.value = "true".equalsIgnoreCase(value);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isRegexValid() {
        return true;
    }

    @Override
    public boolean match(String value) {
        return (this.value == "true".equalsIgnoreCase(value));
    }

    @Override
    public void clear() {
        this.value = false;
    }

    @Override
    public void remove(String value) {
    }

    @Override
    public void sync(FormElement element) {
        switch (element.getType()) {
            case BOOLEAN:
                this.value = ((BooleanCheckbox) element).isValue();
                break;
            case ONELINE:
                this.value = "true".equalsIgnoreCase(((OneLine) element).getValue());
                break;
            case TEXT:
                this.value = "true".equalsIgnoreCase(((Text) element).getValue());
                break;
            case SELECTONE:
                this.value = "true".equalsIgnoreCase(((SelectOne) element).getValue());
                break;
            case INTEGER:   //C-like logic
                this.value = (((IntegerInput) element).getValue() != 0);
                break;
            case DOUBLE:    //Also C-like logic
                this.value = (((DoubleInput) element).getValue() != 0);
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

        return "<field type=\"boolean\" id=\"" + getId() + "\"" + formId + "><value>" + value + "</value></field>";
    }


    @Override
    public Set<String> getXmlForAliases() {
        Set<String> aliases = this.getIdAliases();
        Set<String> xmlForAliases = new HashSet<>();
        for (String alias : aliases) {
            xmlForAliases.add("<field type=\"boolean\" id=\"" + alias + "\"><value>" + value + "</value></field>");
        }
        return xmlForAliases;
    }

}
