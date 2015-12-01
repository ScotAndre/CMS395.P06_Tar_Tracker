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
//    private DatabaseManager db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        setPreferences();

        // instantiate the BacCalculator and DatabaseManager
        bac = new BacCalculator(this);
//        db = new DatabaseManager( this );

        // instantiate the two TextViews
        tvDrinkCount = (TextView) findViewById(R.id.drink_count);
        tvBacLevel = (TextView) findViewById(R.id.bac_value);

        configSounds();

        //TODO: If userName in database is empty, call PersonalSettingsActivity
//        if(db.getName() == null){
//            setPersonalSettings(null);
//        }

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
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadGame(View view){
        startActivity(new Intent(getApplicationContext(), GameActivity.class));
    }

    public void refreshBac(View view){

    }

    public void setPersonalSettings(View view){
        startActivity(new Intent(getApplicationContext(), PersonalSettingsActivity.class));
    }

    public void onClickAddDrink(View view){
//        Toast.makeText(this, "Drink Button Clicked", Toast.LENGTH_LONG).show();
        bac.addDrink();
        int drinkCount = bac.getDrinkCount();
//        Toast.makeText(this, "# of Drinks = " + bac.getDrinkCount(), Toast.LENGTH_LONG).show();
        tvDrinkCount.setText("" + drinkCount);
        tvBacLevel.setText("" + bac.getBac());

        soundPool.play(soundBeerPour, 1, 1, 1, 0, 1.0f);

        TextView current_state = ( TextView )findViewById( R.id.current_state );

        if (drinkCount != 0 ) {
            if(bac.getBac() == 0.0 && bac.getDrinkCount() == 0){
                performAnimation(R.anim.spin);
                current_state.setText(R.string.sobriety_suprisingly_sober);
            } else if(bac.getBac() < bac.BAC_PER_SE_LIMIT_DEFAULT){
                performAnimation(R.anim.spin);
                current_state.setText(R.string.sobriety_tipsy);
            } else if(bac.getBac() >= bac.BAC_PER_SE_LIMIT_DEFAULT && bac.getBac() < bac.BAC_ENHANCED_LIMIT_DEFAULT){
                performAnimation(R.anim.spin);
                current_state.setText(R.string.sobriety_drunk);
            } else if(bac.getBac() >= bac.BAC_ENHANCED_LIMIT_DEFAULT && bac.getBac() < bac.BAC_ENHANCED_LIMIT_DEFAULT * 2){
                performAnimation(R.anim.combo);
                current_state.setText(R.string.sobriety_danger_drunk);
            } else if(bac.getBac() >= bac.BAC_ENHANCED_LIMIT_DEFAULT * 2){
                performAnimation(R.anim.combo);
                current_state.setText(R.string.sobriety_leathaly_drunk);
            }
//            switch (bac.getSobrietyLevel()){
//                case 0:
//                    performAnimation(R.anim.spin);
//                    current_state.setText(R.string.sobriety_suprisingly_sober);
//                    break;
//                case 1:
//                    // sober
//                    performAnimation(R.anim.spin);
//                    current_state.setText(R.string.sobriety_sober);
//                    break;
//                case 2:
//                    // tipsy
//                    performAnimation(R.anim.combo);
//                    current_state.setText(R.string.sobriety_tipsy);
//                    break;
//                case 3:
//                    // drunk
//                    performAnimation(R.anim.spin);
//                    current_state.setText(R.string.sobriety_drunk);
//                    break;
//                case 4:
//                    // dangerously drunk
//                    performAnimation(R.anim.combo);
//                    current_state.setText(R.string.sobriety_danger_drunk);
//                    break;
//                case 5:
//                    // leathly drunk
//                    performAnimation(R.anim.combo);
//                    current_state.setText(R.string.sobriety_leathaly_drunk);
//                    break;
//                case 6:
//                    // Error
//                    break;
//            }
        }
    }

    public void onClickResetButton(View view){
//        Toast.makeText(this, "Reset Button Clicked", Toast.LENGTH_LONG).show();
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

    class  MyAnimationListener implements Animation.AnimationListener{

        /**
         * <p>Notifies the start of the animation.</p>
         *
         * @param animation The started animation.
         */
        @Override
        public void onAnimationStart(Animation animation) {

        }

        /**
         * <p>Notifies the end of the animation. This callback is not invoked
         * for animations with repeat count set to INFINITE.</p>
         *
         * @param animation The animation which reached its end.
         */
        @Override
        public void onAnimationEnd(Animation animation) {

        }

        /**
         * <p>Notifies the repetition of the animation.</p>
         *
         * @param animation The animation which was repeated.
         */
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }// end MyAnimationListener class


    // *********************** Preferences **************************
    public void setPreferences(){
        try{
//            bac.setUnderageBacLimit(Double.parseDouble(mPrefs.getString("underage_bac_limit", bac.BAC_UNDERAGE_LIMIT_DEFAULT)));

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
