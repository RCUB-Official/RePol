package policygenerator.form;

import framework.utilities.Utilities;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import policygenerator.form.condition.exceptions.ConditionNotFoundException;
import policygenerator.session.SessionController;

@ManagedBean(name = "formController", eager = false)
@ViewScoped
public class FormController implements Serializable {

    private static final Logger LOG = Logger.getLogger(FormController.class.getName());

    private final SessionController sessionController;
    private final List<FormHeader> formHeaders;

    String formId = null;

    private Form form;
    private String errorMessage;

    public FormController() {
        sessionController = SessionController.getSessionController();
//        System.out.println("FormContoller: form  headers");
        formHeaders = FormFactory.getInstance().getFormHeaders();
//        for (FormHeader header : formHeaders) {
//            System.out.println(header.getFormId());
//        }

        form = null;
        errorMessage = null;
    }

    @PostConstruct
    public void init() {
        try {
            if (formId == null) {   // Check if there is a GET parameter
                formId = (String) Utilities.getObject("#{param.document_id}");
            }

            if (formId != null) {   // If there is a GET parameter, get the from from the sessionController
                form = sessionController.getForm(formId);
                form.sync();

            } else {    // Form selection mode
                formId = sessionController.getActivityLogger().getLastRequestedFormId(); // Get last form used from sessionController

                if (formId == null && !formHeaders.isEmpty()) { // If it is still null, select the first header
                    formId = formHeaders.get(0).getFormId();
                    form = sessionController.getForm(formId);   // Enabling the "Enter Data" in the navigation menu
                    form.sync();
                }
            }

        } catch (Exception ex) {
            errorMessage = ex.getMessage();
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    // Form already selected
    public Form getForm() {
        return form;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    // Form selection mode
    public List<FormHeader> getFormHeaders() {
//        System.out.println("FormController: formHeaders.size " + formHeaders.size());
        return formHeaders;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        sessionController.getActivityLogger().setLastRequestedFormId(formId);
        this.formId = formId;
    }
}
