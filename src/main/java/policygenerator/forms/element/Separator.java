/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policygenerator.forms.element;

/**
 *
 * @author vasilije
 */
public class Separator extends FormElement {

    public Separator() {
        super(null, Type.SEPARATOR, null, false, null, null, null, null);
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
    public String getXml() {
        return "";
    }

}
