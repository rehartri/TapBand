package com.example.tapband;

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
import androidx.appcompat.app.AppCompatActivity;

@RequiresApi(api = Build.VERSION_CODES.M)

public class Key{
    MediaPlayer player; //Media player key uses
    Button button; //Button the key is associated with
    PlaybackParams params = new PlaybackParams(); //Helps change pitch of the key
    private Rect rect = new Rect(); //Helps track movement within the bounds of the key
    boolean pressed = false; //Helps with touch events
    int color; //Initial color of the key
    float currentPitch; //The current pitch of the key
    float basePitch = 1; //The starting pitch and multiplier for setting the pitch

    /**
     * Creates a new key, sets the pitch, and handles the touch events
     * @param button The button associated with this key
     */
    public Key(final Button button) {
        this.button = button;
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
     * @param player The new media player
     */
    public void setPlayer(MediaPlayer player) {
        if(this.player != null){
            this.player.release();
        }
        this.player = player;
        player.setPlaybackParams(params);
    }

    public float getPitch(){
        return params.getPitch();
    }

    /**
     * Changes the current pitch of the key if the media player is not null
     * @param newPitch The new pitch
     */
    public void setCurrentPitch(float newPitch){
        currentPitch = newPitch * basePitch;
        params.setPitch(currentPitch);
        if(player != null){
            player.setPlaybackParams(params);
        }
    }

    public float getBasePitch(){
        return basePitch;
    }

    /**
     * Sets the base pitch and changes the current pitch if the media player is not null
     * @param basePitch
     */
    public void setBasePitch(float basePitch){
        this.basePitch = basePitch;
        currentPitch = basePitch;
        params.setPitch(currentPitch);
        if(player != null){
            player.setPlaybackParams(params);
        }
    }
}
