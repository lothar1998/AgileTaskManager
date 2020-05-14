package pl.kuglin.agile.ui.table;

import pl.kuglin.agile.ui.AbstractTable;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.popup.SprintTablePopup;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

public class SprintTable extends AbstractTable {

    public SprintTable(AbstractWindow window) {
        super(window);
    }

    @Override
    protected void setUp() {
        addColumn("NO");
        addColumn("SPRINT NO");
        addColumn("NAME");
        TableColumnModel columnModel = getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(30);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        columnModel.getColumn(0).setCellRenderer(centerRenderer);
        columnModel.getColumn(1).setPreferredWidth(60);
        columnModel.getColumn(1).setCellRenderer(centerRenderer);
        columnModel.getColumn(2).setPreferredWidth(1100);
        setComponentPopupMenu(new SprintTablePopup(window));
    }

    @Override
    protected void setUpTableModeListener() {
        listener = e -> {
            if (e.getType() == TableModelEvent.UPDATE)
                rowsEditedId.add((int) window.getTable().getValueAt(e.getFirstRow(), 0));
        };
    }


    @Override
    public Class<?> getColumnClass(int column) {
        if (column == 1)
            return Integer.class;
        else
            return super.getColumnClass(column);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column != 0;
    }
}
