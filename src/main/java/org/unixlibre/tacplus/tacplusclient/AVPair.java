/*
 * AVPair.java
 *
 * 
 */

package org.unixlibre.tacplus.tacplusclient;

/**
 *
 * @author  antoniovl
 */
public class AVPair {
    
    private String avline;
    private String attribute = "";
    private String value = "";
    
    /** Creates a new instance of AVPair */
    public AVPair() {
    }
    
    public AVPair(String attr, String value) {
        this.attribute = attr;
        this.value = value;
    }

    public String getAvline() {
        return avline;
    }

    public void setAvline(String avline) {
        this.avline = avline;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
