/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netention.test;

import com.google.common.base.Predicate;
import java.io.Serializable;
import java.util.Collection;
import netention.Community;
import netention.Detail;
import netention.MemCommunity;
import netention.Message;
import netention.Mode;
import netention.NType;
import netention.Self;
import netention.value.integer.IntProp;
import netention.value.integer.IntegerIs;
import netention.value.node.NodeIs;
import netention.value.node.NodeProp;
import netention.value.set.SelectionProp;
import netention.value.string.StringProp;
import netention.value.time.TimePointProp;
import netention.value.uri.URIProp;

/**
 *
 * @author seh
 */
public class ExampleCommunity extends MemCommunity implements Community, Serializable {

    public ExampleCommunity() {
        super();
        
        try {
            addAll(loadJSON("/tmp/netention.json"));
            System.out.println("Loaded " + getTypes().size() + " types, and " + getProperties().size() + " properties");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        addSeedSelfDefaults();
        
        {
            //add a test type
            NType tt = save(new NType("TypeTester").add(this, 
                new IntProp("integer_test", "Integer Test"),
                new NodeProp("node_test", "Node Test", "TypeTester", "TypeTester")));
            
            Detail d = new Detail();
            d.setTitle("TypeTester Instance");
            d.setMode(Mode.Real);
            d.addType(tt.getId());
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
        //TODO do actual lookup
        return new Self(agent,agent);
    }

    @Override
    public Collection<Detail> getDetails(final Self who) {
        return getDetails((Predicate)null);
    }


    @Override
    public Collection<Message> getMessages(final Self who) {
        return getMessages((Predicate)null);
    }

    public void addSeedSelfDefaults() {
        save(new NType("Thing").
                        setIconURL("media://tango32/categories/preferences-system.png").
                        setDescription("").add(this,
                            new StringProp("tag", "Tag", 0, -1).setDescription("tag, category, genre"),
                            new URIProp("link", "Link", 0, -1).setDescription("tag, category, genre")
                        ));

        save(new NType("Built").
                        setIconURL("media://tango32/categories/preferences-system.png").
                        setDescription("Something that is built or manufactured").
                        add(this,
                            new StringProp("manufacturer", "Manufacturer"),
                            new StringProp("serialNumber", "Serial Number"),
                            new TimePointProp("builtWhen", "When Built"),
                            new SelectionProp("condition", "Condition", "New", "Used")
                        ));
        
        save(new NType("Mobile").setIconURL("media://tango32/places/start-here.png").add(
                    this,
                        new StringProp("currentLocation", "Current Location"),
                        new StringProp("nextLocation", "Next Location")
                        //new IntProp("numWheels", "Number of Wheels"),
                        //new RealProp("wheelRadius", "Wheel Radius"),
                        //new BoolProp("hasKickStand", "Has Kickstand")
                    ));
        

        save(new NType("Person").setIconURL("media://tango32/apps/system-users.png").add(
                this,
                    new StringProp("fullName", "Full Name"),
                    new StringProp("biography", "Biography"),
                    new StringProp("emailAddress", "E-Mail"),
                    new StringProp("webAddress", "Website"),
                    new StringProp("birthdate", "Birthdate"),
                    new SelectionProp("gender", "Gender", "male", "female", "other"),
                    new SelectionProp("speaks", "Spoken Language", "English", "Spanish", "Arabic", "Cantonese", "French", "German", "Japanese", "Other")
                ));
        

//        s.addPattern(new Pattern("Project").setIconURL("media://tango32/mimetypes/x-office-presentation.png"));
//        {
//            s.addProperties("Project",
//                new StringProp("purpose", "Purpose"),
//                new StringProp("goal", "Goal"),
//                new StringProp("member", "Member")
//                );
//        }
//
//        s.addPattern(new Pattern("Event").setIconURL("media://tango32/mimetypes/x-office-calendar.png"));
//        {
//            s.addProperties("Event",
//                new StringProp("startTime", "Start Time"),
//                new StringProp("endTime", "End Time"),
//                new StringProp("location", "Location")
//                );
//        }
//        s.addPattern(new Pattern("Ingestion").setIconURL("media://tango32/mimetypes/x-office-calendar.png"));
//        {
//            s.addProperties("Ingestion",
//                new SelectionProp("ingestionType", "Type", "Food", "Beverage", "Other"),
//                new StringProp("ingestionType", "Type"),
//                new RealProp("ingestionMass", "Volume")
//                );
//        }
//        
//        s.addPattern(new Pattern("Excretion").setIconURL("media://tango32/mimetypes/x-office-calendar.png"));
//        {
//            s.addProperties("Excretion",
//                new SelectionProp("excretionType", "Type", "Urine", "Feces", "Other"),
//                new RealProp("excretionMass", "Mass"),
//                new RealProp("excretionVolume", "Volume")
//                );
//        }
//
//        s.addPattern(new Pattern("Media").setIconURL("media://tango32/categories/applications-multimedia.png"));
//        {
//            s.addProperties("Media",
//                new StringProp("url", "URL")
//                );
//        }
//
//        s.addPattern(new Pattern("Message").setIconURL("media://tango32/apps/internet-mail.png"));
//        {
//            s.addProperty(new StringProp("recipient", "Recipient"), "Message");
//        }
//
//
//        
//        s.addPattern(new Pattern("Business").setIconURL("media://tango32/apps/internet-mail.png"));
//        {
//            s.addProperties("Business",
//                new StringProp("stockticker", "Stock Ticker")
//                );            
//        }
//
//        s.addPattern(new Pattern("Psych").setIconURL("media://tango32/apps/internet-mail.png"));
//        {
//            s.addProperties("Psych",
//                new StringProp("emotion", "Emotion")
//                );
//        }
//
//        MemoryDetail d1 = new MemoryDetail("Red Bike", Mode.Real, "Built", "Mobile");
//        MemoryDetail d11 = new MemoryDetail("Blue Bike", Mode.Real, "Built");
//        MemoryDetail d2 = new MemoryDetail("Imaginary Bike", Mode.Imaginary, "Mobile", "Built");
//        MemoryDetail d3 = new MemoryDetail("What is Netention?", Mode.Real, "Message");
//        {
//            d1.addProperty("numWheels", new IntegerIs(4));
//            d1.addProperty("manufacturer", new StringIs("myself"));
//            d1.addProperty("wheelRadius", new RealIs(16.0));
//            d1.addProperty("hasKickStand", new BoolIs(true));
//            //d1.addProperty("anotherObject", new NodeIs(d2.getID()));
//            {
//                //d11.addProperty("numWheels", new IntegerIs(2));
//                d11.addProperty("manufacturer", new StringIs("myself"));
//                //d11.addProperty("wheelRadius", new RealIs(16.0));
//                //d11.addProperty("hasKickStand", new BoolIs(true));
//                //d11.addProperty("anotherObject", new NodeIs(d2.getID()));
//            }
//
//        }
//        {
//            //d2.addProperty("numWheels", new IntegerEquals(4));
//            d2.addProperty("manufacturer", new StringEquals("myself"));
//            //d2.addProperty("wheelRadius", new RealEquals(16.0));
//            //d2.addProperty("hasKickStand", new BoolEquals(true));
//            //d2.addProperty("anotherObject", new NodeEquals(d1.getID()));
//        }
//        {
//        }
//        s.addDetail(d1);
//        s.addDetail(d11);
//        s.addDetail(d2);
//        s.addDetail(d3);
//<schema>
//
//	<pattern id="Owned" name="Owned">
//		Something that is or can be owned, and possibly change ownership
//		<node id="owner" class="Being"/>
//		<node id="ownerNext" name="Next Owner" class="Being"/>
//	</pattern>
//
//	<pattern id="Valued" name="Worth Something">
//		Value of something; how much its worth
//	</pattern>
//
//	<pattern id="DollarValued" name="Worth US Dollars ($)">
//		Dollar value
//        <extend id="Valued"/>
//	</pattern>
//
//	<pattern id="ThingValued" name="Worth the Same Value as...">
//		Has a value that is the same as other objects
//        <node id="valuedSimilarlyTo" name="Valued Similarly To" maxMult="-1"/>
//        <extend id="Valued"/>
//	</pattern>
//
//	<pattern id="Rented" name="Rentable">
//		Something that is or can be rented/loaned/leased/borrowed
//        <extend id="Owned"/>
//		<node id="renter" class="Being"/>
//		<node id="renterNext" name="Next Renter" class="Being"/>
//		<real id="dailyCost" name="Daily Cost" unit="currency"/>
//	</pattern>
//
//	<pattern id="Located">
//		Something that has a specific location
//		<geopoint id="location"/>
//	</pattern>
//
//	<pattern id="Mobile">
//		Something that has a specific location, and a possible next location
//		<extend id="Located"/>
//		<geopoint id="locationNext" name="Next Location"/>
//	</pattern>
//
//	<pattern id="Delivered">
//		<extend id="Mobile"/>
//	</pattern>
//
//    <pattern id="Service">
//    </pattern>
//
//	<pattern id="Event">
//		<timepoint id="startTime" name="Start Time"/>
//		<timepoint id="endTime" name="End Time"/>
//	</pattern>
//
//    <pattern id="Reservation">
//        A reservation, ticket, or other reservable activity.  Ex: Restaurants, Shows, etc...
//        <extend id="Event"/>
//    </pattern>
//
//    <pattern id="RestaurantVisit" name="Restaurant Visit">
//            <extend id="Reservation"/>
//    </pattern>
//
//    <pattern id="MovieShowing" name="Movie Showing">
//            <extend id="Reservation"/>
//    </pattern>
//
//    <pattern id="Carpool" name="Carpool">
//            <extend id="Event"/>
//            <extend id="Mobile"/>
//    </pattern>
//
//
//    <pattern id="Gig">
//        Job with fixed start and stop time.
//        <extend id="Event"/>
//        <extend id="Service"/>
//    </pattern>
//
//    <pattern id="Project">
//        Project or goal
//    </pattern>
//
//	<pattern id="PhysicalThing" name="Physical Object">
//		<string id="color" maxMult="-1">
//			Predominant exterior color
//		</string>
//
//		<string id="material" maxMult="-1">
//			Predominant exterior material
//		</string>
//
//		<string id="condition" maxMult="1">
//			Old or new
//			<values>old, new</values>
//		</string>
//
//		<real id="mass" unit="mass"/>
//		<real id="length" unit="distance"/>
//		<real id="width" unit="distance"/>
//		<real id="height" unit="distance"/>
//
//	</pattern>
//
//	<pattern id="Built">
//		Something that is built or manufactured
//		<string id="builder" name="Builder Name"/>
//		<string id="serialID" name="Serial Number"/>
//		<timepoint id="builtWhen" name="When Built"/>
//	</pattern>
//
//	<pattern id="Being">
//		Life form
//	</pattern>
//
//    <pattern id="Animal">
//		<extend id="Being"/>
//    </pattern>
//
//	<pattern id="Human">
//		Human being
//		<extend id="Being"/>
//		<string id="firstName" name="First Name"/>
//		<string id="lastName" name="Last Name"/>
//		<string id="biography"/> <!-- richtext -->
//		<string id="emailAddress" name="EMail Address"/>
//		<string id="webURL" name="Website"/>
//	</pattern>
//
//	<pattern id="Bicycle">
//		<extend id="Built"/>
//		<int id="gearCount" name="Gear Count"/>
//		<real id="wheelDiameter" name="Wheel Diameter" unit="distance"/>
//		<string id="bicycleType" name="Bicycle Type">
//			[ 'mountain',
//       			"street",
//       			"hybrid",
//       			"tricycle",
//       			"unicycle",
//       			"tandem",
//       			"recumbent"
//       		]);
//		</string>
//	</pattern>
//
//	<pattern id="Dwelling">
//		<extend id="Built"/>
//		<int id="numBedrooms" name="Number of Bedrooms"/>
//	</pattern>
//
//	<pattern id="Guitar">
//		<extend id="Built"/>
//    </pattern>
//
//	<pattern id="OnlineAccount" name="Online Account">
//        <string id="login" name="Login" minMult="1" maxMult="1"/>
//        <string type="password" id="password" name="Password" minMult="1" maxMult="1"/>
//    </pattern>
//
//    <pattern id="TwitterAccount" name="Twitter Account">
//        Twitter Account
//        <extend id="OnlineAccount"/>
//    </pattern>
//
//    <pattern id="FaceBookAccount" name="FaceBook Account">
//        FaceBook Account
//        <extend id="OnlineAccount"/>
//    </pattern>
//
//    <pattern id="GoogleAccount" name="Google Account">
//        Google Account
//        <extend id="OnlineAccount"/>
//    </pattern>
//
//
//    <pattern id="MotorVehicle" name="Motor Vehicle">
//        <extend id="Built"/>
//    </pattern>
//
//    <pattern id="Test">Test</pattern>
//
//	<pattern id="testReal" name="Real Test">
//		<extend id="test"/>
//		<real id="rn1"/>
//		<real id="rn2"/>
//		<real id="rn3"/>
//		<real id="rn4"/>
//	</pattern>
//
//	<pattern id="testString" name="String Test">
//		<extend id="test"/>
//		<string id="s1"/>
//		<string id="s2"/>
//		<string id="s3"/>
//		<string id="s4"/>
//	</pattern>
//
//	<pattern id="testInteger" name="Integer Test">
//		<extend id="test"/>
//		<int id="i1"/>
//		<int id="i2"/>
//		<int id="i3"/>
//		<int id="i4"/>
//	</pattern>
//
//	<pattern id="testNode" name="Node Test">
//		<extend id="test"/>
//		<node id="n1"/>
//		<node id="n2"/>
//		<node id="n3"/>
//		<node id="n4"/>
//	</pattern>
//
//
//
//</schema>
//        
    }
}
