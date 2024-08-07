package com.kurbetsoft.umpako;

import android.app.Notification;
//import android.app.NotificationChannel;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;


import androidx.core.app.NotificationCompat;

import java.io.IOException;


public class PlayerService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
	
	private MediaPlayer mediaPlayer=null;
	private boolean isPaused=false;
	private boolean isDestroy=false;
	private String playURL;
	private NotificationManager notificationManager=null;
	private static final String channelId="umpako_radio";
	private String channelName="";
	private static final int NOTIFICATION_ID = 1;
	private static final String playerServiceUmpako="PlayerService_Umpako";
	private static final String dataKey="Data";
	private String playerState="";

    @Override
    public void onCreate() {
		super.onCreate();

        channelName = getResources().getString(R.string.app_name);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager!=null) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW);
            channel.setSound(null, null);
            channel.enableVibration(false);
			notificationManager.createNotificationChannel(channel);
        }

        //sendNotif("");
    }

	void cancelNotif()
	{
		if(notificationManager!=null) {
			notificationManager.cancelAll();
		}
	}

	Notification sendNotif(CharSequence statusText) {

		NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
				.setSmallIcon(R.drawable.ego)
				.setContentTitle(getResources().getString(R.string.app_name))
				.setContentText(statusText)
				.setPriority(NotificationCompat.PRIORITY_MAX)
				.setSound(null)
				.setVibrate(new long[]{0L})
				.setDefaults(0);

		Intent targetIntent = new Intent(this, Main.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
		builder.setContentIntent(contentIntent);

		Notification notification=builder.build();
		if(notificationManager!=null) {
			notificationManager.notify(NOTIFICATION_ID, notification);
		}

		return notification;
	}

	@Override
   public IBinder onBind(Intent intent) {

        return new UpdateService.Stub() 
        {
            public String UpdateSrv(String strTest)
            {
            	if(strTest!=null)
            	{
            		if(strTest.equals("pause"))
            		{
            			pauseAudio();
            		}
            		else if(strTest.equals("play"))
            		{
            			playAudio();
            		}
            		else if(strTest.equals("stop"))
            		{
            			stopAudio();
            		}
					else if(strTest.equals("get_state"))
					{
						/*
						Intent in = new Intent(playerServiceUmpako);
						in.putExtra(dataKey,playerState);
						sendBroadcast(in);
						*/
						sendBrod(playerState);
					}
					else if(strTest.equals("cancel_notify")) {
						cancelNotif();
					}
            	}
            	return strTest;
            }
        };
    }

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		playerState="stop";
		Notification notification=sendNotif("");
		startForeground(NOTIFICATION_ID, notification);
		return START_STICKY;
	}


	public void sendBrod(String s)
	{
		Intent in = new Intent(playerServiceUmpako);
		in.putExtra(dataKey,s);
		sendBroadcast(in);
	}

   @Override
   public void onDestroy()
   {
	   isDestroy=true;
	   super.onDestroy();
	   try
	   {
        	if(mediaPlayer!=null)
    		{
        		if(isPaused || mediaPlayer.isPlaying())
        		{
        			mediaPlayer.stop();
        			isPaused=false;
        		}
				mediaPlayer.release();
				mediaPlayer=null;
    		}
	   }
	   catch(Exception e)
	   {

	   }
	   cancelNotif();

        //notificationManager.cancelAll();

        //System.runFinalizersOnExit(true);
		//System.exit(0);

        //super.onDestroy();
   }
   
   private void stopAudio() {
		
		if(!isDestroy && mediaPlayer!=null)
		{
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer=null;
			isPaused=false;

			playerState="stop";
			sendBrod(playerState);
			sendNotif(getResources().getString(R.string.stopped));

			/*
			Intent in = new Intent(playerServiceUmpako);
	        in.putExtra(dataKey,"stop");
	        sendBroadcast(in);
			*/
		}
	}

	private void pauseAudio() {
		if(!isDestroy && mediaPlayer!=null)
		{			
			if(mediaPlayer.isPlaying())
			{
				mediaPlayer.pause();
				isPaused=true;

				playerState="pause";
				sendBrod(playerState);
				sendNotif(getResources().getString(R.string.paused));

				/*
				Intent in = new Intent(playerServiceUmpako);
		        in.putExtra(dataKey,"pause");
		        sendBroadcast(in);
				*/
			}
			
		}
	}

	private void playAudio() {
		if(!isDestroy) {
			if(mediaPlayer!=null && isPaused)
			{
				mediaPlayer.start();
				isPaused=false;

				playerState="play";
				sendBrod(playerState);
				sendNotif(getResources().getString(R.string.played));

				/*
				Intent in = new Intent(playerServiceUmpako);
				in.putExtra(dataKey,"play");
				sendBroadcast(in);
				*/
			}
			else {
				playerState = "preparation";
				sendBrod(playerState);
				sendNotif(getResources().getString(R.string.prepare));

				/*
				Intent in = new Intent(playerServiceUmpako);
				in.putExtra(dataKey, "preparation");
				sendBroadcast(in);
				*/

				playURL = Main.URL;

				mediaPlayer = new MediaPlayer();
				try {
					mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					mediaPlayer.setDataSource(playURL);
					mediaPlayer.setOnCompletionListener(this);
					mediaPlayer.setOnPreparedListener(this);
                    mediaPlayer.setOnErrorListener(this);
					mediaPlayer.prepareAsync();
				}
				catch (Exception e) {
					playerState = "stop";
					sendBrod(playerState);
					sendNotif(getResources().getString(R.string.stopped));
					/*
					Intent in = new Intent(playerServiceUmpako);
					in.putExtra(dataKey, "stop");
					sendBroadcast(in);
					*/

					/*
					if(Main.isDestroyed)
					{
						exitService();
					}
					else
					{
						playerState="stop";
						sendNotif(getResources().getString(R.string.stopped));
						Intent in = new Intent(playerServiceUmpako);
						in.putExtra(dataKey,"stop");
						sendBroadcast(in);
					}
					 */
				}
			}
		}
	}
	
	/*
	private void exitService()
	{
		notificationManager.cancelAll();
		try
    	{
    		stopSelf();
    	}
    	catch(Exception e)
    	{
    		
    	}
	}
	*/

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
		if(!isDestroy && mediaPlayer!=null) {
			playerReconnect(mp);
		}
        return true;
    }

    private boolean playerReconnect(MediaPlayer mp)
    {
		if(!isDestroy && mediaPlayer!=null) {
			mp.reset();
			isPaused = false;
			playerState = "reconnecting";
			sendBrod(playerState);
			sendNotif(getResources().getString(R.string.reconnecting));
			/*
			Intent in = new Intent(playerServiceUmpako);
			in.putExtra(dataKey, "reconnecting");
			sendBroadcast(in);
			*/

			try {
				mp.setDataSource(playURL);
				mp.prepareAsync();
				return true;
			} catch (Exception e) {
				stopAudio();
			}
		}
        return false;
    }
	
    public void onPrepared(MediaPlayer mp) {
		if(!isDestroy && mediaPlayer!=null) {
			mp.start();
			isPaused = false;

			playerState = "play";
			sendBrod(playerState);
			sendNotif(getResources().getString(R.string.played));

			/*
			Intent in = new Intent(playerServiceUmpako);
			in.putExtra(dataKey, "play");
			sendBroadcast(in);
			*/

			 /*
			if(Main.isDestroyed)
			{
				exitService();
			}
			else
			{
				mp.start();
				isPaused=false;

				playerState="play";
				sendNotif(getResources().getString(R.string.played));

				Intent in = new Intent(playerServiceUmpako);
				in.putExtra(dataKey,"play");
				sendBroadcast(in);
			}
			*/
		}
    }
    
	public void onCompletion(MediaPlayer mp) {
        //stopAudio();
		if(!isDestroy && mediaPlayer!=null) {
			playerReconnect(mp);
		}
	}
}