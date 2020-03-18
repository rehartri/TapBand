package com.example.tapband;

import android.graphics.Rect;
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
    MediaPlayer player;
    Button button;
    PlaybackParams params = new PlaybackParams();
    private Rect rect = new Rect();
    boolean pressed = false;

    /**
     * @param button  The button associated with this key
     */
    public Key(final Button button) {
        this.button = button;
        setPitch(1);

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

    private void startup(){
        player.seekTo(0);
        player.start();
    }

    private void end(){

    }

    public void setPlayer(MediaPlayer player) {
        if(this.player != null){
            this.player.release();
        }
        this.player = player;
        player.setPlaybackParams(params);
    }

    public void setPitch(float newPitch){
        params.setPitch(newPitch);
        if(player != null){
            player.setPlaybackParams(params);
        }
    }

    public float getPitch(){
        return params.getPitch();
    }

}
