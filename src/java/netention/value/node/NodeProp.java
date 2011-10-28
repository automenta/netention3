package netention.value.node;

import com.google.common.collect.Sets;
import java.util.Set;
import netention.Mode;
import netention.Property;
import netention.PropertyValue;


public class NodeProp extends Property {

        public Set<String> ranges;

	public NodeProp() {
		super();
	}
	
	public NodeProp(String id, String name, String theDomain, String theRange) {
            this(id, name, Sets.newHashSet(theDomain), Sets.newHashSet(theRange));
        }
        
	public NodeProp(String id, String name, Set<String> domains, Set<String> ranges) {
		super(id, name);
		                
                setDomains(domains);
                setRanges(ranges);
	}

        public void setRanges(Set<String> ranges) {
            this.ranges = ranges;
        }

        public Set<String> getRanges() {
            return ranges;
        }
        
	
	@Override
	public String toString() {
		return getName() + " (" + getID() + ") isA NodeProp<" + getDomains() + ", " + getRanges() + ">";
	}
	
	
        @Override public PropertyValue newDefaultValue(Mode mode) {
		NodeIs ni = new NodeIs("");
		ni.setProperty(getID());
		return ni;
	}
	
}
