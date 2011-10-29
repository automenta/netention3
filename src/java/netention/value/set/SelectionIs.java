/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package netention.value.set;

import netention.DefiniteValue;
import netention.IndefiniteValue;
import netention.PropertyValue;


/**
 *
 * @author seh
 */
public class SelectionIs extends PropertyValue implements DefiniteValue<String> {

	private String option;

	public SelectionIs() { }

	public SelectionIs(String option) {
		super();
		this.option = option;
	}

    public String getOption() {
        return option;
    }
    
	@Override public String getValue() {
		return option;
	}

	public void setValue(String s) {
		this.option = s;
	}

	@Override public double satisfies(IndefiniteValue i) {
		if (i.getClass().equals( SelectionEquals.class )) {
			//TODO implement string-difference-distance fall-off (ex: 0.05 * # of chars different)
			return ((SelectionEquals)i).getValue().equalsIgnoreCase(getValue()) ? 1.0 : 0.0;
		}
		return 0;
	}

}
