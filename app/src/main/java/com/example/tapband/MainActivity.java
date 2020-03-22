package com.example.tapband;

import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Instrument instrument; //The instrument that handles the sounds made by the keyboard
    Intent menuIntent;
    Button recordButton;
    Button helpButton; //Button to reach the help menu
    Button menuButton; //Button to go back to main menu.
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menuIntent = new Intent(this, MenuActivity.class);

        menuButton = findViewById(R.id.Menu);
        recordButton = findViewById(R.id.Record);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextScreen();
            }
        });



        recordButton.setOnClickListener(new View.OnClickListener(){
            int color = 0;
            @Override
            public void onClick(View v){
                if (color == 0) {
                    recordButton.setBackgroundResource(R.drawable.round_button_green);
                    color = 1;
                }else{
                    recordButton.setBackgroundResource(R.drawable.round_button_red);
                    color = 0;
                }
            }
        });

        Button CSharp = findViewById(R.id.CSharp);
        Button DSharp = findViewById(R.id.DSharp);
        Button FSharp = findViewById(R.id.FSharp);
        Button GSharp = findViewById(R.id.GSharp);
        Button ASharp = findViewById(R.id.ASharp);

        //If you touch these the buttons will be messed up, this is the only way I could find to do this
        CSharp.bringToFront();
        DSharp.bringToFront();
        FSharp.bringToFront();
        GSharp.bringToFront();
        ASharp.bringToFront();

        //Provides the functionality for the help button. Please do not delete!
        helpButton = findViewById(R.id.Help);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToHelpActivity();
            }
        });

        //Provides the functionality for the menu button. Please do not delete!
        menuButton = findViewById(R.id.Menu);
        menuButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //Locks piano screen into landscape orientation.

        setInstrument(getIntent().getIntExtra("type", 0)); //Creates instrument based on selection in menu
    }

    public void nextScreen(){
        startActivity(menuIntent);
    }

    /**
     * Creates a new instrument by passing it the keyboard buttons and pitch seek bar form the main activity
     * @return The new instrument
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public Instrument createInstrument(){
        ArrayList<Button> buttonList = new ArrayList<>();
        buttonList.add((Button)findViewById(R.id.CLo));
        buttonList.add((Button)findViewById(R.id.CSharp));
        buttonList.add((Button)findViewById(R.id.D));
        buttonList.add((Button)findViewById(R.id.DSharp));
        buttonList.add((Button)findViewById(R.id.E));
        buttonList.add((Button)findViewById(R.id.F));
        buttonList.add((Button)findViewById(R.id.FSharp));
        buttonList.add((Button)findViewById(R.id.G));
        buttonList.add((Button)findViewById(R.id.GSharp));
        buttonList.add((Button)findViewById(R.id.A));
        buttonList.add((Button)findViewById(R.id.ASharp));
        buttonList.add((Button)findViewById(R.id.B));
        buttonList.add((Button)findViewById(R.id.CHi));
        SeekBar seekBar = findViewById(R.id.seekBar);
        return new Instrument(buttonList, seekBar, getApplicationContext());
    }

    /**
     * Sets the sounds used for the keyboard and initializes instrument
     * @param type the value that determines what type of sounds will be used by the instrument class
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setInstrument(int type){
        if(instrument != null){
            instrument.clear();
        }
        instrument = createInstrument();
        instrument.setType(type);
    }

    /*
    This moves the user from the keyboard screen to the help screen.
     */
    private void moveToHelpActivity(){
        Intent intent = new Intent(MainActivity.this, HelpScreenActivity.class);
        startActivity(intent);
    }
}
