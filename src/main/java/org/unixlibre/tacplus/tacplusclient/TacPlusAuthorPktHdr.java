/*
 * TacPlusAuthorPktHdr.java
 *
 * Created on November 9, 2004, 11:05 AM
 */

package org.unixlibre.tacplus.tacplusclient;


import java.io.*;
import java.util.*;
/**
 *
 * @author  antoniovl
 */
public class TacPlusAuthorPktHdr {
    
    public static byte TAC_PLUS_HDR_SIZE = (byte)12;
    
    public static byte TAC_PLUS_MAJOR_VER_MASK = (byte)0xf0;
    public static byte TAC_PLUS_MAJOR_VER = (byte)0xc0;
    public static byte TAC_PLUS_MINOR_VER_0 = (byte)0x0;
    public static byte TAC_PLUS_VER_0 = (byte)(TAC_PLUS_MAJOR_VER | TAC_PLUS_MINOR_VER_0);
    public static byte TAC_PLUS_MINOR_VER_1 = (byte)0x1;
    public static byte TAC_PLUS_VER_1 = (byte)(TAC_PLUS_MAJOR_VER | TAC_PLUS_MINOR_VER_1);

    public static byte TAC_PLUS_AUTHEN = (byte)0x1;
    public static byte TAC_PLUS_AUTHOR = (byte)0x2;
    public static byte TAC_PLUS_ACCT   = (byte)0x3;

    public static byte TAC_PLUS_ENCRYPTED = (byte)0x0;
    public static byte TAC_PLUS_CLEAR = (byte)0x1;

    private byte version;
    private byte type;
    private byte secNo;
    private byte encryption;
    private int sessionId;
    private int dataLength;

    public TacPlusAuthorPktHdr() {
    }
    
    public void loadBytes(byte[] b) {
        version = b[0];
        type = b[1];
        secNo = b[2];
    }
    
    /**
     * Deserialize data stream.
     */
    public void loadData(DataInputStream d) throws IOException {
        version = d.readByte();
        type = d.readByte();
        secNo = d.readByte();
        encryption = d.readByte();
        sessionId = d.readInt();
        dataLength = d.readInt();
    }
    
    /**
     * Serializes data packet
     */
    public void writeBytes(ByteArrayOutputStream baos) throws IOException {
        baos.write(version);
        baos.write(type);
        baos.write(secNo);
        baos.write(encryption);

        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(sessionId);
        dos.writeInt(dataLength);
    }

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getSecNo() {
        return secNo;
    }

    public void setSecNo(byte secNo) {
        this.secNo = secNo;
    }

    public byte getEncryption() {
        return encryption;
    }

    public void setEncryption(byte encryption) {
        this.encryption = encryption;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getDataLength() {
        return dataLength;
    }

    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }
}
