/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policygenerator.forms;

import policygenerator.forms.element.exceptions.UnknownTypeInputException;
import java.util.LinkedList;
import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import policygenerator.forms.element.*;

/**
 *
 * @author vasilije
 */
public class ListFactory extends XMLHandler {

    private static final ListFactory INSTANCE = new ListFactory();

    private ListFactory() {
        super(true, "List Factory", "/config/selection-lists.xml");
    }

    public static ListFactory getInstance() {
        return INSTANCE;
    }

    public List<SelectionElement> getSelectionList(FormElement forElement, String listId) throws UnknownTypeInputException {
        List<SelectionElement> selectionList = null;

        Node listNode = getNode("list", listId);
        if (listNode != null) {
            selectionList = new LinkedList<SelectionElement>();

            NodeList subElements = listNode.getChildNodes();
            for (int i = 0; i < subElements.getLength(); i++) {
                if (subElements.item(i).getNodeName().equals("element")) {
                    String value = subElements.item(i).getAttributes().getNamedItem("value").getTextContent();
                    String humanReadableLabel = subElements.item(i).getTextContent();
                    selectionList.add(new SelectionElement(forElement, humanReadableLabel != null ? humanReadableLabel : "", value));
                }
            }
        }

        return selectionList;
    }
}
