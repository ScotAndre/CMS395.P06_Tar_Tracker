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
    private TextView tvCurrentState;


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
        tvCurrentState = (TextView) findViewById(R.id.current_state);

        configSounds();

//        UpdateBac ubac = new UpdateBac();
//        ubac.startActivity(
        updateDisplay(null);

        //TODO: If userName in database is empty, call PersonalSettingsActivity
//        if(db.getName().equals("")){
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
        bac.addDrink();
        soundPool.play(soundBeerPour, 1, 1, 1, 0, 1.0f);

        // keep this to maintain the animations
        if (bac.getDrinkCount() != 0 ) {
            if(bac.getBac() == 0.0 && bac.getDrinkCount() == 0){
                performAnimation(R.anim.spin);
                tvCurrentState.setText(R.string.sobriety_suprisingly_sober);
            } else if(bac.getBac() < bac.BAC_PER_SE_LIMIT_DEFAULT && bac.getDrinkCount() > 2){
                performAnimation(R.anim.spin);
                tvCurrentState.setText(R.string.sobriety_tipsy);
            } else if(bac.getBac() >= bac.BAC_PER_SE_LIMIT_DEFAULT && bac.getBac() < bac.BAC_ENHANCED_LIMIT_DEFAULT){
                performAnimation(R.anim.spin);
                tvCurrentState.setText(R.string.sobriety_drunk);
            } else if(bac.getBac() >= bac.BAC_ENHANCED_LIMIT_DEFAULT && bac.getBac() < bac.BAC_ENHANCED_LIMIT_DEFAULT * 3){
                performAnimation(R.anim.combo);
                tvCurrentState.setText(R.string.sobriety_danger_drunk);
            } else if(bac.getBac() >= bac.BAC_ENHANCED_LIMIT_DEFAULT * 3){
                performAnimation(R.anim.combo);
                tvCurrentState.setText(R.string.sobriety_leathaly_drunk);
            }
        }
    }

    public void updateDisplay(View view){
        tvDrinkCount.setText(Integer.toString(bac.getDrinkCount()));
        tvBacLevel.setText(Double.toString(bac.getBac()));

        // no animations here
        if (bac.getDrinkCount() != 0 ) {
            if(bac.getBac() == 0.0 && bac.getDrinkCount() == 0){
                tvCurrentState.setText(R.string.sobriety_suprisingly_sober);
            } else if(bac.getBac() < bac.BAC_PER_SE_LIMIT_DEFAULT && bac.getDrinkCount() > 2){
                tvCurrentState.setText(R.string.sobriety_tipsy);
            } else if(bac.getBac() >= bac.BAC_PER_SE_LIMIT_DEFAULT && bac.getBac() < bac.BAC_ENHANCED_LIMIT_DEFAULT){
                tvCurrentState.setText(R.string.sobriety_drunk);
            } else if(bac.getBac() >= bac.BAC_ENHANCED_LIMIT_DEFAULT && bac.getBac() < bac.BAC_ENHANCED_LIMIT_DEFAULT * 3){
                tvCurrentState.setText(R.string.sobriety_danger_drunk);
            } else if(bac.getBac() >= bac.BAC_ENHANCED_LIMIT_DEFAULT * 3){
                tvCurrentState.setText(R.string.sobriety_leathaly_drunk);
            }
        }
    }

    public void onClickResetButton(View view){
//        Toast.makeText(this, "Reset Button Clicked", Toast.LENGTH_LONG).show();
        bac.resetDrinkCounter();
        tvDrinkCount.setText(Integer.toString(bac.getDrinkCount()));
        tvBacLevel.setText(Double.toString(bac.getBac()));
        tvCurrentState.setText(R.string.sobriety_suprisingly_sober);
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
//            bac.setUnderageBacLimit(Double.parseDouble(mPrefs.getString("underage_bac_limit", Double.toString(bac.BAC_UNDERAGE_LIMIT_DEFAULT))));
//            bac.setPerSeBacLimit(Double.parseDouble(mPrefs.getString("per_se_bac_limit", Double.toString(bac.BAC_PER_SE_LIMIT_DEFAULT))));
//            bac.setEnhancedBacLimit(Double.parseDouble(mPrefs.getString("enhanced_bac_limit", Double.toString(bac.BAC_ENHANCED_LIMIT_DEFAULT))));
//            bac.setLegalDrinkingAge(Integer.parseInt(mPrefs.getString("drinking_age", Integer.toString(bac.LEGAL_AGE_DEFAULT))));

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

//        mPrefsListener = new SharedPreferences.OnSharedPreferenceChangeListener(){
//
//            /**
//             * Called when a shared preference is changed, added, or removed. This
//             * may be called even if a preference is set to its existing value.
//             * <p/>
//             * <p>This callback will be run on your main thread.
//             *
//             * @param sharedPreferences The {@link SharedPreferences} that received
//             *                          the change.
//             * @param key               The key of the preference that was changed, added, or
//             */
//            @Override
//            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//                if(key.equals("underage_bac_limit")){
//                    bac.setUnderageBacLimit(Double.parseDouble(mPrefs.getString("underage_bac_limit", Double.toString(bac.BAC_UNDERAGE_LIMIT_DEFAULT))));
//                }
//                if(key.equals("per_se_bac_limit")){
//                    bac.setPerSeBacLimit(Double.parseDouble(mPrefs.getString("per_se_bac_limit", Double.toString(bac.BAC_PER_SE_LIMIT_DEFAULT))));
//                }
//                if(key.equals("enhanced_bac_limit")){
//                    bac.setEnhancedBacLimit(Double.parseDouble(mPrefs.getString("enhanced_bac_limit", Double.toString(bac.BAC_ENHANCED_LIMIT_DEFAULT))));
//                }
//                if(key.equals("drinking_age")){
//                    bac.setLegalDrinkingAge(Integer.parseInt(mPrefs.getString("drinking_age", Integer.toString(bac.LEGAL_AGE_DEFAULT))));
//                }
//            }
//        };
    }

    public void launchUpdateBac(View view){
        startActivity(new Intent(getApplicationContext(), UpdateBac.class));
    }
}
