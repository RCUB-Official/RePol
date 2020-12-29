/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policygenerator.forms.element;

import framework.utilities.Utilities;
import java.util.LinkedList;
import java.util.List;
import policygenerator.forms.condition.exceptions.ConditionNotFoundException;
import policygenerator.forms.element.exceptions.ElementNotFoundException;

/**
 *
 * @author vasilije
 */
public class PoolPicker extends FormElement {

    private List<SelectionElement> availableValues;
    private final List<SelectionElement> displayedList;
    private final List<SelectionElement> selectedValues;
    private String toBeAdded;

    public PoolPicker(Panel panel, String id, boolean mandatory, String label, String conditionId) {
        super(panel, Type.POOLPICKER, id, mandatory, label, conditionId);

        this.availableValues = null;

        this.displayedList = new LinkedList<SelectionElement>();
        this.selectedValues = new LinkedList<SelectionElement>();
        this.toBeAdded = "";
    }

    @Override
    public void setDefaultValue(String defaultValue) {
        boolean found = false;
        if (availableValues != null) {
            for (SelectionElement av : availableValues) {
                if (av.getValue().equals(defaultValue)) {
                    found = true;
                    selectedValues.add(av);
                    availableValues.remove(av);
                    displayedList.remove(av);
                    break;
                }
            }
        }
        if (!found) {
            selectedValues.add(new SelectionElement(this, null, defaultValue));
        }
        touch();
    }

    @Override
    public void setValidationRegex(String validationRegex) {
    }

    @Override
    public boolean isEmpty() {
        return selectedValues.isEmpty();
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void setByTrigger(String value) {
        setDefaultValue(value);
        touch();
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

    public void setAvailableValues(List<SelectionElement> availableValues) {
        this.availableValues = availableValues;
        this.displayedList.addAll(availableValues);
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
        List<String> values = new LinkedList<String>();
        for (SelectionElement se : selectedValues) {
            values.add(se.getValue());
        }
        return values;
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
        switch (element.getType()) {
            case POOLPICKER:
                for (String v : ((PoolPicker) element).getValues()) {
                    setByTrigger(v);
                }
                break;
            case ADDLIST:
                for (String v : ((AddList) element).getValues()) {
                    setByTrigger(v);
                }
                break;
            case TEXT:
                setByTrigger(((Text) element).getValue());
                break;
            case ONELINE:
                setByTrigger(((OneLine) element).getValue());
                break;
            case INTEGER:
                setByTrigger(((IntegerInput) element).getValue() + "");
                break;
            case DOUBLE:
                setByTrigger(((DoubleInput) element).getValue() + "");
                break;
            case DATE:
                setByTrigger(((DateInput) element).getValue() + "");
                break;
        }
    }

    @Override
    public String getXml() {
        String xml;
        if (!getValues().isEmpty()) {
            xml = "<field type=\"poolpicker\" id=\"" + getId() + "\">";
            for (String value : getValues()) {
                xml += "<value>" + Utilities.xmlEscape(value) + "</value>";
            }
            xml += "</field>";
        } else {
            xml = "";
        }
        return xml;
    }

}
