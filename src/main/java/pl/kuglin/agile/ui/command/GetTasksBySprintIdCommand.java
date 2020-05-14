package pl.kuglin.agile.ui.command;

import pl.kuglin.agile.persistence.entities.ProgressEntity;
import pl.kuglin.agile.persistence.entities.TaskEntity;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.command.strategy.UpdateTasksStrategy;
import pl.kuglin.agile.ui.table.TaskTable;
import pl.kuglin.agile.ui.window.ErrorDialog;

import javax.swing.*;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class GetTasksBySprintIdCommand extends MainWindowCommand {

    private final AbstractWindow window;
    private final Integer projectId;

    public GetTasksBySprintIdCommand(Integer projectId, AbstractWindow window) {
        this.window = window;
        this.projectId = projectId;
    }

    @Override
    public void execute() {
        window.getCallableRunnerFactory().createAndRun(
                window.getRepositoryPack().getTaskRepository()::getAll,
                list -> SwingUtilities.invokeLater(() -> {
                    GetSelectedRowIdentifierCommand command = new GetSelectedRowIdentifierCommand(window);
                    command.execute();

                    TaskTable table = (TaskTable) setNewTable(window, new TaskTable(window));

                    JComboBox<String> comboBox = table.getComboBox();
                    List<ProgressEntity> progresses = new LinkedList<>();
                    try {
                        progresses = window.getRepositoryPack().getProgressRepository().getAll();
                    } catch (SQLException t) {
                        new ErrorDialog(t.toString(), window);
                    }
                    for (ProgressEntity p : progresses)
                        comboBox.addItem(p.getName());

                    List<TaskEntity> tasks = list.stream().filter(t -> t.getSprintId().equals(command.getResult())).collect(Collectors.toList());

                    for (TaskEntity t : tasks) {
                        String progressName = progresses.stream().filter(p -> p.getId().equals(t.getProgressId())).map(ProgressEntity::getName).iterator().next();
                        table.addRow(t.getId(), t.getDescription(), t.getEstimation(), progressName);
                    }

                    removeAllActionListeners(window.getBackButton());
                    removeAllActionListeners(window.getGetMoreButton());
                    removeAllActionListeners(window.getUpdateButton());
                    window.getBackButton().addActionListener(a -> new GetSprintsByProjectIdCommand(window, projectId).execute());
                    window.getUpdateButton().addActionListener(a -> new UpdateEditedTableCommand(window, new UpdateTasksStrategy()).execute());
                    window.getGetMoreButton().setEnabled(false);
                    window.getBackButton().setEnabled(true);
                    changeTopLabelText("Task", window);
                    addNewScrollPane(window.getTableScrollPane(), window);
                    window.setProjectId(projectId);
                    window.setSprintId(command.getResult());
                }),
                t -> SwingUtilities.invokeLater(() -> new ErrorDialog(t.toString(), window))
        );
    }
}
