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
public class IdentifierCollision extends Exception {

    public IdentifierCollision(String id, String entityType) {
        super(entityType + " with identifier \"" + id + "\" already exists in this form");
    }
}
