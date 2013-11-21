package com.example.snaphunt;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

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
	protected int uid;
	protected int gameId;
	protected RequestQueue queue;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_games_overview);
		View jokeView = new View(this.getBaseContext());
		jokeView.inflate(this, R.layout.layout_gameview,(ViewGroup) findViewById(R.id.games_overvew_games_list));

		Intent intent = getIntent();
		uid = intent.getIntExtra("uid", -1);
		queue = Volley.newRequestQueue(this);
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

	private void launchNextActivity(boolean isJudge) {
		Intent intent;
		if(isJudge) {
			intent = new Intent(getBaseContext(), GameplayJudge.class);
			intent.putExtra("uid", uid);
			intent.putExtra("gameId", gameId);
			startActivity(intent);
		} else {
			intent = new Intent(getBaseContext(), Gameplay.class);
			intent.putExtra("uid", uid);
			intent.putExtra("gameId", gameId);
			startActivity(intent);
		}
	}

	private void setGameId(int gameId) {
		this.gameId = gameId;
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
