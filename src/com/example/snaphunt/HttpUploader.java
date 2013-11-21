package com.example.snaphunt;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

public class HttpUploader extends AsyncTask<String, Void, String>{
	String output = null;
	@Override
	protected String doInBackground(String... path) {
		
        for (String sdPath : path) {

		
        Bitmap photo = BitmapFactory.decodeFile(sdPath);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
         
        //Resize the image
        double width = photo.getWidth();
        double height = photo.getHeight();
        double ratio = 640/width;
        int newheight = (int)(ratio*height);
         
        System.out.println("ÑÑÑ-width" + width);
        System.out.println("ÑÑÑ-height" + height);
         
        photo = Bitmap.createScaledBitmap(photo, 640, newheight, true);
		
		photo.compress(Bitmap.CompressFormat.JPEG, 95, bao);
        byte[] ba = bao.toByteArray();
        String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);

        Log.d(Gameplay.class.getName(), "uploading image--- " + ba1);

        
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("image",ba1) );
         
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("75.128.20.108/snapapi/postimage.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
             
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();                

            // print response
            String outPut = EntityUtils.toString(entity);
            Log.i(Gameplay.class.getName(),"Server response- " + outPut);
             
            photo.recycle();
             
        } catch (Exception e) {
            Log.d(Gameplay.class.getName(), "Error in http connection " + e.toString());
            e.printStackTrace();
        }	
	}
		return output;
	}

}
