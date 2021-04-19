package policygenerator.form.condition.exceptions;

public class UnknownOperatorException extends Exception {

    public UnknownOperatorException(String unknownOperator) {
        super("Unknown logical operator \"" + unknownOperator + "\" in condition.");
    }
}
