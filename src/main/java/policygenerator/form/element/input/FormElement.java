package policygenerator.form.element.input;

import policygenerator.form.element.Panel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import policygenerator.form.Form;
import policygenerator.form.trigger.Trigger;
import policygenerator.form.condition.Condition;
import policygenerator.form.condition.exceptions.ConditionNotFoundException;

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

    private static final Logger LOG = Logger.getLogger(FormElement.class.getName());

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

    protected FormElement(Panel panel, Type type, String id, boolean mandatory, String label, String conditionId, String validationRegex, String validationMessage) {
        this.panel = panel;
        this.type = type;
        this.id = id;
        this.mandatory = mandatory;
        this.label = label;
        this.conditionId = conditionId;

        this.validationRegex = validationRegex;
        this.validationMessage = validationMessage;

        this.tooltip = null;
        this.description = null;

        triggers = new LinkedList<>();
        conditions = new HashMap<>();
        defaultValue = null;

        userSet = false;
    }

    public final Form getForm() {
        if (panel != null) {
            return panel.getForm();
        } else {
            return null;
        }
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
    public abstract Object getValue();  // For FreeMarker model

    public abstract void set(String value);

    public abstract void clear();

    public abstract void remove(String value);

    public abstract boolean match(String value);

    public final void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        set(defaultValue);
        touch();
    }

    public final void setByUpload(String value) {
        set(value);
        userSet = true;
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

    public final void setUserSet() {
        userSet = true;
    }

    public final boolean isValid() throws ConditionNotFoundException {
        return ((!mandatory || !isEmpty()) && (validationRegex == null || isRegexValid())) || !isRendered();
    }

    public final boolean isMandatory() {
        return mandatory;
    }

    public String getValidationRegex() {
        return validationRegex;
    }

    public String getValidationMessage() {
        return validationMessage;
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
            return panel.isRendered();
        } else {
            return panel.getForm().getCondition(conditionId).evaluate();
        }
    }

    // TRIGGER FUNCTIONS
    public List<Trigger> getTriggers() {
        return triggers;
    }

    public List<String> getTriggerConditionIds() {
        List<String> conditionIds = new LinkedList<>();
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
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        userSet = true;
        push();
    }

    // DATASHARE FUNCTIONS
    public abstract void sync(FormElement element);

    public void syncElement(FormElement element) {
        sync(element);
        userSet = true;
    }

    private void push() {
        if (panel != null) {    // Can be null if dummy element
            panel.getForm().getSessionController().push(this);
        }
    }

    private void touch() {
        if (panel != null) {    // Can be null if dummy element
            panel.getForm().getSessionController().touch(this);
        }
    }

    // For embedded and standalone export
    public abstract String getXml(boolean includeFormId);

}
