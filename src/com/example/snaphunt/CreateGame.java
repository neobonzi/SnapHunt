package com.example.snaphunt;

import java.util.ArrayList;

import org.json.JSONArray;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class CreateGame extends Activity {

	int uid;
	String ServerRoot = "http://regal-airway-412.appspot.com/";
	ImageButton p1PickBtn;
	ImageButton p2PickBtn;
	ImageButton p3PickBtn;
	Button p4PickBtn;
	RequestQueue queue;
	TextView enterNameHeader;
	EditText p1uname;
	EditText p2uname;
	EditText p3uname;
	EditText createGameField;
	EditText usernameField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_create_game);
		queue = Volley.newRequestQueue(this);
		Intent intent = getIntent();
		uid = intent.getIntExtra("uid", -1);
		initLayout();
		initListeners();
	}

	private void initLayout() {
		createGameField = (EditText)findViewById(R.id.create_game_group_name_text);
		enterNameHeader = (TextView)findViewById(R.id.create_game_enter_username);
		enterNameHeader.setVisibility(View.GONE);

		p1PickBtn = (ImageButton)findViewById(R.id.create_game_player1);
		p2PickBtn = (ImageButton)findViewById(R.id.create_game_player2);
		p3PickBtn = (ImageButton)findViewById(R.id.create_game_player3);		
		p1PickBtn.setBackgroundColor(getResources().getColor(R.color.grey));
		p2PickBtn.setBackgroundColor(getResources().getColor(R.color.grey));
		p3PickBtn.setBackgroundColor(getResources().getColor(R.color.grey));
		
		p1uname = (EditText)findViewById(R.id.create_game_p1uname);
		p2uname = (EditText)findViewById(R.id.create_game_p2uname);
		p3uname = (EditText)findViewById(R.id.create_game_p3uname);
		p1uname.setVisibility(View.GONE);
		p2uname.setVisibility(View.GONE);
		p3uname.setVisibility(View.GONE);
	}

	private void initListeners() {
		p1PickBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				enterNameHeader.setVisibility(View.VISIBLE);
				enterNameHeader.setText("Player 1 Username");
				p1uname.setVisibility(View.VISIBLE);
				p2uname.setVisibility(View.GONE);
				p3uname.setVisibility(View.GONE);
				p1PickBtn.setBackgroundColor(getResources().getColor(R.color.login_button_background));
				p2PickBtn.setBackgroundColor(getResources().getColor(R.color.grey));
				p3PickBtn.setBackgroundColor(getResources().getColor(R.color.grey));
			}
		});
		p2PickBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				enterNameHeader.setVisibility(View.VISIBLE);
				enterNameHeader.setText("Player 2 Username");
				p1uname.setVisibility(View.GONE);
				p2uname.setVisibility(View.VISIBLE);
				p3uname.setVisibility(View.GONE);
				p1PickBtn.setBackgroundColor(getResources().getColor(R.color.grey));
				p2PickBtn.setBackgroundColor(getResources().getColor(R.color.login_button_background));
				p3PickBtn.setBackgroundColor(getResources().getColor(R.color.grey));
			}
		});
		p3PickBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				enterNameHeader.setVisibility(View.VISIBLE);
				enterNameHeader.setText("Player 3 Username");
				p1uname.setVisibility(View.GONE);
				p2uname.setVisibility(View.GONE);
				p3uname.setVisibility(View.VISIBLE);
				p1PickBtn.setBackgroundColor(getResources().getColor(R.color.grey));
				p2PickBtn.setBackgroundColor(getResources().getColor(R.color.grey));
				p3PickBtn.setBackgroundColor(getResources().getColor(R.color.login_button_background));
			}
		});
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_game, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void startNewGame(String p1uname, String p2uname, String p3uname) {
		String url = ServerRoot + "createGame?groupName=" + createGameField.getText().toString() + "&uid="+ uid +"&p1=" + p1uname + "&p2=" + p2uname + "&p3=" + p3uname;
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							Intent intent = new Intent(getBaseContext(),Gameplay.class);
							intent.putExtra("gameId", response.getInt("gameId"));
							intent.putExtra("uid", uid);
							startActivity(intent);
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

	private void verifyPlayers() {
		String p1UnameTxt = p1uname.getText().toString();
		String p2UnameTxt = p2uname.getText().toString();
		String p3UnameTxt = p3uname.getText().toString();
		
		String url = ServerRoot + "verifyPlayers?p1=" + p1UnameTxt + "&p2=" + p2UnameTxt + "&p3=" + p3UnameTxt;
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							boolean p1Valid = response.getBoolean("p1Valid");
							boolean p2Valid = response.getBoolean("p2Valid");
							boolean p3Valid = response.getBoolean("p3Valid");
							p1PickBtn.setBackgroundColor(getResources().getColor(R.color.ok_green));
							p2PickBtn.setBackgroundColor(getResources().getColor(R.color.ok_green));
							p3PickBtn.setBackgroundColor(getResources().getColor(R.color.ok_green));
							
							if(p1Valid && p2Valid && p3Valid) {
								Toast.makeText(getBaseContext(), "Great! Starting Game!", Toast.LENGTH_SHORT).show();
								startNewGame(p1uname.getText().toString(),
										p2uname.getText().toString(),
										p3uname.getText().toString());
							} else {
								String players = "";
								if(!p1Valid) {
									players += p1uname.getText().toString() + " ";
									//Toast.makeText(getBaseContext(),  p1uname.getText().toString() + " couldn't be found", Toast.LENGTH_SHORT).show();
									p1PickBtn.setBackgroundColor(getResources().getColor(R.color.grey));
								}

								if(!p2Valid) {
									players += p2uname.getText().toString() + " ";
									//Toast.makeText(getBaseContext(),  p2uname.getText().toString() + " couldn't be found", Toast.LENGTH_SHORT).show();
									p2PickBtn.setBackgroundColor(getResources().getColor(R.color.grey));

								}

								if(!p3Valid) {
									players += p3uname.getText().toString() + " ";
									//Toast.makeText(getBaseContext(),  p3uname.getText().toString() + " couldn't be found", Toast.LENGTH_SHORT).show();
									p3PickBtn.setBackgroundColor(getResources().getColor(R.color.grey));

								}
								
								Toast.makeText(getBaseContext(), "Users not registered: " + players, Toast.LENGTH_SHORT).show();

							}
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

	public void startThemePicker (View view){
		verifyPlayers();
	}


}
