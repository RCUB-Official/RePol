/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policygenerator.forms.element;

import framework.settings.RepolSettings;
import framework.utilities.Utilities;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author vasilije
 */
public class SelectMany extends FormElement {

    private List<SelectionElement> availableValues;

    public SelectMany(Panel panel, String id, boolean mandatory, String label, String conditionId) {
        super(panel, Type.SELECTMANY, id, mandatory, label, conditionId);
        this.availableValues = null;
    }

    public List<SelectionElement> getAvailableValues() {
        return availableValues;
    }

    public void setAvailableValues(List<SelectionElement> availableValues) {
        this.availableValues = availableValues;
    }

    public List<String> getValues() {
        List<String> list = new LinkedList<String>();
        for (SelectionElement se : availableValues) {
            if (se.isSelected()) {
                list.add(se.getValue());
            }
        }
        return list;
    }

    @Override
    public void set(String value) {
        for (SelectionElement se : availableValues) {
            if (se.getValue().equals(value)) {
                se.setSelected(true);
                break;
            }
        }
    }

    @Override
    public boolean isEmpty() {
        boolean empty = true;
        for (SelectionElement se : availableValues) {
            if (se.isSelected()) {
                empty = false;
                break;
            }
        }
        return empty;
    }

    @Override
    public boolean isRegexValid() {
        return true;
    }

    @Override
    public boolean match(String value) {
        boolean match = false;
        for (SelectionElement se : availableValues) {
            if (se.getValue().equals(value) && se.isSelected()) {
                match = true;
                break;
            }
        }
        return match;
    }

    @Override
    public void clear() {
        for (SelectionElement se : availableValues) {
            se.setSelected(false);
        }
    }

    @Override
    public void remove(String value) {
        for (SelectionElement se : availableValues) {
            if (se.getValue().equals(value)) {
                se.setSelected(false);
            }
        }
    }

    @Override
    public void sync(FormElement element) {
        clear();

        String listDelimiter = RepolSettings.getInstance().getListDelimiter();
        String obtainedValue;

        switch (element.getType()) {
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
            case SELECTONE:
                set(((SelectOne) element).getValue());
                break;
            case SELECTMANY:
                for (String value : ((SelectMany) element).getValues()) {
                    set(value);
                }
                break;
            case POOLPICKER:
                for (String value : ((PoolPicker) element).getValues()) {
                    set(value);
                }
                break;
        }
    }

    @Override
    public String getXml() {
        String xml;
        if (!availableValues.isEmpty()) {
            xml = "<field type=\"selectmany\" id=\"" + getId() + "\">";
            for (SelectionElement av : availableValues) {
                if (av.isSelected()) {
                    xml += "<value>" + Utilities.xmlEscape(av.getValue()) + "</value>";
                }
            }
            xml += "</field>";
        } else {
            xml = "";
        }
        return xml;
    }
}
