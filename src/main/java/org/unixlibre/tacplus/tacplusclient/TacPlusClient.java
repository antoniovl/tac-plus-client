/*
 * TacPlusClient.java
 *
 *
 */

package org.unixlibre.tacplus.tacplusclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.*;
/**
 *
 * @author  antoniovl
 */
public class TacPlusClient {
    public static final Logger logger = LoggerFactory.getLogger(TacPlusClient.class);

    public static int TACPLUS_PORT = 49;
    
    private InetAddress ipAddr;
    private String host;
    private int serverPort;
    private String username = "";
    
    private Identity identity;
    private AuthorPktData data;

    public TacPlusClient(String tacPlusHost) {
        this.host = tacPlusHost;
        this.serverPort = TACPLUS_PORT;
    }

    public TacPlusClient(String tacPlusHost, int serverPort) {
        this.host = tacPlusHost;
        this.serverPort = serverPort;
    }
    
    /**
     * Initializes part of the system, basically the Identity Struct.
     */
    public void init() {
        
        try {
            // Builds Identity Structure
            identity = new Identity();
            ipAddr = InetAddress.getLocalHost();
            identity.setUsername(username);
            identity.setNASName(ipAddr.getCanonicalHostName());
            identity.setNASPort("console");
            identity.setNACAddress(ipAddr.getHostAddress());
            identity.setPrivLvl(AuthenPktStart.TACPLUS_PRIV_LVL_MAX);
            
            // Init Data structure
            data = new AuthorPktData();
            data.setIdentity(identity);
            
            // Init other structures
            Authorization auth = new Authorization();
            auth.setAuthenMethod(AuthorizationData.AUTHEN_METH_TACACSPLUS);
            auth.setPrivLvl((byte)data.getIdentity().getPrivLvl());
            auth.setAuthenType(AuthenPktStart.TACPLUS_AUTHEN_TYPE_ASCII);
            auth.setService(AuthenPktStart.TACPLUS_AUTHEN_SVC_LOGIN);
            auth.setUserLen((byte)data.getIdentity().getUsername().length());
            auth.setPortLen((byte)data.getIdentity().getNASPort().length());
            auth.setRemAddrLen((byte)data.getIdentity().getNACAddress().length());
            data.setAuthor(auth);
            
            // Sends shell request
            requestShellService(data);
            
        } catch (IOException e) {
            logger.error("TacPlusClient.init()", e);
            throw new IllegalStateException(e);
        }
    }
    
    
    /**
     * Creates a new connection. Equivalent to tachs_start_session().
     */
    protected Socket open() throws IOException {
        Socket socket;
        try {
            socket = new Socket(InetAddress.getByName(host), serverPort);
        } catch (IOException e) {
            logger.error("startSession()", e);
            throw e;
        }
        return socket;
    }

    
    /**
     * Gets the reply from the server.
     * Equivalent to tacsh_get_reply()
     */
    protected ByteArrayOutputStream getReply(Socket s) throws IOException {
        byte[] hdrArr = new byte[TacPlusAuthorPktHdr.TAC_PLUS_HDR_SIZE];
        TacPlusAuthorPktHdr hdr = new TacPlusAuthorPktHdr();
        byte[] tmpb;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (DataInputStream dis = new DataInputStream(s.getInputStream())) {
            hdr.loadData(dis);
            hdr.writeBytes(baos);

            tmpb = new byte[hdr.getDataLength()];
            dis.read(tmpb, 0, hdr.getDataLength());
            baos.write(tmpb, 0, tmpb.length);
        }

        return baos;
    }
    
    /**
     * Construye el packet de Autorization Request.
     * Regresa la longitud del packet.
     * equivalente a auth_req_buildpkt.
     * La class AuthorPktData debe contener completa la class Identity
     * y argv.
     */
    public ByteArrayOutputStream buildAuthorReqPkt(AuthorPktData apd) throws IOException {
        if (apd == null) {
            System.err.println("TacPlusClient.buildAuthorreqPkt(): Estructura NULL");
            return null;
        }
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Random rnd = new Random();
        
        /*
        Authorization auth = new Authorization();
        auth.setAuthenMethod(AuthorizationData.AUTHEN_METH_TACACSPLUS);
        auth.setPrivLvl((byte)apd.getIdentity().getPrivLvl()); // perdida de precision???
        auth.setAuthenType(AuthenPktStart.TACPLUS_AUTHEN_TYPE_ASCII);
        auth.setService(AuthenPktStart.TACPLUS_AUTHEN_SVC_LOGIN);
        auth.setUserLen((byte)apd.getIdentity().getUsername().length());
        auth.setPortLen((byte)apd.getIdentity().getNASPort().length());
        auth.setRemAddrLen((byte)apd.getIdentity().getNACAddress().length());
        // Not sure if this belongs here
        apd.setAuthor(auth);
         */
        
        // Builds the pkt body
        ByteArrayOutputStream pktBody = buildAuthorReqPktBody(apd);
        
        // Builds the pkt header
        TacPlusAuthorPktHdr hdr = new TacPlusAuthorPktHdr();
        hdr.setVersion(TacPlusAuthorPktHdr.TAC_PLUS_VER_0);
        hdr.setType(TacPlusAuthorPktHdr.TAC_PLUS_AUTHOR);
        hdr.setSecNo((byte)1);
        hdr.setEncryption(TacPlusAuthorPktHdr.TAC_PLUS_CLEAR);
        hdr.setSessionId(rnd.nextInt());
        hdr.setDataLength(pktBody.size());
        
        // writes the hdr
        ByteArrayOutputStream pktHdr = new ByteArrayOutputStream();
        pktHdr.reset();
        hdr.writeBytes(pktHdr);
        
        // appends data
        pktHdr.write(pktBody.toByteArray(), 0, pktBody.size());

        return pktHdr;
    }
    
