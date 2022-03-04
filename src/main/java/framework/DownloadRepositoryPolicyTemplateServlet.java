package framework;

import framework.utilities.HttpUtilities;
import policygenerator.freemarker.FMHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/repository_policy_template")
public class DownloadRepositoryPolicyTemplateServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(DownloadRepositoryPolicyTemplateServlet.class.getName());
    private final int ARBITARY_SIZE = 1048;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/plain");
        resp.setHeader("Content-disposition", "attachment; filename=repository_policy_template.ftlh");

        try (InputStream in = this.getClass().getResourceAsStream("/freemarker_templates/policy.ftlh");
            OutputStream out = resp.getOutputStream()) {

            byte[] buffer = new byte[ARBITARY_SIZE];

            int numBytesRead;
            while ((numBytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, numBytesRead);
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

    }

    public void downloadRepositoryPolicyTemplate() {
            String id = "policy";

        try (InputStream is = FMHandler.getInstance().getInputStream(id)) {
            HttpUtilities.sendFileToClient(is, "text/plain", id + ".ftlh");

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
}
