package com.example.tapband;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.widget.Toast;
import java.io.IOException;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import android.content.pm.PackageManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
    Button playButton;
    Button pauseButton;
    Button restartButton;
    ArrayList<KeyType> buttonList;

    String saveAudio = null;

    public static final int RequestPermissionCode = 1;

    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menuIntent = new Intent(this, MenuActivity.class);

        menuButton = findViewById(R.id.Menu);

        //Buttons to work the record aspect
        recordButton = findViewById(R.id.Record);
        playButton = findViewById(R.id.Play);
        restartButton = findViewById(R.id.StartOver);

        playButton.setEnabled(false);
        restartButton.setEnabled(true);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextScreen();
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

        recordButton.setOnClickListener(new View.OnClickListener(){//Sets up usability of record button
            int color = 0;
            @Override
            public void onClick(View v){
                if (color == 0) {
                    recordButton.setBackgroundResource(R.drawable.round_button_green);

                    if(checkPermission()) {
                        saveAudio = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Recording" + "AudioRecording.3gp";

                        createMediaRecorder();

                        try{
                            mediaRecorder.prepare();
                            mediaRecorder.start();
                            //This is to test on Kofi's device
                            Toast.makeText(MainActivity.this, "Recording started", Toast.LENGTH_LONG).show();
                        }catch(IllegalStateException e){
                            e.printStackTrace();
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    } else {
                        requestPermission();
                    }

                    playButton.setEnabled(true);
                    restartButton.setEnabled(true);

                    color = 1;
                }else{
                    recordButton.setBackgroundResource(R.drawable.round_button_red);
                    mediaRecorder.stop();
                    Toast.makeText(MainActivity.this, "Recording stopped", Toast.LENGTH_LONG).show();
                    color = 0;
                }
            }
        });

        //Makes the play button work
        playButton.setOnClickListener(new View.OnClickListener(){
            int player = 0;
            @Override
            public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException{
                if(player == 0) {
                    playButton.setBackgroundResource(R.drawable.pause);
                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(saveAudio);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                    player = 1;
                }else{
                    playButton.setBackgroundResource(R.drawable.play);
                    if(mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                    player = 0;
                }
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException{
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer = new MediaPlayer();
                try{
                    mediaPlayer.setDataSource(saveAudio);
                    mediaPlayer.prepare();
                }catch(IOException e){
                    e.printStackTrace();
                }
                mediaPlayer.start();
            }
        });

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

    //Checks to make sure that recording is allowed on the device
    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    //Creates the media recorder
    public void createMediaRecorder(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(saveAudio);
    }

    //Goes to get permission
    private void requestPermission(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    public void onRequestPermissionResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode){
            case RequestPermissionCode:
                if (grantResults.length > 0){
                    boolean StoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    break;
                }
        }
    }

}
