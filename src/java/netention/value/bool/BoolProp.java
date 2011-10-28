/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package netention.value.bool;

import netention.Mode;
import netention.Property;
import netention.PropertyValue;
import netention.value.bool.BoolEquals;
import netention.value.bool.BoolIs;

/**
 *
 * @author seh
 */
public class BoolProp extends Property {

    public BoolProp() {
        super();
    }
    
    public BoolProp(String id, String name) {
        super(id, name);
	}


    @Override
    public PropertyValue newDefaultValue(Mode mode) {
        if (mode == Mode.Imaginary) {
            return new BoolEquals(true);
        }
        else {
            return new BoolIs(true);
        }
    }

}
