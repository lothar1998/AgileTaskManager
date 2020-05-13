package pl.kuglin.agile.ui.command;

import pl.kuglin.agile.ui.AbstractWindow;

import javax.swing.*;

public class GetSelectedRowIdentifierCommand implements Command{

    private final AbstractWindow window;
    private Integer result;

    public GetSelectedRowIdentifierCommand(AbstractWindow window) {
        this.window = window;
    }

    @Override
    public void execute() {
        JTable table = window.getTable();
        int selectedRow = table.getSelectedRow();

        if(selectedRow < 0)
            throw new IllegalStateException("No row selected");

        result = (int)table.getValueAt(selectedRow, 0);
    }

    public Integer getResult() {
        return result;
    }
}
