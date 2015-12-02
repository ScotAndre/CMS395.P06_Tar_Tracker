package edu.rollins.cms395.tartracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

public class PersonalSettingsActivity extends AppCompatActivity {
    private EditText etUserName;
    private EditText etUserAge;
    private EditText etUserWeight;
    private ToggleButton tbUserGender;
    private DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_settings);

        db = new DatabaseManager(this);

        //TODO: Get values from database and insert them into appropriate field
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_personal_settings, menu);
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

    public void saveButtonClicked(View view){
        etUserName = (EditText) findViewById(R.id.user_name);
        etUserAge = (EditText) findViewById(R.id.use_age);
        etUserWeight = (EditText) findViewById(R.id.user_weight);
        tbUserGender = (ToggleButton) findViewById(R.id.toggle_gender);
        String userName = etUserName.getText().toString().trim();
        int userAge = Integer.parseInt(etUserAge.getText().toString().trim());
        int userWeight = Integer.parseInt(etUserWeight.getText().toString().trim());
//        String userGender = tbUserGender.getText();

        StringBuffer sbUserGender = new StringBuffer();
        sbUserGender.append(tbUserGender.getText());
        String userGender = sbUserGender.toString();

        //TODO: Finish saveButtonClicked
//        db.setName(userName);
//        db.setAge(userAge);
//        db.setWeight(userWeight);
//        db.setGender(userGender);

        db.insertProfile(userName, userAge, userWeight, userGender);
        startActivity(new Intent(this, MainActivity.class));
    }

    public void cancelButtonClicked(View view){
        startActivity(new Intent(this, MainActivity.class));
    }
}
