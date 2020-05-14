package pl.kuglin.agile.ui.table;

import pl.kuglin.agile.ui.AbstractTable;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class TaskTable extends AbstractTable {

    private JComboBox<String> comboBox;

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
        TableColumn progressColumn = columnModel.getColumn(3);

        comboBox = new JComboBox<>();
        progressColumn.setCellEditor(new DefaultCellEditor(comboBox));
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column != 0;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if(column == 2)
            return Integer.class;
        else
            return super.getColumnClass(column);
    }

    public JComboBox<String> getComboBox() {
        return comboBox;
    }
}
