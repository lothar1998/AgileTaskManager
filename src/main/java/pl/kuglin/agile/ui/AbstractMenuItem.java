package pl.kuglin.agile.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class AbstractMenuItem extends JMenuItem {

    public AbstractMenuItem(String text) {
        super(text);
        setUp();
        setDefault();
    }

    public AbstractMenuItem(String text, ActionListener action) {
        super(text);
        addActionListener(action);
        setUp();
        setDefault();
    }

    protected void setDefault() {
        setFont(new Font("Georgia", Font.BOLD, 12));
        setBackground(new Color(41, 142, 0));
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(new Color(255, 255, 255));
        setPreferredSize(new Dimension(100, 30));
    }

    protected abstract void setUp();
}
