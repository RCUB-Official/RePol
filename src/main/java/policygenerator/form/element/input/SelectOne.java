package policygenerator.form.element.input;

import policygenerator.form.element.SelectionElement;
import policygenerator.form.element.Panel;
import framework.utilities.xml.XMLUtilities;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import policygenerator.form.element.ListFactory;
import policygenerator.form.element.exceptions.MisconfiguredSelectionList;

public final class SelectOne extends FormElement {

    private final List<SelectionElement> availableValues;
    private String value;

    SelectOne(Panel panel, String id, Set<String> idAliases, boolean mandatory, String label, String conditionId, String listId) throws MisconfiguredSelectionList {
        super(panel, Type.SELECTONE, id, idAliases, mandatory, label, conditionId, null, null);
        availableValues = new LinkedList<>();

        List<SelectionElement> fetchedList = ListFactory.getInstance().getSelectionList(listId);

        if (fetchedList != null) {
            for (SelectionElement se : fetchedList) {
                availableValues.add(new SelectionElement(this, se.getLabel(), se.getValue()));
            }
        } else if (listId != null) {
            throw new MisconfiguredSelectionList(listId);
        }

        if (!availableValues.isEmpty()) {
            this.value = availableValues.get(0).getValue();
        } else {
            this.value = null;
        }
    }

    // Getter and setter for UI interaction
    @Override   // Also for FreeMarker
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getSafeValue() {
        if (value == null) {    // Returns empty String if (value == null)
            return "";
        } else {
            return value;
        }
    }

    @Override
    public void set(String value) {
        for (SelectionElement se : availableValues) {
            if (se.getValue().equals(value)) {
                this.value = value;
                break;
            }
        }
    }

    public List<SelectionElement> getAvailableValues() {
        return availableValues;
    }

    public List<SelectionElement> getAllPossibleValues() {
        return availableValues;
    }

    @Override
    public boolean isEmpty() {
        return (value == null);
    }

    @Override
    public boolean isRegexValid() {
        return true;
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
                set(((SelectOne) element).getValue());
                break;
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
        }
    }

    @Override
    public String getXml(boolean includeFormId) {
        if (value != null) {
            String formId;
            if (getForm() != null && includeFormId) {
                formId = " form=\"" + getForm().getId() + "\"";
            } else {
                formId = "";
            }

            return "<field type=\"oneline\" id=\"" + getId() + "\"" + formId + "><value>" + XMLUtilities.xmlEscape(value) + "</value></field>";
        } else {
            return "";
        }
    }

    @Override
    public Set<String> getXmlForAliases() {
        Set<String> aliases = this.getIdAliases();
        Set<String> xmlForAliases = new HashSet<>();
        for (String alias : aliases) {
            xmlForAliases.add("<field type=\"oneline\" id=\"" + alias + "\"><value>" + XMLUtilities.xmlEscape(value) + "</value></field>");
        }
        return xmlForAliases;
    }

}
