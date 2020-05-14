package pl.kuglin.agile.ui.window;

import pl.kuglin.agile.RepositoryPack;
import pl.kuglin.agile.reactive.ActionRunnerFactory;
import pl.kuglin.agile.reactive.CallableRunnerFactory;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.button.BackButton;
import pl.kuglin.agile.ui.button.GetMoreButton;
import pl.kuglin.agile.ui.button.UpdateButton;
import pl.kuglin.agile.ui.command.GetAllProjectsCommand;
import pl.kuglin.agile.ui.label.TopLabel;
import pl.kuglin.agile.ui.panel.BoxPanel;
import pl.kuglin.agile.ui.table.ProjectTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainWindow extends AbstractWindow {

    private static final String WINDOW_NAME = "Agile Task Manager";

    public MainWindow(RepositoryPack repositoryPack, CallableRunnerFactory callableRunnerFactory, ActionRunnerFactory actionRunnerFactory) {
        super(WINDOW_NAME, repositoryPack, callableRunnerFactory, actionRunnerFactory);
        new GetAllProjectsCommand(this).execute();
    }

    @Override
    protected void setUp() {
        JPanel mainPanel = new BoxPanel(BoxPanel.Axis.Y_AXIS);

        mainPanel.setBorder(new EmptyBorder(new Insets(20, 20, 20, 20)));

            topTextPanel = new BoxPanel(BoxPanel.Axis.X_AXIS);

            topLabel = new TopLabel("Project");

            topTextPanel.add(topLabel);

        mainPanel.add(topTextPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

            tablePanel = new BoxPanel(BoxPanel.Axis.X_AXIS);

            table = new ProjectTable(this);
            tableScrollPane = new JScrollPane(table);

            tablePanel.add(tableScrollPane);

        mainPanel.add(tablePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

            JPanel buttonPanel = new BoxPanel(BoxPanel.Axis.X_AXIS);

            //BUTTONS

            backButton = new BackButton(a -> {});
            backButton.setEnabled(false);
            getMoreButton = new GetMoreButton(a -> {});
            updateButton = new UpdateButton(a -> {});

            buttonPanel.add(backButton);
            buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
            buttonPanel.add(getMoreButton);
            buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
            buttonPanel.add(updateButton);

        mainPanel.add(buttonPanel);

        add(mainPanel);
    }
}
