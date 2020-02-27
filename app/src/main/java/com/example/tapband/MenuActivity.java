package com.example.tapband;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MenuActivity extends AppCompatActivity {

    Intent keyboardIntent;
    Button nextActivityButton;
    Button chooseInstrumentButton;
    ImageView instrumentImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//Do not touch
        setContentView(R.layout.activity_menu);//Do not touch

        keyboardIntent = new Intent(this, MainActivity.class); //Intent allows data to be shared between activities

        nextActivityButton = findViewById(R.id.nextActivityButton); //Finds the button and gives it a name
        chooseInstrumentButton = findViewById(R.id.ChooseInstrument);
        instrumentImageView = findViewById(R.id.instrumentView);


        //Button click listener -- aka this gets called when the continue button is pressed
        nextActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity();
            }
        });
        //Button click listener -- gets called when choose instrument button is pressed
        chooseInstrumentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instrumentImageView.setImageResource(R.drawable.ic_launcher_background); //Set new image, THIS IMAGE IS A PLACEHOLDER TO SHOW THAT THE BUTTON WORKS
            }
        });
    }

    public void nextActivity(){
        startActivity(keyboardIntent);
    }

}
