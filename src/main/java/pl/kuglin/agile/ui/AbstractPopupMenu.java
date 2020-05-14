package pl.kuglin.agile.ui;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractPopupMenu extends JPopupMenu {

    public AbstractPopupMenu() {
        super();
        setUp();
        setDefault();
    }

    protected void setDefault() {
        setFont(new Font("Georgia", Font.BOLD, 12));
        setBackground(new Color(208, 206, 77));
        setBorderPainted(false);
        setFocusable(true);
        setForeground(new Color(0, 0, 0));
    }

    protected abstract void setUp();

}
