package com.example.tapband;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Intent menuIntent;
    Button menuButton;
    Button recordButton;

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
                    recordButton.setBackgroundColor(Color.rgb(0, 255, 0));
                    color = 1;
                }else{
                    recordButton.setBackgroundColor(Color.rgb(255, 0, 0));
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


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //Locks piano screen into landscape orientation.
    }

    public void nextScreen(){
        startActivity(menuIntent);
    }

}
