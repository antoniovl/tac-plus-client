/*
 * AuthorizationPkt.java
 *
 * Header del Authorization Packet
 * Equivalente a struct author
 */

package org.unixlibre.tacplus.tacplusclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author  antoniovl
 */
public class Authorization {
    private static final Logger logger = LoggerFactory.getLogger(Authorization.class);
    public static int AUTHORIZATION_LEN = 8;
    
    private byte authenMethod = 0;
    private byte privLvl = 0;
    private byte authenType = 0;
    private byte service = 0;
    private byte userLen = 0;
    private byte portLen = 0;
    private byte remAddrLen = 0;
    private byte argCnt = 0;

    public Authorization() {
    }

    public byte getAuthenMethod() {
        return authenMethod;
    }

    public void setAuthenMethod(byte authenMethod) {
        this.authenMethod = authenMethod;
    }

    public byte getPrivLvl() {
        return privLvl;
    }

    public void setPrivLvl(byte privLvl) {
        this.privLvl = privLvl;
    }

    public byte getAuthenType() {
        return authenType;
    }

    public void setAuthenType(byte authenType) {
        this.authenType = authenType;
    }

    public byte getService() {
        return service;
    }

    public void setService(byte service) {
        this.service = service;
    }

    public byte getUserLen() {
        return userLen;
    }

    public void setUserLen(byte userLen) {
        this.userLen = userLen;
    }

    public byte getPortLen() {
        return portLen;
    }

    public void setPortLen(byte portLen) {
        this.portLen = portLen;
    }

    public byte getRemAddrLen() {
        return remAddrLen;
    }

    public void setRemAddrLen(byte remAddrLen) {
        this.remAddrLen = remAddrLen;
    }

    public byte getArgCnt() {
        return argCnt;
    }

    public void setArgCnt(byte argCnt) {
        this.argCnt = argCnt;
    }

    /**
     * Serialize this data structure into an array of bytes.
     */
    public void writeBytes(ByteArrayOutputStream baos) throws IOException {
        DataOutputStream dos = new DataOutputStream(baos);
        dos.write(authenMethod);
        dos.write(privLvl);
        dos.write(authenType);
        dos.write(service);
        dos.write(userLen);
        dos.write(portLen);
        dos.write(remAddrLen);
        dos.write(argCnt);
    }
}
