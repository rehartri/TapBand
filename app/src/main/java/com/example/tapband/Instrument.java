package com.example.tapband;

import android.content.Context;
import android.os.Build;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

class Instrument {
    private ArrayList<Key> keyList = new ArrayList<>(); //List of all the keys in the instrument
    private ArrayList<Integer> soundList = new ArrayList<>(); //List of media players used by the keys
    private int type; //The value that determines the type of instrument created


    /**
     * Creates an instrument and handles the slider events
     * @param buttonList List of buttons referenced by the keys
     * @param seekBar Seek bar referenced by the keys
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    Instrument(ArrayList<Button> buttonList, SeekBar seekBar, Context context){
        for(Button button : buttonList){  //Creates each key and passes it it's own button to use
            keyList.add(new Key(button, context));
        }
        keyList.get(keyList.size() - 1).setBasePitch(keyList.get(keyList.size() - 1).getBasePitch() * 2);  //Doubles the pitch of the high c button's sound

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                for(Key key : keyList){
                    key.setCurrentPitch((float)Math.pow(2, progress - 1));  //Sets the pitch of the instrument when the seek bar progress is changed
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
     * Frees up the space taken up by the media players when they are no longer needed
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    void clear(){
        for(Key key: keyList){
            if(key.getPlayer() != null) {
                key.getPlayer().release();
            }
        }
    }

    /**
     * Changes the type of sounds the instrument makes based on the value of type parameter
     * @param type Integer value that determines instrument type
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    void setType(int type){
        if(this.type == type){
            return;
        }
        this.type = type;
        switch(type){
            default:
                pianoBuild();
                break;
        }
        for(int i = 0; i < keyList.size(); i++){
            keyList.get(i).setSound(soundList.get(i));
        }
    }

    /**
     * Changes the sounds in the sound list to piano notes
     */
    private void pianoBuild(){
        if(soundList != null){
            soundList.clear();
        }
        soundList.add(R.raw.piano_c);
        soundList.add(R.raw.piano_csharp);
        soundList.add(R.raw.piano_d);
        soundList.add(R.raw.piano_dsharp);
        soundList.add(R.raw.piano_e);
        soundList.add(R.raw.piano_f);
        soundList.add(R.raw.piano_fsharp);
        soundList.add(R.raw.piano_g);
        soundList.add(R.raw.piano_gsharp);
        soundList.add(R.raw.piano_a);
        soundList.add(R.raw.piano_asharp);
        soundList.add(R.raw.piano_b);
        soundList.add(R.raw.piano_c);
    }
}
