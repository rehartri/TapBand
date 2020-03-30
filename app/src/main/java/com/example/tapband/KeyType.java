package com.example.tapband;

import android.widget.Button;

public class KeyType {
    Button button;
    boolean sharp;

    public KeyType(Button button, boolean sharp){
        this.button = button;
        this.sharp = sharp;
    }

    public Button getButton() {
        return button;
    }

    public boolean isSharp(){
        return sharp;
    }
}
