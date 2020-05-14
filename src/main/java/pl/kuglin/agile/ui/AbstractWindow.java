package pl.kuglin.agile.ui;

import pl.kuglin.agile.persistence.RepositoryPack;
import pl.kuglin.agile.reactive.ActionRunnerFactory;
import pl.kuglin.agile.reactive.CallableRunnerFactory;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractWindow extends JFrame {

    private static final Integer DEFAULT_WIDTH = 1280;
    private static final Integer DEFAULT_HEIGHT = 800;

    private static final Integer DEFAULT_MIN_WIDTH = 800;
    private static final Integer DEFAULT_MIN_HEIGHT = 600;

    protected RepositoryPack repositoryPack;
    protected CallableRunnerFactory callableRunnerFactory;
    protected ActionRunnerFactory actionRunnerFactory;

    protected AbstractButton backButton;
    protected AbstractButton getMoreButton;
    protected AbstractButton updateButton;
    protected JPanel tablePanel;
    protected JScrollPane tableScrollPane;
    protected AbstractTable table;
    protected JPanel topTextPanel;
    protected JLabel topLabel;

    private Integer projectId;
    private Integer sprintId;

    public AbstractWindow(String title) {
        super(title);
    }

    public AbstractWindow(String title, RepositoryPack repositoryPack, CallableRunnerFactory callableRunnerFactory, ActionRunnerFactory actionRunnerFactory) {
        super(title);
        this.repositoryPack = repositoryPack;
        this.callableRunnerFactory = callableRunnerFactory;
        this.actionRunnerFactory = actionRunnerFactory;
        setUp();
        setDefault();
    }

    protected void setDefault() {
        setLocationRelativeTo(null);
        setBackground(Color.WHITE);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setMinimumSize(new Dimension(DEFAULT_MIN_WIDTH, DEFAULT_MIN_HEIGHT));
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    protected abstract void setUp();

    public RepositoryPack getRepositoryPack() {
        return repositoryPack;
    }

    public CallableRunnerFactory getCallableRunnerFactory() {
        return callableRunnerFactory;
    }

    public ActionRunnerFactory getActionRunnerFactory() {
        return actionRunnerFactory;
    }

    public AbstractButton getBackButton() {
        return backButton;
    }

    public AbstractButton getGetMoreButton() {
        return getMoreButton;
    }

    public AbstractButton getUpdateButton() {
        return updateButton;
    }

    public JPanel getTablePanel() {
        return tablePanel;
    }

    public JScrollPane getTableScrollPane() {
        return tableScrollPane;
    }

    public void setTableScrollPane(JScrollPane tableScrollPane) {
        this.tableScrollPane = tableScrollPane;
    }

    public AbstractTable getTable() {
        return table;
    }

    public void setTable(AbstractTable table) {
        this.table = table;
    }

    public JLabel getTopLabel() {
        return topLabel;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getSprintId() {
        return sprintId;
    }

    public void setSprintId(Integer sprintId) {
        this.sprintId = sprintId;
    }
}
