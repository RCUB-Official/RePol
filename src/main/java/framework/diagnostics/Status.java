/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.diagnostics;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author vasilije
 */
@ManagedBean(name = "status")
@RequestScoped
public class Status {

    public enum State {
        uninitialized, operational, malfunction, busy
    }

    State state;
    Exception exception;

    public Status(State state, Exception exception) {
        this.state = state;
        this.exception = exception;
    }

    public State getState() {
        return state;
    }

    public Exception getException() {
        return exception;
    }

    public boolean isOperational() {
        return (state == State.operational);
    }

    public boolean isMalfunction() {
        return (state == State.malfunction);
    }

    public boolean isUninitialized() {
        return (state == State.uninitialized);
    }

}
