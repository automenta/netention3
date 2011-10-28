/**
 * 
 */
package netention.value.integer;

import netention.Mode;
import netention.PropertyValue;
import netention.value.real.RealProp;



public class IntProp extends RealProp {

    public IntProp() {
            super();
    }

    public IntProp(String id, String name) {
        super(id, name);
    }
    public IntProp(String id, String name, String... domain) {
        this(id, name);
        for (String d : domain)
            domains.add(d);
    }
	
//	public IntProp(String id, String name) {
//		super(id, name);
//	} 
	
    public boolean isInteger() { return true; }	

    @Override
    public PropertyValue newDefaultValue(Mode mode) {
        if (mode == Mode.Imaginary) {
            return new IntegerEquals(0);
        }
        else {
            return new IntegerIs(0);
        }
	}

}