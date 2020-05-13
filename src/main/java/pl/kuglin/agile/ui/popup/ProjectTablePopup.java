package pl.kuglin.agile.ui.popup;

import pl.kuglin.agile.persistence.entities.ProjectEntity;
import pl.kuglin.agile.ui.AbstractPopupMenu;
import pl.kuglin.agile.ui.AbstractTable;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.popup.item.TablePopupItem;


public class ProjectTablePopup extends AbstractPopupMenu {

//    private final AbstractWindow<ProjectEntity, Integer> window;
//    private final AbstractTable<ProjectEntity> table;
//
//    public ProjectTablePopup(AbstractWindow<ProjectEntity, Integer> window, AbstractTable<ProjectEntity> table) {
//        this.window = window;
//        this.table = table;
//    }

    @Override
    protected void setUp() {
        //add(new TablePopupItem("Delete", a -> new DeleteProjectCommand(window.getRepository(),window.getActionRunnerFactory(), table).execute()));
    }
}
