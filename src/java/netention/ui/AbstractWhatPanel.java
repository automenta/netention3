/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netention.ui;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import netention.Community;
import netention.Detail;

/**
 *
 * @author seh
 */
public class AbstractWhatPanel extends VerticalLayout {

    protected Detail detail;
    protected final HorizontalSplitPanel hsplit;
    protected final NApplication app;

    public AbstractWhatPanel(NApplication app) {
        super();

        hsplit = new HorizontalSplitPanel();
         
        this.app = app;
            
        Table t = newTable(app.getDetails(app.getSelf()));
        t.setSizeFull();
        hsplit.setFirstComponent(t);
                        
        addComponent(hsplit);
        
        setSizeFull();

    }
    
    
    public void setDetail(Detail d) {
        if (this.detail!=null) {
            //changing from previous
        }
        
        this.detail = d;

        //VerticalSplitPanel vsplit = new VerticalSplitPanel();
        
        VerticalLayout vs = new VerticalLayout();
        
        // Put other components in the right panel
        vs.addComponent(new Label(d.toString()));
        //vsplit.addComponent(new Label("Here's the lower panel"));
        
        hsplit.setSecondComponent(vs);
    }

    public Table newTable(Collection<Detail> details) {        
        final Table table = new Table();
        
        table.addContainerProperty("Name", String.class,  null);
        table.addContainerProperty("Types",  String.class,  null);
        table.addContainerProperty("Last Edited (seconds ago)",       Double.class, null);

        for (final Detail d : details) {
            

            double lastEditedSecondsAgo = (new Date().getTime()) - d.getEdited().getTime();
            table.addItem(new Object[] {
                d.getTitle(), Arrays.asList(d.getTypes()).toString(), lastEditedSecondsAgo
            }, d);
        }
        
        //table.setPageLength(table.size());

        table.setSelectable(true);
        //table.setMultiSelect(false);
        table.setImmediate(true); // react at once when something is selected      

        table.setColumnReorderingAllowed(true);
        
        table.addListener(new Table.ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
                setDetail((Detail)table.getValue());
            }
        });
        
        return table;
    }
    
}
