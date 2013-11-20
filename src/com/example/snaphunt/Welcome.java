package com.example.snaphunt;

import java.net.URL;

import com.example.snaphunt.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


public class Welcome extends Activity {

	URL basePath;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.layout_welcome);
    }
    
    /** Called when the user clicks the Registration text */
    public void startRegistration(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
    
    /** Called when the user clicks the Login Button */
    public void startGamesOverview(View view) {
    
        Intent intent = new Intent(this, GamesOverview.class);
        startActivity(intent);
    }
}
