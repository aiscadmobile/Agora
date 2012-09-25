package com.aiscad.agora;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aiscad.agora.comunication.HttpHandler;
import com.aiscad.agora.objects.Idea;
import com.aiscad.agora.objects.Twiit;

public class MainActivity extends Activity {
	
	//Twiiter
	private TextView twitterView;
	private ProgressBar twitterPb;

	private ImageButton debateButton;
	private ImageButton moreButton;
	private ImageButton votaButton;
	private ImageButton propuestasButton;
	
	//Adaptadores

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        debateButton = (ImageButton) findViewById(R.id.debateButton);
        moreButton = (ImageButton) findViewById(R.id.moreButton);
        votaButton = (ImageButton) findViewById(R.id.votaButton);
        propuestasButton = (ImageButton) findViewById(R.id.propuestasButton);
        
        
        debateButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this,DebateActivity.class));

			}
		});
        moreButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this,RangeMapActivity.class));

			}
		});
        votaButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this,ProposActivity.class));

			}
		});
        propuestasButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this,IdeesActivity.class));

			}
		});


		
    }

    @Override
    protected void onResume() {
    	initTwitter();
    	
    	TextView primer = (TextView) findViewById(R.id.ideaone);
    	TextView segon = (TextView) findViewById(R.id.ideatwo);
    	primer.setText("");
    	segon.setText("");
    	final ArrayList<Idea> ideesServer = HttpHandler.getIdees();
    	if(!ideesServer.isEmpty()){
    		primer.setText(ideesServer.get(0).getInfo());
    		if(ideesServer.size()>1){
        		segon.setText(ideesServer.get(1).getInfo());
    		}
    	}
    	primer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,IdeesActivity.class));
				
			}
		});
    	segon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,IdeesActivity.class));
				
			}
		});
    	super.onResume();
    }
    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    
    //////////////////////////
    //Class methods
    
    //inicia twits
    private void initTwitter(){
        twitterView = (TextView) findViewById(R.id.lastTimeLine);
        twitterPb = (ProgressBar) findViewById(R.id.tpb);
        twitterView.setMovementMethod(LinkMovementMethod.getInstance());
		twitterView.setSelected(true);
		twitterView.setFocusable(true);
    	
    	new Thread(){
    		public void run(){
    			final ArrayList<Twiit> twiits = HttpHandler.getTwits();
    			runOnUiThread(new Runnable() {
					public void run() {
						if(!twiits.isEmpty()){
							Twiit tw = twiits.get(0);
							twitterView.setText(tw.getText());
							twitterView.setFocusable(true);
							twitterView.setSelected(true);
						}
						twitterPb.setVisibility(View.INVISIBLE);
					}
				});
    		}    	
    		
    	}.start();
    }
    
    

//   

    

	
}
