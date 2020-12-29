/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.el.ExpressionFactory;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;

/**
 *
 * @author vasilije
 */
public class Utilities {

    private Utilities() {

    }

    //Dohvati objekat na osnovu binding String-a
    public static Object getObject(String bindingString) {
        if (bindingString.indexOf("{") == -1) {
            bindingString = "#{" + bindingString + "}";
        }
        FacesContext ctx = FacesContext.getCurrentInstance();
        Application app = ctx.getApplication();
        ExpressionFactory ef = app.getExpressionFactory();
        return ef.createValueExpression(ctx.getELContext(), bindingString, Object.class).getValue(
                ctx.getELContext());
    }

    public static void createDirectory(String path) {
        File dir = new File(path);
        dir.mkdir();
    }

    public static void createFile(String path, String content) throws FileNotFoundException, IOException {
        FileOutputStream dc = new FileOutputStream(path);
        dc.write(content.getBytes());
        dc.close();
    }

    public static void purgeDirectory(String path) {
        File dir = new File(path);
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                purgeDirectory(file.getAbsolutePath());
            }
            file.delete();
        }
    }

    public static String xmlEscape(String xml) {
        String escaped = xml;
        escaped = escaped.replace("&", "&amp;");
        escaped = escaped.replace("\"", "&quot;");
        escaped = escaped.replace("'", "&apos;");
        escaped = escaped.replace("<", "&lt;");
        escaped = escaped.replace(">", "&gt;");

        return escaped;
    }

}
