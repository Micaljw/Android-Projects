package com.example.recipecart;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.Toast;

@SuppressLint("NewApi")
public class AdvancedSearch extends Activity {

	private CheckBox chkSearch;
	private Button btnSearch;
	private TableRow row;
	private LinearLayout root;
	private List<CheckBox> allBoxes = new ArrayList<CheckBox>();
	private String appID = "09fab903";
	private String appKey = "29f80e841f54b6bc71aa4df135caace7";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adv_search);

		root = (LinearLayout) findViewById(R.id.tvSQLAdvSearch);

		DatabaseHelperTest db = new DatabaseHelperTest(AdvancedSearch.this);
		db.open();
		db.getData("Meat", chkSearch, root, row, allBoxes);
		db.getData("Fats", chkSearch, root, row, allBoxes);
		db.getData("Fruit", chkSearch, root, row, allBoxes);
		db.getData("Grains", chkSearch, root, row, allBoxes);
		db.getData("Milk", chkSearch, root, row, allBoxes);
		db.getData("Vegetables", chkSearch, root, row, allBoxes);
		db.close();
		addListenerOnAdvancedSearch();
	}

	private void addListenerOnAdvancedSearch() {
		// TODO Auto-generated method stub
		btnSearch = (Button) findViewById(R.id.adv_button);

		btnSearch.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//ProgressBar progressBar = new ProgressBar(activity, null, android.R.attr.progressBarStyleSmall);
				
				String ingredients = "";
				String boxes;
				for (CheckBox cb : allBoxes) {
					if (cb.isChecked()) {
						boxes = (String) cb.getText();
						//ingredients += "allowedIngredient%5B%5D" + boxes.replaceAll("\\s", "%20") + "&";
						ingredients += boxes.replaceAll("\\s", "%20") + "+";
					}
				}
				
				ingredients = "q=" + ingredients;
				
				RetreiveRecipeTask my_task = new RetreiveRecipeTask() { };
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				    my_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (ingredients));
				else
				    my_task.execute(ingredients);
			}

		});

	}
	
	class RetreiveRecipeTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... arg0) {
			try {
				
				//progressBar.setActivated(true);
				HttpClient client = new DefaultHttpClient();
				String getURL = "http://api.yummly.com/v1/api/recipes?" + arg0[0];
				//String getURL = "http://api.yummly.com/v1/api/recipes?" + arg0[0];
				HttpGet get = new HttpGet(getURL);
				get.setHeader("X-Yummly-App-ID", appID);
				get.setHeader("X-Yummly-App-Key", appKey);
				HttpResponse responseGet = client.execute(get);
				HttpEntity resEntityGet = responseGet.getEntity();
				if (resEntityGet != null) {
					String response = EntityUtils.toString(resEntityGet);
					Log.i("GET RESPONSE", response);
					return response;
				} else {
					Toast.makeText(AdvancedSearch.this,
							"Null Response from Yummly", Toast.LENGTH_LONG)
							.show();
				}
			} catch (ClassCastException i){
				Toast.makeText(AdvancedSearch.this, i.getMessage(), Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				Toast.makeText(AdvancedSearch.this, "Exception on URL",
						Toast.LENGTH_LONG).show();
			} 
			return null;
		}
		
		@Override
		protected void onPostExecute(String sResponse) {
			//progressBar.setActivated(false);
			if(sResponse != null){
				Intent i = new Intent(getApplicationContext(),
						DisplayRecipe.class);
				Bundle b = new Bundle();
				b.putString("Recipe", sResponse);
				i.putExtra("Info", b);
				startActivity(i);
			}else{
				Toast.makeText(AdvancedSearch.this, "Returned null", Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_recipes, menu);
		return true;
	}

}
