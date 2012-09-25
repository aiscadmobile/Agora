package com.aiscad.agora.map;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.maps.GeoPoint;

public class GeoTools {

	

	// retorna direccion en string de la localizacion parametrizada
	public static String getAddress(Activity act, GeoPoint point) {
		String address = "";
		Geocoder geoCoder = new Geocoder(act, Locale.getDefault());
		try {

			List<Address> addresses = geoCoder.getFromLocation((point.getLatitudeE6()/ 1E6),(point.getLongitudeE6()/1E6), 1);
			if (address != null) {
				if (addresses.size() > 0) {
					for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
						address = addresses.get(0).getLocality();
						String cp = addresses.get(0).getPostalCode();
						String cc = addresses.get(0).getCountryCode();
						String cname = addresses.get(0).getCountryName();
						
						address = address;
//						addr.setLocality(address);
//						addr.setCountryCode(cc);
//						addr.setPostalCode(cp);
//						addr.setCountryName(cname);
					}
				}
			}

		} catch (IOException e) {
			Log.e("LocationPosition",
					"getAddress(location) ERROR: " + e.toString());
			address = "no disponible";
		}
		return address;
	}
	

	
	//retorna longitud de un geopoint
	public static Double longitude(GeoPoint p) {
		return  p.getLongitudeE6() / 1E6;
	}

	//retorna latitud de un geopoint
	public static Double latitude(GeoPoint p) {
		return p.getLatitudeE6() /  1E6;
	}
	
	
	public static GeoPoint getGeopoint(double latitude, double longitude){
		
		return new GeoPoint((int)(latitude*1E6), (int)(longitude*1E6));
	}
	
    public static String getUrlRoute(GeoPoint gpone, GeoPoint gptwo) {// connect to map web service
    	
		double fromLat = GeoTools.latitude(gpone);
		double fromLon = GeoTools.longitude(gpone);
		double toLat = GeoTools.latitude(gptwo);
		double toLon = GeoTools.longitude(gptwo);

		StringBuffer urlString = new StringBuffer();
		urlString.append("http://maps.google.com/maps?f=d&hl=en");
		urlString.append("&saddr=");// from
		urlString.append(Double.toString(fromLat));
		urlString.append(",");
		urlString.append(Double.toString(fromLon));
		urlString.append("&daddr=");// to
		urlString.append(Double.toString(toLat));
		urlString.append(",");
		urlString.append(Double.toString(toLon));
		urlString.append("&ie=UTF8&0&om=0&output=kml");
		return urlString.toString();
	 }
    
	
    public static String cleanAdress(String address){
    	if(address.length()>0){
    		address = address.replace("á", "a")
    		.replace("à", "a")
    		.replace("é", "e")
    		.replace("è", "e")
    		.replace("í", "i")
    		.replace("ì", "i")
    		.replace("ó", "o")
    		.replace("ú", "u");
    	}
    	return address;
    	
    }
    
}
