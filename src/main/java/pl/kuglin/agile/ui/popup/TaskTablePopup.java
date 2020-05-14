package pl.kuglin.agile.ui.popup;

import pl.kuglin.agile.ui.AbstractPopupMenu;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.command.DeleteElementFromTableCommand;
import pl.kuglin.agile.ui.command.strategy.DeleteTaskStrategy;
import pl.kuglin.agile.ui.popup.item.TablePopupItem;

public class TaskTablePopup extends AbstractPopupMenu {

    private final AbstractWindow window;

    public TaskTablePopup(AbstractWindow window) {
        this.window = window;
    }

    @Override
    protected void setUp() {
        add(new TablePopupItem("Delete", a -> new DeleteElementFromTableCommand(window, new DeleteTaskStrategy()).execute()));
    }
}
