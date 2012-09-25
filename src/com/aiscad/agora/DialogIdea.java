package com.aiscad.agora;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class DialogIdea extends Activity {
	
	private NotificationManager mNotificationManager;
	private Notification notification;
	
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.novaideadialog);
		
		Button ok = (Button) findViewById(R.id.button1);
		Button cancel = (Button) findViewById(R.id.Button01);
		
		ImageButton locButton = (ImageButton) findViewById(R.id.locButton);
		
		locButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mapIntent = new Intent(DialogIdea.this,RangeMapActivity.class);
				mapIntent.putExtra("newidea", true);
				startActivity(mapIntent);
			}
		});
		
		
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		setNotification("Nova proposta a la zona", "Un ciutada ha enviat una proposta a la teva zona. ");
		
		
		
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(DialogIdea.this, "Idea proposta enviada amb èxit, esperi la seva verificació.",  Toast.LENGTH_LONG).show();
				//enviao notificacion
				mNotificationManager.notify(0, notification);
				onBackPressed();
				
			}
		});
		
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();

			}
		});
	}
	
	
	private void setNotification(CharSequence tickerText, CharSequence contentText){
			
			long when = System.currentTimeMillis();
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(DialogIdea.this,IdeesActivity.class), 0);
			Context context = getApplicationContext();
			CharSequence contentTitle = "AGORA notificació";
	
			notification = new Notification(R.drawable.ic_launcher, tickerText, when);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
			notification.defaults |= Notification.FLAG_FOREGROUND_SERVICE;
		}
	
}
