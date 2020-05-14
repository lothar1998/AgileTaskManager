package pl.kuglin.agile.ui.popup;

import pl.kuglin.agile.ui.AbstractPopupMenu;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.command.AddNewItemToTableCommand;
import pl.kuglin.agile.ui.command.DeleteElementFromTableCommand;
import pl.kuglin.agile.ui.command.GetSprintsByProjectIdCommand;
import pl.kuglin.agile.ui.command.strategy.AddProjectStrategy;
import pl.kuglin.agile.ui.command.strategy.DeleteProjectStrategy;
import pl.kuglin.agile.ui.popup.item.TablePopupItem;


public class ProjectTablePopup extends AbstractPopupMenu {

    private final AbstractWindow window;

    public ProjectTablePopup(AbstractWindow window) {
        this.window = window;
    }

    @Override
    protected void setUp() {
        add(new TablePopupItem("Add", a -> new AddNewItemToTableCommand(window, new AddProjectStrategy()).execute()));
        add(new TablePopupItem("Get more ...", a -> new GetSprintsByProjectIdCommand(window).execute()));
        add(new TablePopupItem("Delete", a -> new DeleteElementFromTableCommand(window, new DeleteProjectStrategy()).execute()));
    }
}
