package policygenerator.form.element.exceptions;

public class UnknownTypeInputException extends Exception {

    public UnknownTypeInputException(String type) {
        super("Unknown FormElement input type: \"" + type + "\".");
    }

}
