/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netention.ui;

import com.vaadin.ui.VerticalLayout;
import netention.Detail;
import netention.ui.view.DetailEditPanel;

/**
 *
 * @author seh
 */
public class RealWhatPanel extends AbstractWhatPanel {

    public RealWhatPanel(NApplication app) {
        super(app);
    }

    @Override
    public void setDetail(Detail d) {
        if (this.detail!=null) {
            //changing from previous
        }
        
        this.detail = d;

        //VerticalSplitPanel vsplit = new VerticalSplitPanel();
        
        VerticalLayout vs = new VerticalLayout();
        
        // Put other components in the right panel
        vs.addComponent(new DetailEditPanel(app, d) {

            @Override
            public void cancel() {
            }

            @Override
            public void save() {
            }

            @Override
            public void createNewPattern() {
            }
            
        });
        //vsplit.addComponent(new Label("Here's the lower panel"));
        
        hsplit.setSecondComponent(vs);
    }
    
    
}
