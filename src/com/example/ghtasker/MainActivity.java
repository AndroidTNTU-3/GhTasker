package com.example.ghtasker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.ghtasker.DialogUser.DialogCallBack;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements DialogCallBack{
	
	DialogUser dialogUser;
	String url = ""; //AndroidTNTU-3
	String url_repo = "";
	String user = "";
    String email = "";
	
	TextView textViewName;
	TextView textViewAdress;
	ImageView image;
	ListView listView;
	Button button;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        dialogUser = new DialogUser();
        dialogUser.setDialogCallBack(this);
        
        textViewName = (TextView) findViewById(R.id.textLogin);
        textViewAdress = (TextView) findViewById(R.id.textViewAdress);
        image = (ImageView) findViewById(R.id.imageView1);
        image.setVisibility(View.INVISIBLE);
        button = (Button) findViewById(R.id.b_email);
        button.setOnClickListener(new MyListener());
        button.setVisibility(View.INVISIBLE);
        listView = (ListView) findViewById(R.id.listView1);
        
    }

    
    
    public class MyNetworkTask extends AsyncTask<String, Void, JSONObject> {

    	JSONObject result;
    	
		@Override
		protected JSONObject doInBackground(String... params) {
			HttpClient client = new DefaultHttpClient();
			//HttpPost post = new HttpPost(params[0]);
			HttpGet request = new HttpGet(params[0]);
			
			try {
				HttpResponse response = client.execute(request);
				HttpEntity entity = response.getEntity();
				
				if (entity != null){
					
					String responseBody = EntityUtils.toString(entity);
					Log.i("AAA:", responseBody);
					try {
						result = new JSONObject(responseBody);						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}
		
		@Override
	    protected void onPostExecute(JSONObject result) {
	      super.onPostExecute(result);
	      
	      String name = "";
	      String location = "";
	      String avatar_url = "";

	      
		try {
			name = result.getString("name");
			location = result.getString("location");
			avatar_url = result.getString("avatar_url");			
			email = result.getString("email");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	      textViewName.setText(name);
	      textViewAdress.setText(location);
	      if (email != null){
	    	  button.setVisibility(View.VISIBLE);
	      }
	      new ImageLoaderTasker().execute(avatar_url);
	      
	    }
    	
    }
    
    public class ImageLoaderTasker extends AsyncTask<String, Void, Bitmap> {
    	InputStream input;
    	@Override
    	protected Bitmap doInBackground(String... params) {

    		HttpGet httpRequest = null;

            httpRequest = new HttpGet(params[0]);

            HttpClient httpclient = new DefaultHttpClient();
           
    		try {
    			
    			 HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
    			 HttpEntity entity = response.getEntity();
    	         BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
    	         input = b_entity.getContent();
    	         
    		} catch (ClientProtocolException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            
            Bitmap bitmap = BitmapFactory.decodeStream(input);             		
    		return bitmap;
    	}
    	
    	@Override
	    protected void onPostExecute(Bitmap result) {
	      super.onPostExecute(result);
	      image.setImageBitmap(result);
	      image.setVisibility(View.VISIBLE);
	      
    	}

    }
    
    public class RepoLoaderTasker extends AsyncTask<String, Void, ArrayList<String>> {
    	InputStream input;
    	
    	ArrayList<String> array_repo = new ArrayList<String>();
    	
    	@Override
    	protected ArrayList<String> doInBackground(String... params) {

    		HttpClient client_repo = new DefaultHttpClient();
			HttpGet request_repo = new HttpGet(params[0]);
			
			
    		try {
    			StringBuilder builder = new StringBuilder();
    			HttpResponse response_repo = client_repo.execute(request_repo);
    			HttpEntity entity_repo = response_repo.getEntity();
    			
    			InputStream content = entity_repo.getContent();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
		        String line;
		        while ((line = reader.readLine()) != null) {
		          builder.append(line);
		        }
		        
				try {
					JSONArray jsonArray = new JSONArray(builder.toString());
					for (int i = 0; i < jsonArray.length(); i++) {
				          JSONObject jsonObject = jsonArray.getJSONObject(i);
				         array_repo.add(jsonObject.getString("name"));
				         // Log.i("AAA:", repo[i].toString());
				        }
					
				} catch (JSONException e) {
					e.printStackTrace();
				}				      
		        
    		} catch (ClientProtocolException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
                       		
    		return array_repo;
    	}
    	
    	@Override
	    protected void onPostExecute(ArrayList<String> result) {
	      super.onPostExecute(result);

	      if (result.size() !=0){
	    	  ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
	    	        R.layout.list_item, result);
	      
	    	  listView.setAdapter(adapter);
	      }else{
	    	  
	      listView.setEmptyView(findViewById(android.R.id.empty));
	      
	      }
	      
    	}    	

    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);     
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.item2:
        	dialogUser.show(getFragmentManager(), "dlg1");
            return true;      
        default:

        }
        return super.onOptionsItemSelected(item);
    }
    
    private class MyListener implements android.view.View.OnClickListener{

		@Override
		public void onClick(View v) {
			
			switch (v.getId()) {
	        case R.id.b_email:
	        		        	
	        	Intent intent = new Intent(android.content.Intent.ACTION_SEND);//, Uri.fromParts("mailto",email, null));
	        	intent.setType("text/plain");
	        	intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
	        	intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hi");
	        	intent.putExtra(android.content.Intent.EXTRA_TEXT, "Hi");
	        	
	        	startActivity(Intent.createChooser(intent,
	                    "Send Email..."));

	        break;
	            
	        default:

	        }
		}

		
    	
    }

	@Override
	public void setUser(String user) {
		this.user = user;
		url = "https://api.github.com/users/" + user;
		url_repo = "https://api.github.com/users/"  + user  + "/repos"; //AndroidTNTU-3
		new MyNetworkTask().execute(url);
		new RepoLoaderTasker().execute(url_repo);
	}
    
}
