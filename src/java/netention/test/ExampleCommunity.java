/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netention.test;

import com.google.common.base.Predicate;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import netention.Community;
import netention.Detail;
import netention.MemCommunity;
import netention.Message;
import netention.Message.MessageStatus;
import netention.Mode;
import netention.NType;
import netention.PropertyValue;
import netention.Self;
import netention.value.integer.IntProp;
import netention.value.integer.IntegerIs;
import netention.value.node.NodeIs;
import netention.value.node.NodeProp;

/**
 *
 * @author seh
 */
public class ExampleCommunity extends MemCommunity implements Community, Serializable {

    public ExampleCommunity() {
        super();
        
        try {
            addAll(loadJSON("/tmp/netention.json"));
            //System.out.println("Loaded " + getTypes().size() + " types, and " + getProperties().size() + " properties");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        {
            //add a test type
            NType tt = newType("TypeTester");
            save(new IntProp("integer_test", "Integer Test", tt.id));
            save(new NodeProp("node_test", "Node Test", tt.id, tt.id));
            
            Detail d = new Detail();
            d.setTitle("TypeTester Instance");
            d.setMode(Mode.Real);
            d.addType(tt.getID());
            d.addValue(new IntegerIs(1).forProperty("integer_test"));
            d.addValue(new NodeIs(d).forProperty("node_test"));
            save(d);
        }
        
        
        int numMsg = 3;
        for (int i = 0; i < numMsg; i++) {
            save(new Message((char)('A' + Math.random()*25) + "Someone", new String[] { "You" }, "Hi! How are you?", "Respond when you can.  Incrementally deploying http://localhost:8080/net Completed incremental distribution of http://localhost:8080/net Incrementally redeploying http://localhost:8080/net"));
        }

    }
    
    
    @Override
    public Self getSelf(final String agent) {
        return new Self() {

            @Override
            public String getName() {
                return agent;
            }
            
        };
    }

    @Override
    public Collection<Detail> getDetails(final Self who) {
        return getDetails((Predicate)null);
    }


    @Override
    public Collection<Message> getMessages(final Self who) {
        return getMessages((Predicate)null);
    }

}
