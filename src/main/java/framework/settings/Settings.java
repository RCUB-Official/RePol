package framework.settings;

import framework.diagnostics.Monitorable;
import framework.diagnostics.Status;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

abstract class Settings implements Monitorable {

    private static final Logger LOG = Logger.getLogger(Settings.class.getName());

    protected final String path;
    protected final String label;
    protected final String prefix;
    protected final boolean vital;

    protected Status status;

    public Settings(String path, String label, String prefix, boolean vital) {
        this.path = path;
        this.label = label;
        this.prefix = prefix;
        this.vital = vital;

        status = new Status(Status.State.uninitialized, null);
    }

    protected abstract void loadParameters(Properties properties);

    @Override
    public synchronized void initialize() {
        try (InputStream istream = RepolSettings.class.getResourceAsStream(path)) {
            Properties myProperties = new Properties();
            myProperties.load(istream);

            loadParameters(myProperties);

            status = new Status(Status.State.operational, null);
        } catch (Exception ex) {
            status = new Status(Status.State.malfunction, ex);
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public synchronized void shutdown() {
        status = new Status(Status.State.uninitialized, null);
    }

    @Override
    public synchronized void reload() {
        initialize();
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public boolean isVital() {
        return vital;
    }
}
