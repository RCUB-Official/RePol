/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policygenerator.forms;

import framework.utilities.Utilities;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author vasilije
 */
@ManagedBean(name = "formController", eager = false)
@ViewScoped
public class FormController implements Serializable {

    String formId = null;

    private Form form;
    private String errorMessage;

    public FormController() {
        form = null;
        errorMessage = null;
    }

    @PostConstruct
    public void init() {
        try {
            if (formId == null) {
                formId = (String) Utilities.getObject("#{param.document_id}");
            }
            form = FormFactory.getInstance().getForm(formId);
            if (form != null) {
                form.test();
                form.sync();
                errorMessage = null;
            } else {
                errorMessage = "Form not found!";
            }
        } catch (Exception ex) {
            form = null;
            errorMessage = ex.getMessage();
            Logger.getLogger(FormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void reset() {
        DataShare ds = (DataShare) Utilities.getObject("#{dataShare}");
        if (ds != null) {
            ds.reset();
        }
        init();
    }

    public List<FormHeader> getFormHeaders() {
        return FormFactory.getInstance().getFormHeaders();
    }

    public Form getForm() {
        return form;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
