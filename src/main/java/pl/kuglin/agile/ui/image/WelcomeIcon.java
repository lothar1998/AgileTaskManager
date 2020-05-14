package pl.kuglin.agile.ui.image;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class WelcomeIcon extends AbstractIcon {

    private static final String ICON_PATH = "src/main/resources/logo_solo.png";
    private static final int WIDTH = 200;
    private static final int HEIGHT = 200;

    public WelcomeIcon() {
        super(new ImageIcon(Objects.requireNonNull(getImageFromPath(ICON_PATH, WIDTH, HEIGHT))));
        setSize(new Dimension(WIDTH, HEIGHT));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }
}
