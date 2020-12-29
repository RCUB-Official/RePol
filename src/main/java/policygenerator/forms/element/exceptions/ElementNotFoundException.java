/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policygenerator.forms.element.exceptions;

/**
 *
 * @author vasilije
 */
public class ElementNotFoundException extends Exception {

    public ElementNotFoundException(String elementId) {
        super("FormElement with id \"" + elementId + "\" has not been defined in template-forms.xml configuration file.");
    }
}
