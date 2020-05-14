package pl.kuglin.agile.ui.window;

import pl.kuglin.agile.persistence.BiConsumerSQL;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.button.CancelButton;
import pl.kuglin.agile.ui.button.SaveButton;
import pl.kuglin.agile.ui.panel.BoxPanel;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Map;


public class AddItemDialog extends JDialog {

    private static final String WINDOW_NAME = "Add new item";

    public AddItemDialog(AbstractWindow window, Map<String ,Component> textFields, BiConsumerSQL<AbstractWindow,  Map<String ,Component>> onYes) {
        super(window, WINDOW_NAME);

        JPanel mainPanel = new BoxPanel(BoxPanel.Axis.Y_AXIS);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel topPanel = new BoxPanel(BoxPanel.Axis.Y_AXIS);

        for(Map.Entry<String, Component> entry : textFields.entrySet()){

                JPanel linePanel = new BoxPanel(BoxPanel.Axis.X_AXIS);
                linePanel.setMaximumSize(new Dimension(400, 30));

                JLabel label = new JLabel(entry.getKey());
                label.setPreferredSize(new Dimension(100, 10));
                label.setHorizontalAlignment(SwingConstants.RIGHT);

                Component component = entry.getValue();
                component.setPreferredSize(new Dimension(50, 30));

                linePanel.add(label);
                linePanel.add(Box.createRigidArea(new Dimension(10, 0)));
                linePanel.add(entry.getValue());

            topPanel.add(linePanel);
            topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        mainPanel.add(topPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel buttonPanel = new BoxPanel(BoxPanel.Axis.X_AXIS);

        JButton saveButton = new SaveButton(a -> {
            try {
                onYes.accept(window, textFields);
            } catch (SQLException exception) {
                new ErrorDialog(exception.toString(), window);
            } finally {
                this.dispose();
            }
        });
        JButton cancelButton = new CancelButton(a -> this.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        add(mainPanel);
        setSize(400, textFields.size() * (30 + 10) + 80);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
}
