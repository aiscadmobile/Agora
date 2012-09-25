package com.aiscad.agora;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.aiscad.agora.comunication.HttpHandler;
import com.aiscad.agora.objects.Idea;
import com.aiscad.agora.objects.LocationProfile;
import com.aiscad.agora.sqlite.SqliteManager;

public class IdeesActivity extends Activity{

	private ListView ideesListView;
		
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.idees_main);
		ideesListView = (ListView) findViewById(R.id.popularList);
		ImageButton addIdea = (ImageButton) findViewById(R.id.addIdea);
		ImageButton mapButton = (ImageButton) findViewById(R.id.mapButton);
	
		
		addIdea.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(IdeesActivity.this,DialogIdea.class));
			}
		});
		mapButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(IdeesActivity.this,RangeMapActivity.class);
				intent.putExtra("ideesActivity", true);
				startActivity(intent);
				
			}
		});
	}	
	
	@Override
	protected void onResume() {
		initIdees();
		super.onResume();
	}
	
	 //inicia les propostes
    private void initIdees(){
    	new Thread(){
    		public void run(){
    			final ArrayList<Idea> ideesServer = HttpHandler.getIdees();
    			final ArrayList<Idea> idees = new ArrayList<Idea>();

    			for (Idea idea : ideesServer){
    				
    				if(idea.getGeoPosition()!=null){
    					
	    				if (profileInRange(idea.getLocation())!=null){
	    					idees.add(idea);
	    				}    				
    				}else{
    					idees.add(idea);
    				}
    			}
    			
    			runOnUiThread(new Runnable() {
					public void run() {
						
						ideesListView.setAdapter(new IdeesAdapter(IdeesActivity.this, idees));
						
					}
				});
    		}
    	}.start();    	
    }
//    
    
    
    //compara la localitzacio si esta en el rang
	private LocationProfile profileInRange(Location location){
		if (location!=null){
			ArrayList<LocationProfile> locationLists  = new ArrayList<LocationProfile>();
			SqliteManager sqlite = new SqliteManager(this);
			locationLists =sqlite.getLocationList();
			sqlite.close();
			for (LocationProfile profile : locationLists){
				if(location.distanceTo(profile.getLocation())<=profile.getRange()){
					return profile;
				}
			}
		}
		return null;
	}
	
    
  
    
    public class IdeesAdapter extends ArrayAdapter<Idea>{

    	public IdeesAdapter(Context context, List<Idea> objects) {
    		super(context, R.layout.idees_item, objects);
    		
    	}
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		if(convertView == null){
    			LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    			convertView = li.inflate( R.layout.idees_item, null);		
    		}
    		final Idea idea = getItem(position);
    		
    		final TextView ideatext = (TextView) convertView.findViewById(R.id.infoIdea);
    		TextView vfav = (TextView) convertView.findViewById(R.id.votsfav);
    		TextView vcontra = (TextView) convertView.findViewById(R.id.votscontra);
    		TextView locinfo = (TextView) convertView.findViewById(R.id.locinfo);
    		TextView titleIdea = (TextView) convertView.findViewById(R.id.titleIdea);

    		ImageButton twit = (ImageButton) convertView.findViewById(R.id.twittButton);
    		ImageButton location = (ImageButton) convertView.findViewById(R.id.locButton);
    		
    		if(idea.getGeoPosition()!=null){
    			location.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent mapIntent = new Intent(IdeesActivity.this,RangeMapActivity.class);
						mapIntent.putExtra("location", true);
						mapIntent.putExtra("latitude", idea.getGeoPosition().getLatitudeE6());
						mapIntent.putExtra("longitude", idea.getGeoPosition().getLongitudeE6());
						startActivity(mapIntent);
						
					}
				});
    		}else{
    			locinfo.setText("Sense localització");
    		}
    		
    		int totalVots = idea.getPositiveVotes() + idea.getNegativeVotes();
    		vfav.setText(idea.getPositiveVotes()+"");
    		vcontra.setText(totalVots+"");
    		ideatext.setText(idea.getInfo());
    		titleIdea.setText(idea.getTitle());
    		twit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent tweetIntent = new Intent(Intent.ACTION_SEND);
					tweetIntent.putExtra(Intent.EXTRA_TEXT, "Ágora : "+ ideatext);
					tweetIntent.setType("application/twitter");
					Intent browserIntent = new Intent(tweetIntent);
				    startActivity(browserIntent);					
				}
			});
    		
    		return convertView;
    	}

    }

}
