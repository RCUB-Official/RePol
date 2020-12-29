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
public class MisconfiguredSelectionList extends Exception {

    public MisconfiguredSelectionList(String id) {
        super("List \"" + id + "\" has not been defined in template-forms.xml configuration file.");
    }

}
