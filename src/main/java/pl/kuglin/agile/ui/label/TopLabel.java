package pl.kuglin.agile.ui.label;

import javax.swing.*;
import java.awt.*;

public class TopLabel extends JLabel {

    public TopLabel(String text) {
        super(text);
        setFont(new Font("Arial Black", Font.BOLD, 20));
        setBackground(Color.WHITE);
    }
}
