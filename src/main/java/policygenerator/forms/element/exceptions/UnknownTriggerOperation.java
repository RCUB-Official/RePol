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
public class UnknownTriggerOperation extends Exception {

    public UnknownTriggerOperation(String parsedOperation) {
        super("Unknown trigger operation \"" + parsedOperation + "\".");
    }
}
