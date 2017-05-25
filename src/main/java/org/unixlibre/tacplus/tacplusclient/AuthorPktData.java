/*
 * AuthPktData.java
 *
 * 
 */

package org.unixlibre.tacplus.tacplusclient;

/**
 *
 * @author  antoniovl
 * Authorization Packet Data.
 * Equivalent to struct author_pkt_data
 */
public class AuthorPktData {
    
    private Identity identity = null;
    private Authorization author = null;
    private AuthorizationReply authorReply = null;
    private String[] argv = null;

    /** Creates a new instance of AuthPktData */
    public AuthorPktData() {
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public Authorization getAuthor() {
        return author;
    }

    public void setAuthor(Authorization author) {
        this.author = author;
    }

    public AuthorizationReply getAuthorReply() {
        return authorReply;
    }

    public void setAuthorReply(AuthorizationReply authorReply) {
        this.authorReply = authorReply;
    }

    public String[] getArgv() {
        return argv;
    }

    public void setArgv(String[] argv) {
        this.argv = argv;
    }

    public int getArgc() {
        return argv.length;
    }
}
