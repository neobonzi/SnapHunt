package com.example.snaphunt;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

	

public class GamesOverview extends Activity {
	
	protected LinearLayout gameView;
	 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_games_overview);
		View jokeView = new View(this.getBaseContext());
		View jokeView1 = new View(this.getBaseContext());
		View jokeView2 = new View(this.getBaseContext());
		View jokeView3 = new View(this.getBaseContext());
		View jokeView4 = new View(this.getBaseContext());
		
		

		jokeView.inflate(this, R.layout.layout_gameview,(ViewGroup) findViewById(R.id.games_overvew_games_list));
		jokeView1.inflate(this, R.layout.layout_gameview,(ViewGroup) findViewById(R.id.games_overvew_games_list));
		jokeView2.inflate(this, R.layout.layout_gameview,(ViewGroup) findViewById(R.id.games_overview_invitations_list));
		jokeView3.inflate(this, R.layout.layout_gameview,(ViewGroup) findViewById(R.id.games_overview_invitations_list));
		jokeView4.inflate(this, R.layout.layout_gameview,(ViewGroup) findViewById(R.id.games_overview_invitations_list));

		gameView = (LinearLayout) findViewById(R.id.games_overvew_games_list);
		gameView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.d("SnapHunt_GamesOverview","view clicked "+ arg0.getId());
				Intent intent = new Intent(getBaseContext(), Gameplay.class);
				intent.putExtra("Game_ID", arg0.getId());
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.games_overview, menu);
		return true;
	}
	
    public void startCreateGame(View view) {
        
        Intent intent = new Intent(this, CreateGame.class);
        startActivity(intent);
    }

}
