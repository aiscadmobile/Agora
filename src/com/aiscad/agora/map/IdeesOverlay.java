package com.aiscad.agora.map;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.aiscad.agora.R;
import com.aiscad.agora.objects.Idea;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;

public class IdeesOverlay extends ItemizedOverlay<NfcItem> {

	private ArrayList<NfcItem> itemOverlays = new ArrayList<NfcItem>(); 
	private boolean inpropostes;

	
	
	public IdeesOverlay(Activity activity, Drawable defaultMarker) {
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
		
		super.draw(canvas, mapView, shadow);	
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
	
	public void setIdeesList(ArrayList<Idea> ideaslist){
		itemOverlays.clear();
		for (Idea idea:ideaslist){
			itemOverlays.add(new NfcItem(idea.getGeoPosition(), idea.getTitle(), ""));
		}
		populate();
	}
	
}
