package com.example.tapband;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button helpButton;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helpButton = findViewById(R.id.Help);
        helpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                moveToHelpActivity();
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

    /*
    This moves the user from the keyboard screen to the help screen.
     */
    private void moveToHelpActivity(){
        Intent intent = new Intent(MainActivity.this, HelpScreenActivity.class);
        startActivity(intent);
    }
}
