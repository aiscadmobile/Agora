package com.aiscad.agora;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiscad.agora.comunication.HttpHandler;
import com.aiscad.agora.map.GeoTools;
import com.aiscad.agora.objects.Debat;

public class DebateActivity extends Activity{
	
	private ListView debateListView;
	private DebatAdapter debatAdapter;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.debat_main);

		debateListView = (ListView) findViewById(R.id.listView1);
		
	}
	
	
	@Override
	protected void onResume() {
		initDebats();
		super.onResume();
	}
	
	
	
	  //inicia Debats
    private void initDebats(){
    	
    	new Thread(){
    		public void run(){
    			final ArrayList<Debat> debats = HttpHandler.getDebats();
    			runOnUiThread(new Runnable() {
					public void run() {
						debateListView.setAdapter(new DebatAdapter(DebateActivity.this, debats));

					}
				});
    		}
    	}.start();
    }
    
    
    public class DebatAdapter extends ArrayAdapter<Debat>{

    	public DebatAdapter(Context context, List<Debat> objects) {
    		super(context, R.layout.debat_item, objects);
    		
    	}
    	
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		if(convertView == null){
    			LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    			convertView = li.inflate( R.layout.debat_item, null);		
    		}
    		final Debat debate = getItem(position);
    		
    		TextView title = (TextView) convertView.findViewById(R.id.title);
    		TextView data = (TextView) convertView.findViewById(R.id.fechadebat);
    		TextView forumtext = (TextView) convertView.findViewById(R.id.forumText);
    		final LinearLayout forumLayout = (LinearLayout) convertView.findViewById(R.id.forumLayout);
    		TextView info = (TextView) convertView.findViewById(R.id.infodebat);
    		TextView add = (TextView) convertView.findViewById(R.id.address);
    		TextView asistents = (TextView) convertView.findViewById(R.id.asistents);
    		TextView link = (TextView) convertView.findViewById(R.id.link);

    		Button assButton = (Button) convertView.findViewById(R.id.assistButton);
    		ImageButton buttonNavigation = (ImageButton) convertView.findViewById(R.id.navegaButton);

    		title.setText(debate.getTitle());
    		data.setText(debate.getDataString());
    		info.setText(debate.getInfo());
    		asistents.setText(debate.getAssistents());
    		add.setText(debate.getStreet());
    		link.setText(debate.getLinks());
    		buttonNavigation.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
						
					if(debate.getGeoPosition()!=null){
						String location = "http://maps.google.com/maps?daddr="
								+ Double.toString(GeoTools.latitude(debate.getGeoPosition())) + ","
								+ Double.toString(GeoTools.longitude(debate.getGeoPosition()));
	
						Uri uri = Uri.parse(location);
						startActivity(new Intent(
								android.content.Intent.ACTION_VIEW, uri)
								.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));					
					}else{
						Toast.makeText(DebateActivity.this,"El debat no esta localitzat", Toast.LENGTH_LONG).show();
					}
				}
			});
    	    		
    		forumtext.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				// TODO Auto-generated method stub
    				if(forumLayout.isShown()){
    					forumLayout.setVisibility(View.GONE);
    				}else{
    					forumLayout.setVisibility(View.VISIBLE);
    				}
    			}
    		});
    		assButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(DebateActivity.this, "Asistencia introduida al teu calendari.",Toast.LENGTH_LONG ).show();
				}
			});
    		
    
    		
    		return convertView;
    	}
    }
   

    
    
}
