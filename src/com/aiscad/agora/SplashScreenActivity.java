package com.aiscad.agora;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

public class SplashScreenActivity extends Activity {

    protected int _splashTime = 2000; 

    private Thread splashTread;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        getWindow().setWindowAnimations(android.R.style.Animation_Toast);
        // thread for displaying the SplashScreen
        splashTread = new Thread() {
            @Override
            public void run() {
                try {                   
                    synchronized(this){
                        wait(_splashTime);
                    }

                } catch(InterruptedException e) {} 
                finally {

                    if(!isFinishing()) {
						 Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
						 startActivity(i);
						 finish();
                     }
                }
            }
        };

        splashTread.start();
    }
   
    @Override
    protected void onPause() {

        super.onPause();

        if(splashTread.getState()==Thread.State.TIMED_WAITING){
            //Thread is still waiting and Activity is paused. Means user has pressed Home. Bail out
            finish();
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

    }
}