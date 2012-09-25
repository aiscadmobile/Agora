package com.aiscad.agora.sqlite;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aiscad.agora.objects.LocationProfile;
import com.google.android.maps.GeoPoint;


public class SqliteManager implements DataBaseConstants {
	
	SQLiteDatabase db;
	Context context;
	
	public SqliteManager(Context context) {
		this.context = context;
		OpenHelper oh = new OpenHelper(context);
		db = oh.getWritableDatabase();
	}
		
	
	public int getLastInsertId(String tablename) {
	    int index = 0;
	    Cursor cursor = db.query(
	            "sqlite_sequence",
	            new String[]{"seq"},
	            "name = ?",
	            new String[]{tablename},
	            null,
	            null,
	            null,
	            null
	    );
	    if (cursor.moveToFirst()) {
	        index = cursor.getInt(cursor.getColumnIndex("seq"));
	    }
	    cursor.close();
	    return index;
	}
	
	//LOCATION
	public void addLocationProfile (LocationProfile lProfile){

		Log.i("SqliteManager","Addding in OuputQueue...");
//		int isFavourite=0;
//		if (lProfile.isFavourite()){
//			isFavourite=1;
//		}
		db.execSQL("INSERT INTO " + TABLE_LOCATION
				+ " ( " 
				+ ROW_LOCATION_NAME + " ," 
				+ ROW_LOCATION_LATITUDE + " ," 
				+ ROW_LOCATION_LONGITUDE + " , "
				+ ROW_LOCATION_RANGE +" , " 
//				+ ROW_LOCATION_FAVOURITE +" , " 
				+ ROW_LOCATION_ADDRESS + 
				" ) "
				+ " VALUES ( "
				+ "'" +lProfile.getName() + "', "
				+ "" + lProfile.getGeoPoint().getLatitudeE6() + ", "
				+ "" + lProfile.getGeoPoint().getLongitudeE6()   + ", "
				+ "" + lProfile.getRange()  + ", "
//				+ "" + isFavourite +  ", "
				+ "'" + lProfile.getAddress() + "'"+
				") ");

		lProfile.setId(getLastInsertId(TABLE_LOCATION));
	}
	

	public ArrayList<LocationProfile> getLocationList(){
		//creamos array de registros
		ArrayList<LocationProfile> locationList = new ArrayList<LocationProfile>();
		
		//Creamos un cursor que es una llamada a la bbdd con un SELECT campos fROM...
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LOCATION + " ORDER BY " +  ROW_LOCATION_ID + " DESC"	, null);
		if (cursor.moveToFirst()) {
			do {
				
				//creamos las variables del registro de la base de datos
				int id = cursor.getInt(0);
				int latitude =cursor.getInt(cursor.getColumnIndex(ROW_LOCATION_LATITUDE));
				int longitude = cursor.getInt(cursor.getColumnIndex(ROW_LOCATION_LONGITUDE));
				int range = cursor.getInt(cursor.getColumnIndex(ROW_LOCATION_RANGE));
				String address = cursor.getString(cursor.getColumnIndex(ROW_LOCATION_ADDRESS));

				LocationProfile lProfile = new LocationProfile(id, new GeoPoint(latitude, longitude));
				lProfile.setRange(range);
				lProfile.setAddress(address);
				locationList.add(lProfile);

			} while (cursor.moveToNext());
		}
		
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return locationList;
	}
	

	public void updateLocation(LocationProfile lprofile){
		int isFavourite=0;

		db.execSQL("UPDATE "+TABLE_LOCATION + " SET "+ ROW_LOCATION_RANGE +"="+lprofile.getRange()+ ", "+ ROW_LOCATION_NAME +"= '"+lprofile.getName()+"' WHERE " + ROW_LOCATION_ID + " = " + lprofile.getId() );
	}
	
	public void removeLocation(int id){
		db.execSQL("DELETE FROM "+TABLE_LOCATION + " WHERE " + ROW_LOCATION_ID + " = " + id );
	}

	
	public void close(){
		if (db!=null){
			db.close();
		}
	}
	
	public void checkDB(){
		
		if (!db.isOpen()){
			OpenHelper openHelper = new OpenHelper(this.context);
			this.db = openHelper.getWritableDatabase();
		}
		
	}
	
	//Clase controladora de la bbdd internta
	public class OpenHelper extends SQLiteOpenHelper {

		
		
		public OpenHelper(Context context) {
			
			super(context, DB_NAME, null, DB_VERSION);
			// TODO Auto-generated constructor stub
			
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE if not exists " + TABLE_LOCATION + "("
					+ ROW_LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ ROW_LOCATION_NAME + " TEXT ," 
					+ ROW_LOCATION_LATITUDE + " INTEGER ," 
					+ ROW_LOCATION_LONGITUDE + " INTEGER ,"
					+ ROW_LOCATION_RANGE + " INTEGER ,"
					+ ROW_LOCATION_ADDRESS + " TEXT "
					+ ")");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}
		
		
	}

}
