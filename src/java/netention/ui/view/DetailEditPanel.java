/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netention.ui.view;

import automenta.netention.Detail;
import automenta.netention.Pattern;
import automenta.netention.Property;
import automenta.netention.PropertyValue;
import automenta.netention.Schema;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.VerticalLayout;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;

/**
 *
 * @author seh
 */
abstract public class DetailEditPanel extends VerticalLayout {

    public final Detail detail;
    private MenuItem patternMenu;
    private final Schema schema;
    private final MenuBar patternBar;
    private final VerticalLayout propertiesPanel;
    private final RichTextArea text;

    public DetailEditPanel(Schema schema, Detail d) {
        super();
        this.detail = d;
        this.schema = schema;

        text = new RichTextArea();
        text.setWidth("100%");
        addComponent(text);


        HorizontalLayout typePanel = new HorizontalLayout();

        patternBar = new MenuBar();
        patternBar.setWidth("100%");
        typePanel.addComponent(patternBar);

        refreshPatternBar();

        addComponent(typePanel);

        propertiesPanel = new VerticalLayout();
        propertiesPanel.setWidth("100%");
        addComponent(propertiesPanel);

        HorizontalLayout bottomPanel = new HorizontalLayout();

        Button cancelButton = new Button("Cancel");
        cancelButton.addListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                cancel();
            }
        });
        bottomPanel.addComponent(cancelButton);

        Button saveButton = new Button("Save");
        saveButton.addListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                controlsToObject();
                save();
            }
        });
        bottomPanel.addComponent(saveButton);
        addComponent(bottomPanel);
        setComponentAlignment(bottomPanel, Alignment.TOP_CENTER);

        //setComponentAlignment(bottomPanel, Alignment.TOP_RIGHT);
    }

    protected void controlsToObject() {
        detail.setName(text.toString());
        detail.setWhenModified(new Date());

        //TODO remove and add patterns
        
        //TODO remove and add properties

    }

    abstract public void cancel();

    abstract public void save();

    protected void refreshPatternBar() {
        patternBar.removeItems();
        patternMenu = patternBar.addItem(">>>", null, null);
        
        final Collection<String> allPatterns = schema.getPatterns().keySet();
        final List<String> presentPatterns = detail.getPatterns();

        Collection<String> missingPatterns = new LinkedList(allPatterns);
        CollectionUtils.filter(missingPatterns, new Predicate<String>() {

            @Override
            public boolean evaluate(String t) {
                return !presentPatterns.contains(t);
            }
        });

        //1. add all non-present patterns to 'patternMenu'
        for (final String p : missingPatterns) {
            Pattern pattern = schema.getPatterns().get(p);
            String pName = pattern.getName();
            patternMenu.addItem(pName, new Command() {

                @Override
                public void menuSelected(MenuItem selectedItem) {
                    addNewPattern(p);
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
            Pattern pattern = schema.getPatterns().get(p);
            int total = getPropertiesTotal(pattern);
            MenuItem i = patternBar.addItem(pattern.getName() + " (" + (total - getPropertiesNotPresent(pattern)) + "/" + total + ")", null, null);

            for (String property : pattern.keySet()) {
                if (supportsAnotherProperty(property)) {
                    final Property pr = schema.getProperty(property);
                    i.addItem(pr.getName(), new Command() {
                        @Override
                        public void menuSelected(MenuItem selectedItem) {
                            addProperty(pr.getID());
                        }
                    });
                }
            }

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
        Property p = schema.getProperty(id);
        PropertyValue propertyValue = p.newDefaultValue(detail.getMode());
        propertyValue.setProperty(id);

        detail.getProperties().add(propertyValue);

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

        for (final PropertyValue pv : detail.getProperties()) {
            Property p = schema.getProperty(pv.getProperty());
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
        detail.getProperties().remove(pv);
        refreshPatternBar();
        refreshProperties();
    }

    public boolean definedProperty(String p) {
        for (PropertyValue pv : detail.getProperties()) {
            if (pv.getProperty().equals(p))
                return true;
        }
        return false;
    }

    public int getPropertiesNotPresent(Pattern p) {
        int count = 0;
        for (String pid : p.keySet()) {
            if (!definedProperty(pid)) {
                count++;
            }
        }
        return count;
    }
    public int getPropertiesPresent(String propertyID) {
        int count = 0;
        for (PropertyValue pv : detail.getProperties()) {
            if (pv.getProperty().equals(propertyID))
                count++;
        }
        return count;
    }

    public int getPropertiesTotal(Pattern p) {
        return p.size();
    }

    abstract public void createNewPattern();

    public void addNewPattern(String patternID) {
        if (!detail.getPatterns().contains(patternID)) {
            Pattern p = schema.getPatterns().get(patternID);
            if (p!=null) {
                detail.getPatterns().add(patternID);

                for (String propertyID : p.keySet()) {
                    //String spr = p.get(propertyID);
                    Property pr = schema.getProperty(propertyID);
                    if (pr.getCardinalityMin() > 0) {
                        for (int i = getPropertiesPresent(propertyID); i < pr.getCardinalityMin(); i++) {
                            addProperty(pr.getID());
                        }
                    }
                }
            }


            refreshPatternBar();
        }        
    }

    public void removePattern(String patternID) {
        if (detail.getPatterns().contains(patternID)) {
            detail.getPatterns().remove(patternID);
            refreshPatternBar();
        }
    }

    public boolean supportsAnotherProperty(String propertyID) {
        Property p = schema.getProperty(propertyID);
        if (p.getCardinalityMax() == -1)
            return true;

        int present = getPropertiesPresent(propertyID);
        if (present < p.getCardinalityMax())
            return true;

        return false;
    }

}
