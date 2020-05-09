package pl.kuglin.agile.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public abstract class AbstractTable extends JTable {

    public AbstractTable() {
        super(new DefaultTableModel());
        setUp();
    }

    public AbstractTable(TableModel tableModel) {
        super(tableModel);
        setUp();
    }

    protected void setDefault(){

    }

    protected void addTableColumn(String columnName){
        ((DefaultTableModel)dataModel).addColumn(columnName);
    }

    protected void addTableRow(Object... elements){
        ((DefaultTableModel)dataModel).addRow(elements);
    }

    protected abstract void setUp();
}
