package com.example.ghtasker;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ParserInfo {
		
	public Object parse(String data, String type) throws JSONException{
		
		
		if (type == "user"){
			
			UserInfo userInfo = new UserInfo();
			JSONObject jObject = new JSONObject(data);	
			
			userInfo.setName(jObject.getString("name"));
			userInfo.setLocation(jObject.getString("location"));
			userInfo.setAvatar_url(jObject.getString("avatar_url"));
			userInfo.setEmail(jObject.getString("email"));
			
			return userInfo;
			
		}else{
				RepoInfo repoInfo = new RepoInfo();
				ArrayList<String> array_repo = new ArrayList<String>();
				
				JSONArray array = new JSONArray(data);
				array_repo = new ArrayList<String>();
			
				for (int i = 0; i < array.length(); i++) {
					JSONObject jsonObject = array.getJSONObject(i);
					array_repo.add(jsonObject.getString("name"));
					
		    }
			
			repoInfo.setArray(array_repo);	
			
			return repoInfo;
		}
				
		
	}
	
}
