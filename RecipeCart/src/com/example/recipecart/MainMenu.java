package com.example.recipecart;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainMenu extends Activity{
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    
    public void addIngredients(View v) {
		startActivity(new Intent(this, AddIngredients.class));
	}
    
    public void searchRecipes(View v) {
		startActivity(new Intent(this, SearchRecipe.class));
	}
    
    public void managePantry(View v) {
		startActivity(new Intent(this, ManagePantry.class));
	}
    
    public void manageFavorites(View v) {
		startActivity(new Intent(this, ManageFavorites.class));
	}
    
}

