package com.bbi.pesquisa.model;

import android.widget.EditText;

public class ValidatedForm {
    private boolean changeButtons, readyToSave;
    private String message;
    private EditText setFocusOn;

    public boolean isReadyToSave() {
        return readyToSave;
    }

    public void setReadyToSave(boolean readyToSave) {
        this.readyToSave = readyToSave;
    }

    public boolean isChangeButtons() {
        return changeButtons;
    }

    public void setChangeButtons(boolean changeButtons) {
        this.changeButtons = changeButtons;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EditText getSetFocusOn() {
        return setFocusOn;
    }

    public void setSetFocusOn(EditText setFocusOn) {
        this.setFocusOn = setFocusOn;
    }
}
