package com.example.tapband;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
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
    long recordTime = 0;
    long recordTime2 = 0;
    long pauseTime = 0;
    int player = 0;
    CountDownTimer count;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menuIntent = new Intent(this, MenuActivity.class);
        Intent intent = getIntent();
        int currentImage = intent.getIntExtra("currentImage", 0);

        menuButton = findViewById(R.id.Menu);
        helpButton = findViewById(R.id.Help);

        //Buttons to work the record aspect
        recordButton = findViewById(R.id.Record);
        playButton = findViewById(R.id.Play);
        restartButton = findViewById(R.id.StartOver);

        playButton.setEnabled(false);
        restartButton.setEnabled(false);

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

        Button CLo = findViewById(R.id.CLo);
        Button D = findViewById(R.id.D);
        Button E = findViewById(R.id.E);
        Button F = findViewById(R.id.F);
        Button G = findViewById(R.id.G);
        Button A = findViewById(R.id.A);
        Button B = findViewById(R.id.B);
        Button CHi = findViewById(R.id.CHi);

        //Color change stuff
        int[] colors = intent.getIntArrayExtra("colors");
        menuButton.setBackgroundColor(getResources().getColor(colors[3]));
        helpButton.setBackgroundColor(getResources().getColor(colors[3]));
        CLo.setBackgroundColor(getResources().getColor(colors[1]));
        D.setBackgroundColor(getResources().getColor(colors[1]));
        E.setBackgroundColor(getResources().getColor(colors[1]));
        F.setBackgroundColor(getResources().getColor(colors[1]));
        G.setBackgroundColor(getResources().getColor(colors[1]));
        A.setBackgroundColor(getResources().getColor(colors[1]));
        B.setBackgroundColor(getResources().getColor(colors[1]));
        CHi.setBackgroundColor(getResources().getColor(colors[1]));

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextScreen();
            }
        });


        //If you touch these the buttons will be messed up, this is the only way I could find to do this
        CSharp.bringToFront();
        DSharp.bringToFront();
        FSharp.bringToFront();
        GSharp.bringToFront();
        ASharp.bringToFront();

        //Provides the functionality for the help button. Please do not delete!
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToHelpActivity();
            }
        });

        //Provides the functionality for the menu button. Please do not delete!
        menuButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //Locks piano screen into landscape orientation.

        setInstrument(getIntent().getIntExtra("type", 0)); //Creates instrument based on selection in menu

        recordButton.setOnClickListener(new View.OnClickListener(){//Sets up usability of record button
            int color = 0;//Tells the on click listener to go to record or stop recording
            long start = 0;//Keeps track of the start of the recording
            long end = 0;//Keeps track of the end of the recording
            @Override
            public void onClick(View v){//Activates on click
                if (color == 0) {
                    start = System.currentTimeMillis();//Tracks how long the recording is

                    recordButton.setBackgroundResource(R.drawable.round_button_green);//Switches the color

                    if(checkPermission()) {//Only goes through the next steps if microphone and speakers can be used
                        saveAudio = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Recording" + "AudioRecording.3gp";//Only saves one recording to the phone under the name AudioRecording

                        createMediaRecorder();

                        try{//Starts up the media recorder if there is no Illegal state or IO Exception
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
                        requestPermission();//Gets the permissions that were not yet acquired
                    }
                    color = 1;//Changes to the stop recording button
                }else{
                    recordButton.setBackgroundResource(R.drawable.round_button_red);//Switches the color
                    mediaRecorder.stop();//Makes it so that the recorder is no longer running

                    end = System.currentTimeMillis();//Lets the program know how long the recording was
                    recordTime = end - start;
                    recordTime2 = recordTime;

                    Toast.makeText(MainActivity.this, "Recording stopped", Toast.LENGTH_LONG).show();//Lets the play pause button start to work
                    playButton.setEnabled(true);
                    color = 0;//Switches the button back over to record mode
                }
            }
        });

        //Makes the play button work
        playButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException{
                if(player == 0) {
                    playButton.setBackgroundResource(R.drawable.pause);//Switches the look
                    mediaPlayer = new MediaPlayer();//Started the media player
                    try {//Starts up the player as long as there is not IO Exception
                        mediaPlayer.setDataSource(saveAudio);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();//Starts playing back your recording
                    restartButton.setEnabled(true);

                    player = 1;//Switches into the pause position for the time being

                    //Countdown that switches the appearance and action of the play/pause button if/when the recording is done playing
                    count = new CountDownTimer(recordTime, 500){//Counts dow until the button needs to be switched back over
                        @Override
                        public void onTick(long millisUntilFinished) {
                            pauseTime = millisUntilFinished;//Changes so that if paused the button still switches at the correct time
                        }

                        @Override
                        public void onFinish() {//Switches the timer over, and fixes the appearance and action of the play/pause button
                            player = 0;
                            playButton.setBackgroundResource(R.drawable.play);
                            recordTime = recordTime2;//Resets the record time
                        }
                    }.start();
                    //End

                }else{
                    //Stops and updates the countdown aspects to continue to work when play pressed
                    count.cancel();
                    recordTime = pauseTime;

                    playButton.setBackgroundResource(R.drawable.play);//Switches over the look
                    if(mediaPlayer.isPlaying()) {//Pause only if there is a recording currently playing
                        mediaPlayer.pause();
                    }

                    player = 0;//Switches back over to play button mode
                }
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener(){

            private static final long MIN_DELAY = 500;//Prevent button spam on half second delays

            private long clickTime;

            @Override
            public void onClick(View v) throws IllegalStateException, SecurityException{//Checks to be sure restart isn't being pressed too fast
                long timedClick = clickTime;
                long now = System.currentTimeMillis();
                clickTime = now;
                if(now - timedClick < MIN_DELAY) {//Doesn't allow the spam clicks to work
                    Toast.makeText(MainActivity.this,"STOP SPAMMING", Toast.LENGTH_LONG).show();
                }else {//Starts the media player
                    mediaPlayer.stop();//Cancels any currently playing media player

                    player = 0;//Resets the play pause button and countdown timer
                    playButton.setBackgroundResource(R.drawable.pause);
                    count.cancel();

                    mediaPlayer = new MediaPlayer();//Gets the media player going as long as there is no IO Exception
                    try {
                        mediaPlayer.setDataSource(saveAudio);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();

                    //Countdown to when the pause button should change functionality and display back to play button
                    recordTime = recordTime2;//Get the play pause to display properly using a countdown timer
                    count = new CountDownTimer(recordTime, 500){

                        @Override
                        public void onTick(long millisUntilFinished) {
                            pauseTime = millisUntilFinished;//Changes so that the countdown starts from the properish spot
                        }

                        @Override
                        public void onFinish() {//Switches the pause button back over to play
                            player = 0;
                            playButton.setBackgroundResource(R.drawable.play);
                        }
                    }.start();
                }
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
