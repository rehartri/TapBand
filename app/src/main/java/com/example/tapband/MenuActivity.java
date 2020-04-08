package com.example.tapband;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class MenuActivity extends AppCompatActivity {

    Intent keyboardIntent;
    Intent helpIntent;
    Button nextActivityButton;
    Button chooseInstrumentButton;
    Button helpButton;
    ImageView instrumentImageView;
    int currentImage = 0;  //Determines the image when selecting an instrument on the menu as well as the type of instrument created in the main activity

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//Do not touch
        setContentView(R.layout.activity_menu);//Do not touch
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //Locks main menu into landscape orientation
        keyboardIntent = new Intent(this, MainActivity.class); //Intent allows data to be shared between activities
        helpIntent = new Intent (this, HelpScreenActivity.class);

        nextActivityButton = findViewById(R.id.nextActivityButton); //Finds the button and gives it a name
        chooseInstrumentButton = findViewById(R.id.ChooseInstrument);
        instrumentImageView = findViewById(R.id.instrumentView);
        helpButton = findViewById(R.id.helpButton);

        final int[] drawables = new int[] { R.drawable.piano, R.drawable.synth}; // Put images here


        //Button click listener -- aka this gets called when the continue button is pressed
        nextActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyboardIntent.putExtra("type", currentImage); //Passes the value that determines the type of instrument to create to the main activity
                nextActivity();
            }
        });
        //Button click listener -- gets called when choose instrument button is pressed
        chooseInstrumentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentImage++; // add to index
                if (currentImage > drawables.length-1){ // looks for the index being too large
                    currentImage = 0; //index was too large, reset
                }
                instrumentImageView.setImageResource(drawables[currentImage]); //Set new image
            }
        });
        //Button click listener -- gets called when the help button is pressed
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHelp();
            }
        });
    }

    public void getHelp(){startActivity(helpIntent);}

    public void nextActivity(){
        startActivity(keyboardIntent);
    }

}
