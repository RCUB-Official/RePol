package framework.utilities.xml;

import framework.utilities.xml.MissingAttributeException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

public class XMLUtilities {

    public static String xmlEscape(String xml) {
        String escaped = xml;
        escaped = escaped.replace("&", "&amp;");
        escaped = escaped.replace("\"", "&quot;");
        escaped = escaped.replace("'", "&apos;");
        escaped = escaped.replace("<", "&lt;");
        escaped = escaped.replace(">", "&gt;");

        return escaped;
    }

    public static String getAttributeValue(Node node, String name) {   // Returns null if the attribute does not exist
        String attribute;
        try {
            attribute = node.getAttributes().getNamedItem(name).getTextContent();
        } catch (Exception ex) {
            attribute = null;
        }
        return attribute;
    }

    public static String getRequiredAttributeValue(Node node, String name) throws MissingAttributeException { // Throws an exception if the attribute does not exist.
        String attribute;
        try {
            attribute = node.getAttributes().getNamedItem(name).getTextContent();
        } catch (Exception ex) {
            throw new MissingAttributeException(name);
        }
        return attribute;
    }

    public static String innerXml(Node node) {
        DOMImplementationLS lsImpl = (DOMImplementationLS) node.getOwnerDocument().getImplementation().getFeature("LS", "3.0");
        LSSerializer lsSerializer = lsImpl.createLSSerializer();
        NodeList childNodes = node.getChildNodes();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < childNodes.getLength(); i++) {
            sb.append(lsSerializer.writeToString(childNodes.item(i)));
        }
        return sb.toString().replace("<?xml version=\"1.0\" encoding=\"UTF-16\"?>", "");
    }
}
