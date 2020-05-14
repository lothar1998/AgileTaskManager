package pl.kuglin.agile.ui;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractTable extends JTable {

    protected AbstractWindow window;
    protected TableModelListener listener;
    protected Set<Integer> rowsEditedId;

    public AbstractTable(AbstractWindow window) {
        super(new DefaultTableModel());
        this.window = window;
        setUp();
        rowsEditedId = new HashSet<>();
        setUpTableModeListener();
        dataModel.addTableModelListener(listener);
    }

    public AbstractTable(AbstractWindow window, DefaultTableModel tableModel) {
        super(tableModel);
        this.window = window;
        setUp();
        rowsEditedId = new HashSet<>();
        setUpTableModeListener();
        dataModel.addTableModelListener(listener);
    }

    protected void addColumn(String columnName){
        ((DefaultTableModel)dataModel).addColumn(columnName);
    }

    public void addRow(Object... elements){
        ((DefaultTableModel)dataModel).addRow(elements);
    }

    protected abstract void setUp();

    protected abstract void setUpTableModeListener();

    public Set<Integer> getRowsEditedId() {
        return rowsEditedId;
    }
}
