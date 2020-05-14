package pl.kuglin.agile.ui.table;

import pl.kuglin.agile.ui.AbstractTable;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.popup.ProjectTablePopup;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumnModel;

public class ProjectTable extends AbstractTable {

    public ProjectTable(AbstractWindow window) {
        super(window);
    }

    @Override
    protected void setUp() {
        addColumn("NO");
        addColumn("NAME");
        addColumn("DESCRIPTION");
        setRowHeight(40);
        TableColumnModel columnModel = getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(20);
        columnModel.getColumn(1).setPreferredWidth(300);
        columnModel.getColumn(2).setPreferredWidth(500);
        setComponentPopupMenu(new ProjectTablePopup(window));
    }

    @Override
    protected void setUpTableModeListener() {
        listener = e -> {
            if (e.getType() == TableModelEvent.UPDATE)
                rowsEditedId.add((int) window.getTable().getValueAt(e.getFirstRow(), 0));
        };
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column != 0;
    }
}
