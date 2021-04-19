package policygenerator.form.element;

import framework.utilities.xml.XMLHandler;
import java.util.LinkedList;
import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class ListFactory extends XMLHandler {

    private static final ListFactory INSTANCE = new ListFactory();

    private ListFactory() {
        super(true, "List Factory", "/config/selection-lists.xml");
    }

    public static ListFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public void initializeProcedure() {
        // Do nothing for now
    }

    @Override
    public void shutdownProcedure() {
        // Do nothing for now
    }

    public List<SelectionElement> getSelectionList(String listId) {
        List<SelectionElement> selectionList = null;

        Node listNode = getNode("list", listId);
        if (listNode != null) {
            selectionList = new LinkedList<>();

            NodeList subElements = listNode.getChildNodes();
            for (int i = 0; i < subElements.getLength(); i++) {
                if (subElements.item(i).getNodeName().equals("element")) {
                    String value = subElements.item(i).getAttributes().getNamedItem("value").getTextContent();
                    String humanReadableLabel = subElements.item(i).getTextContent();
                    selectionList.add(new SelectionElement(humanReadableLabel != null ? humanReadableLabel : "", value));
                }
            }
        }

        return selectionList;
    }
}
