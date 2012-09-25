package com.aiscad.agora.objects;

import java.util.ArrayList;
import java.util.Date;

import com.google.android.maps.GeoPoint;

public class Debat {

	public final static int PENDENT = 1;
	public final static int PERDUT = 2;
	public final static int NS = 3;
	public final static int GUANYAT = 4;
	
	private int id;
	private String info;
	private int status;
	private String title;
	private GeoPoint geoPosition;
	private DebatModerator regidor;
	private String dataString;
	private Date data;
	private String street;
	private String assistents;
	private String links;
	
	public Debat(String title,  String info, int status, GeoPoint geoPosition,	DebatModerator regidor, String dataString, String street, String asistents, String links) {
		super();
	
		this.info = info;
		this.title = title;
		this.status = status;
		this.geoPosition = geoPosition;
		this.regidor = regidor;
		this.dataString= dataString;
		this.setStreet(street);
		this.assistents = asistents;
		this.setLinks(links);
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public GeoPoint getGeoPosition() {
		return geoPosition;
	}

	public void setGeoPosition(GeoPoint geoPosition) {
		this.geoPosition = geoPosition;
	}

	public DebatModerator getRegidor() {
		return regidor;
	}

	public void setRegidor(DebatModerator regidor) {
		this.regidor = regidor;
	}

	public String getDataString() {
		return dataString;
	}

	public void setDataString(String dataString) {
		this.dataString = dataString;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getAssistents() {
		return assistents;
	}

	public void setAssistents(String assistents) {
		this.assistents = assistents;
	}

	public String getLinks() {
		return links;
	}

	public void setLinks(String links) {
		this.links = links;
	}


	
}
