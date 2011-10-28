/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netention;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author seh
 */
abstract public class Property implements Serializable {

	private String desc;
	private String id;
	private String name;
	private int cardinalityMax = 1;
	private int cardinalityMin = 0;

        public Set<String> domains = new HashSet();
        
	public Property() {
		super();
		this.desc = "";
	}
	
	public Property(String id, String name) {
		this();
		this.id = id;
		this.name = name;
	}

        public void setDomains(Set<String> domains) {
            this.domains = domains;
        }

        public Set<String> getDomains() {
            return domains;
        }
                        
	public String getDescription() {
		return desc;
	}
	
	public String getID() {
		return id;
	}

        public void setID(String id) {
            this.id = id;
        }
        
        public static String stripQuotes(String text) {
            if (text.startsWith("\""))
                text = text.substring(1);
            if (text.endsWith("\""))
                text = text.substring(0, text.length()-1);
            return text;            
        }
        
        public void setName(String name) {
            this.name = stripQuotes(name);
        }
        
	public String getName() {
		if (name == null)
			return getID();
		return name;
	}
		
	@Override public String toString() {
		return getName() + " (" + getID() + ") isA " + getClass().getSimpleName();
	}

	public abstract PropertyValue newDefaultValue(Mode mode);

	/** max cardinality, -1 if unlimited */
	public int getCardinalityMax() {
		return cardinalityMax;
	}
	/** min cardinality, >= 0 */
	public int getCardinalityMin() {
		return cardinalityMin;
	}

	public void setCardinalityMax(int cardinalityMax) {
		this.cardinalityMax = cardinalityMax;
	}

	public void setCardinalityMin(int cardinalityMin) {
		this.cardinalityMin = cardinalityMin;
	}
	
	public Property setDescription(String description) {
		this.desc = description;
        return this;
	}    
}
