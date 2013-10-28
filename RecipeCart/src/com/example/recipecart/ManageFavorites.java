package com.example.recipecart;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ManageFavorites extends Activity {
	
	private CheckBox chkFav;
	private Button btnDelete;
	private TextView tv;
	private TableRow row;
	private TableLayout root;
	private List<CheckBox> allBoxes = new ArrayList<CheckBox>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites);
		
		root = (TableLayout)findViewById(R.id.tvSQLfavorites);
		tv = (TextView)findViewById(R.id.tvFavDisp);
		
		DatabaseHelperTest db = new DatabaseHelperTest(ManageFavorites.this);
		db.open();
		db.getFavorites(tv, chkFav, root, row, allBoxes);
		db.close();
		addListenerOnDeleteFavorites();
	}

	private void addListenerOnDeleteFavorites() {
		// TODO Auto-generated method stub
		btnDelete = (Button)findViewById(R.id.btnDelete);
		
		btnDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int flag = 0;
				
				for(CheckBox cb : allBoxes){
					if(cb.isChecked()){
						flag = 1;
						DatabaseHelperTest item = new DatabaseHelperTest(ManageFavorites.this);
						
						item.open();
						item.deleteItem("Favorites", cb.getTag().toString());
						item.close();
					}
				}
				
				if(flag == 1){
					Toast.makeText(ManageFavorites.this,
							"Successfully Removed From Favorites",
							Toast.LENGTH_LONG).show();
					finish();
					startActivity(getIntent());
				}
			}});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manage_favorites, menu);
		return true;
	}
}
