package pl.kuglin.agile.ui;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractWindow extends JFrame {

    private static final Integer DEFAULT_WIDTH = 1280;
    private static final Integer DEFAULT_HEIGHT = 800;

    private static final Integer DEFAULT_MIN_WIDTH = 800;
    private static final Integer DEFAULT_MIN_HEIGHT = 600;

    public AbstractWindow(String title) {
        super(title);
        setUp();
        setDefault();
    }

    protected void setDefault(){
        setBackground(new Color(255, 255, 255));
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setMinimumSize(new Dimension(DEFAULT_MIN_WIDTH, DEFAULT_MIN_HEIGHT));
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    protected abstract void setUp();
}
