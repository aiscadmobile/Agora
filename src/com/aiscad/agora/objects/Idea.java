package com.aiscad.agora.objects;

import java.util.Date;

import android.location.Location;

import com.aiscad.agora.map.GeoTools;
import com.google.android.maps.GeoPoint;

public class Idea {

	private int id;
	private String info;
	private String title;
	private int positiveVotes;
	private int negativeVotes;
	private GeoPoint geoPosition;
	private String dateString;
	private Date date;
	
	public Idea(int id, String title, String info, int positiveVotes, int negativeVotes, String dateString,
			GeoPoint geoPosition) {
		super();
		this.id = id;
		this.info = info;
		this.title = title;
		this.positiveVotes = positiveVotes;
		this.negativeVotes = negativeVotes;
		this.geoPosition = geoPosition;
		this.dateString= dateString;
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPositiveVotes() {
		return positiveVotes;
	}

	public void setPositiveVotes(int positiveVotes) {
		this.positiveVotes = positiveVotes;
	}

	public int getNegativeVotes() {
		return negativeVotes;
	}

	public void setNegativeVotes(int negativeVotes) {
		this.negativeVotes = negativeVotes;
	}

	public GeoPoint getGeoPosition() {
		return geoPosition;
	}

	public void setGeoPosition(GeoPoint geoPosition) {
		this.geoPosition = geoPosition;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public Location getLocation(){
		
		Location location = new Location("");
		location.setLatitude(GeoTools.latitude(getGeoPosition()));
		location.setLongitude(GeoTools.longitude(getGeoPosition()));
		return location;
	}
	
}
