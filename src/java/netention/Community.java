/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netention;

import com.google.common.base.Predicate;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author me
 */
public interface Community {
    
        //SELF
        //Any user
        public Self getSelf(String userid);
        
        //MESSAGE
        public Collection<Message> getMessages(Self who);
        public Collection<Message> getMessages(Predicate<Message> pred);
        public void save(Message m);

        //DETAIL
        public Collection<Detail> getDetails(Predicate<Detail> pred);
        public Collection<Detail> getDetails(Self who);
        public void save(Detail d);
        
        //NTYPE
        public NType newType(String id);
        public NType getType(String id);
        public Collection<NType> getTypes(Predicate<NType> pred);
        public void save(NType t);
        public void remove(NType t);
        public Set<String> getSuperTypes(NType type);
        
        //PROPERTY
        public Property getPropertyByID(String id);
        public Collection<Property> getProperties(Predicate<Property> pred);
        public Collection<Property> getProperties(final NType type, boolean includeInherited, boolean includeOrphaned);
        public void save(Property p);
        public void remove(Property p);
        
        public Map<Property, Double> getAvailableProperties(Detail d, String... patternID);
   
//   public Map<Property, Double> getAvailableProperties(Detail d, String... patternID) {
//        Map<Property, Double> a = new HashMap();
//        for (String pid : patternID) {
//            Pattern pat = patterns.get(pid);
//            if (pat != null) {
//                for (String propid : pat.keySet()) {
//                    Property prop = properties.get(propid);
//                    if (!containsProperty(d, prop)) {
//                        Double propStrength = pat.get(propid);
//                        a.put(prop, propStrength);
//                    }
//                }
//
//            }
//        }
//        return a;
//    }        
   
}
