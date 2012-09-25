package com.aiscad.agora;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.aiscad.agora.comunication.HttpHandler;
import com.aiscad.agora.map.GeoTools;
import com.aiscad.agora.map.IdeesOverlay;
import com.aiscad.agora.map.LocationsOverlay;
import com.aiscad.agora.map.NfcItem;
import com.aiscad.agora.map.NfcOverlay;
import com.aiscad.agora.objects.Idea;
import com.aiscad.agora.objects.LocationProfile;
import com.aiscad.agora.sqlite.SqliteManager;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class RangeMapActivity extends MapActivity {
	
	private static String APIKEY_MAP = "0dttZBhwoAF7CacqQNIZHdtJ2cEXMpltyaql2NA";

	
	public static int MAX_METERS = 10000;
	public static int OFFSET_METERS_STANDAR = 0;
	private SharedPreferences prefs;
	private Editor editor;
	
	private LinearLayout loadingLayout;
	private TextView offsetValue;
	private SeekBar offsetBar;
	private GeoPoint lastGeoPoint;
	private MapView mapView;
	
	private MyLocationOverlay myLocationOverlay;
	private LocationsOverlay locationsOverlay;
	private RelativeLayout relativelayout;
	private TextView addressTxt;
	private TextView maxvalueView;
	private ImageButton addButton;
	private AsyncTask<Void, Void, String> task;
	private ArrayList<LocationProfile> locationsList;
	private LocationProfile profileInEdition;
	
//	private int dstanceValue=0;
	private LinearLayout seekLayout;
	
	private boolean islocation;
	private boolean newidea;
	
	private ArrayList<Idea> ideesServer;

	@Override
	protected void onCreate(Bundle icicle) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(icicle);
		setContentView(R.layout.maprange);
	
		prefs = getSharedPreferences("preferences", Context.MODE_PRIVATE);
		editor = prefs.edit();
		locationsList = new ArrayList<LocationProfile>();
		
		editor.putInt("OFFSET", 0);
		editor.commit();
		
		//per veure els punts de les propostes	
		islocation = getIntent().getBooleanExtra("location",false);
		
		//seleccionar nova localitzacio
		newidea = getIntent().getBooleanExtra("newidea",false);

		
        addButton = (ImageButton) findViewById(R.id.addButton);
        seekLayout = (LinearLayout) findViewById(R.id.seekLayout);
        seekLayout.setVisibility(View.INVISIBLE);
        
		maxvalueView = (TextView) findViewById(R.id.textView7);
		loadingLayout = (LinearLayout) findViewById(R.id.ladingLayout);
		addressTxt = (TextView) findViewById(R.id.address);
		offsetValue = (TextView) findViewById(R.id.textView2);
		offsetBar = (SeekBar) findViewById(R.id.seekBar1);
		
		initIdees();
		setPorgressBar();		
		initMapView();
		initOverlays();
		
        seekLayout.setVisibility(View.VISIBLE);
	
		///////////// CONFIGURE VIEWS
		addButton.bringToFront();
		addButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GeoPoint gpoint = locationsOverlay.getTouchGeopoint();
				if (gpoint!=null){

					LocationProfile lprofile = new LocationProfile(0, gpoint);
					lprofile.setRange(prefs.getInt("OFFSET",OFFSET_METERS_STANDAR));
					lprofile.setAddress(addressTxt.getText().toString());
					SqliteManager sqlite = new SqliteManager(RangeMapActivity.this);
					sqlite.addLocationProfile(lprofile);
					sqlite.close();
					locationsOverlay.addItem(lprofile);
					locationsOverlay.setTouchGeopoint(null);
					mapView.invalidate();
	
				}
			}
		});
		
	}

	

	 //inicia les propostes
    private void initIdees(){
    	new Thread(){
    		public void run(){
    			ideesServer = HttpHandler.getIdees();

    		}
    	}.start();    	
    }
	//vuelta del activity previamente en pausa
	@Override
	protected void onResume() {
		super.onResume();
		myLocationOverlay.enableMyLocation();	
		
	}
	
	
	private void setPorgressBar(){
		
		maxvalueView.setText(MAX_METERS+"m");
		offsetBar.setMax(MAX_METERS);
		offsetBar.setProgress(prefs.getInt("OFFSET",OFFSET_METERS_STANDAR));
		int offsetval = prefs.getInt("OFFSET",OFFSET_METERS_STANDAR);
		if (offsetval>0){
			offsetValue.setText(offsetval+"m");
			offsetValue.setTextColor(Color.LTGRAY);
		}else{
			offsetValue.setText("No range");
			offsetValue.setTextColor(Color.RED);
		}
		
		offsetBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				int progress = seekBar.getProgress();
				editor.putInt("OFFSET", progress);
				editor.commit();
				if (profileInEdition!=null){
					profileInEdition.setRange(progress);
					SqliteManager sqlite = new SqliteManager(RangeMapActivity.this);
					sqlite.updateLocation(profileInEdition);
					sqlite.close();
				}
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,	boolean fromUser) {
				// TODO Auto-generated method stub
				locationsOverlay.setDistance(progress);
				if (progress>0){
					offsetValue.setText(progress+" m");
					offsetValue.setTextColor(Color.LTGRAY);
				}else{
					offsetValue.setText("Fora de rang");
					offsetValue.setTextColor(Color.RED);
				}
			}
		});
	}

	
	private void initMapView(){
		// Inicializacion MapView
		mapView = new MapView(this,APIKEY_MAP );
		mapView.getController().setZoom(16);
		mapView.setClickable(true);
		mapView.setEnabled(true);
		mapView.setBuiltInZoomControls(true);
		relativelayout = (RelativeLayout) this.findViewById(R.id.relativeLayout1);
		mapView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		relativelayout.addView(mapView);
		addressTxt.setText("");
	
		
	}
	private void initOverlays(){
		
		ArrayList<Idea> ideaslist = HttpHandler.getIdees();
		
		
		
		locationsOverlay = new LocationsOverlay(this,getResources().getDrawable(R.drawable.makertwo));
		setMyLocationOverlay();
		
		locationsList = new ArrayList<LocationProfile>();
		SqliteManager sqlite = new SqliteManager(this);
		locationsList = sqlite.getLocationList();
		sqlite.close();
		locationsOverlay.setLocationList(locationsList);
		locationsOverlay.setDistance(prefs.getInt("OFFSET",OFFSET_METERS_STANDAR));

		NfcOverlay nfcOverlay = new NfcOverlay(RangeMapActivity.this , getResources().getDrawable(R.drawable.makerone));		
		
		
		
		mapView.getOverlays().add(myLocationOverlay);
		if(getIntent().getBooleanExtra("ideesActivity", false)){
			IdeesOverlay ideasoverlay = new IdeesOverlay(RangeMapActivity.this, getResources().getDrawable(R.drawable.marker));
			ideasoverlay.setIdeesList(ideaslist);
			mapView.getOverlays().add(ideasoverlay);
		}
		
		if (islocation){	//ROPOSTES LOCLITZADES!
			nfcOverlay = new NfcOverlay(RangeMapActivity.this , getResources().getDrawable(R.drawable.marker));	
			Log.i("Rangemap","islocation ..................::::::::");
			int lat =  getIntent().getIntExtra("latitude",0);
			int lng =  getIntent().getIntExtra("longitude",0);
			GeoPoint gp =new GeoPoint(lat,lng);
			nfcOverlay.add(new NfcItem(gp, "", ""));
			findViewById(R.id.seekLayout).setVisibility(View.GONE);
			mapView.getController().animateTo(gp);
			mapView.getOverlays().add(nfcOverlay);
		}			 
		else{
			 if(newidea){//SELECCIONA LOCALITZACIO DE IDEAS
					findViewById(R.id.seekLayout).setVisibility(View.GONE);
			 }
			Log.i("Rangemap","res ..................::::::::");
			nfcOverlay.add(new NfcItem(GeoTools.getGeopoint(41.409568,2.211964), "", ""));
			mapView.getOverlays().add(nfcOverlay);
			mapView.getOverlays().add(locationsOverlay);
		}
		
		mapView.invalidate();

	}
	
	
	public void onTapItemInlayout(final LocationProfile profile){
		profileInEdition = profile;
		offsetBar.setProgress(profile.getRange());
        locationsOverlay.setLocProfile(profile);
        locationsOverlay.setDistance(profile.getRange());
		lastGeoPoint = profile.getGeoPoint();
		addButton.setVisibility(View.GONE);
		locationsOverlay.setOntapItemRange(true);
		addressTxt.setText(profileInEdition.getName());	
		addressTxt.setMovementMethod(LinkMovementMethod.getInstance());
		addressTxt.requestFocus();
		if(!islocation && !newidea)
			seekLayout.setVisibility(View.VISIBLE);
      
	}
	
	public void onTapInlayout(GeoPoint p, MapView mapView){
		lastGeoPoint = p;
		offsetBar.setEnabled(true);
		searchAdress();
		addButton.setVisibility(View.VISIBLE);
		if(!islocation && !newidea)
			seekLayout.setVisibility(View.VISIBLE);       
	}
	
	
	private void searchAdress(){
		loadingLayout.setVisibility(View.VISIBLE);
		
		try{
		task = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
			
				final String adress = GeoTools.getAddress(RangeMapActivity.this,lastGeoPoint );
				return adress;
			}

			@Override
			protected void onPostExecute(String result) {
				loadingLayout.setVisibility(View.GONE);
				addressTxt.setText(result);
				addressTxt.setMovementMethod(LinkMovementMethod.getInstance());
				addressTxt.requestFocus();
				super.onPostExecute(result);
			}
			
		}.execute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	private void setMyLocationOverlay(){
		myLocationOverlay =new MyLocationOverlay(this,mapView);
	    
		myLocationOverlay.enableMyLocation();
		
		addressTxt.setText("Search location ... ");	

		final int id = getIntent().getIntExtra("idprofile",-1);
				
		myLocationOverlay.runOnFirstFix(new Runnable() { 
			public void run() { 	
				lastGeoPoint = myLocationOverlay.getMyLocation();	
				
				if (lastGeoPoint!=null){
					if (id==-1){
						if(!islocation){
							mapView.getController().animateTo(lastGeoPoint);
						}
						final String adress = GeoTools.getAddress(RangeMapActivity.this,lastGeoPoint );
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								loadingLayout.setVisibility(View.GONE);
								addressTxt.setText(adress);	
							}
						});
					}
				}
			}
			
			
		});
			
	}
	

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	


}
