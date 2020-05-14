package pl.kuglin.agile.ui.table;

import pl.kuglin.agile.ui.AbstractTable;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumnModel;

public class ProjectTable extends AbstractTable {

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
    }

    @Override
    protected void setUpTableModeListener() {
        listener = e -> {
            if(e.getType() != TableModelEvent.INSERT)
                rowsEditedId.add(e.getFirstRow());
        };
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column != 0;
    }
}
