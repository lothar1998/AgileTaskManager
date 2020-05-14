package pl.kuglin.agile.ui.command;

import pl.kuglin.agile.ui.AbstractTable;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.command.strategy.UpdateSprintsStrategy;
import pl.kuglin.agile.ui.table.SprintTable;
import pl.kuglin.agile.ui.window.ErrorDialog;

import javax.swing.*;

public class GetSprintsByProjectIdCommand extends MainWindowCommand implements Command{

    private final AbstractWindow window;
    private Integer projectId;

    public GetSprintsByProjectIdCommand(AbstractWindow window) {
        this.window = window;
        this.projectId = null;
    }

    public GetSprintsByProjectIdCommand(AbstractWindow window, Integer projectId) {
        this.window = window;
        this.projectId = projectId;
    }

    @Override
    public void execute() {
        window.getCallableRunnerFactory().createAndRun(
                window.getRepositoryPack().getSprintRepository()::getAll,
                list -> SwingUtilities.invokeLater(() -> {
                    if(projectId == null) {
                        GetSelectedRowIdentifierCommand command = new GetSelectedRowIdentifierCommand(window);
                        command.execute();
                        projectId = command.getResult();
                    }

                    AbstractTable table = setNewTable(window, new SprintTable());
                    list.stream().filter(s -> s.getProjectId().equals(projectId)).forEach(e -> table.addRow(e.getId(), e.getNo(), e.getName()));
                    removeAllActionListeners(window.getBackButton());
                    removeAllActionListeners(window.getGetMoreButton());
                    removeAllActionListeners(window.getUpdateButton());
                    window.getBackButton().addActionListener(a -> new GetAllProjectsCommand(window).execute());
                    window.getGetMoreButton().addActionListener(a -> new GetTasksBySprintIdCommand(projectId, window).execute());
                    window.getUpdateButton().addActionListener(a -> new UpdateEditedTableCommand(window.getActionRunnerFactory(), window, new UpdateSprintsStrategy()).execute());
                    window.getGetMoreButton().setEnabled(true);
                    window.getBackButton().setEnabled(true);
                    changeTopLabelText("Sprint", window);
                    addNewScrollPane(window.getTableScrollPane(), window);
                    window.setProjectId(projectId);
                    window.setSprintId(null);
                }),
                t -> SwingUtilities.invokeLater(() -> new ErrorDialog(t.toString(), window))
        );
    }
}
