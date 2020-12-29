/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policygenerator.forms.element;

import java.util.LinkedList;
import java.util.List;
import policygenerator.forms.Form;
import policygenerator.forms.condition.exceptions.ConditionNotFoundException;

/**
 *
 * @author vasilije
 */
public class Panel {

    private final Form form;
    private final String label;
    private final String conditionId;
    private final List<FormElement> elements;

    public Panel(Form form, String label, String conditionId) {
        this.form = form;
        this.label = label;
        this.conditionId = conditionId;
        this.elements = new LinkedList<FormElement>();
    }

    public String getLabel() {
        return label;
    }

    public void addElements(List<FormElement> elements) {
        this.elements.clear();
        this.elements.addAll(elements);
    }

    public List<FormElement> getElements() throws ConditionNotFoundException {
        List<FormElement> list = new LinkedList<FormElement>();
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
