/**
 * 
 */
package netention.value.string;

import java.util.List;
import netention.Mode;
import netention.Property;
import netention.PropertyValue;

public class StringProp extends Property {

    private List<String> exampleValues;
    private boolean rich;

    public StringProp() {
        super();
    }

    public StringProp(String id, String name) {
        super(id, name);
    }

    public StringProp(String id, String name, int cardMin, int cardMax) {
        this(id, name);
        setCardinalityMin(cardMin);
        setCardinalityMax(cardMax);
    }

    public StringProp(String id, String name, List<String> exampleValues) {
        this(id, name);
        this.exampleValues = exampleValues;
    }

    @Override
    public PropertyValue newDefaultValue(Mode mode) {
        if (mode == Mode.Imaginary) {
            return new StringContains("");
        } else {
            return new StringIs("");
        }
    }

    public List<String> getExampleValues() {
        return exampleValues;
    }

    public boolean isRich() {
        return rich;
    }

    public void setRich(boolean rich) {
        this.rich = rich;
    }
}
