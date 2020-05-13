package pl.kuglin.agile.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public abstract class AbstractTable extends JTable {

    public AbstractTable() {
        super(new DefaultTableModel());
        setUp();
    }

    public AbstractTable(DefaultTableModel tableModel) {
        super(tableModel);
        setUp();
    }

    protected void addColumn(String columnName){
        ((DefaultTableModel)dataModel).addColumn(columnName);
    }

    public void addRow(Object... elements){
        ((DefaultTableModel)dataModel).addRow(elements);
    }

    protected abstract void setUp();
}
