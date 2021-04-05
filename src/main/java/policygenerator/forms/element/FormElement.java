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
import java.util.logging.Level;
import java.util.logging.Logger;
import policygenerator.forms.DataShare;
import policygenerator.forms.Trigger;
import policygenerator.forms.condition.Condition;
import policygenerator.forms.condition.exceptions.ConditionNotFoundException;

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

    protected final boolean mandatory;

    private final String label;
    private String tooltip;
    private String description;

    private final String conditionId;

    List<Trigger> triggers;
    Map<String, Condition> conditions;

    protected String defaultValue;

    protected String validationRegex;
    protected String validationMessage;

    private boolean userSet;

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
        defaultValue = null;

        validationRegex = null;
        validationMessage = null;

        userSet = false;
    }

    public final Type getType() {
        return type;
    }

    public final String getId() {
        return id;
    }

    public final String getLabel() {
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

    public String getDefaultValue() {
        return defaultValue;
    }

    //VALUE FUNCTIONS
    public abstract void set(String value);

    public abstract void clear();

    public abstract void remove(String value);

    public abstract boolean match(String value);

    public final void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        set(defaultValue);
        touch();
    }

    public final void setByTrigger(String value) {
        set(value);
        userSet = true;
        push();
    }
    
    public final void clearByTrigger() {
        clear();
        userSet = true;
        push();
    }

    public final void resetToDefault() {
        clear();
        set(defaultValue);
        push();
    }

    //VALIDATION
    public abstract boolean isEmpty();

    public abstract boolean isRegexValid();

    public final boolean isUserSet() {
        return userSet;
    }

    public final boolean isValid() {
        return (!mandatory || !isEmpty()) && (validationRegex == null || isRegexValid());
    }

    public final boolean isMandatory() {
        return mandatory;
    }

    public void setValidationRegex(String validationRegex) {
        this.validationRegex = validationRegex;
    }

    // CONDITION FUNCTIONS
    public void addCondition(String conditionId, Condition condition) {
        this.conditions.put(id, condition);
    }

    public String getConditionId() {
        return conditionId;
    }

    public Condition getCondition() throws ConditionNotFoundException {
        if (conditionId == null) {
            return null;
        } else {
            return panel.getForm().getCondition(conditionId);
        }
    }

    public final boolean isRendered() throws ConditionNotFoundException {
        if (conditionId == null) {
            return true;
        } else {
            return panel.getForm().getCondition(conditionId).evaluate();
        }
    }

    // TRIGGER FUNCTIONS
    public List<Trigger> getTriggers() {
        return triggers;
    }

    public List<String> getTriggerConditionIds() {
        List<String> conditionIds = new LinkedList<String>();
        for (Trigger t : triggers) {
            conditionIds.add(t.getConditionId());
        }
        return conditionIds;
    }

    public void addTrigger(Trigger trigger) {
        this.triggers.add(trigger);
    }

    public void processTriggers() {
        for (Trigger t : triggers) {
            try {
                if (panel.getForm().getCondition(t.getConditionId()).evaluate()) {
                    panel.getForm().processTrigger(t.getTargetId(), t.getOperation(), t.getValue());
                }
            } catch (Exception ex) {
                Logger.getLogger(FormElement.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        userSet = true;
        push();
    }

    // DATASHARE FUNCTIONS
    protected abstract void sync(FormElement element);

    public void syncElement(FormElement element) {
        sync(element);
        userSet = true;
    }

    private final void push() {
        DataShare myShare = (DataShare) Utilities.getObject("#{dataShare}");
        myShare.push(this);
    }

    private final void touch() {
        DataShare myShare = (DataShare) Utilities.getObject("#{dataShare}");
        myShare.touch(this);
    }

    // For embedded and standalone export
    public abstract String getXml();

}
