package com.aiscad.agora.objects;

import android.location.Location;
import android.location.LocationManager;

import com.aiscad.agora.map.GeoTools;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class LocationProfile extends OverlayItem {

	
	private int id;
	private GeoPoint geoPoint;
	private String name="";
	private int range= 0;
	private String address;

	public LocationProfile(int id, GeoPoint point){
		super(point, "", "");
		this.id=id;
		this.setGeoPoint(point);
		this.name=name;
	}
	
	
	public Location  getLocation(){
		
		Location location = new Location(LocationManager.NETWORK_PROVIDER);
		location.setLatitude(GeoTools.latitude(geoPoint));
		location.setLongitude(GeoTools.longitude(geoPoint));
		return location;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public GeoPoint getGeoPoint() {
		return geoPoint;
	}

	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}
	
	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}



	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}
}
