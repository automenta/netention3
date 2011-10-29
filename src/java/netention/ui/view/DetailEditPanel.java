/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netention.ui.view;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import netention.Detail;
import netention.NType;
import netention.Property;
import netention.PropertyValue;
import netention.ui.NApplication;

/**
 *
 * @author seh
 */
abstract public class DetailEditPanel extends VerticalLayout {

    public final Detail detail;
    private MenuItem patternMenu;
    private final MenuBar patternBar;
    private final VerticalLayout propertiesPanel;
    private final RichTextArea text;
    private final NApplication app;
    private final TextField nameField;

    public DetailEditPanel(NApplication app, Detail d) {
        super();
        this.detail = d;
        this.app = app;

        addStyleName("DetailEditPanel");
        
        HorizontalLayout topArea = new HorizontalLayout();
        topArea.setSpacing(false);
        topArea.setWidth("100%");
        {
            nameField = new TextField();
            nameField.setValue(d.getTitle());
            nameField.setWidth("100%");
            topArea.addComponent(nameField);
            topArea.setExpandRatio(nameField, 1.0f);

            Button expandButton = new Button("...");
            expandButton.addListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    text.setVisible(!text.isVisible());
                }                
            });
            topArea.addComponent(expandButton);

            Button copyButton = new Button("Copy");
            copyButton.addListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    //
                }
            });
            topArea.addComponent(copyButton);
            

            Button deleteButton = new Button("Delete");
            deleteButton.addListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    //
                }
            });
            topArea.addComponent(deleteButton);
            
            Button cancelButton = new Button("Cancel");
            cancelButton.addListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    cancel();
                }
            });
            topArea.addComponent(cancelButton);

            Button saveButton = new Button("Save");
            saveButton.addListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    controlsToObject();
                    save();
                }
            });
            topArea.addComponent(saveButton);
            
        }
        addComponent(topArea);
        
        text = new RichTextArea();
        text.setValue(d.getContent());
        text.setWidth("100%");
        text.setVisible(false);
        addComponent(text);

        HorizontalLayout typePanel = new HorizontalLayout();

        patternBar = new MenuBar();
        patternBar.addStyleName("PatternBar");
        patternBar.setWidth("100%");
        typePanel.addComponent(patternBar);


        addComponent(typePanel);

        propertiesPanel = new VerticalLayout();
        propertiesPanel.setWidth("100%");
        addComponent(propertiesPanel);

