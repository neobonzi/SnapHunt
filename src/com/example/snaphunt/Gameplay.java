package com.example.snaphunt;

import java.util.ArrayList;

import android.os.Bundle;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;



public class Gameplay extends Activity {

	ImageButton userPhotoButton;
	ArrayList<ImageButton> playerPhotoButtons;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLayout();
	}

	private void initLayout(){
		setContentView(R.layout.layout_gameplay);
		userPhotoButton = (ImageButton) findViewById(R.id.gameplay_user_photo_button);
		playerPhotoButtons = new ArrayList<ImageButton>();
		String photoButtonView = "R.id.gameplay_player1";
		int id, i = 1;
		while( (id = getResources().getIdentifier(photoButtonView, "id", getPackageName())) > 0){
			playerPhotoButtons.add((ImageButton) findViewById(id));
			photoButtonView.replace(Integer.toString(i), Integer.toString(++i));
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gameplay, menu);
		return true;
	}
    public void startRoundSummary(View view) {
        
        Intent intent = new Intent(this, RoundSummary.class);
        startActivity(intent);
    }

}
