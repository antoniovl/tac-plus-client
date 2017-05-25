/*
 * Identity.java
 *
 * 
 */

package org.unixlibre.tacplus.tacplusclient;

/**
 *
 * @author  antoniovl
 *
 */
public class Identity {
    
    private String username = "";
    private String NASName = "";
    private String NASPort = "";
    private String NACAddress = "";
    private int privLvl = 0;

    /** Creates a new instance of Identity */
    public Identity() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNASName() {
        return NASName;
    }

    public void setNASName(String NASName) {
        this.NASName = NASName;
    }

    public String getNASPort() {
        return NASPort;
    }

    public void setNASPort(String NASPort) {
        this.NASPort = NASPort;
    }

    public String getNACAddress() {
        return NACAddress;
    }

    public void setNACAddress(String NACAddress) {
        this.NACAddress = NACAddress;
    }

    public int getPrivLvl() {
        return privLvl;
    }

    public void setPrivLvl(int privLvl) {
        this.privLvl = privLvl;
    }
}
