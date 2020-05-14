package pl.kuglin.agile.ui.table;

import pl.kuglin.agile.ui.AbstractTable;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumnModel;

public class SprintTable extends AbstractTable {

    @Override
    protected void setUp() {
        addColumn("NO");
        addColumn("SPRINT NO");
        addColumn("NAME");
        setRowHeight(40);
        TableColumnModel columnModel = getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(20);
        columnModel.getColumn(1).setPreferredWidth(20);
        columnModel.getColumn(2).setPreferredWidth(800);
    }

    @Override
    protected void setUpTableModeListener() {
        listener = e -> {
            if(e.getType() != TableModelEvent.INSERT)
                rowsEditedId.add(e.getFirstRow());
        };
    }


    @Override
    public Class<?> getColumnClass(int column) {
        if(column == 1)
            return Integer.class;
        else
            return super.getColumnClass(column);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column != 0;
    }
}
