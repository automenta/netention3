/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netention;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author seh
 */
public class MemCommunity implements Community, Serializable {

    private Map<String,Property> properties = new ConcurrentHashMap();
    private Map<String,NType> types = new ConcurrentHashMap();
    private Map<UUID,Detail> details = new ConcurrentHashMap();
    private Map<UUID,Message> messages = new ConcurrentHashMap();
    private Map<String,Self> agents = new ConcurrentHashMap();
    
    
    public synchronized void addAll(MemCommunity mc) {
        properties.putAll(mc.properties);
        types.putAll(mc.types);
        details.putAll(mc.details);
        messages.putAll(mc.messages);
        agents.putAll(mc.agents);
    }
    
    public static MemCommunity loadJSON(String filePath) throws Exception {
        MemCommunity c = new JSONDeserializer<MemCommunity>().deserialize(new FileReader(filePath));
        return c;
    }
    
    public void saveJSON(String filePath) throws Exception {
        //new JSONSerializer().prettyPrint(true).deepSerialize(this, new FileWriter(filePath));
        
        StringBuffer sb = new StringBuffer();
        new JSONSerializer().deepSerialize(this, sb);
        new FileWriter(filePath).append(sb.toString()).flush();
    }

    public Map<String, Self> getAgents() {
        return agents;
    }

    public Map<UUID, Detail> getDetails() {
        return details;
    }

    public Map<String, Property> getProperties() {
        return properties;
    }

    public Map<String, NType> getTypes() {
        return types;
    }

    @Override
    public Self getSelf(String agent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<Detail> getDetails(Self who) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<Message> getMessages(Self who) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public NType getType(String id) {
        return types.get(id);
    }


    @Override
    public NType save(NType t) {
        types.put(t.getId(), t);
        return t;
    }

    @Override
    public void remove(NType t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public Property getPropertyByID(String id) {
        return properties.get(id);
    }


    @Override
    public void save(Property p) {
        properties.put(p.getID(), p);
    }

    @Override
    public void remove(Property p) {
        properties.remove(p.getID());
    }

    @Override
    public Map<Property, Double> getAvailableProperties(Detail d, String... patternID) {
        Map<Property,Double> mp = new HashMap();
        for (String t : d.getTypes()) {
            NType nt = getType(t);
            for (Property pp : getProperties(nt, true, true)) {
                if (!d.hasValue(pp.getID())) {
                    //TODO differentiate the properties via different weights
                    mp.put(pp, 1.0);
                }
            }
        }
        return Collections.unmodifiableMap(mp);
    }
    
    @Override
    public Collection<Property> getProperties(final NType type, final boolean includeInherited, final boolean includeOrphaned) {
        return Collections2.filter(getProperties(null), new Predicate<Property>() {
            @Override
            public boolean apply(final Property t) {
                if (type == null)
                    return false;
                
                if (t.getDomains().isEmpty())
                    return includeOrphaned;
                
                if (includeInherited) {
                    Set<String> superclasses = getSuperTypes(type);
                    for (String s : superclasses)
                        if (t.getDomains().contains(s))
                            return true;
                }
                return (t.getDomains().contains(type.id));
            }

        });
    }
    
    @Override
    public Set<String> getSuperTypes(NType type) {
        Set<String> sc = new HashSet();
        getSuperclasses(sc, type);
        return Collections.unmodifiableSet(sc);    
    }
    
    protected void getSuperclasses(Set<String> target, NType t) {
        if (t == null)
            return;
        
        if (t.superTypes == null)
            return;
        
        for (String x : t.superTypes) {
            if (!target.contains(x)) {
                target.add(x);
                getSuperclasses(target, getType(x));
            }
        }
    }

    @Override
    public void save(Detail d) {
        details.put(d.getID(), d);
    }
    
    @Override
    public Collection<Detail> getDetails(Predicate<Detail> pred) {
        if (pred!=null)
            return Collections2.filter(details.values(), pred);
        return
            details.values();
    }
    @Override
    public Collection<Property> getProperties(Predicate<Property> pred) {
        if (pred!=null)
            return Collections2.filter(properties.values(), pred);
        return
            properties.values();        
    }
    
    @Override
    public Collection<NType> getTypes(Predicate<NType> pred) {
        if (pred!=null)
            return Collections2.filter(types.values(), pred);
        return
            types.values();
    }

    @Override
    public void save(Message m) {
        messages.put(m.getID(), m);
    }

    @Override
    public Collection<Message> getMessages(Predicate<Message> pred) {
        if (pred!=null)
            return Collections2.filter(messages.values(), pred);
        return
            messages.values();
    }

    public void print() {
        System.out.println("NTYPE");
        for (NType n : types.values()) {
            System.out.println("  " + n);
        }
        System.out.println("PROPERTY");
        for (Property n : properties.values()) {
            System.out.println("  " + n);
        }
    }
    
    
    
    
    
}
