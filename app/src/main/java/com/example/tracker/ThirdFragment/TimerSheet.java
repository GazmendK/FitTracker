package com.example.tracker.ThirdFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.tracker.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Timer;
import java.util.TimerTask;

public class TimerSheet {
    private TextView timerText;
    private Button stopStartButton;

    private Timer timer;
    private TimerTask timerTask;
    private Double time = 0.0;

    private boolean timerStarted = false;
    private Context context;

    public TimerSheet(Context context) {
        this.context = context;
    }

    public void start() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view1 = LayoutInflater.from(context).inflate(R.layout.fragment_third_timer_sheet, null);
        bottomSheetDialog.setContentView(view1);

        // Initialisiere Views
        timerText = view1.findViewById(R.id.timerText);
        stopStartButton = view1.findViewById(R.id.startStopButton);

        timer = new Timer();

        // Setze Button-Listener
        stopStartButton.setOnClickListener(this::startStopTapped);
        Button resetButton = view1.findViewById(R.id.resetButton);
        resetButton.setOnClickListener(this::resetTapped);

        // Timer auf 0 setzen
        timerText.setText(formatTime(0, 0, 0));

        // Zeige den Dialog
        bottomSheetDialog.show();
    }

    public void resetTapped(View view) {
        if (timerTask != null) {
            timerTask.cancel();
            setButtonUI("Start", R.color.white);
            time = 0.0;
            timerStarted = false;
            timerText.setText(formatTime(0, 0, 0));
        }
    }

    public void startStopTapped(View view) {
        if (!timerStarted) {
            timerStarted = true;
            setButtonUI("Stop", R.color.white);
            startTimer();
        } else {
            timerStarted = false;
            setButtonUI("Start", R.color.white);
            timerTask.cancel();
        }
    }

    private void setButtonUI(String text, int color) {
        stopStartButton.setText(text);
        stopStartButton.setTextColor(ContextCompat.getColor(context, color));
    }

    private void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                // Nutze Handler anstelle von runOnUiThread
                new android.os.Handler(context.getMainLooper()).post(() -> {
                    time += 0.01;
                    timerText.setText(getTimerText());
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 10);
    }

    private String getTimerText() {
        int rounded = (int) (time * 100); // Zeit in Millisekunden

        int milliseconds = rounded % 100; // Millisekunden (zwei Stellen)
        int seconds = (rounded / 100) % 60; // Sekunden
        int minutes = (rounded / 6000); // Minuten

        return formatTime(milliseconds, seconds, minutes);
    }

    private String formatTime(int milliseconds, int seconds, int minutes) {
        return String.format("%02d", minutes) + " : " +
                String.format("%02d", seconds) + " : " +
                String.format("%02d", milliseconds);
    }
}
