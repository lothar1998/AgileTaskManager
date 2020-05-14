package pl.kuglin.agile.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class AbstractButton extends JButton {

    public AbstractButton(String text) {
        super(text);
        setUp();
        setDefault();
    }

    public AbstractButton(String text, ActionListener action) {
        super(text);
        addActionListener(action);
        setUp();
        setDefault();
    }

    protected void setDefault() {
        setSize(20, 20);
        setFont(new Font("Consolas", Font.BOLD, 13));
        setBackground(new Color(41, 142, 0));
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(new Color(255, 255, 255));
    }

    protected abstract void setUp();
}
