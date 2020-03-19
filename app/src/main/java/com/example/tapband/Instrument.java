package com.example.tapband;

import android.media.MediaPlayer;
import android.os.Build;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

class Instrument {
    private ArrayList<Key> keyList; //List of all the keys in the instrument
    private ArrayList<Button> buttonList; //List of all buttons to be used by the keys
    private ArrayList<MediaPlayer> playerList; //List of media players used by the keys
    SeekBar seekBar; //Reference to the seekBar used to change key pitches

    /**
     * Creates an instrument and handles the slider events
     * @param buttonList List of buttons referenced by the keys
     * @param seekBar Seek bar referenced by the keys
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public Instrument(ArrayList<Button> buttonList, SeekBar seekBar){
        this.buttonList = buttonList;
        this.seekBar = seekBar;
        keyList = new ArrayList<>();
        for(Button button : buttonList){
            keyList.add(new Key(button));
        }
        keyList.get(keyList.size() - 1).setBasePitch(keyList.get(keyList.size() - 1).getBasePitch() * 2);

        int pitch;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int pitch;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pitch = progress;
                for(Key key : keyList){
                    key.setCurrentPitch((float)Math.pow(2, pitch));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * Replaces the media players of all the keys to change the sounds
     * @param playerList The media players that will replace the one in the keys
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void changeInstrument(ArrayList<MediaPlayer> playerList){
        this.playerList = playerList;
        for(int i = 0; i < keyList.size(); i++){
            keyList.get(i).setPlayer(playerList.get(i));
        }
    }



}
