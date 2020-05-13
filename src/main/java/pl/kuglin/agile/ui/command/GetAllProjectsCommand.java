package pl.kuglin.agile.ui.command;

import pl.kuglin.agile.ui.AbstractTable;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.table.ProjectTable;

import javax.swing.*;

public class GetAllProjectsCommand extends OnButtonCommand implements Command{

    private final AbstractWindow window;

    public GetAllProjectsCommand(AbstractWindow window) {
        this.window = window;
    }

    @Override
    public void execute() {
        window.getCallableRunnerFactory().createAndRun(
                window.getRepositoryPack().getProjectRepository()::getAll,
                list -> SwingUtilities.invokeLater(() -> {
                        AbstractTable table = setNewTable(window, new ProjectTable());
                        list.forEach(e -> table.addRow(e.getId(), e.getName(), e.getDescription()));
                        removeAllActionListeners(window.getGetMoreButton());
                        window.getGetMoreButton().addActionListener(a -> new GetSprintsByProjectIdCommand(window).execute());
                        window.getBackButton().setEnabled(false);
                        removeAllActionListeners(window.getBackButton());

                        window.getTopLabel().setText("Project");

                        addNewScrollPane(window.getTableScrollPane(), window);
                    }),
                Throwable::printStackTrace //TODO error window
        );
    }
}
