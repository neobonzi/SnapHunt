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
	Integer uid;
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

    /* Returns the userid given a username and password */
    public void login(String username, String password) {
    	String url = scriptPath+"login.php?username="+username+"&password="+password;
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Intent intent = new Intent(getBaseContext(), GamesOverview.class);
						try {
							intent.putExtra("uid", Integer.parseInt((String)response.get("id")));
							startActivity(intent);
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
