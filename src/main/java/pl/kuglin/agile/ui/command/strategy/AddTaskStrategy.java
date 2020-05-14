package pl.kuglin.agile.ui.command.strategy;

import pl.kuglin.agile.persistence.entities.ProgressEntity;
import pl.kuglin.agile.persistence.entities.TaskEntity;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.window.AddItemDialog;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AddTaskStrategy extends AbstractAddStrategy{
    private static final String DESCRIPTION_KEY = "Description: ";
    private static final String ESTIMATION_KEY = "Estimation: ";
    private static final String PROGRESS_KEY = "Progress: ";

    @Override
    public void accept(AbstractWindow window) throws SQLException {

        List<ProgressEntity> progresses = window.getRepositoryPack().getProgressRepository().getAll();

        Map<String, Component> inputTextFields = new LinkedHashMap<>();
        inputTextFields.put(DESCRIPTION_KEY, new JTextField());
        inputTextFields.put(ESTIMATION_KEY, new JTextField());

        JComboBox<String> comboBox = new JComboBox<>();
        progresses.forEach(p -> comboBox.addItem(p.getName()));

        inputTextFields.put(PROGRESS_KEY, comboBox);

        new AddItemDialog(window, inputTextFields, (w, tf) -> {

            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setDescription(((JTextField)tf.get(DESCRIPTION_KEY)).getText());
            taskEntity.setEstimation(Integer.valueOf(((JTextField)tf.get(ESTIMATION_KEY)).getText()));

            String progressName = (String)((JComboBox<String>)tf.get(PROGRESS_KEY)).getSelectedItem();

            Integer progressId = progresses.stream().filter(p -> p.getName().equals(progressName)).map(ProgressEntity::getId).iterator().next();

            taskEntity.setProgressId(progressId);
            taskEntity.setSprintId(window.getSprintId());

            window.getRepositoryPack().getTaskRepository().save(taskEntity);

            window.getTable().addRow(taskEntity.getId(), taskEntity.getDescription(), taskEntity.getEstimation(), progressName);
        });
    }
}
