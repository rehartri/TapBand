package com.example.tapband;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    Intent keyboardIntent;
    Button nextActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        keyboardIntent = new Intent(this, MainActivity.class); //Intent allows data to be shared between activities
        nextActivityButton = findViewById(R.id.nextActivityButton); //Finds the button and gives it a name
    }

    public void nextActivity(){
        startActivity(keyboardIntent);
    }
}
