package com.example.tapband;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.M)
class Key{
    private Context mainContext;  //The context from the main menu
    private MediaPlayer player; //Media player key uses
    private Button button; //Button the key is associated with
    private PlaybackParams params = new PlaybackParams(); //Helps change pitch of the key
    private Rect rect = new Rect(); //Helps track movement within the bounds of the key
    private boolean pressed = false; //Helps with touch events
    private int color; //Initial color of the key
    private float currentPitch; //The current pitch of the key
    private float basePitch = 1; //The starting pitch and multiplier for setting the pitch
    private int soundID;  //The location of the sound file used by the key

    /**
     * Creates a new key, sets the pitch, and handles the touch events
     * @param button The button associated with this key
     */
    Key(final Button button, Context context) {
        this.button = button;
        this.mainContext = context;
        color = ((ColorDrawable)button.getBackground()).getColor();
        setCurrentPitch(basePitch);

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                button.getDrawingRect(rect);
                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
                    pressed = true;
                    startup();
                }
                if (rect.contains((int) event.getX(), (int) event.getY())){
                    if (!pressed) {
                        pressed = true;
                        startup();
                    }
                } else {
                    pressed = false;
                    end();
                }
                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    pressed = false;
                    end();
                }
                return false;
            }
        });
    }

    /**
     * Changes the color of the key and plays the sound
     */
    private void startup(){
        if(player == null){
            player = MediaPlayer.create(mainContext, soundID);
            player.setPlaybackParams(params);
        }
        button.setBackgroundColor(Color.argb(255, 0, 176, 255));
        player.seekTo(0);
        player.start();
    }

    /**
     * Changes the color of the key back to the original color
     */
    private void end(){
        button.setBackgroundColor(color);
    }

    /**
     * Replaces media player and deletes it
     * @param soundID The id of the sound to be played by this key
     */
    void setSound(int soundID) {
        this.soundID = soundID;
    }

    /**
     * Returns either the media player used by the key or null if one has not been created yet
     * @return Either the media player used by the key or null
     */
    MediaPlayer getPlayer(){
        if(player != null){
            return player;
        }else{
            return null;
        }
    }

    /**
     * Changes the current pitch of the key if the media player is not null
     * @param newPitch The new pitch
     */
    void setCurrentPitch(float newPitch){
        currentPitch = newPitch * basePitch;
        params.setPitch(currentPitch);
        if(player != null){
            player.setPlaybackParams(params);
        }
    }

    float getBasePitch(){
        return basePitch;
    }

    /**
     * Sets the base pitch and changes the current pitch if the media player is not null
     * @param basePitch The starting pitch and pitch multiplier for the key
     */
    void setBasePitch(float basePitch){
        this.basePitch = basePitch;
        currentPitch = basePitch;
        params.setPitch(currentPitch);
        if(player != null){
            player.setPlaybackParams(params);
        }
    }
}
