package netention.ui.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.util.Date;
import netention.Detail;

public class WideDetailIcon extends VerticalLayout {

    public static class PatternsPanel extends HorizontalLayout {

        public PatternsPanel(Detail d) {
            super();

            for (String p : d.getTypes()) {
                addComponent(new Button(p));
            }

        }


    }
    
    public WideDetailIcon(Detail d) {
        this(d, null);
    }

    public WideDetailIcon(Detail d, ClickListener nameClicked) {
        super();

        addStyleName("WideDetailIcon");

        if (nameClicked==null) {
            Label nameLabel = new BigLabel(d.getTitle());
            addComponent(nameLabel);
        }
         else {

            Button nameButton = new Button(d.getTitle());
            nameButton.addStyleName(Button.STYLE_LINK);
            nameButton.addListener(nameClicked);
            addComponent(nameButton);
         }
    
        if (d.getTypes()!=null)
            if (d.getTypes().length > 0)
                addComponent(new PatternsPanel(d));

        //Date modified = d.getEdited();
        //String s = DurationFormatUtils.formatDurationWords(new Date().getTime() - modified.getTime(), true, true);
        //addComponent(new SmallLabel("Updated " + s + " ago"));
    }
}
