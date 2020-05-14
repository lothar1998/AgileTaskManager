package pl.kuglin.agile.ui.table;

import pl.kuglin.agile.ui.AbstractTable;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.popup.TaskTablePopup;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class TaskTable extends AbstractTable {

    private JComboBox<String> comboBox;

    public TaskTable(AbstractWindow window) {
        super(window);
    }

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
        setComponentPopupMenu(new TaskTablePopup(window));
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
