package policygenerator.form.element.input;

import policygenerator.form.element.Panel;

import java.util.HashSet;
import java.util.Set;

public final class DoubleInput extends FormElement {

    private Double value;

    DoubleInput(Panel panel, String id, Set<String> idAliases, boolean mandatory, String label, String conditionId) {
        super(panel, Type.DOUBLE, id, idAliases, mandatory, label, conditionId, null, null); // TODO: regex validation
        this.value = null;
    }

    // Getter and setter for UI interaction
    @Override   // Also for FreeMarker
    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    // Safe value
    @Override
    public Double getSafeValue() {
        if (value == null) {    // Returns 0 if (value == null)
            return 0.0;
        } else {
            return value;
        }
    }

    @Override
    public void set(String value) {
        try {
            this.value = Double.parseDouble(value);
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
        // TODO: regex validate toString()
        return true;
    }

    @Override
    public boolean match(String value) {
        boolean match;
        try {
            match = (this.value == Double.parseDouble(value));
        } catch (Exception ex) {
            match = false;
        }
        return match;
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

        return "<field type=\"double\" id=\"" + getId() + "\"><value>" + value + "</value></field>";
    }

    @Override
    public Set<String> getXmlForAliases(Set<String> skipIds) {
        Set<String> aliases = this.getIdAliases();
        Set<String> xmlForAliases = new HashSet<>();
        for (String alias : aliases) {
            if (!skipIds.contains(alias) && !getId().equals(alias)) {
                xmlForAliases.add("\n\t<field type=\"double\" id=\"" + alias + "\"><value>" + value + "</value></field>");
            }
        }
        return xmlForAliases;
    }

}
