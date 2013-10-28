package com.example.recipecart;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.Toast;

@SuppressLint("InlinedApi")
public class Meat extends Activity{
	
	private CheckBox chkMeat;
	private Button btnDelete;
	private TableRow row;
	private LinearLayout root;
	private List<CheckBox> allBoxes = new ArrayList<CheckBox>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meat);
		
		root = (LinearLayout)findViewById(R.id.tvSQLmeat);
		
		DatabaseHelperTest db = new DatabaseHelperTest(Meat.this);
		db.open();
		db.getData("Meat", chkMeat, root, row, allBoxes);
		db.close();
		addListenerOnChkMeat();
	}

	private void addListenerOnChkMeat() {
		
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
						DatabaseHelperTest item = new DatabaseHelperTest(Meat.this);
						
						item.open();
						item.deleteItem("Meat", (String) cb.getText());
						item.close();
					}
				}
				
				if(flag == 1){
					Toast.makeText(Meat.this,
							"Successfully removed ingredients", Toast.LENGTH_LONG).show();
					finish();
					startActivity(getIntent());
				}
					
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_ingredients, menu);
		return true;
	}
}
