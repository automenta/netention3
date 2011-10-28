/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netention;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author seh
 */
public class NType implements Serializable {
    
    public static class NMember {        
        public boolean required;        
        //double weight
    }

    public String id;  //URI
    private String name;     //TODO internationalize
    public String description;      //TODO internationalize
    
    public List<String> superTypes = new LinkedList();    
    
    public Map<String,NMember> members = new HashMap();

    public NType() {
        super();
    }
    
    public NType(final String id) {
        super();
        
        this.id = id;
        this.name = id;
//        this.node = n;
//        try {
//            if (node!=null)
//                new JSONDeserializer().deserializeInto(node.getProperty("json").getString(), this);
//        } catch (Exception ex) { 
//            ex.printStackTrace();
//        }
        
    }
    
//    public void save() {
//        try {
//            node.setProperty("json", new JSONSerializer().deepSerialize(this));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    public void setName(String name) {
        this.name = Property.stripQuotes(name);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }      
    
    
    @Override
    public String toString() {
        return name + " (" + id + ") extends " + superTypes.toString();
    }
    
    public String toString2(Community c) {
        String s = "";
        s += name + " (" + id + ")\n\n";
        s += "Extends:\n";
        for (String x : superTypes) {
            s +="    " + x + "\n";
        }
        //s += c.getSuperTypes(this).toString() + "\n";                
        
        s += "\n";
        
        s += "Local Properties:\n";
        Collection<Property> props = c.getProperties(this, false, false);
        for (Property x : props) {
            s +="    " + x.toString() + "\n";
        }
        
        s += "\n";

        s += "Inherited Properties:\n";
        Collection<Property> props2 = c.getProperties(this, true, false);
        for (Property x : props2) {
            if (!props.contains(x))
                s +="    " + x.toString() + "\n";
        }

        s += "\n";

        s += "Other Properties:\n";
        Collection<Property> props3 = c.getProperties(this, true, true);
        for (Property x : props3) {
            if (!props.contains(x))
                if (!props2.contains(x))
                    s +="    " + x.toString() + "\n";
        }
        
        return s;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NType) {
            return id.equals((((NType)obj)).id);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    
    
    
    
 
    
    
}
