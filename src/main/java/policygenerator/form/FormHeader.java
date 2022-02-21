package policygenerator.form;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class FormHeader {

    private final String formId;
    private final String label;
    private final String description;
    private final Map<String, List<String>> dataIdAliases;

    public FormHeader(String formId, String label, String description, Map<String, List<String>> dataIdAliases) {
        this.formId = formId;
        this.label = label;
        this.description = description;
        this.dataIdAliases = dataIdAliases;
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

    public boolean hasAtrribute(String attribute) {
        // check ids
        boolean hasAttribute = this.hasAttributeId(attribute);
        if (!hasAttribute) {
            for (String elementId : this.dataIdAliases.keySet()) {
                if (dataIdAliases.get(elementId).contains(attribute)) {
                    hasAttribute = true;
                    break;
                }
            }
        }
        return hasAttribute;
    }


    public boolean hasAttributeId(String id) {
        return this.dataIdAliases.containsKey(id);
    }

    public Set<String> getAliasesForElementId(String alias) {
        Set<String> elementIds = new HashSet<>();
        Set<String> keySet = this.dataIdAliases.keySet();
        if (keySet.contains(alias)) {
            elementIds.add(alias);
        }
        for (String elementId : keySet) {
            List<String> aliases = this.dataIdAliases.get(elementId);
            if (aliases.contains(alias)) {
                elementIds.add(elementId);
            }
        }
        return elementIds;
    }
}
