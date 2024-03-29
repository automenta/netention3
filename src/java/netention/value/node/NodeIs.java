package netention.value.node;

import netention.DefiniteValue;
import netention.Detail;
import netention.IndefiniteValue;
import netention.PropertyValue;



/** references a Node/URI  */
public class NodeIs extends PropertyValue implements DefiniteValue<String> {

	private String nodeID;
	private String property;

	public NodeIs() { 
		super();
	}
	
	public NodeIs(Detail d) {
            this(d.getID().toString());
        }
        
	public NodeIs(String nodeID) {
		super();
		
		this.nodeID = nodeID;		
	}
	
	public String getNode() {
		return nodeID;
	}
	
	@Override public String getValue() {
		return nodeID;
	}
	
	@Override
	public double satisfies(IndefiniteValue i) {
		if (i.getClass().equals(NodeEquals.class)) {
			return ((NodeEquals)i).getNode().equals(getNode()) ? 1.0 : 0.0;
		}
		else if (i.getClass().equals(NodeNotEquals.class)) {
			return (!((NodeEquals)i).getNode().equals(getNode())) ? 1.0 : 0.0;			
		}
		return 0;
	}
	
	
	
}
