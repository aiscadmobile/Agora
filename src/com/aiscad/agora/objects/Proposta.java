package com.aiscad.agora.objects;

import com.google.android.maps.GeoPoint;

public class Proposta {

	private int id;
	private String title;
	private String info;
	private String finalData;
	private String initData;
	private GeoPoint geoPoint;
	
	
	
	public Proposta(int id, String title, String info, GeoPoint geoPoint, String finalData) {
		this.id= id;
		this.title = title;
		this.info = info;
		this.geoPoint = geoPoint;
		this.finalData = finalData;
	}
	
	
	public String getFinalData() {
		return finalData;
	}


	public void setFinalData(String finalData) {
		this.finalData = finalData;
	}


	public String getInitData() {
		return initData;
	}


	public void setInitData(String initData) {
		this.initData = initData;
	}


	public GeoPoint getGeoPoint() {
		return geoPoint;
	}


	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
}
