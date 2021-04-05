/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.settings;

import framework.diagnostics.Monitorable;
import framework.diagnostics.Status;
import framework.diagnostics.Status.State;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vasilije
 */
public final class RepolSettings implements Monitorable {

    private static RepolSettings instance = null;

    private final String path = "/config/settings.cfg";
    private final String label = "RePol Settings";
    private final String prefix = "repol";
    private final boolean vital = true;

    //Settings Parameters
    private String templatePath;
    private String authenticationPin;
    private String repolVersion;
    private String repolUrl;

    private boolean useSafeDefaults;
    private String listDelimiter;

    private Status status;

    private RepolSettings() {
        status = new Status(State.uninitialized, null);
    }

    public static RepolSettings getInstance() {
        if (instance == null) {
            instance = new RepolSettings();
        }
        return instance;
    }

    @Override
    public synchronized void initialize() {
        InputStream istream = null;
        try {
            Properties myProperties = new Properties();
            istream = RepolSettings.class.getResourceAsStream(path);
            myProperties.load(istream);

            templatePath = myProperties.getProperty(prefix + ".templatePath");
            authenticationPin = myProperties.getProperty(prefix + ".authenticationPin");
            repolVersion = myProperties.getProperty(prefix + ".version");
            repolUrl = myProperties.getProperty(prefix + ".url");
            useSafeDefaults = "true".equals(myProperties.getProperty(prefix + ".useSafeDefaults"));
            listDelimiter = myProperties.getProperty(prefix + ".listDelimiter", ";");
            //intField = Integer.parseInt(myProperties.getProperty(prefix + ".intField"));
            status = new Status(State.operational, null);
        } catch (Exception ex) {
            status = new Status(State.malfunction, ex);
            Logger.getLogger(RepolSettings.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (istream != null) {
                try {
                    istream.close();
                } catch (IOException ex) {
                    Logger.getLogger(RepolSettings.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public void shutdown() {
        status = new Status(State.uninitialized, null);
    }

    @Override
    public synchronized void reload() {
        initialize();
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public boolean isVital() {
        return vital;
    }

    //Config-specific getters
    public String getTemplatePath() {
        return templatePath;
    }

    public String getAuthenticationPin() {
        return authenticationPin;
    }

    public String getRepolVersion() {
        return repolVersion;
    }

    public String getRepolUrl() {
        return repolUrl;
    }

    public boolean isUseSafeDefaults() {
        return useSafeDefaults;
    }

    public String getListDelimiter() {
        return listDelimiter;
    }

}
