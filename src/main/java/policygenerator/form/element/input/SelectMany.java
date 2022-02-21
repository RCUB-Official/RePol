package policygenerator.form.element.input;

import policygenerator.form.element.SelectionElement;
import policygenerator.form.element.Panel;
import framework.settings.RepolSettings;
import framework.utilities.xml.XMLUtilities;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import policygenerator.form.element.ListFactory;
import policygenerator.form.element.exceptions.MisconfiguredSelectionList;

public final class SelectMany extends FormElement {

    private final List<SelectionElement> availableValues;

    SelectMany(Panel panel, String id, Set<String> idAliases, boolean mandatory, String label, String conditionId, String listId) throws MisconfiguredSelectionList {
        super(panel, Type.SELECTMANY, id, idAliases, mandatory, label, conditionId, null, null);
        availableValues = new LinkedList<>();

        List<SelectionElement> fetchedList = ListFactory.getInstance().getSelectionList(listId);

        if (fetchedList != null) {
            for (SelectionElement se : fetchedList) {
                availableValues.add(new SelectionElement(this, se.getLabel(), se.getValue()));
            }
        } else if (listId != null) {
            throw new MisconfiguredSelectionList(listId);
        }
    }

    public List<SelectionElement> getAvailableValues() {
        return availableValues;
    }

    public List<SelectionElement> getAllPossibleValues() {
        return availableValues;
    }

    public List<String> getValues() {
        List<String> list = new LinkedList<>();
        for (SelectionElement se : availableValues) {
            if (se.isSelected()) {
                list.add(se.getValue());
            }
        }
        return list;
    }

    @Override
    public List<String> getValue() {
        return getValues();
    }

    @Override   // List is never empty
    public List<String> getSafeValue() {
        return getValues();
    }

    @Override
    public void set(String value) {
        for (SelectionElement se : availableValues) {
            if (se.getValue().equals(value)) {
                se.setSelected(true);
                break;
            }
        }
    }

    @Override
    public boolean isEmpty() {
        boolean empty = true;
        for (SelectionElement se : availableValues) {
            if (se.isSelected()) {
                empty = false;
                break;
            }
        }
        return empty;
    }

    @Override
    public boolean isRegexValid() {
        return true;
    }

    @Override
    public boolean match(String value) {
        boolean match = false;
        for (SelectionElement se : availableValues) {
            if (se.getValue().equals(value) && se.isSelected()) {
                match = true;
                break;
            }
        }
        return match;
    }

    @Override
    public void clear() {
        for (SelectionElement se : availableValues) {
            se.setSelected(false);
        }
    }

    @Override
    public void remove(String value) {
        for (SelectionElement se : availableValues) {
            if (se.getValue().equals(value)) {
                se.setSelected(false);
            }
        }
    }

    @Override
    public void sync(FormElement element) {
        clear();

        String listDelimiter = RepolSettings.getInstance().getListDelimiter();
        String obtainedValue;

        switch (element.getType()) {
            case ONELINE:
                obtainedValue = ((OneLine) element).getValue();
                if (obtainedValue.contains(listDelimiter)) {
                    for (String val : obtainedValue.split(listDelimiter)) {
                        set(val);
                    }
                } else {
                    set(obtainedValue);
                }
                break;
            case TEXT:
                obtainedValue = ((Text) element).getValue();
                if (obtainedValue.contains(listDelimiter)) {
                    for (String val : obtainedValue.split(listDelimiter)) {
                        set(val);
                    }
                } else {
                    set(obtainedValue);
                }
                break;
            case INTEGER:
                set(((IntegerInput) element).getValue() + "");
                break;
            case DOUBLE:
                set(((DoubleInput) element).getValue() + "");
                break;
            case DATE:
                set(((DateInput) element).getValue() + "");
                break;
            case SELECTONE:
                set(((SelectOne) element).getValue());
                break;
            case SELECTMANY:
                for (String value : ((SelectMany) element).getValues()) {
                    set(value);
                }
                break;
            case ADDLIST:
                for (String value : ((AddList) element).getValues()) {
                    set(value);
                }
                break;
            case POOLPICKER:
                for (String value : ((PoolPicker) element).getValues()) {
                    set(value);
                }
                break;
        }
    }

    @Override
    public String getXml(boolean includeFormId) {
        String xml;

        String formId;
        if (getForm() != null && includeFormId) {
            formId = " form=\"" + getForm().getId() + "\"";
        } else {
            formId = "";
        }

        if (!availableValues.isEmpty()) {
            xml = "<field type=\"selectmany\" id=\"" + getId() + "\"" + formId + ">";
            for (SelectionElement av : availableValues) {
                if (av.isSelected()) {
                    xml += "<value>" + XMLUtilities.xmlEscape(av.getValue()) + "</value>";
                }
            }
            xml += "</field>";
        } else {
            xml = "";
        }
        return xml;
    }

    @Override
    public Set<String> getXmlForAliases(Set<String> skipIds) {
        Set<String> aliases = this.getIdAliases();
        Set<String> xmlForAliases = new HashSet<>();
        for (String alias : aliases) {
            String xml;
            if (!skipIds.contains(alias) && !getId().equals(alias)) {
                if (!availableValues.isEmpty()) {
                    xml = "\n\t<field type=\"selectmany\" id=\"" + alias + "\">";
                    for (SelectionElement av : availableValues) {
                        if (av.isSelected()) {
                            xml += "<value>" + XMLUtilities.xmlEscape(av.getValue()) + "</value>";
                        }
                    }
                    xml += "</field>";
                } else {
                    xml = "";
                }
                if (!xml.isEmpty()) {
                    xmlForAliases.add(xml);
                }
            }
        }
        return xmlForAliases;
    }

}
