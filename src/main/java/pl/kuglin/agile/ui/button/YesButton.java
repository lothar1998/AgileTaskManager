package pl.kuglin.agile.ui.button;

import pl.kuglin.agile.ui.AbstractButton;

import java.awt.event.ActionListener;

public class YesButton extends AbstractButton {
    private static final String BUTTON_MESSAGE = "Yes";

    public YesButton(ActionListener action) {
        super(BUTTON_MESSAGE, action);
    }

    @Override
    protected void setUp() {

    }
}
