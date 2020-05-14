package pl.kuglin.agile.ui.button;

import pl.kuglin.agile.ui.AbstractButton;

import java.awt.event.ActionListener;

public class GetMoreButton extends AbstractButton {

    private static final String BUTTON_MESSAGE = "Get more ...";

    public GetMoreButton(ActionListener action) {
        super(BUTTON_MESSAGE, action);
    }

    @Override
    protected void setUp() {

    }
}
