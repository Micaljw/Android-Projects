package com.example.recipecart;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class ManagePantry extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_pantry);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manage_pantry, menu);
		return true;
	}
	
	public void fats(View v) {
		startActivity(new Intent(this, Fats.class));
	}
	
	public void milk(View v) {
		startActivity(new Intent(this, Milk.class));
	}
	
	public void meat(View v) {
		startActivity(new Intent(this, Meat.class));
	}
	
	public void vegetables(View v) {
		startActivity(new Intent(this, Vegetables.class));
	}
	
	public void fruit(View v) {
		startActivity(new Intent(this, Fruit.class));
	}
	
	public void grains(View v) {
		startActivity(new Intent(this, Grains.class));
	}

}
