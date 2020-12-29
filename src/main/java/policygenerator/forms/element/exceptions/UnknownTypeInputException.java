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
public class UnknownTypeInputException extends Exception {

    public UnknownTypeInputException(String type) {
        super("Unknown FormElement input type: \"" + type + "\".");
    }

}
