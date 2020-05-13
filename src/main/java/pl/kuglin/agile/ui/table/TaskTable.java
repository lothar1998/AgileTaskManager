package pl.kuglin.agile.ui.table;

import pl.kuglin.agile.ui.AbstractTable;

import javax.swing.table.TableColumnModel;

public class TaskTable extends AbstractTable {
    @Override
    protected void setUp() {
        addColumn("NO");
        addColumn("DESCRIPTION");
        addColumn("ESTIMATION");
        addColumn("PROGRESS");
        setRowHeight(40);
        TableColumnModel columnModel = getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(20);
        columnModel.getColumn(1).setPreferredWidth(800);
        columnModel.getColumn(2).setPreferredWidth(20);
        columnModel.getColumn(3).setPreferredWidth(50);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column != 0;
    }
}
