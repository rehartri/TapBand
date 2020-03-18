package com.example.tapband;

import android.media.MediaPlayer;
import android.os.Build;
import android.widget.Button;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class Instrument {
    private ArrayList<Key> keyList;
    private ArrayList<Button> buttonList;
    private ArrayList<MediaPlayer> playerList;


    @RequiresApi(api = Build.VERSION_CODES.M)
    public Instrument(ArrayList<Button> buttonList){
        this.buttonList = buttonList;
        keyList = new ArrayList<>();
        for(Button button : buttonList){
            keyList.add(new Key(button));
        }
        keyList.get(keyList.size() - 1).setPitch(keyList.get(keyList.size() - 1).getPitch() * 2);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void changeInstrument(ArrayList<MediaPlayer> playerList){
        this.playerList = playerList;
        for(int i = 0; i < keyList.size(); i++){
            keyList.get(i).setPlayer(playerList.get(i));
        }
    }



}
