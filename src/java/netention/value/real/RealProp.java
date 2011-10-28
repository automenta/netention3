/**
 * 
 */
package netention.value.real;

import netention.Mode;
import netention.Property;
import netention.PropertyValue;



public class RealProp extends Property {

	//private Unit unit;
	
	public RealProp() {
		super();
	}
	
	public RealProp(String id, String name) {
		super(id, name);
	}
	
//	public RealProp(String id, String name, Unit unit) {
//		super(id, name);
//		this.unit = unit;	
//	} 
	
//	public Unit getUnit() {
//		return unit;
//	}
	
    public boolean isInteger() { return false; }	

    @Override public PropertyValue newDefaultValue(Mode mode) {
        if (mode == Mode.Imaginary)
            return new RealEquals(0.0);
        else
            return new RealIs(0.0);
	}

//	public static Unit getUnit(String s) {
//		if (s == null)
//			return null;
//		
//		if (s.equalsIgnoreCase("mass")) return Unit.Mass;
//		if (s.equalsIgnoreCase("volume")) return Unit.Volume;
//		if (s.equalsIgnoreCase("area")) return Unit.Area;
//		if (s.equalsIgnoreCase("distance")) return Unit.Distance;
//		if (s.equalsIgnoreCase("currency")) return Unit.Currency;
//		if (s.equalsIgnoreCase("number")) return Unit.Number;
//		if (s.equalsIgnoreCase("speed")) return Unit.Speed;
//		if (s.equalsIgnoreCase("timeduration")) return Unit.TimeDuration;
//		if (s.equalsIgnoreCase("timepoint")) return Unit.TimePoint;
//		return null;
//	}
	
}