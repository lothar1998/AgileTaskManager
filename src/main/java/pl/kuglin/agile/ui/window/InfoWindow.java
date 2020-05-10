package pl.kuglin.agile.ui.window;

import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.button.OkButton;

import javax.swing.*;
import java.awt.*;

public class InfoWindow extends AbstractWindow {

    private static final Integer DEFAULT_WIDTH = 350;
    private static final Integer DEFAULT_HEIGHT = 120;

    private final Runnable action;
    private JLabel label;

    public InfoWindow(String title, String infoText) {
        super(title);
        label.setText(infoText);
        this.action = () -> {};
    }

    public InfoWindow(String title, String infoText, Runnable action){
        super(title);
        label.setText(infoText);
        this.action = action;
    }

    @Override
    protected void setDefault() {
        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(new Color(255, 255, 255));
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    @Override
    protected void setUp() {
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.weightx = 1.0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        label = new JLabel();
        layout.setConstraints(label, constraints);
        add(label);

        constraints.insets = new Insets(20, 0, 0, 0);
        constraints.anchor = GridBagConstraints.PAGE_END;
        OkButton okButton = new OkButton(a -> {
            SwingUtilities.invokeLater(action);
            dispose();
        });
        layout.setConstraints(okButton, constraints);
        add(okButton);
    }
}
