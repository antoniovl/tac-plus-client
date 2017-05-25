/*
 * AuthorizationReplyPkt.java
 *
 * Equivalente al struct author_reply
 */

package org.unixlibre.tacplus.tacplusclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 *
 * @author  antoniovl
 */
public class AuthorizationReply {

    public static final Logger logger = LoggerFactory.getLogger(AuthorizationReply.class);

    private byte status = 0;
    private byte argCnt = 0;
    private short msgLen = 0;
    private short dataLen = 0;

    public AuthorizationReply() {
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public byte getArgCnt() {
        return argCnt;
    }

    public void setArgCnt(byte argCnt) {
        this.argCnt = argCnt;
    }

    public short getMsgLen() {
        return msgLen;
    }

    public void setMsgLen(short msgLen) {
        this.msgLen = msgLen;
    }

    public short getDataLen() {
        return dataLen;
    }

    public void setDataLen(short dataLen) {
        this.dataLen = dataLen;
    }

    /**
     * Deserializes the input stream into this class.
     * @param dis
     * @throws IOException
     */
    public void loadData(DataInputStream dis) throws IOException {
        status = dis.readByte();
        argCnt = dis.readByte();
        msgLen = dis.readShort();
        dataLen = dis.readShort();
    }
}
