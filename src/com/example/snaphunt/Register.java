package com.example.snaphunt;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
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
import android.widget.Toast;

public class Register extends Activity {
	EditText username;
	EditText password;
	EditText passwordVerify;
	RequestQueue queue;
	EditText registerEmail;
	String ServerRoot = "http://regal-airway-412.appspot.com/";
	String validEmailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+" +
			")*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$;";
	String validUsernameRegex = "^[A-Za-z0-9_]+$";
	String validPasswordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,15}$";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        queue = Volley.newRequestQueue(this);
        setContentView(R.layout.layout_register);
        username = (EditText)findViewById(R.id.registerUsername);
        password = (EditText)findViewById(R.id.registerPassword);
        passwordVerify = (EditText)findViewById(R.id.registerVerifyPassword);
        registerEmail = (EditText)findViewById(R.id.registerEmail);
    }

    private boolean checkDuplicateUsername(String username) {
    	boolean returnVal = false;
    	RequestFuture<JSONObject> future = RequestFuture.newFuture();
    	JsonObjectRequest request = new JsonObjectRequest(Method.POST, ServerRoot + "uniqueUsername?username=" + username,null,future,future);
    	queue.add(request);

    	try {
			JSONObject response = future.get();
			Integer isValid;
			isValid = response.getInt("valid");
			returnVal = isValid.equals(Integer.valueOf(1));
    	} catch (JSONException e) {
    		e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
    	return returnVal;
    }

    private boolean validRegistration(String username, String password, String verif, String email) {
    	boolean usernameValid = true;
    	boolean passwordValid = true;
    	boolean emailValid = true;
        Pattern p = Pattern.compile(validUsernameRegex);
        Matcher m  = p.matcher(username);
    	//Validate username
    	if(username.length() < 5 || username.length() > 13) {
    		usernameValid = false;
    		Toast.makeText(this, "Username must be longer than 5 and shorter than 14 Characters.", Toast.LENGTH_LONG).show();
    	}else if(!m.matches()) {
    		Toast.makeText(this, "Username must be alphanumeric with underscores.", Toast.LENGTH_LONG).show();
    		usernameValid = false;
    	}
//    	}else if(!checkDuplicateUsername(username)) {
//    		Toast.makeText(this, "Username already exists!", Toast.LENGTH_LONG).show();
//    		usernameValid = false;
//    	}

    	p = Pattern.compile(validPasswordRegex);
    	m = p.matcher(password);
    	if(!password.equals(verif)) {
    		passwordValid = false;
    		Toast.makeText(this, "Password and verification must match", Toast.LENGTH_LONG).show();
    	}else if(!m.matches()) {
    		passwordValid = false;
    		Toast.makeText(this, "Password must be 8 to 15 alphanumeric characters, have 1 uppercase, and 1 number", Toast.LENGTH_LONG).show();
    	}

    	return usernameValid && passwordValid && emailValid;
    }

    private void launchGamesOverview(int id) {
    	Intent intent = new Intent(this, GamesOverview.class);
    	intent.putExtra("uid", id);
        startActivity(intent);
    }

    public void registerSubmit(View view) {
    	String userTxt = username.getText().toString();
    	String passTxt = password.getText().toString();
    	String passVerifTxt = passwordVerify.getText().toString();
    	String emailTxt = registerEmail.getText().toString();
    	Button submitBtn = (Button)view;
    	submitBtn.setEnabled(false);
    	boolean validInfo = validRegistration(userTxt, passTxt, passVerifTxt, emailTxt);

    	if (validInfo) {
    		Toast.makeText(this, "Thanks! Creating User", Toast.LENGTH_LONG).show();
    		String url = ServerRoot + "register?username=" + userTxt
    				+ "&password=" + passTxt
    				+ "&email=" + emailTxt;
    		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
    				new Response.Listener<JSONObject>() {
    					@Override
    					public void onResponse(JSONObject response) {
    						try {
    							launchGamesOverview(Integer.parseInt((String)response.get("id")));
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
    	} else {
    		submitBtn.setEnabled(true);
    	}
    }
}
