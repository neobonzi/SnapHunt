package com.example.snaphunt;

import java.io.File;
import java.util.ArrayList;

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

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;



public class Gameplay extends Activity {

	ImageButton userPhotoButton;
	ArrayList<ImageButton> playerPhotoButtons;
	private int uid;
	private static final int PICTURE_REQUEST_CODE = 1;
	private int gameId;
	private RequestQueue queue;
	private ImageLoader imageLoader;
	private Bitmap photo = null;
	private Bitmap scaledPhoto;
	private Uri imageFileUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		uid = intent.getIntExtra("uid", -1);
		gameId = intent.getIntExtra("gameId", -1);

		initLayout();
		queue = Volley.newRequestQueue(this);
		findPlayersSubmittedNotJudges();
	}

	private void setImagesForPlayers(ArrayList<Integer> submittedIds) {
		Toast.makeText(this, "Setting images for all players", Toast.LENGTH_SHORT).show();
		/* Remove current user */
		boolean player1Set = false;
		for(Integer anId : submittedIds) {
			if(anId.equals(Integer.valueOf(uid))) {
				setPlayerImageActive(uid);
			}
			if(!anId.equals(Integer.valueOf(uid)) && !player1Set) {
				Log.d("Setting images", "uid is "+uid+"submittedId is "+anId);
				setPlayerImage1(anId);
				player1Set = true;
			} else {
				if(!anId.equals(Integer.valueOf(uid))) {
					Log.d("Setting images", "uid is "+uid+"submittedId is "+anId);
					setPlayerImage2(anId);
				}
			}
		}
	}

	private void setPlayerImageActive(Integer playerId) {
		String url = "http://75.128.20.108/SnapAPI/getPicsByGameId.php?gameId="+gameId+"&uid="+playerId;
		Toast.makeText(this, "Retreiving image for player" + playerId, Toast.LENGTH_SHORT).show();
		ImageRequest jsImgRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
	        @Override
	        public void onResponse(Bitmap response) {
	        	ImageButton playerActivePhoto = (ImageButton)findViewById(R.id.gameplay_user_photo_button);
	        	playerActivePhoto.setImageBitmap(response);
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
	        	ImageButton player1Photo = (ImageButton)findViewById(R.id.gameplay_player1);
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
		String url = "http://75.128.20.108/SnapAPI/getPicsByGameId.php?gameId="+gameId+"&uid="+playerId;
		Toast.makeText(this, "Retreiving image for player" + playerId, Toast.LENGTH_SHORT).show();
		ImageRequest jsImgRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
	        @Override
	        public void onResponse(Bitmap response) {
	        	ImageButton player2Photo = (ImageButton)findViewById(R.id.gameplay_player2);
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
		Toast.makeText(this, "Getting player status", Toast.LENGTH_SHORT);
		/* First get all player ids who have submitted and are not judges */
		String url = "http://75.128.20.108/snapAPI/getPlayerSubmitted.php?&gameId="+gameId;
		Toast.makeText(this, "Checking if judge", Toast.LENGTH_SHORT).show();
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							JSONArray jsonArray = response.getJSONArray("ids");
							ArrayList<Integer> submittedIds = new ArrayList<Integer>();
							JSONObject idObjects = jsonArray.getJSONObject(0);
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

	private void initLayout(){
		setContentView(R.layout.layout_gameplay);
		userPhotoButton = (ImageButton) findViewById(R.id.gameplay_user_photo_button);
		playerPhotoButtons = new ArrayList<ImageButton>();
		String photoButtonView = "R.id.gameplay_player1";
		int id, i = 1;
		while( (id = getResources().getIdentifier(photoButtonView, "id", getPackageName())) > 0){
			playerPhotoButtons.add((ImageButton) findViewById(id));
			photoButtonView.replace(Integer.toString(i), Integer.toString(++i));
		}

		initAddListeners();

	}

	private void initAddListeners(){
		userPhotoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				  imageFileUri = getOutputMediaFileUri();
				  intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
				  Log.d("SnapHunt", "Taking picture: requested " + imageFileUri);
				  startActivityForResult(intent, PICTURE_REQUEST_CODE);

			}
		});

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gameplay, menu);
		return true;
	}
    public void startRoundSummary(View view) {

        Intent intent = new Intent(this, RoundSummary.class);
        startActivity(intent);
    }

    @Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data){
			Log.d(Gameplay.class.getName(),"request Code: " + requestCode + ". ResultCode: "+ resultCode );

			if(requestCode == PICTURE_REQUEST_CODE && resultCode == RESULT_OK){
					assert(imageFileUri != null);

						Bitmap photo = BitmapFactory.decodeFile(imageFileUri.getPath());
						scaledPhoto = Bitmap.createScaledBitmap(photo, 640, 480, false);
						if(photo != null){
							photo.recycle();
						}

					if(scaledPhoto != null){
						userPhotoButton.setAdjustViewBounds(true);
						userPhotoButton.setImageBitmap(scaledPhoto);
						userPhotoButton.setBackgroundColor(Color.TRANSPARENT);
					}

			}
	}

    private Uri getOutputMediaFileUri(){

		File file = getOutputMediaFile(this);
		return Uri.fromFile(file);

	}
	private File getOutputMediaFile(Context ctx){
		File cache, mediaFile;

	    // SD Card Mounted
	    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
	      cache = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
	              "/Android/data/" + ctx.getPackageName() + "/cache/");
	    }
	    // Use internal storage
	    else {
	      cache = ctx.getCacheDir();
	    }

		if (!cache.exists()){
			cache.mkdirs();
		}

		mediaFile = new File(cache.getPath(), "SnapHuntTempPhoto.jpg");

		return mediaFile;
	}

}
