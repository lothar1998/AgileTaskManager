package pl.kuglin.agile.ui.command.strategy;

import pl.kuglin.agile.persistence.entities.SprintEntity;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.window.AddItemDialog;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.Map;

public class AddSprintStrategy extends AbstractAddStrategy{
    private static final String NO_KEY = "No: ";
    private static final String NAME_KEY = "Name: ";

    private static final NumberFormatter numberFormatter;

    static {
        NumberFormat integerFormat = NumberFormat.getIntegerInstance();

        numberFormatter = new NumberFormatter(integerFormat);
        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setOverwriteMode(true);
    }

    @Override
    public void accept(AbstractWindow window) throws SQLException {

        Map<String, Component> inputTextFields = new LinkedHashMap<>();
        inputTextFields.put(NO_KEY, new JFormattedTextField(numberFormatter));
        inputTextFields.put(NAME_KEY, new JTextField());

        new AddItemDialog(window, inputTextFields, (w, tf) -> {

            SprintEntity sprintEntity = new SprintEntity();
            sprintEntity.setNo(Integer.valueOf((Integer)(((JFormattedTextField)tf.get(NO_KEY)).getValue())));
            sprintEntity.setName(((JTextField)tf.get(NAME_KEY)).getText());
            sprintEntity.setProjectId(window.getProjectId());

            window.getRepositoryPack().getSprintRepository().save(sprintEntity);

            window.getTable().addRow(sprintEntity.getId(), sprintEntity.getNo(), sprintEntity.getName());
        });
    }
}
