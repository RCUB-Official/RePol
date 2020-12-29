/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policygenerator.forms.condition.exceptions;

/**
 *
 * @author vasilije
 */
public class ConditionNotFoundException extends Exception {

    public ConditionNotFoundException(String elementId) {
        super("Condition \"" + elementId + "\" has not been defined in template-forms.xml configuration file.");
    }
}
