package policygenerator.form.element.exceptions;

public class IdentifierCollision extends Exception {

    public IdentifierCollision(String id, String entityType) {
        super(entityType + " with identifier \"" + id + "\" already exists in this form");
    }
}
