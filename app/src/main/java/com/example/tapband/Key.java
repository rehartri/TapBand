package com.example.tapband;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.widget.Button;
import java.util.ArrayList;

class Key{
    private Context mainContext;  //The context from the main menu
    private SoundPool pool;  //SoundPool that handles playing the sounds
    private ArrayList<Integer> sounds = new ArrayList<>();  //List of sounds loaded by the SoundPool
    private Button button; //Button the key is associated with
    private Key currentKey; //The current key used during the touch event that originated from this key
    private int color; //Initial color of the key
    private int colorPrime; //Color the key changes to when it is pressed
    private int octave = 1;  //Determines what sounds are played by the SoundPool
    private int volume = 1; //Determines how loud each sound will play
    private int index; //Keeps track of which key in the piano this is
    private boolean pressed = false; //Helps with touch events
    private boolean sharp; //Helps with touch events by determining weather or not to switch between keys on overlap

    /**
     * Creates a key, initializes the SoundPool, and handles the touch events for the button
     * @param button The button that activates this key
     * @param context The context form the main activity
     */
    Key(Button button, boolean sharp, int index, Context context) {
        this.button = button;
        this.sharp = sharp;
        this.index = index;
        this.mainContext = context;
        color = ((ColorDrawable)button.getBackground()).getColor();
    }

    /**
     * Changes the color of the key and plays sounds
     */
    public void start(){
        if (!pressed) {
            pressed = true;
            pool.play(sounds.get(octave),1, 1, 0, 0, 1);
            button.setBackgroundColor(mainContext.getResources().getColor(colorPrime));
        }
    }

    /**
     * Changes the color of the key back to the original color
     */
    public void end(){
        button.setBackgroundColor(color);
        pressed = false;
    }


    /**
     * Discards resources used by the key
     */
    void clear(){
        sounds.clear();
    }

    /**
     * Adds a sound into the SoundPool
     * @param soundID The id of the sound to be added to the SoundPool
     */
    void addSound(int soundID){
        sounds.add(pool.load(mainContext, soundID, 1));
    }

    /**
     * Changes the volume at which the keys play each sound
     * @param volume The value the SoundPool uses for the left and right volume
     */
    void setVolume(int volume){
        this.volume = volume;
    }

    /**
     * Changes the octave of the instrument which determines the sounds used by the key
     * @param octave The new octave
     */
    void setOctave(int octave){
        this.octave = octave;
    }

    public Button getButton() {
        return button;
    }

    public int getIndex(){
        return index;
    }

    public boolean isSharp(){
        return sharp;
    }

    public Key getCurrentKey(){
        return currentKey;
    }

    public void setCurrentKey(Key currentKey){
        this.currentKey = currentKey;
    }

    public void setSoundPool(SoundPool soundPool){
        pool = soundPool;
    }

    public void setColor(int color){
        colorPrime = color;
    }
}
