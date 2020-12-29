/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import framework.diagnostics.Monitorable;
import framework.diagnostics.Status.State;
import framework.settings.RepolSettings;
import policygenerator.freemarker.FMHandler;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import policygenerator.forms.FormFactory;
import policygenerator.forms.ListFactory;

/**
 *
 * @author vasilije
 */
@ManagedBean(name = "dashboard", eager = true)
@ApplicationScoped
public class Dashboard implements Serializable {

    private final ArrayList<Monitorable> monitorables;

    public Dashboard() {
        monitorables = new ArrayList<Monitorable>();
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
                Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, ex.getMessage());
                break;
            }
            if (monitorable.getStatus().getState() == State.malfunction && monitorable.isVital()) {
                break;
            }
        }
    }

    @PreDestroy
    public void shutdown() {
        ArrayList<Monitorable> reversed = new ArrayList<Monitorable>();
        reversed.addAll(monitorables);
        Collections.reverse(reversed);
        for (Monitorable m : reversed) {
            m.shutdown();
        }
    }

    public ArrayList<Monitorable> getMonitorables() {
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
