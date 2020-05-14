package pl.kuglin.agile.ui.popup;

import pl.kuglin.agile.ui.AbstractPopupMenu;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.command.AddNewItemToTableCommand;
import pl.kuglin.agile.ui.command.DeleteElementFromTableCommand;
import pl.kuglin.agile.ui.command.GetTasksBySprintIdCommand;
import pl.kuglin.agile.ui.command.strategy.AddSprintStrategy;
import pl.kuglin.agile.ui.command.strategy.DeleteSprintStrategy;
import pl.kuglin.agile.ui.popup.item.TablePopupItem;

public class SprintTablePopup extends AbstractPopupMenu {

    private final AbstractWindow window;

    public SprintTablePopup(AbstractWindow window) {
        this.window = window;
    }

    @Override
    protected void setUp() {
        add(new TablePopupItem("Add", a -> new AddNewItemToTableCommand(window, new AddSprintStrategy()).execute()));
        add(new TablePopupItem("Get more ...", a -> new GetTasksBySprintIdCommand(window.getProjectId(), window).execute()));
        add(new TablePopupItem("Delete", a -> new DeleteElementFromTableCommand(window, new DeleteSprintStrategy()).execute()));
    }
}
