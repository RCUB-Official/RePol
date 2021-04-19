package policygenerator.form.element;

import policygenerator.form.element.input.FormElement;
import policygenerator.form.element.input.PoolPicker;

public final class SelectionElement {

    private final FormElement element;
    private final String label;
    private final String value;
    private boolean selected = false;

    public SelectionElement(String label, String value) {
        this.element = null;
        this.label = label;
        this.value = value;
    }

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
    }

    @Override
    public String toString() {
        if (label != null && !"".equals(label)) {
            return label;
        } else {
            return value;
        }
    }

    public void select() {
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

    public void deselect() {
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
