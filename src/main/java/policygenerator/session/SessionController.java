package policygenerator.session;

import framework.cache.Cache;
import framework.settings.RepolSettings;
import framework.utilities.Utilities;
import framework.utilities.xml.MissingAttributeException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.FileUploadEvent;
import policygenerator.form.Form;
import policygenerator.form.FormFactory;
import policygenerator.form.FormNotFoundException;
import policygenerator.form.condition.exceptions.ConditionNotFoundException;
import policygenerator.form.condition.exceptions.UnknownOperatorException;
import policygenerator.form.element.exceptions.ElementNotFoundException;
import policygenerator.form.element.exceptions.IdentifierCollision;
import policygenerator.form.element.exceptions.MisconfiguredSelectionList;
import policygenerator.form.element.exceptions.UnknownTriggerOperation;
import policygenerator.form.element.exceptions.UnknownTypeInputException;
import policygenerator.form.element.input.FormElement;

@ManagedBean(name = "sessionController", eager = true)
@SessionScoped
public final class SessionController implements Serializable {

    private static final Logger LOG = Logger.getLogger(SessionController.class.getName());

    private final DataShare dataShare = new DataShare();
    private final ActivityLogger activityLogger = new ActivityLogger();
    private final Cache formCache;

    private boolean used;

    public SessionController() {
        formCache = new Cache(RepolSettings.getInstance().getFormCacheSize());
        used = false;
    }

    public static SessionController getSessionController() {
        return (SessionController) Utilities.getObject("#{sessionController}");
    }

    public ActivityLogger getActivityLogger() {
        return activityLogger;
    }

    public Form getForm(String id) throws UnknownTypeInputException, MisconfiguredSelectionList, UnknownOperatorException,
            ElementNotFoundException, IdentifierCollision, ConditionNotFoundException, UnknownTriggerOperation, MissingAttributeException, FormNotFoundException {

        Form form = (Form) formCache.get(id);

        if (form == null) { // Cache miss
            form = FormFactory.getInstance().getForm(this, id);
            formCache.put(form);
        }

        boolean firstTime = activityLogger.openedForm(id);
        if (!firstTime) {
            form.userSetAll();
        }

        return form;
    }

    public Form getCachedForm(String id) {
        return (Form) formCache.get(id);
    }

    private void preload(Set<String> formIds) {
        for (String id : formIds) {
            try {
                Form form;
                if (Objects.isNull(formCache.get(id))) {
                    form = FormFactory.getInstance().getForm(this, id);
                    formCache.put(form);
                } else {
                    form = (Form) formCache.get(id);
                }
                form.sync();
            } catch (Exception ex) {    // No need to throw an exception because the id might come from a faulty imported file
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }

    // DATASHARE FUNCTIONS
    public void reset() {
        dataShare.reset();
        formCache.clear();

        activityLogger.resetValues();

        used = false;
    }

    public void requestSync(FormElement element) {
        dataShare.requestSync(element);
        used = true;
    }

    public void push(FormElement element) {
        dataShare.push(element);
        used = true;
    }

    public void touch(FormElement element) {
        dataShare.touch(element);
        used = true;
    }

    public boolean isUsed() {
        return used;
    }

    // IMPORT/EXPORT
    public void uploadFile(FileUploadEvent event) {
        Set<String> formIds = dataShare.processUpload(event);
        System.out.println("JOVANA sessionController discoveredForms: " + Arrays.toString(formIds.toArray()));

        used = true;

        activityLogger.uploadedDocument();

        if (Objects.nonNull(formIds) && !formIds.isEmpty()) {
//            if (FormFactory.getInstance().validateFormId(formIds.get(0))) {
//                activityLogger.setLastRequestedFormId(formIds.get(0));
//            }
            preload(formIds);
        }
    }

    public void downloadData() throws IOException {
        dataShare.downloadData();
        activityLogger.downloadedStandalone();
    }
}
