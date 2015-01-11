package com.example.android.nightcontrols;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by stephen on 01/01/15.
 */
public class Dialog extends Activity {
    private SeekBar redSeekBar;
    private SeekBar greenSeekBar;
    private SeekBar blueSeekBar;
    private TextView redText;
    private TextView greenText;
    private TextView blueText;
    private Button cancelButton;
    private Button okButton;
    SharedUserPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        shared = new SharedUserPreferences(this);

        initControls();
        initListeners();
    }

    private void initControls() {
        redSeekBar = (SeekBar) findViewById(R.id.redControl);
        greenSeekBar = (SeekBar) findViewById(R.id.greenControl);
        blueSeekBar = (SeekBar) findViewById(R.id.blueControl);
        redText = (TextView) findViewById(R.id.redLabel);
        greenText = (TextView) findViewById(R.id.greenLabel);
        blueText = (TextView) findViewById(R.id.blueLabel);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        okButton = (Button) findViewById(R.id.okButton);
        redSeekBar.setProgress(shared.getRed());
        greenSeekBar.setProgress(shared.getGreen());
        blueSeekBar.setProgress(shared.getBlue());


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelClick(v);
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyClick(v);
                Toast.makeText(getApplicationContext(), "Couleur " + shared.getColor(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initListeners() {
        redSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                shared.setRed(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        greenSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                shared.setGreen(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        blueSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                shared.setBlue(progress);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void cancelClick(View v) {
        finish();
    }

    private void applyClick(View v) {
//        shared.setAlpha(shared.getAlpha());
        shared.setBlue(shared.getBlue());
        shared.setRed(shared.getRed());
        shared.setGreen(shared.getGreen());
        startService(new Intent(Dialog.this, Filter.class));

        finish();
    }

}
