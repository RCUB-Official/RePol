package policygenerator.form.element;

import policygenerator.form.element.input.FormElement;

public final class Separator extends FormElement {

    protected Separator() {
        super(null, Type.SEPARATOR, null, false, null, null, null, null);
    }

    @Override
    public Object getValue() {      // Just in case someone calls this on the separator
        throw new UnsupportedOperationException("Separator has no value.");
    }

    @Override
    public Object getSafeValue() {  // Same as getValue()
        throw new UnsupportedOperationException("Separator has no value.");
    }

    @Override
    public boolean isEmpty() { // Always empty
        return true;
    }

    @Override
    public boolean isRegexValid() {
        return true;
    }

    @Override
    public void set(String value) { // Not supposed to have a value
    }

    @Override
    public boolean match(String value) {
        return false;
    }

    @Override
    public void clear() {
    }

    @Override
    public void remove(String value) {
    }

    @Override
    public void sync(FormElement element) {
    }

    @Override
    public String getXml(boolean includeFormId) {
        return "";
    }

}
