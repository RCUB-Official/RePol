package policygenerator.form.element.exceptions;

public class ElementNotFoundException extends Exception {

    public ElementNotFoundException(String elementId) {
        super("FormElement with id \"" + elementId + "\" has not been defined in template-forms.xml configuration file.");
    }
}
