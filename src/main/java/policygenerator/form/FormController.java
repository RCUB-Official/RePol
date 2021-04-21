package policygenerator.form;

import framework.utilities.Utilities;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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
        formHeaders = FormFactory.getInstance().getFormHeaders();

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

                if (!formHeaders.isEmpty()) {
                    if (formId == null) {
                        formId = formHeaders.get(0).getFormId();
                    }
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
