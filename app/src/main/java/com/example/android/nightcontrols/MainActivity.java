package com.example.android.nightcontrols;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private AudioManager audioManager;
    private SeekBar seekBarVolume;
    private TextView seekBarVolumeValue;
    private Switch switchVolumeButton;
    private SeekBar seekBarBrightness;
    private TextView seekBarBrightnessValue;
    private Switch switchBrightnessButton;
    SharedUserPreferences shared;

    NotificationManager nm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shared = new SharedUserPreferences(this);

        initControls();
        switchButtonChecked();
        initListeners();

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        startService(new Intent(getApplicationContext(), Filter.class));
        makeNotification();


        //Changement de luminosité via Android.
        /*
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = 0;
        getWindow().setAttributes(layoutParams);
*/

//        Intent intent = new Intent(MainActivity.this, Filter.class);
//        startService(intent);

    }

    private void makeNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Volume Control")
                .setContentText("Hello world ")
                .setOngoing(true);

        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(pendingIntent);
        int mNotificationId = 001;
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    private void initControls() {
        switchVolumeButton = (Switch) findViewById(R.id.switch_button);
        switchVolumeButton.setChecked(true);
        switchBrightnessButton = (Switch) findViewById(R.id.switch_button_brightness);
        switchBrightnessButton.setChecked(true);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        seekBarVolume = (SeekBar) findViewById(R.id.seekbar);
        seekBarVolume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekBarVolume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        seekBarVolume = (SeekBar) findViewById(R.id.seekbar);

        seekBarVolumeValue = (TextView) findViewById(R.id.seekbar_value);
        seekBarVolumeValue.setText(String.valueOf(seekBarVolume.getProgress()));

        seekBarBrightness = (SeekBar) findViewById(R.id.alphaControl);
        seekBarBrightnessValue = (TextView) findViewById(R.id.seekbar_brightness_value);
        seekBarBrightness.setProgress(seekBarBrightness.getMax());
        seekBarBrightnessValue.setText(String.valueOf(seekBarBrightness.getProgress() / 10));

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 25) {
            seekBarVolume.setProgress(seekBarVolume.getProgress() - 1);
        }
        if (keyCode == 24) {
            seekBarVolume.setProgress(seekBarVolume.getProgress() + 1);
        }

        return super.onKeyDown(keyCode, event);
    }

    private void switchButtonChecked() {
        switchVolumeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    seekBarVolume.setClickable(true);
                    seekBarVolume.setEnabled(true);
                } else {
                    seekBarVolume.setClickable(false);
                    seekBarVolume.setEnabled(false);
                }
            }
        });
        switchBrightnessButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    makeNotification();
                    startService(new Intent(MainActivity.this, Filter.class));
                    seekBarBrightness.setClickable(true);
                    seekBarBrightness.setEnabled(true);
                } else {
                    if (Filter.STATE == Filter.ACTIVE) {
                        stopService(new Intent(MainActivity.this, Filter.class));
                    }
                    nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    nm.cancelAll();
                    seekBarBrightness.setClickable(false);
                    seekBarBrightness.setEnabled(false);
                }
            }
        });
    }

    private void initListeners() {
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarVolumeValue.setText(String.valueOf(progress));
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarBrightnessValue.setText(String.valueOf(progress / 10));
                shared.setAlpha(progress);

                startService(new Intent(MainActivity.this, Filter.class));



//                int color = shared.getColor();
//                ColorDrawable cd = new ColorDrawable(color);
//                getWindow().setBackgroundDrawable(cd);


                /* Permet de changer la résolution du téléphone */
                /*
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.screenBrightness = brightnessValue;
                getWindow().setAttributes(params);
                */

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        if (id == R.id.filter_settings) {
            Toast.makeText(getApplicationContext(), "Appuie sur Filter settings", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, Dialog.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Filter.STATE == Filter.ACTIVE) {
            stopService(new Intent(MainActivity.this, Filter.class));
        }
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancelAll();

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        moveTaskToBack(true);
    }
}
