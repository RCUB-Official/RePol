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
public class UnknownOperatorException extends Exception {

    public UnknownOperatorException(String unknownOperator) {
        super("Unknown logical operator \"" + unknownOperator + "\" in condition.");
    }
}
