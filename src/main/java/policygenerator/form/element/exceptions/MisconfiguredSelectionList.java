package policygenerator.form.element.exceptions;

public class MisconfiguredSelectionList extends Exception {

    public MisconfiguredSelectionList(String id) {
        super("List \"" + id + "\" has not been defined in template-forms.xml configuration file.");
    }

}
