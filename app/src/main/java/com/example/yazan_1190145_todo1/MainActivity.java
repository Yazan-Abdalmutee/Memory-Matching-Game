package com.example.yazan_1190145_todo1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.Main_toolbar);

        setSupportActionBar(toolbar);

        EditText player1Name = findViewById(R.id.editTextt_player1Name);

        EditText player2Name = findViewById(R.id.editTextt_player2Name);

        TextView nameErrorMsg = findViewById(R.id.textView_nameError);

        Button startGameButton = findViewById(R.id.button_start);

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //player name not exist
                if (player1Name.getText().toString().isEmpty() || player2Name.getText().toString().isEmpty()) {
                    nameErrorMsg.setText("Sorry,Please Enter the player names !");
                    //players have the same names
                } else if ((player1Name.getText().toString()).equalsIgnoreCase(player2Name.getText().toString())) {
                    nameErrorMsg.setText("Sorry,Names must be different !");
                } else {
                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                    intent.putExtra("player1Name", player1Name.getText().toString());
                    intent.putExtra("player2Name", player2Name.getText().toString());
                    MainActivity.this.startActivity(intent);
                    finish();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.exit_option) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}