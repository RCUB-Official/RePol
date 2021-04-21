package policygenerator.form;

import framework.utilities.xml.XMLHandler;
import policygenerator.form.element.Panel;
import framework.utilities.xml.XMLUtilities;
import policygenerator.form.element.exceptions.UnknownTypeInputException;
import policygenerator.form.element.exceptions.MisconfiguredSelectionList;
import java.util.LinkedList;
import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import policygenerator.form.condition.Composite;
import policygenerator.form.condition.exceptions.ConditionNotFoundException;
import framework.utilities.xml.MissingAttributeException;
import policygenerator.form.condition.exceptions.UnknownOperatorException;
import policygenerator.form.element.PanelFactory;
import policygenerator.form.element.exceptions.ElementNotFoundException;
import policygenerator.form.element.exceptions.IdentifierCollision;
import policygenerator.form.element.exceptions.UnknownTriggerOperation;
import policygenerator.session.SessionController;

public class FormFactory extends XMLHandler {

    private static final FormFactory INSTANCE = new FormFactory();

    private List<FormHeader> headers = null;

    private FormFactory() {
        super(true, "Form Factory", "/config/template-forms.xml");
    }

    public static FormFactory getInstance() {
        return INSTANCE;
    }

    @Override
    protected void initializeProcedure() throws MissingAttributeException {
        headers = new LinkedList<>();

        NodeList forms = xmlDoc.getElementsByTagName("form");
        for (int i = 0; i < forms.getLength(); i++) {
            String formId = XMLUtilities.getRequiredAttributeValue(forms.item(i), "id");
            String label = XMLUtilities.getAttributeValue(forms.item(i), "label");
            String description = null;

            NodeList subnodes = forms.item(i).getChildNodes();

            for (int k = 0; k < subnodes.getLength(); k++) {
                if (subnodes.item(k).getNodeName().equals("description")) {
                    description = subnodes.item(k).getTextContent();
                    break;
                }
            }

            headers.add(new FormHeader(formId, label, description));
        }
    }

    @Override
    protected void shutdownProcedure() {
        headers = null;
    }

    public List<FormHeader> getFormHeaders() {
        return headers;
    }

    public boolean validateFormId(String formId) {
        boolean valid = false;
        for (FormHeader fh : headers) {
            if (fh.getFormId().equals(formId)) {
                valid = true;
                break;
            }
        }
        return valid;
    }

    public Form getForm(SessionController sessionController, String id) throws UnknownTypeInputException, MisconfiguredSelectionList,
            UnknownOperatorException, ElementNotFoundException, IdentifierCollision, ConditionNotFoundException,
            UnknownTriggerOperation, MissingAttributeException, FormNotFoundException {

        Form form = null;

        Node formNode = getNode("form", id);
        if (formNode != null) {
            String label = XMLUtilities.getAttributeValue(formNode, "label");

            form = new Form(sessionController, id);

            form.setLabel(label);

            List<Panel> panels = new LinkedList<>();
            List<Node> conditionNodes = new LinkedList<>();

            String description = null;

            NodeList subElements = formNode.getChildNodes();
            for (int i = 0; i < subElements.getLength(); i++) {

                Node elementNode = subElements.item(i);
                String nodeName = elementNode.getNodeName();
                switch (nodeName) {
                    case "description":
                        description = XMLUtilities.innerXml(elementNode);
                        break;
                    case "panel":
                        panels.add(PanelFactory.parsePanel(form, elementNode));
                        break;
                    case "condition":
                        conditionNodes.add(subElements.item(i)); // Parsing later
                        break;
                }
            }

            form.addPanels(panels);
            form.setDescription(description);

            //Parsing Conditions
            for (Node cn : conditionNodes) {
                String conditionId = XMLUtilities.getRequiredAttributeValue(cn, "id");
                form.addCondition(new Composite(form, conditionId, cn));
            }
        } else {
            throw new FormNotFoundException(id);
        }

        form.test();    // Factory tested

        return form;
    }
}
