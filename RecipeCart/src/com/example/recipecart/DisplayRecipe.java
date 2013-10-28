package com.example.recipecart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class DisplayRecipe extends Activity{
	
	private String recipe = null;
	private List<CheckBox> allBoxes = new ArrayList<CheckBox>();
	private CheckBox chkRecipe;
	private TableRow row;
	private Button addFavorites;
	private TextView tv;
	private TableLayout layout;
	private String[] url = new String[20];
	HashMap<String, String> names;
	int count = 0;
	int myCounter = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_recipe);
		layout = (TableLayout)findViewById(R.id.LlDisp);
		tv = (TextView)findViewById(R.id.tvDisp);
		
		Intent i = getIntent();
		Bundle b = new Bundle();
		b = i.getBundleExtra("Info");
		recipe = b.getString("Recipe");
		//tv.setText(recipe);
		getKey(recipe);
		addListenerOnAddFavorites();
	}
	
	@SuppressLint("NewApi")
	public void getKey(String recipes){
		try{
			JSONObject jobject = new JSONObject(recipes);
			@SuppressWarnings("unchecked") //Legacy API
			Iterator<String> keys = jobject.keys();
			Map<String, String> map = new HashMap<String, String>();
			String matches;
			
			while(keys.hasNext()){
				String key = (String)keys.next();
				map.put(key, jobject.getString(key));
			}
			matches = map.get("matches");
			
			JSONArray jArr = new JSONArray(matches);
			names = new HashMap<String, String>();
		//	ScrollView contLayout = (ScrollView)findViewById(R.id.svDisp);
			
			for(int i = 0; i < jArr.length(); i++){
				String id = jArr.getJSONObject(i).getString("id");
				String recName = jArr.getJSONObject(i).getString("recipeName");
				names.put(id, recName);
			}
			
			int i = 0;
			//Iterator<String> it = names.values().iterator();
			//myRecipes = names.values().toString().replace(',', '\n');
			for(Map.Entry<String, String> entry : names.entrySet()){
				String key = entry.getKey();
				if(i <= 19){
					RetreiveSourceTask my_task = new RetreiveSourceTask() { };
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
						my_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (key));
					else
						my_task.execute(key);
					i++;
				}
				
			}
			
		} catch(JSONException e){
			Toast.makeText(DisplayRecipe.this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	class RetreiveSourceTask extends AsyncTask<String, Void, String[]>{
		private String appID = "09fab903";
		private String appKey = "29f80e841f54b6bc71aa4df135caace7";
		
		@Override
		protected String[] doInBackground(String... arg0) {
			try {
				String[] items = new String[2];
				HttpClient client = new DefaultHttpClient();
				String getURL = "http://api.yummly.com/v1/api/recipe/" + arg0[0];
				HttpGet get = new HttpGet(getURL);
				get.setHeader("X-Yummly-App-ID", appID);
				get.setHeader("X-Yummly-App-Key", appKey);
				HttpResponse responseGet = client.execute(get);
				HttpEntity resEntityGet = responseGet.getEntity();
				if (resEntityGet != null) {
					String response = EntityUtils.toString(resEntityGet);
					Log.i("GET RESPONSE", response);
					JSONObject recObj = new JSONObject(response);
					
					@SuppressWarnings("unchecked")
					Iterator<String> recKeys = recObj.keys();
					Map<String, String> recMap = new HashMap<String, String>();
					String recSource;
					while (recKeys.hasNext()) {
						String recKey = (String)recKeys.next();
						recMap.put(recKey, recObj.getString(recKey));
					}
					recSource = recMap.get("source");
					JSONObject srcObj = new JSONObject(recSource);
					
					@SuppressWarnings("unchecked")
					Iterator<String> srcKeys = srcObj.keys();
					Map<String, String> srcMap = new HashMap<String, String>();
					String srcURL;
					while (srcKeys.hasNext()) {
						String srcKey = (String)srcKeys.next();
						srcMap.put(srcKey, srcObj.getString(srcKey));
					}
					srcURL = srcMap.get("sourceRecipeUrl");
					items[0] = arg0[0];
					items[1] = srcURL;
					
					if(count <= 20){
						url[count] = srcURL;
						count++;
						if(count == 20)
							count = 0;
					} else
						count = 0;
					
					return items;
				} else {
					Toast.makeText(DisplayRecipe.this,
							"Null Response from Yummly", Toast.LENGTH_LONG)
							.show();
				}

			
			} catch (ClassCastException i){
				Toast.makeText(DisplayRecipe.this, i.getMessage(), Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				Toast.makeText(DisplayRecipe.this, "Exception on URL",
						Toast.LENGTH_LONG).show();
			} 
			return null;
		}
		
		@Override
		protected void onPostExecute(String[] sResponse) {
			if(sResponse != null){
				String recipeName = names.get(sResponse[0]);
				
				chkRecipe = new CheckBox(DisplayRecipe.this);
				row = new TableRow(DisplayRecipe.this);
				tv = new TextView(DisplayRecipe.this);
				chkRecipe.setTag(recipeName);
				tv.setText(Html.fromHtml("<a href=\"" + sResponse[1] + "\">" + recipeName + "</a>"));
				tv.setMovementMethod(LinkMovementMethod.getInstance());
				allBoxes.add(chkRecipe);
				row.addView(chkRecipe);
				row.addView(tv);
				layout.addView(row);
			}else{
				Toast.makeText(DisplayRecipe.this, "Returned null", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	private void addListenerOnAddFavorites() {
		
		// TODO Auto-generated method stub
		addFavorites = (Button)findViewById(R.id.btnFavorites);
		
		addFavorites.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int flag = 0;
				
				for(CheckBox cb : allBoxes){
					if(cb.isChecked()){
						flag = 1;
						DatabaseHelperTest item = new DatabaseHelperTest(DisplayRecipe.this);
						
						item.open();
						item.addToFavorites(cb.getTag().toString(), url[myCounter]);
						item.close();
					}
					myCounter++;
				}
				myCounter = 0;
				
				if(flag == 1){
					Toast.makeText(DisplayRecipe.this,
							"Successfully added to Favorites",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
