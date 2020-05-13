package pl.kuglin.agile.ui.command;

import pl.kuglin.agile.ui.AbstractTable;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.table.TaskTable;

import javax.swing.*;

public class GetTasksBySprintIdCommand extends OnButtonCommand{

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

                    AbstractTable table = setNewTable(window, new TaskTable());
                    list.stream().filter(t -> t.getSprintId().equals(command.getResult())).forEach(e -> table.addRow(e.getId(), e.getDescription(), e.getEstimation(), e.getProgressId()));
                    removeAllActionListeners(window.getBackButton());
                    removeAllActionListeners(window.getGetMoreButton());
                    window.getBackButton().addActionListener(a -> new GetSprintsByProjectIdCommand(window, projectId).execute());
                    window.getGetMoreButton().setEnabled(false);
                    window.getBackButton().setEnabled(true);

                    window.getTopLabel().setText("Task");

                    addNewScrollPane(window.getTableScrollPane(), window);
                }),
                Throwable::printStackTrace //TODO error window
        );
    }
}
