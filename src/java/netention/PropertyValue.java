/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netention;

public abstract class PropertyValue implements Value {
	
	private String property;


	/** property ID or URI */
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}
        
        public PropertyValue forProperty(String propertyID) {
            this.property = propertyID;
            return this;
        }

}