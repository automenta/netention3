/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netention.ui;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import java.util.Collection;
import netention.NType;

/**
 *
 * @author seh
 */
public class SchemaPanel extends VerticalLayout {
    private final NApplication app;
    private final HorizontalSplitPanel hsplit;
    private NType type;

    public SchemaPanel(NApplication app) {
        super();
    
        this.app = app;
        hsplit = new HorizontalSplitPanel();
        
        refresh();
    }
    
    
    protected synchronized void refresh() {
        removeAllComponents();
        
        //addComponent(new Label(app.getTypes(null).toString()));
       
        Table t = newTable(app.getTypes(null));
        t.setSizeFull();
        hsplit.setFirstComponent(t);
                        
        addComponent(hsplit);
        
        setSizeFull();

    }
    
    
    public void setType(NType t) {
        if (this.type!=null) {
            //changing from previous
        }
        
        this.type = t;

        VerticalSplitPanel vsplit = new VerticalSplitPanel();
        
        // Put other components in the right panel
        TextArea ta = new TextArea();
        ta.setValue(t.toString2(app));
        ta.setReadOnly(true);
        ta.setSizeFull();
        
        vsplit.addComponent(ta);
        vsplit.addComponent(new Label("Here's the lower panel"));
        
        hsplit.setSecondComponent(vsplit);
    }

    public Table newTable(Collection<NType> types) {        
        final Table table = new Table();
        
        table.addContainerProperty("Name", String.class,  null);
        table.addContainerProperty("# Inherited Properties",  Integer.class,  null);
        table.addContainerProperty("# Local Properties",  Integer.class,  null);
        table.addContainerProperty("Supertypes",       String.class, null);

        for (final NType t : types) {
            

            int numLocal = app.getProperties(t, false, false).size();
            int numTotal = app.getProperties(t, true, false).size();
            
            table.addItem(new Object[] {
                t.getName(), numTotal - numLocal, numLocal, t.superTypes.toString()}, t);
        }
        
        //table.setPageLength(table.size());

        table.setSelectable(true);
        //table.setMultiSelect(false);
        table.setImmediate(true); // react at once when something is selected      

        table.setColumnReorderingAllowed(true);
        
        table.addListener(new Table.ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
                setType((NType)table.getValue());
            }
        });
        
        return table;
    }
        
    
    
}
