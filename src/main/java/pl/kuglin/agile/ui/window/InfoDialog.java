package pl.kuglin.agile.ui.window;

import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.button.OkButton;
import pl.kuglin.agile.ui.panel.BoxPanel;

import javax.swing.*;
import java.awt.*;

public class InfoDialog extends JDialog {

    private static final String WINDOW_NAME = "INFO";

    public InfoDialog(String text, AbstractWindow window) {
        super(window, WINDOW_NAME);

        JPanel mainPanel = new BoxPanel(BoxPanel.Axis.Y_AXIS);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel textPanel = new BoxPanel(BoxPanel.Axis.X_AXIS);

        textPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        JTextArea infoText = new JTextArea(text);
        infoText.setLineWrap(true);
        infoText.setEditable(false);
        infoText.setFont(new Font("Georgia", Font.PLAIN, 13));
        textPanel.add(infoText);
        textPanel.add(Box.createRigidArea(new Dimension(30, 0)));

        mainPanel.add(textPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel buttonPanel = new BoxPanel(BoxPanel.Axis.X_AXIS);

        JButton okButton = new OkButton(a -> this.dispose());

        buttonPanel.add(okButton);

        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        add(mainPanel);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

}
