package framework.settings;

import java.util.Properties;

public final class RepolSettings extends Settings {

    private static final RepolSettings INSTANCE = new RepolSettings();

    private String templatePath;
    private String authenticationPin;
    private String repolVersion;
    private String repolUrl;

    private boolean useSafeDefaults;
    private String listDelimiter;

    private RepolSettings() {
        super("/config/settings.cfg", "RePol Settings", "repol", true);
    }

    public static RepolSettings getInstance() {
        return INSTANCE;
    }

    @Override
    protected void loadParameters(Properties properties) {
        templatePath = properties.getProperty(prefix + ".templatePath");
        authenticationPin = properties.getProperty(prefix + ".authenticationPin");
        repolVersion = properties.getProperty(prefix + ".version");
        repolUrl = properties.getProperty(prefix + ".url");
        useSafeDefaults = "true".equals(properties.getProperty(prefix + ".useSafeDefaults"));
        listDelimiter = properties.getProperty(prefix + ".listDelimiter", ";");
    }

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
