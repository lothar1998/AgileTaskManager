package pl.kuglin.agile.ui.command;

import pl.kuglin.agile.ui.AbstractTable;
import pl.kuglin.agile.ui.AbstractWindow;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class OnButtonCommand implements Command{
    protected void removeAllActionListeners(JButton button){
        ActionListener[] actionListeners = button.getActionListeners();

        for(ActionListener a : actionListeners)
            button.removeActionListener(a);
    }

    protected void addNewScrollPane(JScrollPane scrollPane, AbstractWindow window){
        window.getTablePanel().add(scrollPane);
        window.getTablePanel().revalidate();
        window.getTablePanel().repaint();
    }

    protected AbstractTable setNewTable(AbstractWindow window, AbstractTable table){
        window.getTablePanel().remove(0);
        window.setTableScrollPane(new JScrollPane(table));
        window.setTable(table);
        return table;
    }
}