//        HorizontalLayout bottomPanel = new HorizontalLayout();
//
//        addComponent(bottomPanel);
//        setComponentAlignment(bottomPanel, Alignment.TOP_CENTER);

        //setComponentAlignment(bottomPanel, Alignment.TOP_RIGHT);
        
        refreshPatternBar();
        refreshProperties();
    }

    protected void controlsToObject() {
        detail.setTitle(text.toString());
        detail.setEdited(new Date());

        //TODO remove and add patterns
        
        //TODO remove and add properties

    }

    abstract public void cancel();

    abstract public void save();

    protected void refreshPatternBar() {
        patternBar.removeItems();
        patternMenu = patternBar.addItem(">>>", null, null);
        
        final Collection<NType> allPatterns = app.getTypes(null);
        final List<String> presentPatterns = detail.getTypes();

        Collection<NType> missingPatterns = Collections2.filter(allPatterns, new Predicate<NType>() {
            @Override
            public boolean apply(NType t) {
                return !presentPatterns.contains(t.getId());
            }
        });

        //1. add all non-present patterns to 'patternMenu'
        for (final NType pattern : missingPatterns) {
            String pName = pattern.getName();
            patternMenu.addItem(pName, new Command() {

                @Override
                public void menuSelected(MenuItem selectedItem) {
                    addNewPattern(pattern.getId());
                }
            });
        }
        patternMenu.addSeparator();
        patternMenu.addItem("New Pattern...", new Command() {
            @Override
            public void menuSelected(MenuItem selectedItem) {
                createNewPattern();
            }
        });


        //2. add all present patterns to 'patternBar' as menus
        for (final String p : presentPatterns) {
            NType pattern = app.getType(p);
            int total = getPropertiesTotal(pattern);
            MenuItem i = patternBar.addItem(pattern.getName() + " (" + (total - getPropertiesNotPresent(pattern)) + "/" + total + ")", null, null);

            for (final Property property : app.getProperties(pattern, true, false)) {
                if (supportsAnotherProperty(property)) {
                    i.addItem(property.getName(), new Command() {
                        @Override
                        public void menuSelected(MenuItem selectedItem) {
                            addProperty(property.getID());
                        }
                    });
                }
            }

            //i.addSeparator();
            //TODO add "Specialize to " [ subclasses]
            
            i.addSeparator();

            //move left (more important)
            //move right (less important)

            i.addItem("Remove", new Command() {
                @Override
                public void menuSelected(MenuItem selectedItem) {
                    removePattern(p);
                }
            });
        }
    }

    public void addProperty(String id) {
        Property p = app.getPropertyByID(id);
        PropertyValue propertyValue = p.newDefaultValue(detail.getMode());
        propertyValue.setProperty(id);

        detail.addValue(propertyValue);

        refreshPatternBar();
        refreshProperties();
    }


    public class MissingPropertyPanel extends HorizontalLayout {

        public MissingPropertyPanel(PropertyValue pv) {
            String lt = "Missing Property for Value: " + pv.toString();
            addComponent(new Label(lt));
        }

    }

    protected void refreshProperties() {
        propertiesPanel.removeAllComponents();

        for (final PropertyValue pv : detail.getValues()) {
            Property p = app.getPropertyByID(pv.getProperty());
            if (p!=null) {
                propertiesPanel.addComponent(new ValueEditPanel(p, pv) {
                    @Override
                    public void removeThis() {
                        removeProperty(pv);
                    }
                });
            }
             else {
                propertiesPanel.addComponent(new MissingPropertyPanel(pv));

             }
        }

    }

    public void removeProperty(PropertyValue pv) {
        detail.removeValue(pv);
        refreshPatternBar();
        refreshProperties();
    }

    public boolean definedProperty(String p) {
        for (PropertyValue pv : detail.getValues()) {
            if (pv.getProperty().equals(p))
                return true;
        }
        return false;
    }

    public int getPropertiesNotPresent(NType p) {
        int count = 0;
        for (Property pid : app.getAvailableProperties(detail, p.getId()).keySet()) {
            if (!definedProperty(pid.getID())) {
                count++;
            }
        }
        return count;
    }
    public int getPropertiesPresent(String propertyID) {
        int count = 0;
        for (PropertyValue pv : detail.getValues()) {
            if (pv.getProperty().equals(propertyID))
                count++;
        }
        return count;
    }

    public int getPropertiesTotal(NType p) {
        return app.getProperties(p, true, false).size();
    }

    abstract public void createNewPattern();

    public void addNewPattern(String patternID) {
        if (!detail.getTypes().contains(patternID)) {
            NType p = app.getType(patternID);
            if (p!=null) {
                detail.addType(patternID);

                for (Property pr : app.getProperties(p, false, false)) {
                    if (pr.getCardinalityMin() > 0) {
                        for (int i = getPropertiesPresent(pr.getID()); i < pr.getCardinalityMin(); i++) {
                            addProperty(pr.getID());
                        }
                    }
                }
            }


            refreshPatternBar();
        }        
    }

    public void removePattern(String patternID) {
        if (detail.getTypes().contains(patternID)) {
            detail.getTypes().remove(patternID);
            refreshPatternBar();
        }
    }

    public boolean supportsAnotherProperty(Property p) {
        if (p.getCardinalityMax() == -1)
            return true;

        int present = getPropertiesPresent(p.getID());
        if (present < p.getCardinalityMax())
            return true;

        return false;
    }

    
}
