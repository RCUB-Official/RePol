package policygenerator.form;

import java.util.List;

public final class FormHeader {

    private final String formId;
    private final String label;
    private final String description;

    private final List<String> mandatoryFieldIds;

    public FormHeader(String formId, String label, String description, List<String> mandatoryFieldIds) {
        this.formId = formId;
        this.label = label;
        this.description = description;
        this.mandatoryFieldIds = mandatoryFieldIds;
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

    public List<String> getMandatoryFieldIds() {
        return mandatoryFieldIds;
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
