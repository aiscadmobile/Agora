package com.aiscad.agora;

import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

import com.aiscad.agora.map.GeoTools;
import com.aiscad.agora.map.NfcItem;
import com.aiscad.agora.map.NfcOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;


public class MapaActivity extends MapActivity{
	
	
	private static String APIKEY_MAP = "0dttZBhwoAF7CacqQNIZHdtJ2cEXMpltyaql2NA";
	private MapView mapView;
	private MyLocationOverlay locationOverlay;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.map_main);
		initMap();
		initOverlay();
	}
	
	 private void initMap(){
    	//creamos nuevo objeto mapview añadimos la apikey que hemos registado
    	mapView = new MapView(this, APIKEY_MAP);
    	//seteamos el objeto...
		mapView.setSatellite(false);
		mapView.setClickable(true);
		mapView.setEnabled(true);
		mapView.setBuiltInZoomControls(true);
		//zoom inicial
		mapView.getController().setZoom(18);

		//instanciamos relative layout - por si en un futuro queremos añadir botones o componentes flotantes fijos en el mapa
		RelativeLayout mapLayout = (RelativeLayout) this.findViewById(R.id.mapLayout);
		mapView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		//y añadimos el mapview a la vista 
		mapLayout.addView(mapView);	
    }
	
	private void initOverlay(){
		
		locationOverlay = new MyLocationOverlay(this, mapView);
		locationOverlay.enableMyLocation();
		locationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
            	
            	if (locationOverlay.getMyLocation()!=null){
                    mapView.getController().animateTo(locationOverlay.getMyLocation());
            	}
            }
    	        
        });
		
		
		NfcOverlay nfcOverlay = new NfcOverlay(getResources().getDrawable(R.drawable.makerone));
		
		nfcOverlay.add(new NfcItem(GeoTools.getGeopoint(41.409568,2.211964), "", ""));
		
		mapView.getOverlays().add(locationOverlay);
		mapView.getOverlays().add(nfcOverlay);
		mapView.invalidate();
		
				
	}
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
