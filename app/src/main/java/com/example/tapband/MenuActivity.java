package com.example.tapband;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    Intent keyboardIntent;
    Intent helpIntent;
    Button nextActivityButton;
    Button chooseInstrumentButton;
    Button helpButton;
    ImageView instrumentImageView;
    Button changeColorButton;

    ImageView logoImage;
    TextView logoText;

    int currentImage = 0;  //Determines the image when selecting an instrument on the menu as well as the type of instrument created in the main activity
    int currentColor = 0;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
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
        changeColorButton = findViewById(R.id.colorButton);
        logoImage = findViewById(R.id.logoBar);
        logoText = findViewById(R.id.textView);

        final int[] drawables = new int[] { R.drawable.piano, R.drawable.synth, R.drawable.flute}; // Put images here

        final int[][] colorArray = new int[][] { { R.color.colorPrimary, R.color.ButtonColor, R.color.ButtonColor2, R.color.MenuButtons }, {R.color.RedPrimary, R.color.Red, R.color.RedDark, R.color.RedLight}, {R.color.GrayPrimary ,R.color.Gray, R.color.GrayDark, R.color.GrayLight} } ;

        changeColorButton.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    currentColor++;
                    if (currentColor >= colorArray.length)
                    {
                        currentColor = 0;
                    }
                    logoImage.setImageResource(colorArray[currentColor][0]);
                    logoText.setTextColor(colorArray[currentColor][3]);
                    keyboardIntent.putExtra("colors", colorArray[currentColor]);
                  }
             }

        );

        keyboardIntent.putExtra("colors", colorArray[currentColor]);

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
                keyboardIntent.putExtra("currentImage", currentImage); // Sends the current image variables's value to the main activity under the same name
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
