package pl.kuglin.agile.ui.command;

import pl.kuglin.agile.persistence.ConsumerSQL;
import pl.kuglin.agile.reactive.ActionRunnerFactory;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.window.ErrorDialog;


public class UpdateEditedTableCommand extends MainWindowCommand{

    private ActionRunnerFactory factory;
    private AbstractWindow window;
    private ConsumerSQL<AbstractWindow> strategy;

    public UpdateEditedTableCommand(ActionRunnerFactory factory, AbstractWindow window, ConsumerSQL<AbstractWindow> strategy) {
        this.factory = factory;
        this.window = window;
        this.strategy = strategy;
    }

    @Override
    public void execute() {
        factory.createAndRun(
                () -> strategy.accept(window),
                () -> {},
                t -> new ErrorDialog(t.toString(), window)
        );
    }
}
