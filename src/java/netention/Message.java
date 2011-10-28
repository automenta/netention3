/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netention;

import java.util.Date;
import java.util.UUID;

/**
 *
 * @author seh
 */
public class Message {
    private Date sent = new Date();
    private String from;
    private String[] to;
    private String subject;
    private UUID id = UUID.randomUUID();
    private String content;
    private MessageStatus status = MessageStatus.Unread;
    
    public static enum MessageStatus {
        Unread, Read, Trashed
    }

    public Message() {
        super();
    }

    public Message(String from, String[] to, String subject, String content) {
        this();
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }           
    
    public Date getSent() {
        return sent;
    }
    
    public String getFrom() {
        return from;
    }
    
    public String[] getTo() {
        return to;
    }
    
    public String getSubject() {
        return subject;        
    }
    
    public String getContent() {
        return content;
    }
    
    public UUID getID() {
        return id;
    }
    
    public MessageStatus getStatus() {
        return status;
    }
    
    public void setStatus(MessageStatus m) {
        this.status = m;
    }
}
