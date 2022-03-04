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
import java.net.URL;
import java.util.Arrays;
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
//            File templateDirectory = new File(RepolSettings.getInstance().getTemplatePath());
//            templateFiles = templateDirectory.listFiles();
            String fmTemplateDirPath = RepolSettings.getInstance().getTemplatePath();
//            System.out.println("JOVANA: " + fmTemplateDirPath);
            URL freemarkerTemplatesDirectory = FMHandler.class.getResource(fmTemplateDirPath);
            File templateDirectory = new File(freemarkerTemplatesDirectory.toURI());
//            System.out.println("JOVANA: " + freemarkerTemplatesDirectory);
            templateFiles = templateDirectory.listFiles();
//            System.out.println("JOVANA: templates " + Arrays.toString(templateFiles));
//            final java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(
//                    FMHandler.class.getClassLoader().getResourceAsStream(fmTemplateDirPath)));
//            java.util.List<File> templates = new ArrayList<>();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                URL templateURL = FMHandler.class.getResource(fmTemplateDirPath + "/" + line);
//                templates.add(new File(templateURL.toURI()));
//            }
//            templateFiles = templates.toArray(new File[0]);

            configuration = new Configuration(Configuration.VERSION_2_3_30);
            configuration.setDirectoryForTemplateLoading(new File(freemarkerTemplatesDirectory.toURI()));
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
        return FMHandler.class.getResourceAsStream(RepolSettings.getInstance().getTemplatePath() + "/" + filename + ".ftlh");
//        File file = new File(RepolSettings.getInstance().getTemplatePath() + "/" + filename + ".ftlh");
//        return new FileInputStream(file);
    }

}
