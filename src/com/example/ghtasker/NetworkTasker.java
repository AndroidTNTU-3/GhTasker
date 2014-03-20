package com.example.ghtasker;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class NetworkTasker extends AsyncTask<String, Void, Object> {
	UserInfo userInfo;
	
	public static interface ParserCallBack{
		public void setInfo(Object info);
	}
	
	ParserCallBack parserCallBack;
	ParserInfo parserInfo;
	Object ob;
	
	@Override
	protected Object doInBackground(String... params) {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(params[0]);

		try {
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			
			if (entity != null){
				
				String responseBody = EntityUtils.toString(entity);

				try {
					parserInfo = new ParserInfo();
					//userInfo = parserInfo.parse(responseBody, params[1]);
					ob = parserInfo.parse(responseBody, params[1]);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ob;
	}
	
	@Override
    protected void onPostExecute(Object result) {
      super.onPostExecute(result);
      
      	if (parserCallBack != null){
		parserCallBack.setInfo(result);
		}
      	
	}
	
	public void setParserCallBack(ParserCallBack parserCallBack) {
		this.parserCallBack = parserCallBack;
	}

}
