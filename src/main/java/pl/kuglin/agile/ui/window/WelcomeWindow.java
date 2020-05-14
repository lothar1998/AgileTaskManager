package pl.kuglin.agile.ui.window;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kuglin.agile.persistence.RepositoryPack;
import pl.kuglin.agile.reactive.ActionRunnerFactory;
import pl.kuglin.agile.reactive.CallableRunnerFactory;
import pl.kuglin.agile.ui.image.ProgramTitleImage;
import pl.kuglin.agile.ui.image.WelcomeIcon;
import pl.kuglin.agile.ui.panel.BoxPanel;

import javax.swing.*;;import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class WelcomeWindow extends JFrame {

    private Logger log = LoggerFactory.getLogger(getClass());

    private static final String WINDOW_NAME = "Agile Task Manager";

    private final RepositoryPack repositoryPack;
    private final CallableRunnerFactory callableRunner;
    private final ActionRunnerFactory actionRunner;

    public WelcomeWindow(RepositoryPack repositoryPack, CallableRunnerFactory callableRunnerFactory, ActionRunnerFactory actionRunnerFactory) {
        super(WINDOW_NAME);
        this.repositoryPack = repositoryPack;
        this.callableRunner = callableRunnerFactory;
        this.actionRunner = actionRunnerFactory;
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new BoxPanel(BoxPanel.Axis.Y_AXIS);

            mainPanel.add(Box.createRigidArea(new Dimension(0, 35)));

            JPanel iconPanel = new BoxPanel(BoxPanel.Axis.X_AXIS);

                JLabel icon = new WelcomeIcon();

                iconPanel.add(icon);

            JPanel textPanel = new BoxPanel(BoxPanel.Axis.X_AXIS);

                JLabel text = new ProgramTitleImage();
                textPanel.add(text);

            JPanel authorPanel = new BoxPanel(BoxPanel.Axis.X_AXIS);

                JLabel author = new JLabel("Developed by Lothar © 2020");
                author.setFont(new Font("Georgia", Font.PLAIN, 13));

                authorPanel.add(author);

        mainPanel.add(iconPanel);
        mainPanel.add(textPanel);
        mainPanel.add(authorPanel);

        add(mainPanel);

        setSize(400, 400);
        setVisible(true);

        JFrame thisWindow = this;

        try {
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    SwingUtilities.invokeLater(() -> new MainWindow(repositoryPack, callableRunner, actionRunner));
                    thisWindow.dispose();
                }
            }, 1500);
        } catch (Exception ex){
            log.error("{}", "Cannot load main window", ex);
        }
    }
}
