package pl.kuglin.agile.ui.command;

import pl.kuglin.agile.persistence.ConsumerSQL;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.window.ErrorDialog;


public class UpdateEditedTableCommand extends MainWindowCommand{

    private AbstractWindow window;
    private ConsumerSQL<AbstractWindow> strategy;

    public UpdateEditedTableCommand(AbstractWindow window, ConsumerSQL<AbstractWindow> strategy) {
        this.window = window;
        this.strategy = strategy;
    }

    @Override
    public void execute() {
        window.getActionRunnerFactory().createAndRun(
                () -> strategy.accept(window),
                () -> {},
                t -> new ErrorDialog(t.toString(), window)
        );
    }
}
