package pl.kuglin.agile.ui.command.strategy;

import pl.kuglin.agile.persistence.ConsumerSQL;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.window.InfoDialog;

import java.util.Set;

public abstract class AbstractDeleteStrategy implements ConsumerSQL<AbstractWindow> {
    protected String generateDialogWindowMessage(String element) {
        return "Do you really want to delete \"" + element + "\" ?";
    }

    protected int getRowNoByValue(AbstractWindow window, int value) {
        int rowCount = window.getTable().getRowCount();
        for (int i = 0; i < rowCount; i++) {
            if ((int) window.getTable().getValueAt(i, 0) == value)
                return i;
        }

        return -1;
    }

    protected boolean isUpdated(AbstractWindow window, int row) {
        Set<Integer> rowsEditedId = window.getTable().getRowsEditedId();
        if (rowsEditedId.contains((int)window.getTable().getValueAt(row, 0))) {
            new InfoDialog("You cannot delete updated row!", window);
            return true;
        }
        return false;
    }
}
