package com.example.snaphunt;

import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.snaphunt.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Welcome extends Activity {
	String ServerRoot = "http://75.128.20.108/";
	String APIPath = "snapAPI/";
	String scriptPath = ServerRoot + APIPath;
	URL basePath;
	int gameId;
	Integer uid;
	boolean isJudge = true;
	RequestQueue queue;

	EditText m_username;
	EditText m_password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_welcome);

        /* Set the view elements */
        m_username = ((EditText)findViewById(R.id.loginUsername));
        m_password = ((EditText)findViewById(R.id.loginPassword));
        queue = Volley.newRequestQueue(this);
        Button loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				String username = m_username.getText().toString();
				String password = m_password.getText().toString();
				login(username, password);
			}
        });
    }

    /** Called when the user clicks the Registration text */
    public void startRegistration(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void routeJudge() {
    	isJudge = true;
    	winnerPickedCheck();
    }

    public void routePlayer() {
    	isJudge = false;
    	winnerPickedCheck();
    }

    public void routeUser(Integer uid) {
    	this.uid = uid;
    	getPlayerGameId();
    }

    public void winnerPickedCheck() {
    	String url = "http://75.128.20.108/snapAPI/checkJudgePicked.php?gameId="+gameId;
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							Boolean judgePicked = response.getBoolean("picked");
							if(judgePicked){
								Intent intent = new Intent(getBaseContext(), RoundSummary.class);
								intent.putExtra("uid", uid);
								intent.putExtra("gameId", gameId);
								startActivity(intent);
							} else if (isJudge) {
								Intent intent = new Intent(getBaseContext(), GameplayJudge.class);
								intent.putExtra("uid", uid);
								intent.putExtra("gameId", gameId);
								startActivity(intent);
							} else {
								Intent intent = new Intent(getBaseContext(), Gameplay.class);
								intent.putExtra("uid", uid);
								intent.putExtra("gameId", gameId);
								startActivity(intent);
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
								routeJudge();
							} else {
								routePlayer();
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

	private void getPlayerGameId() {
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

    /* Returns the userid given a username and password */
    public void login(String username, String password) {
    	String url = scriptPath+"login.php?username="+username+"&password="+password;
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							routeUser(Integer.parseInt((String)response.get("id")));
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

    /** Called when the user clicks the Login Button */
    public void startGamesOverview(View view) {

        Intent intent = new Intent(this, GamesOverview.class);
        startActivity(intent);
    }
}
