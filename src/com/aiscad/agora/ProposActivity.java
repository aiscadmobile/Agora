package com.aiscad.agora;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.aiscad.agora.comunication.HttpHandler;
import com.aiscad.agora.objects.Proposta;

public class ProposActivity extends Activity{
	
	private ListView propuListView;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.propu_main);
		propuListView = (ListView) findViewById(R.id.porplist);
		ImageButton addIdea = (ImageButton) findViewById(R.id.addIdea);
		
		addIdea.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ProgressDialog pd = new ProgressDialog(ProposActivity.this);
				pd.setTitle("Depositant vots");
				pd.setMessage("Aproximis al punt NFC per depositar els seus vots");
				pd.show();
			}
		});
		
	}
	
	
	@Override
	protected void onResume() {
		initPropos();
		super.onResume();
	}
	
	
	
	  //inicia Debats
    private void initPropos(){
    	
    	new Thread(){
    		public void run(){
    			final ArrayList<Proposta> propos = HttpHandler.getPropostes();
    			runOnUiThread(new Runnable() {
					public void run() {
						Log.i("propos", "size: " +  propos.size());
						propuListView.setAdapter(new PropostesAdapter(ProposActivity.this, propos));

					}
				});
    		}
    	}.start();
    }
    
    
    public class PropostesAdapter extends ArrayAdapter<Proposta>{

    	public PropostesAdapter(Context context, List<Proposta> objects) {
    		super(context, R.layout.propu_item, objects);
    		
    	}
    	
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		if(convertView == null){
    			LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    			convertView = li.inflate( R.layout.propu_item, null);		
    		}
    		Proposta propo = getItem(position);
    		
    		TextView data = (TextView) convertView.findViewById(R.id.finalizeData);
    		TextView info = (TextView) convertView.findViewById(R.id.infoPropo);
    		TextView title = (TextView) convertView.findViewById(R.id.title);

       		ImageButton twit = (ImageButton) convertView.findViewById(R.id.twittButton);
    		ImageButton location = (ImageButton) convertView.findViewById(R.id.locButton);
    		
    		title.setText(propo.getTitle());
    		info.setText(propo.getInfo());
    		data.setText(propo.getFinalData());
    		

    		
    		return convertView;
    	}
    }

    
}
