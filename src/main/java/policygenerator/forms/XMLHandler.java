package policygenerator.forms;

import framework.diagnostics.Monitorable;
import framework.diagnostics.Status;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import policygenerator.forms.condition.exceptions.MissingAttributeException;

abstract class XMLHandler implements Monitorable {

    private static final Logger LOG = Logger.getLogger(XMLHandler.class.getName());

    protected Document xmlDoc;

    private final boolean vital;
    private final String label;

    protected final String docPath;

    protected Status status;

    public XMLHandler(boolean vital, String label, String docPath) {
        this.vital = vital;
        this.label = label;
        this.docPath = docPath;
        this.status = new Status(Status.State.uninitialized, null);
    }

    protected abstract void initializeProcedure() throws MissingAttributeException;

    @Override
    public synchronized void initialize() {
        try (InputStream is = XMLHandler.class.getResourceAsStream(docPath)) {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            xmlDoc = db.parse(is);

            initializeProcedure();

            status = new Status(Status.State.operational, null);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    protected abstract void shutdownProcedure();

    @Override
    public synchronized void shutdown() {
        shutdownProcedure();

        status = new Status(Status.State.uninitialized, null);
    }

    @Override
    public synchronized void reload() {
        shutdown();
        initialize();
    }

    protected Node getNode(String tagName, String id) {
        NodeList nodes = xmlDoc.getElementsByTagName(tagName);
        Node node = null;
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getAttributes().getNamedItem("id").getTextContent().equals(id)) {
                node = nodes.item(i);
                break;
            }
        }
        return node;
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

    public InputStream getStream() {
        return XMLHandler.class.getResourceAsStream(docPath);
    }
}
