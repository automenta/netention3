/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netention.ui.view;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.VerticalLayout;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Pattern;
import netention.Detail;
import netention.NType;
import netention.Property;
import netention.PropertyValue;
import netention.ui.NApplication;

/**
 *
 * @author seh
 */
abstract public class DetailEditPanel2 extends VerticalLayout {

    public final Detail detail;
    private final VerticalLayout patternList = new VerticalLayout();
    private final RichTextArea text;
    private final GridLayout patternBar;
    int patternColumns = 7;
    private final NApplication app;

    protected class PatternPanel extends VerticalLayout {

        public PatternPanel(NType p) {
            super();
            
            addStyleName("detailEditPanelPattern");
            addComponent(new BigLabel(p.getName()));

            VerticalLayout properties = new VerticalLayout();
            properties.setMargin(false, false, false, true);
            addComponent(properties);

            for (final PropertyValue pv : detail.getValues()) {
//                if (!p.containsKey(pv.getProperty()))
//                    continue;
                
                Property r = app.getPropertyByID(pv.getProperty());
                if (r != null) {
                    properties.addComponent(new ValueEditPanel(r, pv) {

                        @Override
                        public void removeThis() {
                            removeProperty(pv);
                        }
                    });
                } else {
                    properties.addComponent(new MissingPropertyPanel(pv));
                }
            }

            Button removeButton = new Button("Remove");
            removeButton.addStyleName("SmallLabel");
            addComponent(removeButton);
            setComponentAlignment(removeButton, Alignment.BOTTOM_RIGHT);
        }
    }

    public DetailEditPanel2(NApplication app, Detail d) {
        super();
        this.detail = d;
        this.app = app;

        text = new RichTextArea();
        text.setWidth("100%");
        addComponent(text);


        patternBar = new GridLayout();
        patternBar.setWidth("100%");

        refreshPatternBar();
        refreshProperties();

        addComponent(patternBar);
        
        patternList.setWidth("100%");
        patternList.setHeight("100%");
        addComponent(patternList);

        HorizontalLayout bottomPanel = new HorizontalLayout();
        {

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
        }
        bottomPanel.setMargin(true);
        addComponent(bottomPanel);
        setComponentAlignment(bottomPanel, Alignment.TOP_RIGHT);
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
        patternBar.removeAllComponents();

        final Collection<NType> allPatterns = app.getTypes(null);

        patternBar.setColumns(patternColumns);
        patternBar.setRows((int) Math.ceil(allPatterns.size() / patternColumns));

        //1. add all non-present patterns to 'patternMenu'
        for (final NType pattern : allPatterns) {
            String pName = pattern.getName();

            final PushButton pButton = new PushButton(pName, detail.getTypes().contains(pattern)) {

                @Override
                public void setPushed(boolean b) {
                    super.setPushed(b);
                    if (isPushed()) {
                        addNewPattern(pattern.getId());
                    } else {
                        removePattern(pattern.getId());
                    }
                }
            };
            pButton.setWidth("100%");
            pButton.setHeight("100%");
            //pButton.setIcon(new ThemeResource(pattern.getIconURL()));

            patternBar.addComponent(pButton);
        }
//        patternMenu.addSeparator();
//        patternMenu.addItem("New Pattern...", new Command() {
//
//            @Override
//            public void menuSelected(MenuItem selectedItem) {
//                createNewPattern();
//            }
//        });


//        //2. add all present patterns to 'patternBar' as menus
//        for (final String p : presentPatterns) {
//            Pattern pattern = schema.getPatterns().get(p);
//            int total = getPropertiesTotal(pattern);
//            MenuItem i = patternBar.addItem(pattern.getName() + " (" + (total - getPropertiesNotPresent(pattern)) + "/" + total + ")", null, null);
//
//            for (String property : pattern.keySet()) {
//                if (supportsAnotherProperty(property)) {
//                    final Property pr = schema.getProperty(property);
//                    i.addItem(pr.getName(), new Command() {
//
//                        @Override
//                        public void menuSelected(MenuItem selectedItem) {
//                            addProperty(pr.getID());
//                        }
//                    });
//                }
//            }
//
//            i.addSeparator();
//
//            //move left (more important)
//            //move right (less important)
//
//            i.addItem("Remove", new Command() {
//
//                @Override
//                public void menuSelected(MenuItem selectedItem) {
//                    removePattern(p);
//                }
//            });
//        }
    }

    public void addProperty(String id) {
        Property p = app.getPropertyByID(id);
        PropertyValue propertyValue = p.newDefaultValue(detail.getMode());
        propertyValue.setProperty(id);

        detail.getValues().add(propertyValue);

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
        patternList.removeAllComponents();

        for (String patternID : detail.getTypes()) {
            PatternPanel dp = new PatternPanel(app.getType(patternID));
            patternList.addComponent(dp);
        }

    }

    public void removeProperty(PropertyValue pv) {
        detail.getValues().remove(pv);
        refreshPatternBar();
        refreshProperties();
    }

    public boolean definedProperty(String p) {
        for (PropertyValue pv : detail.getValues()) {
            if (pv.getProperty().equals(p)) {
                return true;
            }
        }
        return false;
    }

    public int getPropertiesNotPresent(NType p) {
        int count = 0;
        for (Property pid : app.getProperties(p, true, true)) {
            if (!definedProperty(pid.getID())) {
                count++;
            }
        }
        return count;
    }

    public int getPropertiesPresent(String propertyID) {
        int count = 0;
        for (PropertyValue pv : detail.getValues()) {
            if (pv.getProperty().equals(propertyID)) {
                count++;
            }
        }
        return count;
    }

//    public int getPropertiesTotal(NType p) {
//        return p.size();
//    }

    abstract public void createNewPattern();

    public void addNewPattern(String patternID) {
        if (!detail.getTypes().contains(patternID)) {
            NType p = app.getType(patternID);
            if (p != null) {
                detail.addType(patternID);

                for (Property pr : app.getAvailableProperties(detail).keySet()) {
                    if (pr.getCardinalityMin() > 0) {
                        for (int i = getPropertiesPresent(pr.getID()); i < pr.getCardinalityMin(); i++) {
                            addProperty(pr.getID());
                        }
                    }
                }
            }


            refreshPatternBar();
            refreshProperties();
        }
    }

    public void removePattern(String patternID) {
        if (detail.getTypes().contains(patternID)) {
            detail.getTypes().remove(patternID);
            refreshPatternBar();
            refreshProperties();
        }
    }

    public boolean supportsAnotherProperty(String propertyID) {
        Property p = app.getPropertyByID(propertyID);
        if (p.getCardinalityMax() == -1) {
            return true;
        }

        int present = getPropertiesPresent(propertyID);
        if (present < p.getCardinalityMax()) {
            return true;
        }

        return false;
    }
}
