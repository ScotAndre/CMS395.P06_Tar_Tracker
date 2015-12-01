package edu.rollins.cms395.tartracker;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    private int mButtonPressed;
    private int mProgress;
    private Chronometer mTimer;
    private TextView mTvWrongAnswers;
    private boolean mGameInProgress = false;
    private int mGameScore;


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
            startActivity(new Intent(this, SettingsActivity.class));
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

        // provide a sorted number list as the answers
        Collections.sort(numbers);

        // cycle through the sorted list of numbers from smallest to largest
        // look up the index the current number occupies
        // store that in an array listing the correct order
        int indexOfSmallest;
        int index = 0;
        for(int smallest : numbers){
            indexOfSmallest = numbers.indexOf(smallest);
            mButtonCorrectOrder[index] = indexOfSmallest;
            index++;
        }
    }

    private void startGame(){
        // generate a new set of random numbers
        generateRandomNumbers();

        // set the counters to 0
        mProgress = 0;
        mWrongAnswerCounter = 0;
        mGameScore = 0;

        // reset the text of each button
        for (int i = 0; i < mButtonValues.length; i++){
            mNumberButtons[i].setText("" + mButtonValues[i]);
        }

        // start timer
        mTimer.setBase(SystemClock.elapsedRealtime());
        mTimer.start();

        // set game in progress flag
        mGameInProgress = true;
    }

    private void endGame(){
        // stop the timer
        mTimer.stop();

        // reset flag
        mGameInProgress = false;

        // calculate elapsed time
        long elapsedTime = SystemClock.elapsedRealtime() - mTimer.getBase();

        // calculate score
        mGameScore = calculateScore(elapsedTime);
    }

    private void checkCorrectAnswer(){

    }


    private int calculateScore(long elapsedTime){
        /*
         * Calculate score for the game. Smaller scores means higher sobriety,
         * while higher scores suggest intoxication. The score is based on a
         * value between 1 and 10, where 50% of the points are calculated from
         * the time that is taken to complete the puzzle, and the other half
         * calculated from the number of incorrect answers.
         */
        int adjustedTimeScore;
        int score = mWrongAnswerCounter;

        /*
         * The elapsed time is given to us in milliseconds. We need to boil it
         * down to a value between 1 and 5. A user that takes 20 seconds or
         * longer receives a maximum (bad) score of 5, while a user that takes
         * ten seconds or below is given a low score of 0.
         */
        adjustedTimeScore = (int) ((elapsedTime / 1000.0) -10) / 2;
        if(adjustedTimeScore < 0){
            adjustedTimeScore = 0;
        } else if (adjustedTimeScore > 5){
            adjustedTimeScore = 5;
        }

        score += adjustedTimeScore;

        if(score == 0){
            return 1;
        } else if(score > 10){
            return 10;
        } else {
            return score;
        }
    }

    // **************************** Button Listener *********************** //
    View.OnClickListener mButtonListener = new View.OnClickListener(){
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            // if it is the first time a user clicks on a tile,
            // this should start the game
            if(!mGameInProgress){
                startGame();
                return;
            }

            // any other clicks should get checked to see if answer was right
            switch (v.getId()){
                case R.id.button_game_0:
                    break;
                case R.id.button_game_1:
                    break;
                case R.id.button_game_2:
                    break;
                case R.id.button_game_3:
                    break;
                case R.id.button_game_4:
                    break;
                case R.id.button_game_5:
                    break;
                case R.id.button_game_6:
                    break;
                case R.id.button_game_7:
                    break;
                case R.id.button_game_8:
                    break;
            }
        }
    };
}
