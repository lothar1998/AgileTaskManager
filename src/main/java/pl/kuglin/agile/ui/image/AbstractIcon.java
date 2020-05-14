package pl.kuglin.agile.ui.image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Icon;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class AbstractIcon extends JLabel {

    private static Logger log = LoggerFactory.getLogger(AbstractIcon.class);

    public AbstractIcon(Icon image) {
        super(image);
    }

    protected static Image getImageFromPath(String path, int width, int height){
        try {
            return ImageIO.read(new File(path)).getScaledInstance(width, height, Image.SCALE_SMOOTH);
        } catch (IOException ex){
            log.warn("{}", "Cannot load icon", ex);
        }
        return null;
    }
}
