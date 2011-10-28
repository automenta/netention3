/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netention;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author seh
 */
public class Detail implements Serializable {   
    
    private UUID id;
    private Date created, edited;
    private String author;
    private String title;
    private String content;
    private List<String> types = new LinkedList();
    private List<PropertyValue> values = new LinkedList();
    private Mode mode = Mode.Unknown;

    public Detail() {
        super();
        id = UUID.randomUUID();
        created = edited = new Date();
    }   
    
    public void setID(UUID id) {
        this.id = id;
    }
        
    public UUID getID() {
        return id;
    }
    
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
        
    public Date getEdited() {
        return edited;
    }

    public void setEdited(Date edited) {
        this.edited = edited;
    }
    
    public String getAuthor() {
        return author;
    }       
    
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    
    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
       
    public List<PropertyValue> getValues() {
        return values;
    }

    public void setValues(List<PropertyValue> values) {
        this.values = values;
    }

    public void addType(String typeID) {
        types.add(typeID);
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    public void addValue(PropertyValue p) {
        values.add(p);
    }
    public void removeValue(PropertyValue p) {
        values.remove(p);
    }

    boolean hasValue(String v) {
        for (PropertyValue pv : values) {
            if (pv.getProperty().equals(v))
                return true;
        }
        return false;
    }
        
    
    
}
