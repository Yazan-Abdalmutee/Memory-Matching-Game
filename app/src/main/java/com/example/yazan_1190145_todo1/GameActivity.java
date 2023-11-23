package com.example.yazan_1190145_todo1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements MyTimer.CountdownCallback {
    //player1Turn:to check if this turn for player1 or not
    //viewClicked:to check if the card is clicked or not
    boolean player1Turn = false, viewClicked = true;
    //counter:to set the number of card clicked in the round for 2
    //start:to set the first turn for player1
    int counter, start, firstValue, secondValue, player1Score, player2Score, firstCardIndex, secondCardIndex, player1Attempts = 9, player2Attempts = 9;
    TextView firstCard, secondCard;
    //array to store the index of clicked cards .. if cards0 is clicked then index 0 will be 1
    int[] isClicked = new int[12];
    //cards values
    int[] array = new int[12];
    TextView[] cards = new TextView[12];
    Random rand = new Random();
    int timeFinish;
    //common views between oncreate and the methods
    TextView timerView, player1Name, player2Name, playerTurn, attemptsLeftView, description;
    private MyTimer myTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        player1Name = (TextView) findViewById(R.id.textView_player1Name);
        player2Name = (TextView) findViewById(R.id.textView_player2Name);
        TextView player1ScoreView = (TextView) findViewById(R.id.textView_player1Score);
        TextView player2ScoreView = (TextView) findViewById(R.id.textView_player2Score);
        description = (TextView) findViewById(R.id.textView_description);
        attemptsLeftView = (TextView) findViewById(R.id.textView_attempts);
        player1Name.setText(getIntent().getStringExtra("player1Name"));
        player2Name.setText(getIntent().getStringExtra("player2Name"));
        playerTurn = (TextView) findViewById(R.id.textView_playerTurn);
        Button nextTurnButton = (Button) findViewById(R.id.button_NextTurn);
        Button restartButton = (Button) findViewById(R.id.button_Restart);
        timerView = (TextView) findViewById(R.id.textView_Timer);
        cards[0] = (TextView) findViewById(R.id.textView_A_card);
        cards[1] = (TextView) findViewById(R.id.textView_B_card);
        cards[2] = (TextView) findViewById(R.id.textView_C_card);
        cards[3] = (TextView) findViewById(R.id.textView_D_card);
        cards[4] = (TextView) findViewById(R.id.textView_E_card);
        cards[5] = (TextView) findViewById(R.id.textView_F_card);
        cards[6] = (TextView) findViewById(R.id.textView_G_card);
        cards[7] = (TextView) findViewById(R.id.textView_H_card);
        cards[8] = (TextView) findViewById(R.id.textView_I_card);
        cards[9] = (TextView) findViewById(R.id.textView_J_card);
        cards[10] = (TextView) findViewById(R.id.textView_K_card);
        cards[11] = (TextView) findViewById(R.id.textView_L_card);
        cardsSettings();
        for (int i = 0; i < cards.length; i++) {
            int finalI = i;
            cards[i].setOnClickListener(v -> {
                CardOnclickActions(cards[finalI], finalI);
            });
        }
        attemptsLeftView.setText("9 Attempts left");
        description.setText("If you didn't start the next turn on time\nYour selected cards will not count :)");
        description.setTextSize(16);
        if (start == 0) {
            playerTurn.setText(player1Name.getText().toString() + " Turn !");
            start += 1;
        }
        myTimer = new MyTimer(this);
        myTimer.startCountdown();
        //restart button
        restartButton.setOnClickListener(view -> {
            counter = 0;
            for (int i = 0; i < cards.length; i++) {
                cards[i].setEnabled(true);
            }
            for (int i = 0; i < array.length - 1; i++) {
                array[i] = i;
                array[i + 1] = i;
                i += 1;
            }
            for (int i = 0; i < isClicked.length; i++) {
                isClicked[i] = 0;
            }
            for (int i = 0; i < cards.length; i++) {
                char letter = (char) ('A' + i);
                cards[i].setText(String.valueOf(letter));
                cards[i].setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            description.setText("If you didn't start the next turn on time\nYour selected cards will not count :)");
            description.setTextSize(16);

            playerTurn.setText(player1Name.getText().toString() + " Turn !");
            player1ScoreView.setText("0");
            player2ScoreView.setText("0");
            rand.setSeed(System.currentTimeMillis());
            player1Turn = false;
            viewClicked = true;
            start = 0;
            counter = 0;
            player1Attempts = 9;
            player2Attempts = 9;
            attemptsLeftView.setText("9 Attempts left");
            myTimer.startCountdown();
            cardsSettings();
        });

        //next turn button
        nextTurnButton.setOnClickListener(view -> {
            int finalScore = player1Score + player2Score;
            if (counter == 2 && finalScore < 5 && (player1Attempts > 0 || player2Attempts > 0)) {
                myTimer.startCountdown();
                if (firstValue != secondValue) {
                    char letter1 = (char) ('A' + firstCardIndex);
                    char letter2 = (char) ('A' + secondCardIndex);
                    firstCard.setText(String.valueOf(letter1));
                    secondCard.setText(String.valueOf(letter2));
                    firstCard.setBackgroundColor(Color.parseColor("#4CAF50"));
                    secondCard.setBackgroundColor(Color.parseColor("#4CAF50"));
                    isClicked[firstCardIndex] = 0;
                    isClicked[secondCardIndex] = 0;
                    //when the pair of the selected cards are not equal...make them allowed to be clicked again in next round
                    secondCard.setEnabled(true);
                    firstCard.setEnabled(true);
                } else {
                    //if the selected cards is equal and game not finish the player will take point
                    if (!player1Turn) {
                        player1Score += 1;
                        player1ScoreView.setText(String.valueOf(player1Score));
                    } else {
                        player2Score += 1;
                        player2ScoreView.setText(String.valueOf(player2Score));
                    }
                }
                if (!player1Turn) {
                    player1Attempts -= 1;
                    attemptsLeftView.setText(player2Attempts + " Attempts left");
                } else {
                    player2Attempts -= 1;
                    attemptsLeftView.setText(player1Attempts + " Attempts left");
                }
                updatePlayerTurn(playerTurn, player1Name, player2Name);
            }
            if (finalScore == 5 || (player1Attempts == 0 && player2Attempts == 0)) {
                {
                    if (player1Attempts != 0 || player2Attempts != 0) {
                        for (int i = 0; i < isClicked.length; i++) {
                            if (isClicked[i] == 0) {
                                viewClicked = false;
                                break;
                            }
                        }
                    }
                    //check if all cards are selected
                    if (viewClicked) {
                        myTimer.cancelCountdown();
                        //we reach here when we have only 2 cards at the end ..so the player in this turn will take the points Naturally.
                        playerTurn.setText("!! GAME FINISH !!");
                        if (player1Attempts != 0 || player2Attempts != 0) {
                            if (!player1Turn) {
                                player1Score += 1;
                                player1ScoreView.setText(String.valueOf(player1Score));
                            } else {
                                player2Score += 1;
                                player2ScoreView.setText(String.valueOf(player2Score));
                            }
                        }
                        for (int i = 0; i < cards.length; i++) {
                            cards[i].setEnabled(false);
                        }
                        winnerMsg();
                        timerView.setText("---");
                    }
                }
                viewClicked = true;
            }
        });
    }

    //this method will update the player turn by using the value of counter ..when counter=2 this is the next player turn
    private void updatePlayerTurn(TextView playerTurn, TextView player1Name, TextView player2Name) {
        if (counter == 2) {
            firstCard = null;
            secondCard = null;
            if (player1Turn) {

                playerTurn.setText(player1Name.getText().toString() + " Turn !");
            } else {
                playerTurn.setText(player2Name.getText().toString() + " Turn !");
            }
            player1Turn = !player1Turn;
            counter = 0;
        }
    }

    //update the cards colors
    private void updateCard(TextView card) {
        if (!player1Turn) {
            card.setBackgroundColor(Color.RED);
        } else {
            card.setBackgroundColor(Color.YELLOW);
        }
    }

    //save the values of the selected cards in the round
    private void checkValues(TextView card, int value, int index) {
        if (counter == 0) {
            firstCard = card;
            firstCardIndex = index;
            firstValue = value;
        }
        if (counter == 1) {
            secondCard = card;
            secondCardIndex = index;
            secondValue = value;
        }
    }

    //this will generate and  random the the cards values
    private void cardsSettings() {
        //generate card values
        for (int i = 0; i < array.length - 1; i++) {
            array[i] = i;
            array[i + 1] = i;
            i += 1;
        }
        //random cards values
        for (int i = array.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            // Swap array[i] and array[j]
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
        player1Score = 0;
        player2Score = 0;
    }

    //operation at the cards
    private void CardOnclickActions(TextView c, int k) {
        if (isClicked[k] == 0 && counter < 2) {
            //when the cards is selected ..isclcked get the value -1
            isClicked[k] = -1;
            c.setText(String.valueOf(array[k]));
            checkValues(cards[k], array[k], k);
            updateCard(cards[k]);
            counter += 1;
            c.setEnabled(false);
        }
    }

    public void onCountdownTick(int secondsRemaining) {
        // Update the TextView with the countdown value
        updateCountdownTextView(secondsRemaining);
    }

    //when timer finish
    @Override
    public void onCountdownFinish() {
        // Countdown has finished, perform any final actions
        Log.d("Countdown", "Countdown finished!");
        if (firstCard != null) {
            char letter1 = (char) ('A' + firstCardIndex);
            firstCard.setText(String.valueOf(letter1));
            firstCard.setBackgroundColor(Color.parseColor("#4CAF50"));
            isClicked[firstCardIndex] = 0;
            firstCard.setEnabled(true);
        }
        firstCard = null;
        if (secondCard != null) {
            char letter1 = (char) ('A' + secondCardIndex);
            secondCard.setText(String.valueOf(letter1));
            secondCard.setBackgroundColor(Color.parseColor("#4CAF50"));
            isClicked[secondCardIndex] = 0;
            secondCard.setEnabled(true);
        }
        if (player1Attempts > 0 || player2Attempts > 0) {
            if (!player1Turn) {
                player1Attempts -= 1;
                attemptsLeftView.setText(player2Attempts + " Attempts left");
            } else {
                player2Attempts -= 1;
                attemptsLeftView.setText(player1Attempts + " Attempts left");
            }
        }
        counter = 2;
        updatePlayerTurn(playerTurn, player1Name, player2Name);
        secondCard = null;
        if (player1Attempts == 0 && player2Attempts == 0) {
            winnerMsg();
            timerView.setText("---");
            myTimer.cancelCountdown();
            playerTurn.setText("!! GAME FINISH !!");
            for (int i = 0; i < cards.length; i++) {
                cards[i].setEnabled(false);
            }
        } else {

            myTimer.startCountdown();
        }
    }
    // Method to update the countdown TextView
    private void updateCountdownTextView(final int secondsRemaining) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timerView.setText("Countdown: " + secondsRemaining);
            }
        });
    }

    private void winnerMsg() {
        description.setTextSize(20);
        if (player1Score > player2Score) {
            description.setText("The Winner is " + player1Name.getText().toString() + "\n" + player1Name.getText().toString() + " Score:" + player1Score + " - " + player2Name.getText().toString() + " Score:" + player2Score);
        } else if (player1Score < player2Score) {
            description.setText("The Winner is " + player2Name.getText().toString() + "\n" + player1Name.getText().toString() + " Score:" + player1Score + " - " + player2Name.getText().toString() + " Score:" + player2Score);
        } else {
            description.setText("!! DRAW !! " + "\n" + player1Name.getText().toString() + " Score:" + player1Score + " - " + player2Name.getText().toString() + " Score:" + player2Score);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Create an explicit intent to go to the main activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        // Finish the current activity
        finish();
        return true;
    }
}