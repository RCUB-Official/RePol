package policygenerator.session;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public final class ActivityLogger implements Serializable {

    private static final Logger LOG = Logger.getLogger(ActivityLogger.class.getName());

    private final String sessionId;
    private final String ipAddr;
    private final String header;

    Set<String> formSet = new HashSet<>();
    private String lastRequestedFormId = null;

    ActivityLogger() {
        sessionId = FacesContext.getCurrentInstance().getExternalContext().getSessionId(true);
        ipAddr = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr();
        header = sessionId + " (" + ipAddr + ")";

        LOG.log(Level.INFO, "{0}: started a session.", header);
    }

    //Event Listeners
    public boolean openedForm(String formId) {  // Returns true if it is the first time this form is opened
        boolean firstTime;

        if (!formSet.contains(formId)) {
            LOG.log(Level.INFO, "{0}: opened the form \"{1}\".", new Object[]{header, formId});
            formSet.add(formId);

            firstTime = true;
        } else {
            firstTime = false;
        }

        lastRequestedFormId = formId;

        return firstTime;
    }

    public void documentGenerated(String formId) {
        LOG.log(Level.INFO, "{0}: generated the document \"{1}\".", new Object[]{header, formId});
    }

    public void downloadedConfig() {
        LOG.log(Level.INFO, "{0}: downloaded the configuration file.", header);
    }

    public void downloadedLists() {
        LOG.log(Level.INFO, "{0}: downloaded the list file.", header);
    }

    public void downloadedTemplate(String formId) {
        LOG.log(Level.INFO, "{0}: downloaded the template \"{1}\".", new Object[]{header, formId});
    }

    public void downloadedStandalone() {
        LOG.log(Level.INFO, "{0}: downloaded a standalone export file.", header);
    }

    public void uploadedDocument() {
        LOG.log(Level.INFO, "{0}: uploaded a document for parsing.", header);
    }

    public void resetValues() {
        LOG.log(Level.INFO, "{0}: triggered a data reset.", header);
        formSet.clear();
    }

    @PreDestroy
    public void endedSession() {
        LOG.log(Level.INFO, "{0}: ended a session.", header);
    }

    // Requested forms
    public String getLastRequestedFormId() {
        return lastRequestedFormId;
    }

    public void setLastRequestedFormId(String lastRequestedFormId) {    // Only for FormController, do not log any activity for mere form selection
        this.lastRequestedFormId = lastRequestedFormId;
    }
}
