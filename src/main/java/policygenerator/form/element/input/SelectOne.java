package policygenerator.form.element.input;

import framework.settings.RepolSettings;
import policygenerator.form.element.SelectionElement;
import policygenerator.form.element.Panel;
import framework.utilities.xml.XMLUtilities;
import java.util.LinkedList;
import java.util.List;
import policygenerator.form.element.ListFactory;
import policygenerator.form.element.exceptions.MisconfiguredSelectionList;

public final class SelectOne extends FormElement {

    private final List<SelectionElement> availableValues;
    private String value;

    protected SelectOne(Panel panel, String id, boolean mandatory, String label, String conditionId, String listId) throws MisconfiguredSelectionList {
        super(panel, Type.SELECTONE, id, mandatory, label, conditionId, null, null);
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

    @Override
    public String getValue() {
        if (value == null && RepolSettings.getInstance().isUseSafeDefaults()) {
            return "";
        } else {
            return value;
        }
    }

    public void setValue(String value) {
        this.value = value;
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
    public String getXml() {
        if (value != null) {
            return "<field type=\"oneline\" id=\"" + getId() + "\"><value>" + XMLUtilities.xmlEscape(value) + "</value></field>";
        } else {
            return "";
        }
    }

}
