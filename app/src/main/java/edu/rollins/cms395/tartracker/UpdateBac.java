package edu.rollins.cms395.tartracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class UpdateBac extends AppCompatActivity {
    private BacCalculator bac;
    private TextView tvDrinkCount;
    private TextView tvBacLevel;
    private TextView tvCurrentState;
    private boolean stopThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bac);
        bac = new BacCalculator(this);
        tvDrinkCount = (TextView) findViewById(R.id.drink_count);
        tvBacLevel = (TextView) findViewById(R.id.bac_value);

        stopThread = false;

        Thread thread = new Thread();
    }

    private Runnable bacUpdate = new Runnable() {
        private static final int DELAY = 300000;
        @Override
        public void run() {
            while(!stopThread){
                try{
                    Thread.sleep(DELAY);
                } catch (InterruptedException ie){
                    ie.printStackTrace();
                }
                // Update stuff here

                if (bac.getDrinkCount() != 0 ) {
                    if (bac.getBac() == 0.0 && bac.getDrinkCount() == 0) {
//                        performAnimation(R.anim.spin);
                        tvCurrentState.setText(R.string.sobriety_suprisingly_sober);
                    } else if (bac.getBac() < bac.BAC_PER_SE_LIMIT_DEFAULT && bac.getDrinkCount() > 2) {
//                        performAnimation(R.anim.spin);
                        tvCurrentState.setText(R.string.sobriety_tipsy);
                    } else if (bac.getBac() >= bac.BAC_PER_SE_LIMIT_DEFAULT && bac.getBac() < bac.BAC_ENHANCED_LIMIT_DEFAULT) {
//                        performAnimation(R.anim.spin);
                        tvCurrentState.setText(R.string.sobriety_drunk);
                    } else if (bac.getBac() >= bac.BAC_ENHANCED_LIMIT_DEFAULT && bac.getBac() < bac.BAC_ENHANCED_LIMIT_DEFAULT * 3) {
//                        performAnimation(R.anim.combo);
                        tvCurrentState.setText(R.string.sobriety_danger_drunk);
                    } else if (bac.getBac() >= bac.BAC_ENHANCED_LIMIT_DEFAULT * 3) {
//                        performAnimation(R.anim.combo);
                        tvCurrentState.setText(R.string.sobriety_leathaly_drunk);
                    }
                }
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_bac, menu);
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
}
