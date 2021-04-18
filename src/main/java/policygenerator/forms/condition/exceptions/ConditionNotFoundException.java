package policygenerator.forms.condition.exceptions;

public class ConditionNotFoundException extends Exception {

    public ConditionNotFoundException(String elementId) {
        super("Condition \"" + elementId + "\" has not been defined in template-forms.xml configuration file.");
    }
}
