package com.example.snaphunt;

import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
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
	String ServerRoot = "http://regal-airway-412.appspot.com/";
	URL basePath;
	int gameId;
	int uid;
	RequestQueue queue;

	Button m_loginButton;
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
        m_loginButton = (Button)findViewById(R.id.loginButton);
        m_username = ((EditText)findViewById(R.id.loginUsername));
        m_password = ((EditText)findViewById(R.id.loginPassword));
        queue = Volley.newRequestQueue(this);
        Button loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				String username = m_username.getText().toString();
				String password = m_password.getText().toString();
				m_loginButton.setClickable(false);
				login(username, password);
				
			}
        });
    }

    /** Called when the user clicks the Registration text */
    public void startRegistration(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    

    private void startGamesOverview() {
		m_loginButton.setClickable(true);

        Intent intent = new Intent(this, GamesOverview.class);
        intent.putExtra("uid", this.uid);
        startActivity(intent);
    }

    
    /* Returns the userid given a username and password */
    public void login(String username, String password) {
    	String url = ServerRoot + "login?username="+username+"&password="+password;
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							uid = response.getInt("id");
							startGamesOverview();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getBaseContext(), "Invalid username/password", Toast.LENGTH_LONG).show();
						m_loginButton.setClickable(true);

					}
				});
		queue.add(jsObjRequest);
    }


}
