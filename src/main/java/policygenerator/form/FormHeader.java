package policygenerator.form;

public final class FormHeader {

    private final String formId;
    private final String label;
    private final String description;

    public FormHeader(String formId, String label, String description) {
        this.formId = formId;
        this.label = label;
        this.description = description;
    }

    public String getFormId() {
        return formId;
    }

    public String getLabel() {
        if (label != null) {
            return label;
        } else {
            return formId;
        }
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        if (label != null) {
            return label;
        } else {
            return formId;
        }
    }
}
