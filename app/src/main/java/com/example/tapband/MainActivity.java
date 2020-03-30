package com.example.tapband;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Instrument instrument; //The instrument that handles the sounds made by the keyboard
    Intent menuIntent;
    Button recordButton;
    Button helpButton; //Button to reach the help menu
    Button menuButton; //Button to go back to main menu.
    ArrayList<KeyType> buttonList;

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
    public Instrument createInstrument(){
        buttonList = new ArrayList<>();
        buttonList.add(new KeyType((Button)findViewById(R.id.CLo), false));
        buttonList.add(new KeyType((Button)findViewById(R.id.CSharp), true));
        buttonList.add(new KeyType((Button)findViewById(R.id.D), false));
        buttonList.add(new KeyType((Button)findViewById(R.id.DSharp), true));
        buttonList.add(new KeyType((Button)findViewById(R.id.E), false));
        buttonList.add(new KeyType((Button)findViewById(R.id.F), false));
        buttonList.add(new KeyType((Button)findViewById(R.id.FSharp), true));
        buttonList.add(new KeyType((Button)findViewById(R.id.G), false));
        buttonList.add(new KeyType((Button)findViewById(R.id.GSharp), true));
        buttonList.add(new KeyType((Button)findViewById(R.id.A), false));
        buttonList.add(new KeyType((Button)findViewById(R.id.ASharp), true));
        buttonList.add(new KeyType((Button)findViewById(R.id.B), false));
        buttonList.add(new KeyType((Button)findViewById(R.id.CHi), false));
        SeekBar seekBar = findViewById(R.id.seekBar);
        return new Instrument(buttonList, seekBar, getApplicationContext());
    }

    /**
     * Sets the sounds used for the keyboard and initializes instrument
     * @param type the value that determines what type of sounds will be used by the instrument class
     */
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
