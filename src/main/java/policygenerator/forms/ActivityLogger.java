/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policygenerator.forms;

import framework.utilities.Utilities;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author vasilije
 */
@ManagedBean(name = "activityLogger", eager = true)
@SessionScoped
public class ActivityLogger implements Serializable {

    private static final Logger LOG = Logger.getLogger(ActivityLogger.class.getName());

    private final String sessionId;
    private final String ipAddr;

    private final String HEADER;

    Set<String> formSet = new HashSet<String>();
    private String lastRequestedFormId = null;

    public ActivityLogger() {
        sessionId = FacesContext.getCurrentInstance().getExternalContext().getSessionId(true);
        ipAddr = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr();
        HEADER = sessionId + " (" + ipAddr + ")";

        LOG.log(Level.INFO, "{0}: started a session.", HEADER);
    }

    public static ActivityLogger getActivityLogger() {
        return (ActivityLogger) Utilities.getObject("#{activityLogger}");
    }

    //Event Listeners
    public void openedForm(String formId) {
        if (!formSet.contains(formId)) {
            LOG.log(Level.INFO, "{0}: opened the form \"{1}\".", new Object[]{HEADER, formId});
            formSet.add(formId);
        }
        lastRequestedFormId = formId;
    }

    public void documentGenerated(String formId) {
        LOG.log(Level.INFO, "{0}: generated the document \"{1}\".", new Object[]{HEADER, formId});
    }

    public void downloadedConfig() {
        LOG.log(Level.INFO, "{0}: downloaded the configuration file.", HEADER);
    }

    public void downloadedTemplate(String formId) {
        LOG.log(Level.INFO, "{0}: downloaded the template \"{1}\".", new Object[]{HEADER, formId});
    }

    public void downloadedStandalone() {
        LOG.log(Level.INFO, "{0}: downloaded a standalone export file.", HEADER);
    }

    public void uploadedDocument() {
        LOG.log(Level.INFO, "{0}: uploaded a document for parsing.", HEADER);
    }

    public void resetValues() {
        LOG.log(Level.INFO, "{0}: triggered a data reset.", HEADER);
    }

    @PreDestroy
    public void endedSession() {
        LOG.log(Level.INFO, "{0}: ended a session.", HEADER);
    }

    public String getLastRequestedFormId() {
        return lastRequestedFormId;
    }

}
