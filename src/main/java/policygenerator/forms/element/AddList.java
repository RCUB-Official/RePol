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
    private String validationRegex;

    public AddList(Panel panel, String id, boolean mandatory, String label, String conditionId) {
        super(panel, Type.ADDLIST, id, mandatory, label, conditionId);
        this.values = new LinkedList<String>();
        this.toBeAdded = "";
        validationRegex = null;
    }

    public void add() throws ElementNotFoundException, ConditionNotFoundException {
        if (!"".equals(toBeAdded)) {
            values.add(toBeAdded);
            toBeAdded = "";
            processTriggers();
        }
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
    public void setDefaultValue(String defaultValue) {
        values.add(defaultValue);
        push();
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public void setValidationRegex(String validationRegex) {
        this.validationRegex = validationRegex;
    }

    @Override
    public boolean isValid() {
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
        return allValuesValid && !(isEmpty() && mandatory);
    }

    @Override
    public void setByTrigger(String value) {
        values.add(value);
        touch();
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
                    setByTrigger(s);
                }
                break;
            case POOLPICKER:
                for (SelectionElement se : ((PoolPicker) element).getSelectedValues()) {
                    setByTrigger(se.getValue());
                }
                break;
            case SELECTMANY:
                for (SelectionElement se : ((PoolPicker) element).getSelectedValues()) {
                    setByTrigger(se.getValue());
                }
                break;
            case ONELINE:
                setByTrigger(((OneLine) element).getValue());
                break;
            case TEXT:
                setByTrigger(((Text) element).getValue());
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
