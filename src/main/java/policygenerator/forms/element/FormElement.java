/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policygenerator.forms.element;

import framework.utilities.Utilities;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import policygenerator.forms.DataShare;
import policygenerator.forms.Trigger;
import policygenerator.forms.condition.Condition;
import policygenerator.forms.condition.exceptions.ConditionNotFoundException;
import policygenerator.forms.element.exceptions.ElementNotFoundException;

public abstract class FormElement {

    public enum Type {
        ONELINE,
        TEXT,
        BOOLEAN,
        INTEGER,
        DOUBLE,
        DATE,
        ADDLIST,
        SELECTONE,
        SELECTMANY,
        POOLPICKER,
        SEPARATOR,
    }

    private final Panel panel;

    private final Type type;
    private final String id;

    final boolean mandatory;

    private final String label;
    private String tooltip;
    private String description;

    private final String conditionId;

    List<Trigger> triggers;
    Map<String, Condition> conditions;

    FormElement(Panel panel, Type type, String id, boolean mandatory, String label, String conditionId) {
        this.panel = panel;
        this.type = type;
        this.id = id;
        this.mandatory = mandatory;
        this.label = label;
        this.conditionId = conditionId;

        this.tooltip = null;
        this.description = null;

        triggers = new LinkedList<Trigger>();
        conditions = new HashMap<String, Condition>();
    }

    public Type getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        if (label != null) {
            return label;
        } else {
            return id;
        }
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public abstract void setDefaultValue(String defaultValue);

    public abstract void setValidationRegex(String validationRegex);

    public abstract void clear();

    public abstract void remove(String value);

    public abstract boolean isEmpty();

    public boolean isMandatory() {
        return mandatory;
    }

    public abstract boolean isValid();

    public void addTrigger(Trigger trigger) {
        this.triggers.add(trigger);
    }

    public void addCondition(String conditionId, Condition condition) {
        this.conditions.put(id, condition);
    }

    public abstract void setByTrigger(String value);

    public abstract boolean match(String value);

    public void push() {
        DataShare myShare = (DataShare) Utilities.getObject("#{dataShare}");
        myShare.push(this);
    }

    public void touch() {
        DataShare myShare = (DataShare) Utilities.getObject("#{dataShare}");
        myShare.touch(this);
    }

    public void processTriggers() throws ElementNotFoundException, ConditionNotFoundException {
        for (Trigger t : triggers) {
            if (panel.getForm().getCondition(t.getConditionId()).evaluate()) {
                panel.getForm().processTrigger(t.getTargetId(), t.getOperation(), t.getValue());
            }
        }
        
        push();
    }

    public List<String> getTriggerConditionIds() {
        List<String> conditionIds = new LinkedList<String>();
        for (Trigger t : triggers) {
            conditionIds.add(t.getConditionId());
        }
        return conditionIds;
    }

    public boolean isRendered() throws ConditionNotFoundException {
        if (conditionId == null) {
            return true;
        } else {
            return panel.getForm().getCondition(conditionId).evaluate();
        }
    }

    public abstract void sync(FormElement element);

    public abstract String getXml();
}
