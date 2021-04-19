package policygenerator.form.element.exceptions;

public class UnknownTriggerOperation extends Exception {

    public UnknownTriggerOperation(String parsedOperation) {
        super("Unknown trigger operation \"" + parsedOperation + "\".");
    }
}
