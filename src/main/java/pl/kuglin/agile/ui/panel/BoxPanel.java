package pl.kuglin.agile.ui.panel;

import javax.swing.*;
import java.awt.*;

public class BoxPanel extends JPanel {

    private final Axis axis;

    public BoxPanel(Axis axis) {
        super();
        this.axis = axis;
        setDefault();
        setBackground(Color.WHITE);
    }

    protected void setDefault() {
        if (axis.equals(Axis.X_AXIS))
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        else
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public enum Axis {
        X_AXIS,
        Y_AXIS
    }
}
