package pl.kuglin.agile.ui.scrollpane;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class CustomScrollPane extends JScrollPane {

    public CustomScrollPane(Component view) {
        super(view);
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(0, 0,0 ,0));
        getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.WHITE;
                this.trackColor = Color.WHITE;
                this.thumbHighlightColor = new Color(130, 130, 124);
                this.thumbDarkShadowColor = new Color(130, 130, 124);
            }
        });
    }
}
