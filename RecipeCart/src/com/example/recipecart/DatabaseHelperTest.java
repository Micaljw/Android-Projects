package com.example.recipecart;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

@SuppressLint("NewApi")
public class DatabaseHelperTest {
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_LINK = "link";
	
	private static final String DATABASE_NAME = "RecipeCart";
	private static final String DATABASE_TABLE = "Fats";
	private static final String DATABASE_TABLE_2 = "Milk";
	private static final String DATABASE_TABLE_3 = "Meat";
	private static final String DATABASE_TABLE_4 = "Vegetables";
	private static final String DATABASE_TABLE_5 = "Fruit";
	private static final String DATABASE_TABLE_6 = "Grains";
	private static final String DATABASE_TABLE_7 = "Favorites";
	private static final int DATABASE_VERSION = 1;
	
	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
	
	private static class DbHelper extends SQLiteOpenHelper{

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" +
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_NAME + " TEXT NOT NULL);"
			);
			
			db.execSQL("CREATE TABLE " + DATABASE_TABLE_2 + " (" +
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_NAME + " TEXT NOT NULL);"
			);
			
			db.execSQL("CREATE TABLE " + DATABASE_TABLE_3 + " (" +
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_NAME + " TEXT NOT NULL);"
			);
			
			db.execSQL("CREATE TABLE " + DATABASE_TABLE_4 + " (" +
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_NAME + " TEXT NOT NULL);"
			);
			
			db.execSQL("CREATE TABLE " + DATABASE_TABLE_5 + " (" +
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_NAME + " TEXT NOT NULL);"
			);
			
			db.execSQL("CREATE TABLE " + DATABASE_TABLE_6 + " (" +
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_NAME + " TEXT NOT NULL);"
			);
			
			db.execSQL("CREATE TABLE " + DATABASE_TABLE_7 + " (" +
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_NAME + " TEXT NOT NULL, " +
					KEY_LINK + " TEXT NOT NULL);"
			);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_2);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_3);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_4);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_5);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_6);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_7);
			onCreate(db);
		}
		
	}
	
	public DatabaseHelperTest(Context cursor){
		ourContext = cursor;
	}
	
	public DatabaseHelperTest open() throws SQLException{
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		ourHelper.close();
	}

	public long createEntry(String name, String userPantrySelection){
		// TODO Auto-generated method stub
		
		//ourHelper.onUpgrade(ourDatabase, DATABASE_VERSION, DATABASE_VERSION+1);
		
		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, name);
		
		return ourDatabase.insert(userPantrySelection, null, cv);
	}

	public void getData(String category, CheckBox myBox, LinearLayout features, TableRow row, List<CheckBox> allBoxes) {
		
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID, KEY_NAME};
		
		Cursor c = ourDatabase.query(category, columns, null, null, null, null, null);
		
		int iName = c.getColumnIndex(KEY_NAME);
		int iD = 1;
		
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			myBox = new CheckBox(ourContext);
			row = new TableRow(ourContext);
			myBox.setText(c.getString(iName));
			myBox.setTextColor(Color.WHITE);
			myBox.setId(iD++);
			allBoxes.add(myBox);
			row.addView(myBox);
			features.addView(row);
		}
		iD=1;
	}

	public void deleteItem(String table, String item) {	
		ourDatabase.delete(table, KEY_NAME + "='" + item + "'", null);
	}

	public long addToFavorites(String recipeName, String recipeUrl) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, recipeName.replaceAll("\\'", "\\s"));
		cv.put(KEY_LINK, recipeUrl);
		
		return ourDatabase.insert("Favorites", null, cv);
	}

	public void getFavorites(TextView tv, CheckBox chkFav, TableLayout root, TableRow row, List<CheckBox> allBoxes) {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID, KEY_NAME, KEY_LINK};
		
		Cursor c = ourDatabase.query("Favorites", columns, null, null, null, null, null);
		
		int iName = c.getColumnIndex(KEY_NAME);
		int iLink = c.getColumnIndex(KEY_LINK);
		
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			chkFav = new CheckBox(ourContext);
			row = new TableRow(ourContext);
			tv = new TextView(ourContext);
			chkFav.setTag(c.getString(iName));
			tv.setText(Html.fromHtml("<a href=\"" + c.getString(iLink) + "\">" + c.getString(iName) + "</a>"));
			tv.setMovementMethod(LinkMovementMethod.getInstance());
			allBoxes.add(chkFav);
			row.addView(chkFav);
			row.addView(tv);
			root.addView(row);
		}
	}
}
