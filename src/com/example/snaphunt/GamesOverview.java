package com.example.snaphunt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import android.widget.Toast;



public class GamesOverview extends Activity {

	protected LinearLayout gameView;
	protected int uid;
	protected int gameId;
	protected ArrayList<Game> games;
	protected ArrayList<Game> invites;
	protected String ServerRoot = "http://regal-airway-412.appspot.com/";
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
		games = new ArrayList<Game>();
		invites = new ArrayList<Game>();
		fillGames();
		fillInvites();
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

	private void invitesFilled() {

	}

	private void gamesFilled() {

	}

	private void fillInvites() {
		String url = ServerRoot + "getPlayerInvites?id=" + uid;
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
							try {
								JSONArray invitesJson = response.getJSONArray("invites");
								if(invitesJson.length() > 0) {
									Gson gson = new Gson();
									TypeToken<List<Game>> token = new TypeToken<List<Game>>(){};
									invites = gson.fromJson(invitesJson.toString(), token.getType());
								}
								invitesFilled();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}
				}, new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						Log.e("error",error.toString());
					}
				});
		queue.add(jsObjRequest);
	}

	private void fillGames() {
		String url = ServerRoot + "getPlayerGames?id=" + uid;
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
							try {
								JSONArray gamesJson = response.getJSONArray("games");
								if(gamesJson.length() > 0) {
									Gson gson = new Gson();
									TypeToken<List<Game>> token = new TypeToken<List<Game>>(){};
									games = gson.fromJson(gamesJson.toString(), token.getType());
								}
								gamesFilled();
							} catch (JSONException e) {
								e.printStackTrace();
							}
					}
				}, new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						Log.e("error",error.toString());
					}
				});
		queue.add(jsObjRequest);
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
