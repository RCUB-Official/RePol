/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author vasilije
 */
abstract class XMLHandler implements Monitorable {
    
    protected Document xmlDoc;

    protected final boolean VITAL;
    protected final String LABEL;

    protected final String docPath;

    protected Status status;

    public XMLHandler(boolean VITAL, String LABEL, String docPath) {
        this.VITAL = VITAL;
        this.LABEL = LABEL;
        this.docPath = docPath;
        this.status = new Status(Status.State.uninitialized, null);
    }

    @Override
    public synchronized void initialize() {
        try (InputStream is = XMLHandler.class.getResourceAsStream(docPath)) {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            xmlDoc = db.parse(is);

            status = new Status(Status.State.operational, null);
        } catch (Exception ex) {
            Logger.getLogger(XMLHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public synchronized void shutdown() {
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
        return LABEL;
    }

    @Override
    public boolean isVital() {
        return VITAL;
    }

}
