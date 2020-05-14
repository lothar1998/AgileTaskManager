package pl.kuglin.agile.ui;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
        setDefault();
        rowsEditedId = new HashSet<>();
        setUpTableModeListener();
        dataModel.addTableModelListener(listener);
    }

    public AbstractTable(AbstractWindow window, DefaultTableModel tableModel) {
        super(tableModel);
        this.window = window;
        setUp();
        setDefault();
        rowsEditedId = new HashSet<>();
        setUpTableModeListener();
        dataModel.addTableModelListener(listener);
    }

    protected void addColumn(String columnName) {
        ((DefaultTableModel) dataModel).addColumn(columnName);
    }

    public void addRow(Object... elements) {
        ((DefaultTableModel) dataModel).addRow(elements);
    }

    protected abstract void setUp();

    protected abstract void setUpTableModeListener();

    protected void setDefault(){
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        getTableHeader().setBackground(new Color(24, 126, 205));
        getTableHeader().setForeground(Color.WHITE);
        getTableHeader().setPreferredSize(new Dimension(1200, 50));
        setSelectionBackground(new Color(247, 76, 96));
        setSelectionForeground(Color.WHITE);
        setRowHeight(30);

    }

    public Set<Integer> getRowsEditedId() {
        return rowsEditedId;
    }
}
