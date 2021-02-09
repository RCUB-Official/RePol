/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policygenerator.forms.element;

import policygenerator.forms.condition.exceptions.ConditionNotFoundException;
import policygenerator.forms.element.exceptions.ElementNotFoundException;

/**
 *
 * @author vasilije
 */
public class SelectionElement {

    private final FormElement element;
    private final String label;
    private final String value;
    private boolean selected = false;

    public SelectionElement(FormElement element, String label, String value) {
        this.element = element;
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        element.push();
    }

    @Override
    public String toString() {
        if (label != null && !"".equals(label)) {
            return label;
        } else {
            return value;
        }
    }

    public void select() throws ElementNotFoundException, ConditionNotFoundException {
        this.selected = true;
        if (element instanceof PoolPicker) {
            PoolPicker pp = (PoolPicker) element;
            pp.getAvailableValues().remove(this);
            pp.getDisplayedList().remove(this);
            pp.getSelectedValues().add(this);
            pp.reload();
        }
        element.processTriggers();
    }

    public void deselect() throws ElementNotFoundException, ConditionNotFoundException {
        this.selected = false;
        if (element instanceof PoolPicker) {
            PoolPicker pp = (PoolPicker) element;
            pp.getSelectedValues().remove(this);
            if (label != null) {
                pp.getAvailableValues().add(this);
                pp.reload();
            }
        }
        element.processTriggers();
    }
}
