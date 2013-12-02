package com.example.snaphunt;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class GameplayJudge extends Activity {
	String ServerRoot = "http://regal-airway-412.appspot.com/";
	private int uid;
	private int gameId;
	private RequestQueue queue;
	private ImageLoader imageLoader;
	private TextView themeField;
	private HashMap<Integer,Integer> idsToPics;
	private Integer judgePick;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_gameplay_judge);
		themeField = ((TextView)findViewById(R.id.gameplay_judge_current_theme));
		idsToPics = new HashMap<Integer,Integer>();
		setListeners();
		Intent intent = getIntent();
		judgePick = Integer.valueOf(4);
		uid = intent.getIntExtra("uid", -1);
		gameId = intent.getIntExtra("gameId", -1);
		queue = Volley.newRequestQueue(this);
		setTheme();
		findPlayersSubmittedNotJudges();
	}
	private void updateTheme(String theme) {
		themeField.setText(theme);
	}

	private void setTheme() {
		/* First get all player ids who have submitted and are not judges */
		String url = ServerRoot + "getTheme?gameId="+gameId;
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							updateTheme(response.getString("theme"));
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
	private void setListeners() {
		ImageButton player1pic = (ImageButton)findViewById(R.id.gameplay_judge_player1);
		ImageButton player2pic = (ImageButton)findViewById(R.id.gameplay_judge_player2);
		ImageButton player3pic = (ImageButton)findViewById(R.id.gameplay_judge_player3);

		player1pic.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				judgePick = idsToPics.get(Integer.valueOf(1));
				setJudgePick(idsToPics.get(Integer.valueOf(1)));
			}});

		player2pic.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				judgePick = idsToPics.get(Integer.valueOf(2));
				setJudgePick(idsToPics.get(Integer.valueOf(2)));
			}});

		player3pic.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				judgePick = idsToPics.get(Integer.valueOf(3));
				setJudgePick(judgePick);
			}});
	}

	private void postJudgePick() {
		Intent intent = new Intent(this,RoundSummary.class);
		intent.putExtra("gameId",gameId);
		intent.putExtra("uid",uid);
		startActivity(intent);
	}

	private void setJudgePick(Integer judgePick) {
		String url = ServerRoot + "judgePick?gameId="+gameId+"&winnerId="+judgePick;
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						postJudgePick();
					}
				}, new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						Log.e("error",error.toString());
					}
				});
		queue.add(jsObjRequest);
	}

	private void setImagesForPlayers(ArrayList<Integer> submittedIds) {
		/* Remove current user */
		boolean player1Set = false;
		boolean player2Set = false;
		for(Integer anId : submittedIds) {
			if(!player1Set) {
				player1Set = true;
				idsToPics.put(Integer.valueOf(1), anId);
				setPlayerImage1(anId);
			}else if(!player2Set) {
				player2Set = true;
				idsToPics.put(Integer.valueOf(2), anId);
				setPlayerImage2(anId);
			}else {
				idsToPics.put(Integer.valueOf(3), anId);
				setPlayerImage3(anId);
			}
		}
	}

	private void setPlayerImage3(Integer playerId) {
		String url = ServerRoot + "getPicsByGameId?gameId="+gameId+"&uid="+playerId;
		ImageRequest jsImgRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
	        @Override
	        public void onResponse(Bitmap response) {
	        	ImageButton player3Photo = (ImageButton)findViewById(R.id.gameplay_judge_player3);
	        	player3Photo.setImageBitmap(response);
	        }
	    }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
	        @Override
	        public void onErrorResponse(VolleyError error) {
	            Log.e("error", error.toString());
	        }
	    });
		queue.add(jsImgRequest);
	}

	private void setPlayerImage1(Integer playerId) {
		String url = ServerRoot + "getPicsByGameId?gameId="+gameId+"&uid="+playerId;
		ImageRequest jsImgRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
	        @Override
	        public void onResponse(Bitmap response) {
	        	ImageButton player1Photo = (ImageButton)findViewById(R.id.gameplay_judge_player1);
	        	player1Photo.setImageBitmap(response);
	        }
	    }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
	        @Override
	        public void onErrorResponse(VolleyError error) {
	            Log.e("error", error.toString());
	        }
	    });
		queue.add(jsImgRequest);
	}

	private void setPlayerImage2(Integer playerId) {
		String url = ServerRoot + "getPicsByGameId?gameId="+gameId+"&uid="+playerId;
		ImageRequest jsImgRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
	        @Override
	        public void onResponse(Bitmap response) {
	        	ImageButton player2Photo = (ImageButton)findViewById(R.id.gameplay_judge_player2);
	        	player2Photo.setImageBitmap(response);
	        }
	    }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
	        @Override
	        public void onErrorResponse(VolleyError error) {
	            Log.e("error", error.toString());
	        }
	    });
		queue.add(jsImgRequest);
	}

	private void findPlayersSubmittedNotJudges() {
		/* First get all player ids who have submitted and are not judges */
		String url = ServerRoot + "getPlayerSubmitted?&gameId="+gameId;
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							JSONArray jsonArray = response.getJSONArray("ids");
							ArrayList<Integer> submittedIds = new ArrayList<Integer>();
							for(int i = 0; i < jsonArray.length(); i++) {
								submittedIds.add(jsonArray.getJSONObject(i).getInt("id"));
							}
							setImagesForPlayers(submittedIds);
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
		getMenuInflater().inflate(R.menu.gameplay_judge, menu);
		return true;
	}

	public void showRoundSummary(View v){
        Intent intent = new Intent(this, RoundSummary.class);
        startActivity(intent);
	}
}
