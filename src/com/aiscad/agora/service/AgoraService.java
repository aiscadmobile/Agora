package com.aiscad.agora.service;

import java.util.ArrayList;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.RemoteViews;

import com.aiscad.agora.IdeesActivity;
import com.aiscad.agora.R;
import com.aiscad.agora.RangeMapActivity;
import com.aiscad.agora.objects.LocationProfile;
import com.aiscad.agora.sqlite.SqliteManager;

//import com.stkdevlopers.homeunlock.tools.InteractFile;


public class AgoraService extends Service implements LocationListener {
	
	
	public static int OFFSET_METERS_STANDAR = RangeMapActivity.OFFSET_METERS_STANDAR;
	public static int DISTANCE_REFRESH_LOCATION = 0; // in mettes
	public static int TIME_REFRESH_LOCATION = 0; //
	public static String ACTION_WIDGET = "ACTION.WIDGET";

	private LocationManager locationManager;   
	private NotificationManager mNotificationManager;
	private Notification notification;
	
	private SharedPreferences prefs;
	private Editor editor;

	static String TAG = "Service";
	private boolean inlocate;
	private LocationProfile locationProfile;
	
	@Override
	public void onCreate() {
		super.onCreate();
//		Log.i("SERVICE", "onCreate ... ");

		prefs = getSharedPreferences("preferences", Context.MODE_PRIVATE);
		editor = prefs.edit();
	
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME_REFRESH_LOCATION, DISTANCE_REFRESH_LOCATION, this);
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		setNotification("Nova proposta a la zona", "Un ciutada aa enviat una proposta a la teva zona. ");
	
	}
	
	
	
	private void setNotification(CharSequence tickerText, CharSequence contentText){
		
		long when = System.currentTimeMillis();
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(AgoraService.this,IdeesActivity.class), 0);
		Context context = getApplicationContext();
		CharSequence contentTitle = "Home Unlock PRO";

		notification = new Notification(R.drawable.ic_launcher, tickerText, when);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		notification.defaults |= Notification.FLAG_FOREGROUND_SERVICE;
	}
	
	
	
	private String getBestProvider(){
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		return locationManager.getBestProvider(criteria, true);
	}
	
	private boolean checkMyLocation(){
		Location location = locationManager.getLastKnownLocation(getBestProvider());
		locationProfile = profileInRange(location);	
		editor.putBoolean("inlocate", inlocate);
		editor.commit();
		return inlocate;
	}
	

	
	
	//////////////////////////////////
	// LOCATIONS
	
	@Override
	public void onLocationChanged(Location location) {
	
		locationProfile = profileInRange(location);			
		editor.putBoolean("inlocate", inlocate);
		editor.commit();
		setConfiguration();
		
	}

	
	private LocationProfile profileInRange(Location location){
		if (location!=null){
			ArrayList<LocationProfile> locationLists  = new ArrayList<LocationProfile>();
			SqliteManager sqlite = new SqliteManager(this);
			locationLists =sqlite.getLocationList();
			sqlite.close();
			for (LocationProfile profile : locationLists){
				if(location.distanceTo(profile.getLocation())<=profile.getRange()){
					inlocate = true;
					return profile;
				}
			}
			inlocate = false;
		}
		return null;
	}
	
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}
	@Override
	public void onProviderEnabled(String provider) {
		Log.i("Server", "enable" + provider);		
	}
	@Override
	public void onProviderDisabled(String provider) {
//		Log.i("Server", "disable " + provider);
		Intent intentnet = new Intent("LOCATION.RECEIVER");
		inlocate= false;
		editor.putBoolean("inlocate", inlocate);
		editor.commit();
		intentnet.putExtra("inlocate", inlocate);
		sendOrderedBroadcast(intentnet, null);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStart(intent, startId);
		//WIDGET
		if (intent!=null && intent.getAction()!=null && intent.getAction().compareTo(AppWidgetManager.ACTION_APPWIDGET_UPDATE)==0){

			editor.putBoolean("ACTIVATE", !prefs.getBoolean("ACTIVATE", false));
			editor.commit();
			checkMyLocation();
			setConfiguration();
			
		}
		return Service.START_STICKY;
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private void setConfiguration(){
		Intent intentnet = new Intent("LOCATION.RECEIVER");
		inlocate = prefs.getBoolean("inlocate", false);
		
		if (locationProfile!=null){
			editor.putInt("locationProfile", locationProfile.getId());
			editor.commit();
			intentnet.putExtra("locationProfile", locationProfile.getId());
	
		}
	}

}

