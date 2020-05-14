package pl.kuglin.agile.ui.image;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Icon extends AbstractIcon {

    private static final String ICON_PATH = "src/main/resources/logo_mini.png";
    private static final int WIDTH = 40;
    private static final int HEIGHT = 40;

    public Icon() {
        super(new ImageIcon(Objects.requireNonNull(getImageFromPath(ICON_PATH, WIDTH, HEIGHT))));
        setSize(new Dimension(WIDTH, HEIGHT));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }
}
