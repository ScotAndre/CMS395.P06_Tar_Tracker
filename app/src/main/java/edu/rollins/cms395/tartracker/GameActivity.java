package edu.rollins.cms395.tartracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends AppCompatActivity {
    private final int NUMBER_OF_BUTTONS = 9;
    private Button mButton0;
    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private Button mButton4;
    private Button mButton5;
    private Button mButton6;
    private Button mButton7;
    private Button mButton8;
    private Button[] mNumberButtons;
    private int[] mButtonValues;
    private int[] mButtonCorrectOrder;
    private int mWrongAnswerCounter;
    private Chronometer mTimer;
    private TextView mTvWrongAnswers;
    private boolean mGameInProgress = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // set up the Timer
        mTimer = (Chronometer) findViewById(R.id.button_game_timer);
        mTvWrongAnswers = (TextView) findViewById(R.id.number_missed_value);
        mTvWrongAnswers.setText(R.string.number_missed_value_default);

        // set up the Buttons
        mButton0 = (Button) findViewById(R.id.button_game_0);
        mButton1 = (Button) findViewById(R.id.button_game_1);
        mButton2 = (Button) findViewById(R.id.button_game_2);
        mButton3 = (Button) findViewById(R.id.button_game_3);
        mButton4 = (Button) findViewById(R.id.button_game_4);
        mButton5 = (Button) findViewById(R.id.button_game_5);
        mButton6 = (Button) findViewById(R.id.button_game_6);
        mButton7 = (Button) findViewById(R.id.button_game_7);
        mButton8 = (Button) findViewById(R.id.button_game_8);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void generateRandomNumbers(){
        final int MAX_NUMBERS = 100;
        mButtonValues = new int[NUMBER_OF_BUTTONS];
        mButtonCorrectOrder = new int[NUMBER_OF_BUTTONS];
        ArrayList<Integer> allNumbers = new ArrayList<Integer>(MAX_NUMBERS);
        ArrayList<Integer> numbers = new ArrayList<Integer>(NUMBER_OF_BUTTONS);

        // generate an ArrayList of integers 0 to MAX_NUMBERS
        for(int i = 0; i < MAX_NUMBERS; i++){
            allNumbers.add(i);
        }
        // randomize the ArrayList
        Collections.shuffle(allNumbers);

        // shrink the list to match the NUMBER_OF_BUTTONS
        numbers = (ArrayList<Integer>) allNumbers.subList(0, NUMBER_OF_BUTTONS);

        // convert the ArrayList to an array
        for(int i = 0; i < mButtonValues.length; i++){
            mButtonValues[i] = numbers.get(i);
        }
    }
}
