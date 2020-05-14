package pl.kuglin.agile.ui.command;

import pl.kuglin.agile.persistence.ConsumerSQL;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.window.ErrorDialog;

public class AddNewItemToTableCommand extends MainWindowCommand {

    private final AbstractWindow window;
    private final ConsumerSQL<AbstractWindow> strategy;

    public AddNewItemToTableCommand(AbstractWindow window, ConsumerSQL<AbstractWindow> strategy) {
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
