package com.example.tapband;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

class Instrument {
    private ArrayList<Key> keyList = new ArrayList<>(); //List of all the keys in the instrument
    private ArrayList<Key> sharpKeyList = new ArrayList<>(); //List of all the sharp keys which helps reduce searching in touch listener
    private ArrayList<Integer> soundIDs = new ArrayList<>(); //List of id numbers for the sounds in the instrument
    private SoundPool pool;
    private int type = -1; //The value that determines the type of instrument created


    /**
     * Creates an instrument, handles the slider events, and passes the context from the main activity into each of the buttons
     * @param buttonList List of buttons referenced by the keys
     * @param seekBar Seek bar referenced by the keys
     * @param context The context of the main activity which gets passed into each key
     */
    Instrument(final ArrayList<KeyType> buttonList, SeekBar seekBar, final Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){ //Initializes the SoundPool based on the version of Android
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            pool = new SoundPool.Builder()
                    .setMaxStreams(3)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }else{
            pool = new SoundPool(40, AudioManager.STREAM_MUSIC, 0);
        }
        for(int i = 0; i < buttonList.size(); i++){  //Creates each key and passes it it's own button to use
            final Key key = new Key(buttonList.get(i).getButton(), buttonList.get(i).isSharp(), i, context);
            key.setSoundPool(pool);
            keyList.add(key);
            if(key.isSharp()){
                sharpKeyList.add(key);
            }
            buttonList.get(i).getButton().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
//                    Toast.makeText(context, String.valueOf(key.getButton().getX()) + ", " + String.valueOf(((key.getButton().getX() * 1.0) + ((key.getButton().getX() + key.getButton().getWidth()) * 1.0)) / 2) + ", " + String.valueOf((key.getButton().getX() + key.getButton().getWidth())) , Toast.LENGTH_SHORT).show();
                    key.start();
                    key.setCurrentKey(key);
                    return true;
                }else if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE) {
                    if (inBounds(key.getCurrentKey(), event)){
                        if(!key.getCurrentKey().isSharp()){
                            searchSharp(key, event);
                        }
                        key.getCurrentKey().start();
                    }else{
                        key.getCurrentKey().end();
                        if(key.getCurrentKey().isSharp()){
                            if(event.getRawX() < x(key)){
                                searchLeft(key, event);
                            }else if(event.getRawX() > (x(key) + width(key))){
                                searchRight(key, event);
                            }else if(event.getRawY() > (y(key) + height(key))){
                                if(event.getRawX() < (((x(key) * 1.0) + ((x(key) + width(key)) * 1.0)) / 2)){
                                    searchLeft(key, event);
                                }else{
                                    searchRight(key, event);
                                }
                            }
                        }else{
                            if(event.getRawX() < x(key)){
                                searchLeft(key, event);
                            }else if(event.getRawX() > (x(key) + width(key))){
                                searchRight(key, event);
                            }
                        }
                        return true;
                    }
                }else if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    key.getCurrentKey().end();
                    return false;
                }
                return false;
            }
            
            boolean searchLeft(Key key, MotionEvent event){
                for(int i = key.getCurrentKey().getIndex(); i >= 0; i--){
                    if(inBounds(keyList.get(i), event)){
                        key.setCurrentKey(keyList.get(i));
                        return true;
                    }
                }
                return false;
            }

            boolean searchRight(Key key, MotionEvent event){
                for(int i = key.getCurrentKey().getIndex(); i < keyList.size(); i++){
                    if(inBounds(keyList.get(i), event)){
                        key.setCurrentKey(keyList.get(i));
                        return true;
                    }
                }
                return false;
            }

            boolean searchSharp(Key key, MotionEvent event){
                for(int i = 0; i < sharpKeyList.size(); i++){
                    if(inBounds(sharpKeyList.get(i), event)){
                        key.getCurrentKey().end();
                        key.setCurrentKey(sharpKeyList.get(i));
                        return true;
                    }
                }
                return false;
            }

            boolean inBounds(Key key, MotionEvent event) {
                if(key.isSharp()){
                    return event.getRawX() >= key.getButton().getX() && event.getRawX() <= (key.getButton().getX() + key.getButton().getWidth()) && event.getRawY() >= key.getButton().getY() && event.getRawY() <= (key.getButton().getY() + key.getButton().getHeight() + 65);
                }else{
                    return event.getRawX() >= key.getButton().getX() && event.getRawX() <= (key.getButton().getX() + key.getButton().getWidth()) && event.getRawY() >= key.getButton().getY() && event.getRawY() <= (key.getButton().getY() + key.getButton().getHeight());
                }
            }

            double x(Key key){
                return key.getCurrentKey().getButton().getX();
            }

            double y(Key key){
                return key.getCurrentKey().getButton().getY();
            }

            double width(Key key){
                return key.getCurrentKey().getButton().getWidth();
            }

            double height(Key key){
                return key.getCurrentKey().getButton().getHeight();
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
}
