package pl.kuglin.agile.ui.image;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ProgramTitleImage extends AbstractIcon{

    private static final String ICON_PATH = "src/main/resources/program_title.png";
    private static final int WIDTH = 326;
    private static final int HEIGHT = 37;

    public ProgramTitleImage() {
        super(new ImageIcon(Objects.requireNonNull(getImageFromPath(ICON_PATH, WIDTH, HEIGHT))));
        setSize(new Dimension(WIDTH, HEIGHT));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }
}
