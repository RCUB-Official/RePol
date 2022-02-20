package policygenerator.form.element.input;

import policygenerator.form.element.Panel;

import java.util.*;
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

    private final Set<String> idAliases;

    protected final boolean mandatory;

    private final String label;
    private String tooltip;
    private String description;

    private final String conditionId;

    private final List<Trigger> triggers;

    protected final List<String> defaultValues;

    protected String validationRegex;
    protected String validationMessage;

    private boolean userSet;

    protected FormElement(Panel panel, Type type, String id, Set<String> idAliases, boolean mandatory, String label, String conditionId, String validationRegex, String validationMessage) {
        this.panel = panel;
        this.type = type;
        this.id = id;
        this.idAliases = idAliases;
        this.mandatory = mandatory;
        this.label = label;
        this.conditionId = conditionId;

        this.validationRegex = validationRegex;
        this.validationMessage = validationMessage;

        this.tooltip = null;
        this.description = null;

        triggers = new LinkedList<>();
        defaultValues = new LinkedList<>();

        userSet = false;
    }

//    public final String getRealId (String alias) {
//        String realId = this.idAliases.get(alias);
//        if (Objects.isNull(realId)) {
//            return alias;
//        } else {
//            return realId;
//        }
//    }
//
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

    public boolean hasAlias(String alias) {
        return this.idAliases.contains(alias);
    }

    public final Set<String> getIdAliases() {
        return this.idAliases;
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

    void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public String getDefaultValue() {
        if (!defaultValues.isEmpty()) {
            return defaultValues.get(defaultValues.size() - 1);
        } else {
            return null;
        }
    }

    public List<String> getDefaultValues() {
        return defaultValues;
    }

    //VALUE FUNCTIONS
    public abstract Object getValue();  // For FreeMarker model

    public abstract Object getSafeValue(); // Get a non-null value that will not crash FreeMarker

    protected abstract void set(String value);

    protected abstract void clear();

    public abstract void remove(String value);

    public abstract boolean match(String value);

    final void setDefaultValue(String defaultValue) {
        this.defaultValues.add(defaultValue);
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
        for (String dv : defaultValues) {
            set(dv);
        }
        push();
    }

    //
    public final boolean isIdAliased() {
        return Objects.nonNull(this.idAliases) && !this.idAliases.isEmpty();
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

    @SuppressWarnings("UseSpecificCatch")   // Don't crash, just log if something goes wrong.
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

    public abstract Set<String> getXmlForAliases(Set<String> skipIds);

}
