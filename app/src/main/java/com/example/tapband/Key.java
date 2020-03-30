package com.example.tapband;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;

class Key{
    private Context mainContext;  //The context from the main menu
    private SoundPool pool;  //SoundPool that handles playing the sounds
    private ArrayList<Integer> soundIDs = new ArrayList<>();  //The list of sounds used by the key
    private ArrayList<Integer> sounds = new ArrayList<>();  //List of sounds loaded by the SoundPool
    private Button button; //Button the key is associated with
    private Rect rect = new Rect(); //Helps track movement within the bounds of the key
    private int color; //Initial color of the key
    private int octave = 1;  //Determines what sounds are played by the SoundPool
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
            pool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        }

//        button.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                button.getDrawingRect(rect);
//                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
//                    pressed = true;
//                    start();
//                }
//                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE) {
//                    if (rect.contains((int) event.getX(), (int) event.getY())){
//                        if (!pressed) {
//                            pressed = true;
//                            start();
//                        }
//                    } else {
//                        pressed = false;
//                        end();
//                    }
//                }
//
//                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
//                    pressed = false;
//                    end();
//                }
//                return pressed;
//
//            }
//        });
    }

    /**
     * Changes the color of the key and plays sounds
     */
    public void start(){
        if (!pressed) {
            pressed = true;
            pool.play(sounds.get(octave), 1, 1, 0, 0, 1);
            button.setBackgroundColor(Color.argb(255, 0, 176, 255));
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
        pool.release();
        pool = null;
        soundIDs.clear();
        sounds.clear();
    }

    /**
     * Adds a sound into the SoundPool
     * @param soundID The id of the sound to be added to the SoundPool
     */
    void addSoundID(int soundID){
        soundIDs.add(soundID);
        sounds.add(pool.load(mainContext, soundID, 1));
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

    public Rect getRect(){
        return rect;
    }

    public void setPressed(boolean pressed){
        this.pressed = pressed;
    }

    public int getIndex(){
        return index;
    }

    public boolean isPressed(){
        return pressed;
    }

    public boolean isSharp(){
        return sharp;
    }
}
