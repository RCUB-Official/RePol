package policygenerator.freemarker;

import framework.diagnostics.Monitorable;
import framework.diagnostics.Status;
import framework.diagnostics.Status.State;
import framework.settings.RepolSettings;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class FMHandler implements Monitorable {

    private static final FMHandler INSTANCE = new FMHandler();
    private static final Logger LOG = Logger.getLogger(FMHandler.class.getName());

    private Configuration configuration;
    private File[] templateFiles;

    private final boolean vital = true;
    private final String label = "FreeMarker";
    private Status status;

    private FMHandler() {
        status = new Status(State.uninitialized, null);
    }

    public static FMHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public synchronized void initialize() {
        try {
            File templateDirectory = new File(RepolSettings.getInstance().getTemplatePath());
            templateFiles = templateDirectory.listFiles();

            configuration = new Configuration(Configuration.VERSION_2_3_30);
            configuration.setDirectoryForTemplateLoading(templateDirectory);
            configuration.setDefaultEncoding("UTF-8");
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            configuration.setLogTemplateExceptions(false);
            configuration.setWrapUncheckedExceptions(true);
            configuration.setFallbackOnNullLoopVariable(false);

            status = new Status(State.operational, null);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            status = new Status(State.malfunction, ex);
        }

    }

    @Override
    public synchronized void shutdown() {
        configuration = null;
        status = new Status(State.uninitialized, null);
    }

    @Override
    public synchronized void reload() {
        shutdown();
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

    public Configuration getConfiguration() {
        return configuration;
    }

    public boolean templateExists(String fileName) {
        boolean exists = false;
        for (File tf : templateFiles) {
            if (tf.getName().equals(fileName)) {
                exists = true;
                break;
            }
        }
        return exists;
    }

    public static InputStream getInputStream(String filename) throws FileNotFoundException {
        File file = new File(RepolSettings.getInstance().getTemplatePath() + "/" + filename + ".ftlh");
        return new FileInputStream(file);
    }

}
