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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class RoundSummary extends Activity {

	ImageButton winPic;
	ImageButton pic1;
	ImageButton pic2;
	private int uid;
	private int gameId;
	ArrayList<Integer> playerIds;
	RequestQueue queue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_round_summary);
		Intent intent = getIntent();
		uid = intent.getIntExtra("uid", -1);
		gameId = intent.getIntExtra("gameId", -1);
		queue = Volley.newRequestQueue(this);
		pic1 = (ImageButton)findViewById(R.id.round_summary_player1);
		pic2 = (ImageButton)findViewById(R.id.round_summary_player2);
		winPic = (ImageButton)findViewById(R.id.round_summary_winner);
		findPlayersByGameId();
	}


	private void setImages(Integer winnerId) {
		Toast.makeText(this, "Setting images for all players", Toast.LENGTH_SHORT).show();
		/* Remove current user */
		boolean player1Set = false;
		for(Integer anId : playerIds) {
			if(anId.equals(winnerId)) {
				player1Set = true;
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
		String url = "http://75.128.20.108/SnapAPI/getPicsByGameId.php?gameId="+gameId+"&uid="+playerId;
		Toast.makeText(this, "Retreiving image for player" + playerId, Toast.LENGTH_SHORT).show();
		ImageRequest jsImgRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
	        @Override
	        public void onResponse(Bitmap response) {
	        	pic2.setImageBitmap(response);
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
		String url = "http://75.128.20.108/SnapAPI/getPicsByGameId.php?gameId="+gameId+"&uid="+playerId;
		Toast.makeText(this, "Retreiving image for player" + playerId, Toast.LENGTH_SHORT).show();
		ImageRequest jsImgRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
	        @Override
	        public void onResponse(Bitmap response) {
	        	pic1.setImageBitmap(response);
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
		String url = "http://75.128.20.108/SnapAPI/getPicsByGameId.php?gameId="+gameId+"&uid="+playerId;
		ImageRequest jsImgRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
	        @Override
	        public void onResponse(Bitmap response) {
	        	winPic.setImageBitmap(response);
	        }
	    }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
	        @Override
	        public void onErrorResponse(VolleyError error) {
	            Log.e("error", error.toString());
	        }
	    });
		queue.add(jsImgRequest);
	}

	private void getWinner(ArrayList<Integer> playerIds) {
		this.playerIds = playerIds;
		/* First get all player ids who have submitted and are not judges */
		String url = "http://75.128.20.108/snapAPI/getWinner.php?&gameId="+gameId;
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							Integer judgeChoice = Integer.parseInt((String)response.get("judgeChoice"));
							setImages(judgeChoice);
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

	private void findPlayersByGameId() {
		/* First get all player ids who have submitted and are not judges */
		String url = "http://75.128.20.108/snapAPI/getPlayerSubmitted.php?&gameId="+gameId;
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
