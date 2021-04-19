package policygenerator.form.element;

import java.util.LinkedList;
import java.util.List;
import policygenerator.form.Form;
import policygenerator.form.condition.exceptions.ConditionNotFoundException;
import policygenerator.form.element.input.FormElement;

public final class Panel {

    private final Form form;
    private final String label;
    private final String conditionId;
    private final List<FormElement> elements;

    protected Panel(Form form, String label, String conditionId) {
        this.form = form;
        this.label = label;
        this.conditionId = conditionId;
        this.elements = new LinkedList<>();
    }

    public String getLabel() {
        return label;
    }

    public void addElements(List<FormElement> elements) {
        this.elements.clear();
        this.elements.addAll(elements);
    }

    public List<FormElement> getElements() throws ConditionNotFoundException {
        List<FormElement> list = new LinkedList<>();
        list.addAll(elements);
        return list;
    }

    public Form getForm() {
        return form;
    }

    public boolean isRendered() throws ConditionNotFoundException {
        if (conditionId == null) {
            return true;
        } else {
            return form.getCondition(conditionId).evaluate();
        }
    }

}
