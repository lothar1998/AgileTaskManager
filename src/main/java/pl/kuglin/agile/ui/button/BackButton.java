package pl.kuglin.agile.ui.button;

import pl.kuglin.agile.ui.AbstractButton;

import java.awt.event.ActionListener;

public class BackButton extends AbstractButton {

    private static final String BUTTON_MESSAGE = "Back";

    public BackButton(ActionListener action) {
        super(BUTTON_MESSAGE, action);
    }

    @Override
    protected void setUp() {

    }
}
