/*
 * AuthenPktStart.java
 *
 * equivalente a struct authen_start
 */

package org.unixlibre.tacplus.tacplusclient;

/**
 *
 * @author  antoniovl
 */
public class AuthenPktStart {
    
    public static byte TACPLUS_AUTHEN_TYPE_ASCII = (byte)0x1;
    public static byte TACPLUS_AUTHEN_SVC_LOGIN = (byte)0x1;
    public static byte TACPLUS_PRIV_LVL_MAX = (byte)0xf;
    
    /** Creates a new instance of AuthenPktStart */
    public AuthenPktStart() {
    }
    
}
