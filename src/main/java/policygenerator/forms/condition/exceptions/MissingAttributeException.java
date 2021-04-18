package policygenerator.forms.condition.exceptions;

public class MissingAttributeException extends Exception {

    public MissingAttributeException(String name) {
        super("Attribute \"" + name + "\" is required but missing.");
    }

}
