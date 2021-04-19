package policygenerator.form;

import framework.utilities.Utilities;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "formController", eager = false)
@ViewScoped
public class FormController implements Serializable {

    private static final Logger LOG = Logger.getLogger(FormController.class.getName());

    private final DataShare dataShare;

    String formId = null;

    private Form form;
    private String errorMessage;

    public FormController() {
        form = null;
        errorMessage = null;

        dataShare = DataShare.getDataShare();
    }

    @PostConstruct
    public void init() {
        try {
            if (formId == null) {
                formId = (String) Utilities.getObject("#{param.document_id}");
            }
            form = FormFactory.getInstance().getForm(this, formId);
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
            LOG.log(Level.SEVERE, null, ex);
        }

        if (form == null) {
            formId = ActivityLogger.getActivityLogger().getLastRequestedFormId();

            if (formId == null && !getFormHeaders().isEmpty()) {
                formId = getFormHeaders().get(0).getFormId();
            }
        }
    }

    public static FormController getFormController() {
        return (FormController) Utilities.getObject("#{formController}");
    }

    public List<FormHeader> getFormHeaders() {
        return FormFactory.getInstance().getFormHeaders();
    }

    public Form getForm() {
        return form;
    }

    public DataShare getDataShare() {
        return dataShare;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }
}
