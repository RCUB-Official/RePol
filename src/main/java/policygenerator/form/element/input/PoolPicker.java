package policygenerator.form.element.input;

import policygenerator.form.element.SelectionElement;
import policygenerator.form.element.Panel;
import framework.settings.RepolSettings;
import framework.utilities.xml.XMLUtilities;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import policygenerator.form.element.ListFactory;
import policygenerator.form.condition.exceptions.ConditionNotFoundException;
import policygenerator.form.element.exceptions.ElementNotFoundException;
import policygenerator.form.element.exceptions.MisconfiguredSelectionList;

public final class PoolPicker extends FormElement {

    private final List<SelectionElement> availableValues;
    private final List<SelectionElement> displayedList;
    private final List<SelectionElement> selectedValues;
    private String toBeAdded;

    PoolPicker(Panel panel, String id, Set<String> idAliases, boolean mandatory, String label, String conditionId, String validationRegex, String validationMessage, String listId) throws MisconfiguredSelectionList {
        super(panel, Type.POOLPICKER, id, idAliases, mandatory, label, conditionId, validationRegex, validationMessage);
        availableValues = new LinkedList<>();

        List<SelectionElement> fetchedList = ListFactory.getInstance().getSelectionList(listId);

        if (fetchedList != null) {
            for (SelectionElement se : fetchedList) {
                availableValues.add(new SelectionElement(this, se.getLabel(), se.getValue()));
            }
        } else if (listId != null) {
            throw new MisconfiguredSelectionList(listId);
        }

        this.displayedList = new LinkedList<>();
        this.displayedList.addAll(availableValues);

        this.selectedValues = new LinkedList<>();
        this.toBeAdded = "";
    }

    @Override
    public void set(String value) {
        boolean found = false;
        if (availableValues != null) {
            for (SelectionElement av : availableValues) {
                if (av.getValue().equals(value)) {
                    found = true;
                    selectedValues.add(av);
                    availableValues.remove(av);
                    displayedList.remove(av);
                    break;
                }
            }
        }
        if (!found) {
            selectedValues.add(new SelectionElement(this, null, value));
        }
    }

    @Override
    public boolean isEmpty() {
        return selectedValues.isEmpty();
    }

    @Override
    public boolean isRegexValid() {
        return true;
    }

    @Override
    public boolean match(String value) {
        boolean match = false;
        for (SelectionElement se : selectedValues) {
            if (se.getValue().equals(value)) {
                match = true;
                break;
            }
        }
        return match;
    }

    public void search() {
        displayedList.clear();
        for (SelectionElement av : availableValues) {
            if (av.getLabel().contains(toBeAdded) || av.getValue().contains(toBeAdded)) {
                displayedList.add(av);
            }
        }
    }

    public void reload() {
        displayedList.clear();
        displayedList.addAll(availableValues);
        toBeAdded = "";
    }

    public void addNew() throws ElementNotFoundException, ConditionNotFoundException {
        // TODO: Validation Regex

        selectedValues.add(new SelectionElement(this, null, toBeAdded));
        reload();
        toBeAdded = "";
        processTriggers();
    }

    public String getToBeAdded() {
        return toBeAdded;
    }

    public void setToBeAdded(String toBeAdded) {
        this.toBeAdded = toBeAdded;
    }

    public List<SelectionElement> getAvailableValues() {
        return availableValues;
    }

    public List<SelectionElement> getDisplayedList() {
        return displayedList;
    }

    public List<SelectionElement> getSelectedValues() {
        return selectedValues;
    }

    public List<String> getValues() {
        List<String> values = new LinkedList<>();
        for (SelectionElement se : selectedValues) {
            values.add(se.getValue());
        }
        return values;
    }

    public List<SelectionElement> getAllPossibleValues() {
        List<SelectionElement> possibleValues = new LinkedList<>();
        possibleValues.addAll(selectedValues);
        possibleValues.addAll(availableValues);
        return possibleValues;
    }

    @Override
    public List<String> getValue() {
        return getValues();
    }

    @Override   // List is never null
    public List<String> getSafeValue() {
        return getValues();
    }

    @Override
    public void clear() {
        toBeAdded = "";
        for (SelectionElement se : selectedValues) {
            if (se.getLabel() != null) {
                availableValues.add(se);
            }
        }
        selectedValues.clear();
        displayedList.clear();
        displayedList.addAll(availableValues);
    }

    @Override
    public void remove(String value) {
        for (SelectionElement se : selectedValues) {
            if (se.getValue().equals(value)) {
                if (se.getLabel() != null) {
                    availableValues.add(se);
                }
                selectedValues.remove(se);
            }
        }
        displayedList.clear();
        displayedList.addAll(availableValues);
    }

    @Override
    public void sync(FormElement element) {
        clear();

        String listDelimiter = RepolSettings.getInstance().getListDelimiter();
        String obtainedValue;

        switch (element.getType()) {
            case POOLPICKER:
                for (String v : ((PoolPicker) element).getValues()) {
                    set(v);
                }
                break;
            case ADDLIST:
                for (String v : ((AddList) element).getValues()) {
                    set(v);
                }
                break;
            case SELECTMANY:
                for (String v : ((SelectMany) element).getValues()) {
                    set(v);
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

        if (!getValues().isEmpty()) {
            xml = "<field type=\"poolpicker\" id=\"" + getId() + "\"" + formId + ">";
            for (String value : getValues()) {
                xml += "<value>" + XMLUtilities.xmlEscape(value) + "</value>";
            }
            xml += "</field>";
        } else {
            xml = "";
        }
        return xml;
    }

    @Override
    public Set<String> getXmlForAliases(Set<String> skipIds) {
        Set<String> aliases = this.getIdAliases();
        Set<String> xmlForAliases = new HashSet<>();
        for (String alias : aliases) {
            String xml;
            if (!skipIds.contains(alias) && !getId().equals(alias)) {
                if (!getValues().isEmpty()) {
                    xml = "\n\t<field type=\"poolpicker\" id=\"" + getId() + "\">";
                    for (String value : getValues()) {
                        xml += "<value>" + XMLUtilities.xmlEscape(value) + "</value>";
                    }
                    xml += "</field>";
                } else {
                    xml = "";
                }
                if (!xml.isEmpty()) {
                    xmlForAliases.add(xml);
                }
            }
        }
        return xmlForAliases;
    }

}
