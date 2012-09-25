package com.aiscad.agora.map;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.aiscad.agora.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;

public class NfcOverlay extends ItemizedOverlay<NfcItem> {

	private ArrayList<NfcItem> itemOverlays = new ArrayList<NfcItem>(); 
	private boolean inpropostes;
	private GeoPoint touchGeopoint;
	private Activity activity; 
	
	public NfcOverlay(Activity activity, Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		// TODO Auto-generated constructor stub
	}

	@Override
	protected NfcItem createItem(int arg0) {
		// TODO Auto-generated method stub
		return itemOverlays.get(arg0);
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		
		if (inpropostes && touchGeopoint!=null){
			Point point = new Point();
			mapView.getProjection().toPixels(touchGeopoint, point);
			int x = point.x;
			int y = point.y;
			Drawable maker = activity.getResources().getDrawable(R.drawable.makertwo);
			maker.setBounds(x-maker.getIntrinsicWidth()/2, y-maker.getIntrinsicHeight(), x+maker.getIntrinsicWidth()/2, y);
			maker.draw(canvas);
			mapView.invalidate();
			
		}

		super.draw(canvas, mapView, shadow);	
	}
	

	@Override
	public boolean onTap(GeoPoint p, MapView arg1) {
		
		if(inpropostes){
			Log.i("Rangemap","ontap! ..................::::::::");
			touchGeopoint = p;
		}
		return true;
	}
	
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return itemOverlays.size();
	}

	
	public void add(NfcItem overlay){
		itemOverlays.add(overlay);
		  populate();
	}
	
	public boolean isInpropostes() {
		return inpropostes;
	}

	public void setInpropostes(boolean inpropostes) {
		this.inpropostes = inpropostes;
	}
	
	
}
