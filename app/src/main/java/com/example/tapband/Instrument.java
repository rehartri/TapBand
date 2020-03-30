package com.example.tapband;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import java.util.ArrayList;
import java.util.Iterator;

class Instrument {
    private ArrayList<Key> keyList = new ArrayList<>(); //List of all the keys in the instrument
    private ArrayList<Integer> soundIDs = new ArrayList<>(); //List of id numbers for the sounds in the instrument
    private int type = -1; //The value that determines the type of instrument created


    /**
     * Creates an instrument, handles the slider events, and passes the context from the main activity into each of the buttons
     * @param buttonList List of buttons referenced by the keys
     * @param seekBar Seek bar referenced by the keys
     * @param context The context of the main activity which gets passed into each key
     */
    Instrument(final ArrayList<KeyType> buttonList, SeekBar seekBar, Context context){
        for(int i = 0; i < buttonList.size(); i++){  //Creates each key and passes it it's own button to use
            final Key key = new Key(buttonList.get(i).getButton(), buttonList.get(i).isSharp(), i, context);
            keyList.add(key);
            buttonList.get(i).getButton().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                key.getButton().getDrawingRect(key.getRect());
                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
                    key.start();
                }
                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE) {
                    if (event.getX() > key.getButton().getLeft() && event.getX() < key.getButton().getRight() && event.getY() < key.getButton().getBottom() && event.getY() > key.getButton().getTop()){
//                        key.getRect().contains((int) event.getX(), (int) event.getY())
                        if (!key.isPressed()) {
                            key.start();
                        }
                    } else {
                        if(key.isSharp()){
                            if(event.getX() < key.getButton().getLeft()){
                                return dispatchLeft(event);
                            }else if(event.getX() > key.getButton().getRight()){
                                return dispatchRight(event);
                            }else if(event.getY() > key.getButton().getBottom()){
                                if(event.getX() < (((key.getButton().getLeft() * 1.0) + (key.getButton().getRight() * 1.0)) / 2)){
                                    dispatchLeft(event);
                                }else{
                                    dispatchRight(event);
                                }
                            }else{
                                return false;
                            }
                        }else{
                            if(event.getX() < key.getButton().getLeft()){
                                return dispatchLeft(event);
                            }else if(event.getX() > key.getButton().getRight()){
                                return dispatchRight(event);
                            }else{
                                return false;
                            }
                        }
                    }
                }
                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    key.end();
                }
                return key.isPressed();

            }
            boolean dispatchLeft(MotionEvent event){
                key.end();
                if(key.getIndex() >= keyList.size() - 1){
                    return false;
                }else{
                    return keyList.get(key.getIndex() + 1).getButton().dispatchTouchEvent(event);
                }
            }
            boolean dispatchRight(MotionEvent event){
                key.end();
                if(key.getIndex() <= 0){
                    return false;
                }else{
                    return keyList.get(key.getIndex() - 1).getButton().dispatchTouchEvent(event);
                }
            }
        });
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                for(Key key : keyList){
                    key.setOctave(progress);
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
     * Frees up the space taken up by the keys' SoundPools when they are no longer needed
     */
    void clear(){
        for(Key key: keyList){
            key.clear();
        }
    }

    /**
     * Changes the type of sounds the instrument makes based on the value of the type parameter
     * @param type Integer value that determines instrument type
     */
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
        Iterator it = soundIDs.iterator();
        for(int i = 0; it.hasNext(); i++){
            if(i >= keyList.size() - 1){
                i = 0;
            }
            keyList.get(i).addSoundID((Integer) it.next());
        }
        for(int i = 12; i < soundIDs.size(); i += 12){
            keyList.get(keyList.size() - 1).addSoundID(soundIDs.get(i));
        }
    }

    /**
     * Changes the sounds in the sound list to piano notes
     */
    private void pianoBuild(){
        if(soundIDs != null){
            soundIDs.clear();
        }
        //Octave 1
        soundIDs.add(R.raw.piano_c3);
        soundIDs.add(R.raw.piano_csharp3);
        soundIDs.add(R.raw.piano_d3);
        soundIDs.add(R.raw.piano_dsharp3);
        soundIDs.add(R.raw.piano_e3);
        soundIDs.add(R.raw.piano_f3);
        soundIDs.add(R.raw.piano_fsharp3);
        soundIDs.add(R.raw.piano_g3);
        soundIDs.add(R.raw.piano_gsharp3);
        soundIDs.add(R.raw.piano_a3);
        soundIDs.add(R.raw.piano_asharp3);
        soundIDs.add(R.raw.piano_b3);
        //Octave 2
        soundIDs.add(R.raw.piano_c4);
        soundIDs.add(R.raw.piano_csharp4);
        soundIDs.add(R.raw.piano_d4);
        soundIDs.add(R.raw.piano_dsharp4);
        soundIDs.add(R.raw.piano_e4);
        soundIDs.add(R.raw.piano_f4);
        soundIDs.add(R.raw.piano_fsharp4);
        soundIDs.add(R.raw.piano_g4);
        soundIDs.add(R.raw.piano_gsharp4);
        soundIDs.add(R.raw.piano_a4);
        soundIDs.add(R.raw.piano_asharp4);
        soundIDs.add(R.raw.piano_b4);
        //Octave 3
        soundIDs.add(R.raw.piano_c5);
        soundIDs.add(R.raw.piano_csharp5);
        soundIDs.add(R.raw.piano_d5);
        soundIDs.add(R.raw.piano_dsharp5);
        soundIDs.add(R.raw.piano_e5);
        soundIDs.add(R.raw.piano_f5);
        soundIDs.add(R.raw.piano_fsharp5);
        soundIDs.add(R.raw.piano_g5);
        soundIDs.add(R.raw.piano_gsharp5);
        soundIDs.add(R.raw.piano_a5);
        soundIDs.add(R.raw.piano_asharp5);
        soundIDs.add(R.raw.piano_b5);
        //Octave 4
        soundIDs.add(R.raw.piano_c6);
    }


    public ArrayList<Key> getKeyList(){
        return keyList;
    }
}
