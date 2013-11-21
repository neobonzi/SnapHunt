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
import android.widget.Toast;



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
		getPlayerGameId(uid);
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
		Toast.makeText(this, "launching next activity", Toast.LENGTH_SHORT).show();
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

	private void judgeCheck(int gameId) {
		String url = "http://75.128.20.108/snapAPI/checkIfJudge.php?uid="+uid+"&gameId="+gameId;
		this.gameId = gameId;
		Toast.makeText(this, "Checking if judge", Toast.LENGTH_SHORT).show();
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							Boolean isJudge = response.getBoolean("isJudge");
							if(isJudge){
								launchNextActivity(true);
							} else {
								launchNextActivity(false);
							}
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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

	private void setGameId(int gameId) {
		this.gameId = gameId;
	}

	private void getPlayerGameId(int uid) {
		Toast.makeText(this, "Trying to get player game id", Toast.LENGTH_SHORT).show();
		String url = "http://75.128.20.108/snapAPI/getPlayerGameId.php?id="+uid;
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							judgeCheck(Integer.parseInt((String)response.get("id")));
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
