/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.access;

import framework.EventHandler;
import framework.settings.RepolSettings;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author vasilije
 */
@ManagedBean(name = "authenticator", eager = true)
@SessionScoped
public final class Authenticator implements Serializable {

    private String pin;
    private boolean authenticated;

    public Authenticator() {
        pin = "";
        authenticated = false;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void authenticate() {
        authenticated = RepolSettings.getInstance().getAuthenticationPin().equals(pin);
        if (!authenticated) {
            EventHandler.alertUserError("Access Denied!", "Wrong PIN.");
        }
    }
}
