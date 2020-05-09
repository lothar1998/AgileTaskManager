package pl.kuglin.agile.ui;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractWindow extends JFrame {

    private static final Integer DEFAULT_WIDTH = 1280;
    private static final Integer DEFAULT_HEIGHT = 800;

    public AbstractWindow(String title) {
        super(title);
        setUp();
        setDefault();
    }

    protected void setDefault(){
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setVisible(true);
        setLayout();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    protected void setLayout(){
        setLayout(new GridBagLayout());
    }

    public void add(Container container){
        this.getContentPane().add(container);
    }

    protected abstract void setUp();
}
