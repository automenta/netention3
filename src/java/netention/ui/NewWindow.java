/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netention.ui;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import java.util.HashMap;
import java.util.Map;
import netention.Detail;
import netention.Mode;
import netention.NType;

/**
 *
 * @author seh
 */
abstract public class NewWindow extends VerticalLayout {

    Detail detail = new Detail();
    private final TextField nameField;
    private final NativeSelect modeSelect;
    private final NApplication app;
    
    public NewWindow(NApplication app) {
        super();
        
        this.app = app;
        
        HorizontalLayout top = new HorizontalLayout();
        {
            modeSelect = new NativeSelect();
            modeSelect.addItem("Real");
            modeSelect.addItem("Imaginary");
            modeSelect.setNullSelectionAllowed(false);
            modeSelect.setValue("Real");
            top.addComponent(modeSelect);

            nameField = new TextField();
            nameField.setValue("Untitled");
            top.addComponent(nameField);
            
            top.setExpandRatio(modeSelect, 0.0f);
            top.setExpandRatio(nameField, 1.0f);
            
            Button cancelButton = new Button("Cancel");
            cancelButton.addListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    cancel();
                }                
            });
            top.addComponent(cancelButton);

            Button createButton = new Button("Create");
            createButton.addListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    create();
                }                
            });
            top.addComponent(createButton);

            top.setComponentAlignment(cancelButton, Alignment.BOTTOM_RIGHT);
            top.setComponentAlignment(createButton, Alignment.BOTTOM_RIGHT);
            
        }
        top.setWidth("100%");
        addComponent(top);
        
        
        TreeTable ta = new TreeTable();
        ta.addContainerProperty("Type", String.class,  null);
        ta.addContainerProperty("Description", String.class,  null);
        ta.addContainerProperty("Involved",  HorizontalLayout.class,  null);

        Map<NType,Object> mp = new HashMap();
        
        for (final NType t : app.getTypes(null)) {
                        
            HorizontalLayout hl = new HorizontalLayout();
            hl.addComponent(new CheckBox(""));
            
            mp.put(t, ta.addItem(new Object[] {
                t.getName(), t.getDescription(), hl}, t));
            
        }
        for (final NType t : app.getTypes(null)) {
            if (t.getSubtypes(t, app).isEmpty())
                ta.setChildrenAllowed(mp.get(t), false);
            else
                ta.setCollapsed(mp.get(t), false);
            for (String s : t.superTypes) {
                ta.setParent(mp.get(t), mp.get(app.getType(s)));      
                break;
            }
        }

//        //All roots
//        for (final NType t : app.getTypes(new Predicate<NType>() {
//            @Override
//            public boolean apply(NType t) {
//                return t.superTypes.isEmpty();
//            }            
//        })) {
//            drillDown(table, mp, t);
//
//        }
        
        ta.setPageLength(10);

        ta.setSelectable(true);
        //table.setMultiSelect(false);
        ta.setImmediate(true); // react at once when something is selected      

        ta.setWidth("100%");
        ta.setHeight("25%");
        addComponent(ta);
        
        
        setSizeFull();
        
    }

    public Detail getDetail() {
        return detail;
    }
    
    abstract protected void close();
    
    protected void cancel() {
        detail = null;
        close();
    }
    
    protected void create() {
        detail.setTitle(nameField.getValue().toString());
        detail.setAuthor(app.getSelf()==null ? "" : app.getSelf().getUsername());
        detail.setMode( modeSelect.getValue().equals("Real") ? Mode.Real : Mode.Imaginary);
                
        //TODO store types
        
        app.save(detail);
        
        close();        
    }
    
}
