package policygenerator.form;

public class FormNotFoundException extends Exception {

    public FormNotFoundException(String formId) {
        super("Form \"" + formId + "\" has not been defined in template-forms.xml configuration file.");
    }
}
