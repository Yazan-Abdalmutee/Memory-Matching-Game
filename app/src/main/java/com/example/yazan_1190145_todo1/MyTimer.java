package com.example.yazan_1190145_todo1;

import android.os.CountDownTimer;
import android.os.Handler;

public class MyTimer {
    private CountdownCallback countdownCallback;
    private CountDownTimer countDownTimer;
    private Handler handler;

    public MyTimer(CountdownCallback callback) {
        this.countdownCallback = callback;
        this.handler = new Handler();
    }

    public void startCountdown() {
        // Cancel the previous timer if running
        int seconds = 10;
        cancelCountdown();
        // Create a new CountDownTimer
        countDownTimer = new CountDownTimer(seconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                if (countdownCallback != null) {
                    countdownCallback.onCountdownTick(secondsRemaining);
                }
            }

            @Override
            public void onFinish() {
                if (countdownCallback != null) {
                    countdownCallback.onCountdownFinish();
                }
            }
        };
        // Start the CountDownTimer
        countDownTimer.start();
    }

    public void cancelCountdown() {
        // Cancel the previous timer if not null
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public interface CountdownCallback {
        void onCountdownTick(int secondsRemaining);

        void onCountdownFinish();
    }
}
