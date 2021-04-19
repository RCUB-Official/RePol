package framework.utilities;

import javax.el.ExpressionFactory;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;

public class Utilities {

    //Dohvati objekat na osnovu binding String-a
    public static Object getObject(String bindingString) {
        if (!bindingString.contains("{")) {
            bindingString = "#{" + bindingString + "}";
        }
        FacesContext ctx = FacesContext.getCurrentInstance();
        Application app = ctx.getApplication();
        ExpressionFactory ef = app.getExpressionFactory();
        return ef.createValueExpression(ctx.getELContext(), bindingString, Object.class).getValue(
                ctx.getELContext());
    }
}
