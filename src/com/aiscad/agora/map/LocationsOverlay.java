package com.aiscad.agora.map;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.aiscad.agora.R;
import com.aiscad.agora.RangeMapActivity;
import com.aiscad.agora.objects.LocationProfile;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;

public class LocationsOverlay extends ItemizedOverlay<LocationProfile> {

	private RangeMapActivity activity; 
	private GeoPoint touchGeopoint=null;

	private ArrayList<LocationProfile> locationList = new ArrayList<LocationProfile>();
	private int distance=0;
	private boolean ontapItemRange;
	private LocationProfile locProfile;
	
	public LocationsOverlay(RangeMapActivity activity,  Drawable draw) {
		super(boundCenterBottom(draw));
		this.activity = activity;
		populate();

	}

	public void addItem(LocationProfile locProf){
		locationList.add(locProf);
		populate();
	}
	
	@Override
	protected LocationProfile createItem(int i) {
		// TODO Auto-generated method stub
		return locationList.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return locationList.size();
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		
		if (touchGeopoint!=null){
			Point point = new Point();
			mapView.getProjection().toPixels(touchGeopoint, point);
			int x = point.x;
			int y = point.y;
			Drawable maker = activity.getResources().getDrawable(R.drawable.makertwo);
			maker.setBounds(x-maker.getIntrinsicWidth()/2, y-maker.getIntrinsicHeight(), x+maker.getIntrinsicWidth()/2, y);
			maker.draw(canvas);
			drawCircle(canvas, mapView, touchGeopoint, distance, Color.RED);
			mapView.invalidate();
			
		}
		else if (ontapItemRange && locProfile!=null){		
			drawCircle(canvas, mapView, locProfile.getGeoPoint(), distance, Color.GREEN);
			mapView.invalidate();
		}
		
		super.draw(canvas, mapView, shadow);	
	}
	


	private void drawCircle(Canvas canvas, MapView mapView, GeoPoint geoPoint, int distance , int color){
		Point point = new Point();
		mapView.getProjection().toPixels(geoPoint, point);
		Paint paint = new Paint();
		paint.setColor(color);
		paint.setAntiAlias(true);
		paint.setAlpha(50);
		canvas.drawCircle(point.x, point.y, mapView.getProjection().metersToEquatorPixels(distance), paint);
		
	}
	
	@Override
	protected boolean onTap(int index) {
		touchGeopoint=null;
		ontapItemRange = true;
		Log.i("LOCAIONSOVRLAY" , "ONTAPINDEX : " + index);
		locProfile = locationList.get(index);
		activity.onTapItemInlayout(locProfile);
		return true;	
	}
	
	
	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
		try{
			Log.i("LOCAIONSOVRLAY" , "TAPLAYOUT ");
			locProfile =null;
			ontapItemRange= false;
			touchGeopoint = p;
			activity.onTapInlayout(p, mapView);
			if (locationList.isEmpty()){
				return true;
			}
			return super.onTap(p, mapView);
			
		}catch(Exception e){
			e.printStackTrace();
			return true;
		}
	}
	
	public LocationProfile getLocProfile() {
		return locProfile;
	}

	public void setLocProfile(LocationProfile locProfile) {
		this.locProfile = locProfile;
	}

	public ArrayList<LocationProfile> getLocationList() {
		return locationList;
	}

	public void setLocationList(ArrayList<LocationProfile> locationList) {
		this.locationList = locationList;
		populate();
	}
	
	public void removeItem(LocationProfile profile){
		locationList.remove(profile);
		populate();
	}
	
	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}
	public GeoPoint getTouchGeopoint() {
		return touchGeopoint;
	}

	public void setTouchGeopoint(GeoPoint touchGeopoint) {
		this.touchGeopoint = touchGeopoint;
	}

	public boolean isOntapItemRange() {
		return ontapItemRange;
	}

	public void setOntapItemRange(boolean ontapItemRange) {
		this.ontapItemRange = ontapItemRange;
	}

}
