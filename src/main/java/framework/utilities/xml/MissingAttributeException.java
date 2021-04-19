package framework.utilities.xml;

public class MissingAttributeException extends Exception {

    public MissingAttributeException(String name) {
        super("Attribute \"" + name + "\" is required but missing.");
    }

}
