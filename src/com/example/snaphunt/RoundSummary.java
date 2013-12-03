package com.example.snaphunt;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class RoundSummary extends Activity {
	String ServerRoot = "http://regal-airway-412.appspot.com/";
	ImageButton winPic;
	ImageButton pic1;
	ImageButton pic2;
	Button contBtn;
	TextView themeField;
	private int uid;
	private int gameId;
	ArrayList<Integer> playerIds;
	RequestQueue queue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_round_summary);
		themeField = ((TextView)findViewById(R.id.round_summary_theme));
		Intent intent = getIntent();
		uid = intent.getIntExtra("uid", -1);
		gameId = intent.getIntExtra("gameId", -1);
		queue = Volley.newRequestQueue(this);
		pic1 = (ImageButton)findViewById(R.id.round_summary_player1);
		pic2 = (ImageButton)findViewById(R.id.round_summary_player2);
		winPic = (ImageButton)findViewById(R.id.round_summary_winner);
		contBtn= ((Button)findViewById(R.id.round_summary_done_button));
		contBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				contPressed();
			}
		});

		findPlayersByGameId();
		setTheme();
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

	private void notifyWaiting() {
		Toast.makeText(this, "Waiting for all players to be ready", Toast.LENGTH_SHORT).show();
	}

	private void contPressed() {
		String url = ServerRoot + "contPressed?gameId="+gameId+"&uid="+uid;
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							boolean restart = response.getBoolean("restart");
							if(restart) {
								initNextRound();
							} else {
								notifyWaiting();
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


	private void setImages(Integer winnerId) {
		Toast.makeText(this, "Setting images for all players", Toast.LENGTH_SHORT).show();
		/* Remove current user */
		boolean player1Set = false;
		for(Integer anId : playerIds) {
			if(anId.equals(winnerId)) {
				setPlayerImageWinner(anId);
			}else if(!player1Set) {
				player1Set = true;
				setPlayerImage1(anId);
			}else {
				setPlayerImage2(anId);
			}
		}
	}

	private void setPlayerImage2(Integer playerId) {
		String url = ServerRoot + "getPicsByGameId?gameId="+gameId+"&uid="+playerId;
		Toast.makeText(this, "Retreiving image for player" + playerId, Toast.LENGTH_SHORT).show();
		ImageRequest jsImgRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
	        @Override
	        public void onResponse(Bitmap response) {
	        	pic2.setImageBitmap(response);
	        	pic2.setAdjustViewBounds(true);
	        	pic2.setBackgroundColor(Color.TRANSPARENT);
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
		Toast.makeText(this, "Retreiving image for player" + playerId, Toast.LENGTH_SHORT).show();
		ImageRequest jsImgRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
	        @Override
	        public void onResponse(Bitmap response) {
	        	pic1.setImageBitmap(response);
	        	pic1.setAdjustViewBounds(true);
	        	pic1.setBackgroundColor(Color.TRANSPARENT);
	        }
	    }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
	        @Override
	        public void onErrorResponse(VolleyError error) {
	            Log.e("error", error.toString());
	        }
	    });
		queue.add(jsImgRequest);
	}

	private void setPlayerImageWinner(Integer playerId) {
		String url = ServerRoot + "getPicsByGameId?gameId="+gameId+"&uid="+playerId;
		ImageRequest jsImgRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
	        @Override
	        public void onResponse(Bitmap response) {
	        	winPic.setImageBitmap(response);
	        	winPic.setAdjustViewBounds(true);
	        	winPic.setBackgroundColor(Color.TRANSPARENT);
	        }
	    }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
	        @Override
	        public void onErrorResponse(VolleyError error) {
	            Log.e("error", error.toString());
	        }
	    });
		queue.add(jsImgRequest);
	}

	private void initNextRound() {
		String url = ServerRoot + "resetRound?gameId="+gameId+"&uid="+uid;
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							boolean isJudge = response.getBoolean("isJudge");
							if(isJudge) {
								Intent intent = new Intent(getBaseContext(), GameplayJudge.class);
								intent.putExtra("uid",uid);
								intent.putExtra("gameId", gameId);
								startActivity(intent);
							}else {
								Intent intent = new Intent(getBaseContext(), Gameplay.class);
								intent.putExtra("uid",uid);
								intent.putExtra("gameId", gameId);
								startActivity(intent);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
					}
				});
		queue.add(jsObjRequest);
	}

	private void getWinner(ArrayList<Integer> playerIds) {
		this.playerIds = playerIds;
		/* First get all player ids who have submitted and are not judges */
		String url = ServerRoot + "getWinner?gameId="+gameId;
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							Integer winner = response.getInt("judgeChoice");
							setImages(winner);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
					}
				});
		queue.add(jsObjRequest);
	}

	private void findPlayersByGameId() {
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
							getWinner(submittedIds);
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
		getMenuInflater().inflate(R.menu.round_summary, menu);
		return true;
	}
	public void showGamesOverview(View v){
        Intent intent = new Intent(this, GamesOverview.class);
        startActivity(intent);
	}
}
