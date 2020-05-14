package pl.kuglin.agile.ui.command.strategy;

import pl.kuglin.agile.persistence.entities.ProjectEntity;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.window.AddItemDialog;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class AddProjectStrategy extends AbstractAddStrategy{
    private static final String NAME_KEY = "Name: ";
    private static final String DESCRIPTION_KEY = "Description: ";

    @Override
    public void accept(AbstractWindow window) throws SQLException {

        Map<String, Component> inputTextFields = new LinkedHashMap<>();
        inputTextFields.put(NAME_KEY, new JTextField());
        inputTextFields.put(DESCRIPTION_KEY, new JTextField());


        new AddItemDialog(window, inputTextFields, (w, tf) -> {

            ProjectEntity projectEntity = new ProjectEntity();
            projectEntity.setName(((JTextField)tf.get(NAME_KEY)).getText());
            projectEntity.setDescription(((JTextField)tf.get(DESCRIPTION_KEY)).getText());

            window.getRepositoryPack().getProjectRepository().save(projectEntity);

            window.getTable().addRow(projectEntity.getId(), projectEntity.getName(), projectEntity.getDescription());
        });
    }
}
