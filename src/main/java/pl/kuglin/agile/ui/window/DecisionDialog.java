package pl.kuglin.agile.ui.window;

import pl.kuglin.agile.persistence.ConsumerSQL;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.button.CancelButton;
import pl.kuglin.agile.ui.button.YesButton;
import pl.kuglin.agile.ui.panel.BoxPanel;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class DecisionDialog extends JDialog {

    private static final String WINDOW_NAME = "Are you sure?";

    public DecisionDialog(String text, AbstractWindow window, ConsumerSQL<AbstractWindow> onYes) {
        super(window, WINDOW_NAME);

        JPanel mainPanel = new BoxPanel(BoxPanel.Axis.Y_AXIS);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel textPanel = new BoxPanel(BoxPanel.Axis.X_AXIS);

        textPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        JTextArea infoText = new JTextArea(text);
        infoText.setLineWrap(true);
        infoText.setEditable(false);
        textPanel.add(infoText);
        textPanel.add(Box.createRigidArea(new Dimension(30, 0)));

        mainPanel.add(textPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel buttonPanel = new BoxPanel(BoxPanel.Axis.X_AXIS);

        JButton yesButton = new YesButton(a -> {
            try {
                onYes.accept(window);
            } catch (SQLException exception) {
                new ErrorDialog(exception.toString(), window);
            }
            this.dispose();
        });
        JButton cancelButton = new CancelButton(a -> {
            this.dispose();
        });
        buttonPanel.add(yesButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        add(mainPanel);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
}