    /**
     * Builds the packet body
     */
    public ByteArrayOutputStream buildAuthorReqPktBody(AuthorPktData apd) throws IOException {
        
        if (apd == null) {
            logger.error("AuthorPktData with null valued passed to buildAuthorReqPktBody()");
            throw new IllegalArgumentException("AuthorPktData null");
        }
        
        byte[] tmpb = null;
        
        // Calculates the String lengths.
        String[] argv = apd.getArgv();
        //int argLen = argv[0].length();
        //for (int i = 1; i < argv.length; i++) {
        //    argLen += argv[i].length();
        //}

        Authorization author = apd.getAuthor();
        author.setArgCnt((byte)apd.getArgc());
        apd.setAuthor(author);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        author.writeBytes(baos);
        
        // Write args lengths
        for (int i = 0; i < argv.length; i++) {
            baos.write(argv[i].length());
        }

        Identity id = apd.getIdentity();
        
        tmpb = id.getUsername().getBytes();
        baos.write(tmpb, 0, tmpb.length);
        tmpb = id.getNASPort().getBytes();
        baos.write(tmpb, 0, tmpb.length);
        tmpb = id.getNACAddress().getBytes();
        baos.write(tmpb, 0, tmpb.length);
        
        // Writes the args
        for (int i = 0; i < argv.length; i++) {
            tmpb = argv[i].getBytes();
            baos.write(tmpb, 0, tmpb.length);
        }

        return baos;
    }
    
    /**
     * Sends a byte array
     */
    protected void sendRequest(byte[] b, Socket s) throws IOException {
        try {
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            dos.write(b, 0, b.length);
            //dos.close();
        } catch (IOException e) {
            logger.error("sendRequest()", e);
            throw e;
        }
    }
    
    
    /**
     * Sends a shell request. this is the start of the authorization process.
     */
    protected void requestShellService(AuthorPktData apd) throws IOException {
        String[] argv = new String[2];
        argv[0] = "service=shell";
        argv[1] = "cmd*";
        apd.setArgv(argv);
        
        ByteArrayOutputStream baos = buildAuthorReqPkt(apd);
        
        try (Socket s = open()) {
            sendRequest(baos.toByteArray(), s);

            getReply(s);
            // for the moment ingnore the answer
        }
    }
    
    /**
     * Argv formatting.
     */
    protected void formatCmdLine(String[] argv, AuthorPktData apd) {
        String[] tmpArg = new String[argv.length + 1];
        
        tmpArg[0] = "service=shell";
        StringBuffer m = new StringBuffer("cmd=");
        
        m.append(argv[0]);
        tmpArg[1] = m.toString();
        
        for (int i = 1; i < argv.length; i++) {
            m = new StringBuffer("cmd-arg=");
            m.append(argv[i]);
            tmpArg[i+1] = m.toString();
        }
        
        apd.setArgv(tmpArg);
    }
    
    
    /**
     * Simple parsing of the command line argument to process.
     */
    public String[] splitCmdLine(String cmdLine) {
        StringTokenizer st = new StringTokenizer(cmdLine);
        String[] argv = new String[st.countTokens()];
        int i = 0;
        while (st.hasMoreTokens()) {
            argv[i] = st.nextToken();
            i++;
        }
        return argv;
    }


    /**
     * Send the command for auhorization.
     */
    public boolean sendCmd(String[] argv) throws IOException {
        //Socket s = null;

        formatCmdLine(argv, this.data);

        ByteArrayOutputStream boas = buildAuthorReqPkt(data);

        try (Socket s = new Socket(InetAddress.getByName(host), serverPort)) {
            //s = open();
            sendRequest(boas.toByteArray(), s);

            ByteArrayOutputStream reply = getReply(s);
            ByteArrayInputStream bais = new ByteArrayInputStream(reply.toByteArray());
            try (DataInputStream din = new DataInputStream(bais)) {

                TacPlusAuthorPktHdr hdr = new TacPlusAuthorPktHdr();
                hdr.loadData(din);

                AuthorizationReply authorReply = new AuthorizationReply();
                authorReply.loadData(din);


                //data.setAuthorReply(authorReply);
                switch (authorReply.getStatus()) {
                    case 16:
                        logger.error("Authorization Failed");
                        break;
                    case 17:
                        logger.error("Authorization Error");
                        break;
                }

                return (authorReply.getStatus() == 1);
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
