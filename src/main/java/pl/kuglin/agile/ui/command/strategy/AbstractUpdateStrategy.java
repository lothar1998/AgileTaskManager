package pl.kuglin.agile.ui.command.strategy;

import pl.kuglin.agile.persistence.ConsumerSQL;
import pl.kuglin.agile.ui.AbstractWindow;

import java.util.List;

public abstract class AbstractUpdateStrategy implements ConsumerSQL<AbstractWindow> {
    protected String generateDialogWindowMessage(List<String> elements) {
        StringBuilder stringBuilder = new StringBuilder("Do you really want to update: \n");
        for (String e : elements) {
            stringBuilder.append("\"");
            stringBuilder.append(e);
            stringBuilder.append("\"\n");
        }

        int lastNewLineSignIndex = stringBuilder.lastIndexOf("\n");

        stringBuilder.replace(lastNewLineSignIndex, lastNewLineSignIndex + 1, "");
        stringBuilder.append(" ?");

        return stringBuilder.toString();
    }

    protected int getRowNoByValue(AbstractWindow window, int value) {
        int rowCount = window.getTable().getRowCount();
        for (int i = 0; i < rowCount; i++) {
            if ((int) window.getTable().getValueAt(i, 0) == value)
                return i;
        }

        return -1;
    }
}
