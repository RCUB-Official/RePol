/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policygenerator.forms.element;

import framework.utilities.Utilities;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import policygenerator.forms.condition.exceptions.ConditionNotFoundException;
import policygenerator.forms.element.exceptions.ElementNotFoundException;

/**
 *
 * @author vasilije
 */
public class AddList extends FormElement {

    private final List<String> values;
    private String toBeAdded;

    public AddList(Panel panel, String id, boolean mandatory, String label, String conditionId) {
        super(panel, Type.ADDLIST, id, mandatory, label, conditionId);
        this.values = new LinkedList<String>();
        this.toBeAdded = "";
    }

    public void add() throws ElementNotFoundException, ConditionNotFoundException {
        set(toBeAdded);
        toBeAdded = "";
        processTriggers();
    }

    public List<String> getValues() {
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
                set(((OneLine) element).getValue());
                break;
            case TEXT:
                set(((Text) element).getValue());
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
    public String getXml() {
        String xml;
        if (!values.isEmpty()) {
            xml = "<field type=\"addlist\" id=\"" + getId() + "\">";
            for (String value : values) {
                xml += "<value>" + Utilities.xmlEscape(value) + "</value>";
            }
            xml += "</field>";
        } else {
            xml = "";
        }
        return xml;
    }

}
