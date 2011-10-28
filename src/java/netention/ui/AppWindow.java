/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netention.ui;

import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import netention.Community;
import netention.Self;
import org.vaadin.appfoundation.authentication.SessionHandler;

/**
 *
 * @author seh
 */
public class AppWindow extends Window {
    private final Community community;
    private Self agent;
    private final String agentID;

    private final NApplication app;
    
    public AppWindow(NApplication app, Community c, String agent) {
        super("Netention");
        
        this.app = app;
        this.community = c;
        this.agentID = agent;

        getContent().setSizeFull();

        refresh();
        
    }
    
    protected void refresh() {
        this.agent = (Self)SessionHandler.get();
        
        getContent().removeAllComponents();
        
        //Panel panel = new Panel(/*getAgent().getName()*/);
        
        VerticalLayout xv = new VerticalLayout();
        
        

//        private static final ThemeResource icon1 = new ThemeResource(
//            "../sampler/icons/action_save.gif");
//        private static final ThemeResource icon2 = new ThemeResource(
//            "../sampler/icons/comment_yellow.gif");
//        private static final ThemeResource icon3 = new ThemeResource(
//            "../sampler/icons/icon_info.gif");

        TabSheet t;

        
        // Tab 2 content
        VerticalLayout l2 = new VerticalLayout();
        l2.setMargin(true);
        l2.addComponent(new Label("There are no saved notes."));
        // Tab 3 content
        VerticalLayout l3 = new VerticalLayout();
        l3.setMargin(true);
        l3.addComponent(new Label("There are currently no issues."));
        // Tab 4 content
        VerticalLayout l4 = new VerticalLayout();
        l4.setMargin(true);
        l4.addComponent(new Label("There are no comments."));
        // Tab 5 content
        VerticalLayout l5 = new VerticalLayout();
        l5.setMargin(true);
        l5.addComponent(new Label("There is no new feedback."));

        t = new TabSheet();
        t.addTab(new SchemaPanel(app), "Ontology");
        t.addTab(new NowPanel(app), "Now");
        t.addTab(new RealWhatPanel(app), "What Is");
        t.addTab(l5, "What Will Be");
        t.addTab(l2, "Who");
        t.addTab(l3, "Where");
        t.addTab(l4, "When");
        
        //t.addListener(this);
        t.setSizeFull();

        Header header = new Header(app, this, agent);
        xv.addComponent(header);

        xv.addComponent(t);
        
        xv.setExpandRatio(header, 0);
        xv.setExpandRatio(t, 1.0f);

        
        xv.setSizeFull();
        //panel.setContent(xv);

        //panel.setSizeFull();
        
        getContent().addComponent(xv);

    }
}
