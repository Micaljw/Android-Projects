package com.example.recipecart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ManualEntry extends Activity implements OnClickListener{

		Button sqlUpdate;
		EditText sqlName;//, sqlPantry;
		private Spinner spinner1;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_manual_entry);
			
			spinner1 = (Spinner) findViewById(R.id.spinner1);
		//	spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
			
		    sqlUpdate = (Button) findViewById(R.id.bSQLUpdate);
			sqlName = (EditText) findViewById(R.id.etSQLName);
			
			sqlUpdate.setOnClickListener(this);
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.search_recipes, menu);
			return true;
		}
		
		 @Override
			public void onClick(View arg0) {
				
				// TODO Auto-generated method stub
				switch (arg0.getId()){
					case R.id.bSQLUpdate:
						
						boolean didItWork = true;
						String userPantrySelection = String.valueOf(spinner1.getSelectedItem());
						
						try{
							String name = sqlName.getText().toString();
						
							DatabaseHelperTest entry = new DatabaseHelperTest(ManualEntry.this);
							
							entry.open();
							entry.createEntry(name, userPantrySelection);
							entry.close();
						}catch (Exception e){
							didItWork = false;
						}finally{
							if(didItWork){
								AlertDialog.Builder builder = new AlertDialog.Builder(this);
								builder.setMessage("Ingredient Successfully Added")
							       .setTitle("Recipe Cart");
								builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							           public void onClick(DialogInterface dialog, int id) {
											finish();
											startActivity(getIntent());
							           }
							       });
								AlertDialog dialog = builder.create();
								dialog.show();
							}
						}
						break;
				}
				
			}
	
}


