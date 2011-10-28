/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netention.ui;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import netention.Message;
import netention.Message.MessageStatus;

/**
 * Profile and Messages Status Display
 * @author seh
 */
public class NowPanel extends VerticalLayout {
    private Table table;
    
    HashSet<Object> markedRows = new HashSet<Object>();
    //private final Label selected;
    private final NApplication app;

    public NowPanel(NApplication app) {
        super();
        
        this.app = app;
        
        refresh();
    }
    
    protected void refresh() {
        removeAllComponents();
        
        final Collection<Message> messages = app.getMessages(app.getSelf());

        table = updateTable(messages);
        
        // Label to indicate current selection
        //selected = new Label("None selected");
        //addComponent(selected);

        // size
        table.setSizeFull();


        addComponent(table);
        
        setSizeFull();
        
    }

    public Table updateTable(Collection<Message> messages) {        
        final Table table = new Table("Messages");
        
        table.addContainerProperty("Sent", Date.class,  null);
        table.addContainerProperty("From",  String.class,  null);
        table.addContainerProperty("Subject",       String.class, null);
        table.addContainerProperty("Message",       VerticalLayout.class, null);

        for (final Message m : messages) {
            if (m.getStatus() == MessageStatus.Trashed)
                continue;
            
            VerticalLayout v = new VerticalLayout();
            
            TextArea f = new TextArea(null, m.getContent());
            f.setColumns(20);
            f.setImmediate(true);
            f.setWordwrap(true);
            f.setReadOnly(true);
            f.addStyleName("MessageContentPreview");
            f.setWidth("100%");
            
            v.addComponent(f);
            
            HorizontalLayout c = new HorizontalLayout();
            
            Button deleteButton = new Button("Trash");
            deleteButton.addListener(new ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    m.setStatus(MessageStatus.Trashed);
                    refresh();
                }
            });
            c.addComponent(deleteButton);
            
            v.addComponent(c);
            
            v.setExpandRatio(f, 1f);
            v.setExpandRatio(c, 0);

            table.addItem(new Object[] {
                m.getSent(), m.getFrom(), m.getSubject(), v
            }, m.getID());
        }
        
        //table.setPageLength(table.size());

        // selectable
        
        table.setSelectable(true);
        //table.setMultiSelect(false);
        //table.setImmediate(true); // react at once when something is selected      

        // turn on column reordering and collapsing
        table.setColumnReorderingAllowed(true);
        //table.setColumnCollapsingAllowed(true);

//
//        // Column alignment
//        table.setColumnAlignment(ExampleUtil.iso3166_PROPERTY_SHORT,
//                Table.ALIGN_CENTER);
//
//        // Column width
//        table.setColumnExpandRatio(ExampleUtil.iso3166_PROPERTY_NAME, 1);
//        table.setColumnWidth(ExampleUtil.iso3166_PROPERTY_SHORT, 70);
//
//        // Collapse one column - the user can make it visible again
//        table.setColumnCollapsed(ExampleUtil.iso3166_PROPERTY_FLAG, true);

        // show row header w/ icon
//        table.setRowHeaderMode(Table.ROW_HEADER_MODE_ICON_ONLY);
//        table.setItemIconPropertyId(ExampleUtil.iso3166_PROPERTY_FLAG);

//        // Actions (a.k.a context menu)
//        table.addActionHandler(new Action.Handler() {
//            public Action[] getActions(Object target, Object sender) {
//                if (markedRows.contains(target)) {
//                    return ACTIONS_MARKED;
//                } else {
//                    return ACTIONS_UNMARKED;
//                }
//            }
//
//            public void handleAction(Action action, Object sender, Object target) {
//                if (ACTION_MARK == action) {
//                    markedRows.add(target);
//                    table.requestRepaint();
//                } else if (ACTION_UNMARK == action) {
//                    markedRows.remove(target);
//                    table.requestRepaint();
//                } else if (ACTION_LOG == action) {
//                    Item item = table.getItem(target);
//                    addComponent(new Label("Saved: "
//                            + target
//                            + ", "
//                            + item.getItemProperty(
//                                    ExampleUtil.iso3166_PROPERTY_NAME)
//                                    .getValue()));
//                }
//
//            }
//
//        });
//
//        // style generator
//        table.setCellStyleGenerator(new CellStyleGenerator() {
//            public String getStyle(Object itemId, Object propertyId) {
//                if (propertyId == null) {
//                    // no propertyId, styling row
//                    return (markedRows.contains(itemId) ? "marked" : null);
//                } else if ("Subject".equals(propertyId)) {
//                    return "bold";
//                } else {
//                    // no style
//                    return null;
//                }
//
//            }
//            
//        });

        // listen for valueChange, a.k.a 'select' and update the label
        table.addListener(new Table.ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
                // in multiselect mode, a Set of itemIds is returned,
                // in singleselect mode the itemId is returned directly
                //selected.setValue("Selected: " + table.getValue());
            }
        });
        
        return table;
    }
    
}
