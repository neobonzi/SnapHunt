package com.example.snaphunt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;



public class GamesOverview extends Activity {

	protected ListView gameView;
	protected ListView gameInvitesView;

	protected int uid;
	protected int gameId;
	protected ArrayList<Game> games;
	protected ArrayList<Game> invites;
	protected String ServerRoot = "http://regal-airway-412.appspot.com/";
	protected RequestQueue queue;
	protected GamesOverviewAdapter gamesOverviewAdapter;
	protected GamesOverviewInvitesAdapter gamesOverviewInvitesAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_games_overview);


		Intent intent = getIntent();
		uid = intent.getIntExtra("uid", -1);
		queue = Volley.newRequestQueue(this);
		gameView = (ListView) findViewById(R.id.games_overvew_games_list);
		gameInvitesView = (ListView) findViewById(R.id.games_overview_invitations_list);
		games = new ArrayList<Game>();
		invites = new ArrayList<Game>();
		
		gamesOverviewAdapter = new GamesOverviewAdapter(this, games);
		gameView.setAdapter(gamesOverviewAdapter);
		
		gamesOverviewInvitesAdapter = new GamesOverviewInvitesAdapter(this, invites);
		gameInvitesView.setAdapter(gamesOverviewInvitesAdapter);
		
		fillGames();
		fillInvites();
		
		initListeners();
		

	}
	
	@Override
	protected void onResume(){
		super.onResume();
		if(games.size() > 0 && invites.size() > 0){
			gameView.removeAllViewsInLayout();
			gameInvitesView.removeAllViewsInLayout();
			fillGames();
			fillInvites();
		}
	}

	private void initListeners() {
		gameView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Log.d("SnapHunt_GamesOverview","view: "+ view + " pos: "+ position + "id: " + id);
				Log.d("SnapHunt_GamesOverview","gameID: " + games.get(position).getId());

				Intent intent = new Intent(getBaseContext(), Gameplay.class);
				intent.putExtra("gameId", games.get(position).getId());
				intent.putExtra("uid", uid);
				startActivity(intent);
				
			}

		});
		
		gameInvitesView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Log.d("SnapHunt_GamesOverview","view: "+ view + " pos: "+ position + "id: " + id);
				Log.d("SnapHunt_GamesOverview","gameID: " + invites.get(position).getId());

				Intent intent = new Intent(getBaseContext(), Gameplay.class);
				intent.putExtra("gameId", invites.get(position).getId());
				intent.putExtra("uid", uid);
				startActivity(intent);
				
			}

		});		
	}

	private void invitesFilled() {
		gamesOverviewInvitesAdapter.notifyDataSetChanged();
	}

	private void gamesFilled() {
		gamesOverviewAdapter.notifyDataSetChanged();
	}

	private void fillInvites() {
		invites.clear();
		String url = ServerRoot + "getPlayerInvites?id=" + uid;
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
							try {
								JSONArray invitesJson = response.getJSONArray("invites");
								if(invitesJson.length() > 0) {
//									Gson gson = new Gson();
//									TypeToken<List<Game>> token = new TypeToken<List<Game>>(){};
//									invites = gson.fromJson(invitesJson.toString(), token.getType());
									for(int i = 0; i < invitesJson.length();i++){
										JSONObject json = (JSONObject)invitesJson.getJSONObject(i);
										invites.add(new Game(json.getInt("id"),
												json.getString("groupName"),
												json.getString("theme"),
												json.getString("p1username"),
												json.getString("p2username"),
												json.getString("p3username"),
												json.getString("p4username")));
									}
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
		games.clear();
		String url = ServerRoot + "getPlayerGames?id=" + uid;
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
							try {
								JSONArray gamesJson = response.getJSONArray("games");
								Log.d("snaphunt", "json: " + gamesJson.toString());
								
								if(gamesJson.length() > 0) {
//									Gson gson = new Gson();
//									TypeToken<List<Game>> token = new TypeToken<List<Game>>(){};
//									games = gson.fromJson(gamesJson.toString(), token.getType());
									
									for(int i = 0; i < gamesJson.length();i++){
										JSONObject json = (JSONObject)gamesJson.getJSONObject(i);
										games.add(new Game(json.getInt("id"),
												json.getString("groupName"),
												json.getString("theme"),
												json.getString("p1username"),
												json.getString("p2username"),
												json.getString("p3username"),
												json.getString("p4username")));
									}
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
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

}
