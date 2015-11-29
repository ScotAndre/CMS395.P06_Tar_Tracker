package edu.rollins.cms395.tartracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences mPrefs;
    private SharedPreferences.OnSharedPreferenceChangeListener mPrefsListener;
    private BacCalculator bac;
    private TextView tvDrinkCount;
    private TextView tvBacLevel;


    private SoundPool soundPool;
    private int soundBeerPour;
    private DatabaseManager db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        setPreferences();

        // instantiate the BacCalculator and DatabaseManager
        bac = new BacCalculator(this);
        db = new DatabaseManager( this );

        // instantiate the two TextViews
        tvDrinkCount = (TextView) findViewById(R.id.drink_count);
        tvBacLevel = (TextView) findViewById(R.id.bac_value);

        configSounds();
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

    public void loadGame(View view){
        startActivity(new Intent(getApplicationContext(), GameActivity.class));
    }

    public void onClickAddDrink(View view){
        Toast.makeText(this, "Drink Button Clicked", Toast.LENGTH_LONG).show();
        bac.addDrink();
        Toast.makeText(this, "# of Drinks = " + bac.getDrinkCount(), Toast.LENGTH_LONG).show();
        tvDrinkCount.setText("" + bac.getDrinkCount());
        tvBacLevel.setText("" + bac.getBac());

        soundPool.play(soundBeerPour, 1, 1, 1, 0, 1.0f);

        TextView current_state = ( TextView )findViewById( R.id.current_state );

        if ( bac.getDrinkCount() != 0 ) {

            if ( bac.getBac() < .02 ){
                performAnimation(R.anim.spin);
                current_state.setText( "Suprisingly Sober");
            }
            else if ( ( bac.getBac() >= .02 ) /* && ( db.getAge() < 21 ) */){
                performAnimation(R.anim.combo);
                current_state.setText( "YOUR DRUNK KID!");
            }
            else if ( bac.getBac() >= .08 ){
                performAnimation(R.anim.spin);
                current_state.setText( "Your kinda drunk");
            }
            else if ( bac.getBac() >= .15 ){
                performAnimation(R.anim.combo);
                current_state.setText( "YOUR BEYOND DRUNK!");
            }
        }
    }

    public void onClickResetButton(View view){
        Toast.makeText(this, "Reset Button Clicked", Toast.LENGTH_LONG).show();
        bac.resetDrinkCounter();
        tvDrinkCount.setText("" + bac.getDrinkCount());
        tvBacLevel.setText("" + bac.getBac());

    }

    private void configSounds( ){
        SoundPool.Builder spb = new SoundPool.Builder( );
        spb.setMaxStreams( 1 );
        soundPool = spb.build( );
        soundBeerPour = soundPool.load( this, R.raw.beer_pour, 1 );
    }

    public void startRotate ( View v ) {
        performAnimation(R.anim.spin);
    }


    private void performAnimation( int animationResourceID ) {
        Animation an = AnimationUtils.loadAnimation(this, animationResourceID);
        an.setAnimationListener(new MyAnimationListener());
        TextView current_state = ( TextView )findViewById( R.id.current_state );
        current_state.startAnimation(an);
    }

    class MyAnimationListener implements Animation.AnimationListener {

        public void onAnimationStart( Animation animation ) { }

        public void onAnimationEnd( Animation animation ) { }

        public void onAnimationRepeat( Animation animation ) { }
    }

    // *********************** Preferences **************************
    public void setPreferences(){
        try{

        } catch (NumberFormatException nfe){
            // reset the default in case user entered a number that was too large
            SharedPreferences.Editor sharedEditor = mPrefs.edit();

            sharedEditor.putString("underage_bac_limit", String.valueOf(bac.BAC_UNDERAGE_LIMIT_DEFAULT));
            sharedEditor.putString("per_se_bac_limit", String.valueOf(bac.BAC_PER_SE_LIMIT_DEFAULT));
            sharedEditor.putString("enhanced_bac_limit", String.valueOf(bac.BAC_ENHANCED_LIMIT_DEFAULT));
            sharedEditor.putString("drinking_age", String.valueOf(bac.LEGAL_AGE_DEFAULT));

            bac.setUnderageBacLimit(bac.BAC_UNDERAGE_LIMIT_DEFAULT);
            bac.setPerSeBacLimit(bac.BAC_PER_SE_LIMIT_DEFAULT);
            bac.setEnhancedBacLimit(bac.BAC_ENHANCED_LIMIT_DEFAULT);
            bac.setLegalDrinkingAge(bac.LEGAL_AGE_DEFAULT);
        }
    }
}
