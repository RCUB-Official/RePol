package framework;

import framework.diagnostics.Monitorable;
import framework.diagnostics.Status.State;
import framework.settings.RepolSettings;
import policygenerator.freemarker.FMHandler;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import policygenerator.form.FormFactory;
import policygenerator.form.element.ListFactory;

@ManagedBean(name = "dashboard", eager = true)
@ApplicationScoped
public class Dashboard implements Serializable {

    private static final Logger LOG = Logger.getLogger(Dashboard.class.getName());

    private final List<Monitorable> monitorables;

    public Dashboard() {
        monitorables = new ArrayList<>();
    }

    private void formChain() {
        monitorables.add(RepolSettings.getInstance());
        monitorables.add(FMHandler.getInstance());
        monitorables.add(FormFactory.getInstance());
        monitorables.add(ListFactory.getInstance());
    }

    @PostConstruct
    public void initialize() {
        formChain();
        for (Monitorable monitorable : monitorables) {
            try {
                monitorable.initialize();
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, ex.getMessage());
                break;
            }
            if (monitorable.getStatus().getState() == State.malfunction && monitorable.isVital()) {
                break;
            }
        }
    }

    @PreDestroy
    public void shutdown() {
        List<Monitorable> reversed = new ArrayList<>();
        reversed.addAll(monitorables);
        Collections.reverse(reversed);
        for (Monitorable m : reversed) {
            m.shutdown();
        }
    }

    public List<Monitorable> getMonitorables() {
        return monitorables;
    }

    public boolean isOperational() {
        boolean operational = true;
        for (Monitorable monitorable : monitorables) {
            if (monitorable.getStatus().isMalfunction() && monitorable.isVital()) {
                operational = false;
            }
        }
        return operational;
    }

}
