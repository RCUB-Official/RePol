/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.diagnostics;

/**
 *
 * @author vasilije
 */
public interface Monitorable {

    public void initialize();

    public void shutdown();

    public void reload();

    public Status getStatus();

    public String getLabel();

    public boolean isVital();
}
