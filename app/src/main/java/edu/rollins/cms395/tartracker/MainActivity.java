package edu.rollins.cms395.tartracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences mPrefs;
    private BacCalculator bac;
    TextView tvDrinkCount;

    //TODO: add button listeners to GameActivity
    //TODO: finish GameActivity
    //TODO: finish BAC Calculator

    // Patrick:
    //TODO: work on sound effects
    //TODO: auto refresh mainActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        setPreferences();
        bac = new BacCalculator(this);

        tvDrinkCount = (TextView) findViewById(R.id.drink_count);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setPreferences(){
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

    }

    public void loadGame(View view){
        startActivity(new Intent(getApplicationContext(), GameActivity.class));
    }

    public void onClickAddDrink(View view){
        Toast.makeText(this, "Drink Button Clicked", Toast.LENGTH_LONG).show();
        bac.addDrink();
        Toast.makeText(this, "# of Drinks = " + bac.getmDrinkCount(), Toast.LENGTH_LONG).show();
        tvDrinkCount.setText("" + bac.getmDrinkCount());
    }

    public void onClickResetButton(View view){
        Toast.makeText(this, "Reset Button Clicked", Toast.LENGTH_LONG).show();
        bac.resetDrinkCounter();
        tvDrinkCount.setText("" + bac.getmDrinkCount());

    }
}
