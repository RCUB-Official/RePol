package policygenerator.form.element;

import framework.utilities.xml.XMLUtilities;
import framework.utilities.xml.MissingAttributeException;
import java.util.LinkedList;
import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import policygenerator.form.Form;
import policygenerator.form.element.exceptions.MisconfiguredSelectionList;
import policygenerator.form.element.exceptions.UnknownTriggerOperation;
import policygenerator.form.element.exceptions.UnknownTypeInputException;
import policygenerator.form.element.input.FormElement;
import policygenerator.form.element.input.FormElementFactory;

public class PanelFactory {

    public static Panel parsePanel(Form form, Node panelNode) throws UnknownTypeInputException, MisconfiguredSelectionList, UnknownTriggerOperation, MissingAttributeException {
        String label = XMLUtilities.getAttributeValue(panelNode, "label");
        String conditionId = XMLUtilities.getAttributeValue(panelNode, "rendered");

        Panel panel = new Panel(form, label, conditionId);

        List<FormElement> elements = new LinkedList<>();

        NodeList subElements = panelNode.getChildNodes();
        for (int i = 0; i < subElements.getLength(); i++) {
            Node elementNode = subElements.item(i);
            String nodeName = elementNode.getNodeName();

            switch (nodeName) {
                case "input":
                    elements.add(FormElementFactory.parseInputElement(panel, elementNode));
                    break;
                case "separator":
                    elements.add(new Separator());
                    break;
            }
        }

        panel.addElements(elements);
        return panel;
    }
}
