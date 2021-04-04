/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policygenerator.forms.element;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author vasilije
 */
public class DateInput extends FormElement {

    private Date value;

    public DateInput(Panel panel, String id, boolean mandatory, String label, String conditionId) {
        super(panel, Type.DATE, id, mandatory, label, conditionId);
        this.value = new Date(System.currentTimeMillis());
    }

    public Date getValue() {
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }

    @Override
    public void set(String value) {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            this.value = format.parse(value);
        } catch (Exception ex) {
            this.value = null;
        }
    }

    @Override
    public boolean isEmpty() {
        return (value == null);
    }

    @Override
    public boolean isRegexValid() {
        //TODO: validacija nad toString()
        return true;
    }

    @Override
    public boolean match(String value) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(this.value).contains(value);
    }

    @Override
    public void clear() {
    }

    @Override
    public void remove(String value) {
    }

    @Override
    public void sync(FormElement element) {
        switch (element.getType()) {
            case DATE:
                this.value = ((DateInput) element).getValue();
                break;
            case ONELINE:
                set(((OneLine) element).getValue());
                break;
            case TEXT:
                set(((Text) element).getValue());
                break;
            case SELECTONE:
                set(((SelectOne) element).getValue());
                break;
        }
    }

    @Override
    public String getXml() {
        return "<field type=\"date\" id=\"" + getId() + "\"><value>" + value + "</value></field>";
    }

}
