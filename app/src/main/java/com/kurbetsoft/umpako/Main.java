package com.kurbetsoft.umpako;


import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.os.Build;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.View;
import android.net.Uri;
import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.IntentFilter;
import android.content.ServiceConnection;
//import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.IBinder;
import java.lang.reflect.Method;
import android.view.Window;

import com.kurbetsoft.umpako.UpdateService;
import com.kurbetsoft.umpako.PlayerService;


public class Main extends Activity implements OnClickListener {

	private BroadcastReceiver brService = null;
	private UpdateService iService = null;

	private Timer myTimer = null;
	private ImageButton btn_play,btn_pause,btn_stop;
	private ProgressBar progress;
	private boolean isPaused=false;
	public boolean isPlayed=false;
	
	public static String umpLnk="<font color=\"#FF9900\">&nbsp;</font>";
	
	public TextView textView1;
	
	//public static boolean isDestroyed=false;
	
	public static String URL="https://umpako.com/stream";//"http://stream.umpako.com:8006/listen";

	public static final String playerServiceUmpako="PlayerService_Umpako";
	
	private ServiceConnection mConnection = new ServiceConnection() 
    {
        public void onServiceDisconnected(ComponentName name)
        {
            iService = null;
        }
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            iService = UpdateService.Stub.asInterface(service);
			updateSrv("get_state");                          ;
        }
    };

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //isDestroyed=false;
        setContentView(R.layout.main);
        
        RelativeLayout currentLayout = (RelativeLayout) findViewById(R.id.mainlayout);
        currentLayout.setBackgroundColor(Color.BLACK);
        
        init();

		if(iService==null) {

			IntentFilter filter = new IntentFilter();
			filter.addAction(playerServiceUmpako);

			brService = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					if (intent.getAction().equals(playerServiceUmpako)) {
						String action = intent.getStringExtra("Data");
						if (action.equals("play")) {
							btn_play.setEnabled(false);
							btn_pause.setEnabled(true);
							btn_stop.setEnabled(true);

							btn_play.setClickable(false);
							btn_pause.setClickable(true);
							btn_stop.setClickable(true);

							progress.setVisibility(ProgressBar.INVISIBLE);
							updateCurrentPlayedSong(umpLnk);
							textView1.setVisibility(View.VISIBLE);

							isPaused = false;
							isPlayed = true;
						}
						else if (action.equals("pause")) {
							btn_play.setEnabled(true);
							btn_pause.setEnabled(false);
							btn_stop.setEnabled(true);

							btn_play.setClickable(true);
							btn_pause.setClickable(false);
							btn_stop.setClickable(true);

							progress.setVisibility(ProgressBar.INVISIBLE);
							textView1.setVisibility(View.INVISIBLE);

							isPaused = true;
							isPlayed = false;

						}
						else if (action.equals("stop")) {
							btn_play.setEnabled(true);
							btn_pause.setEnabled(false);
							btn_stop.setEnabled(false);

							btn_play.setClickable(true);
							btn_pause.setClickable(false);
							btn_stop.setClickable(false);

							progress.setVisibility(ProgressBar.INVISIBLE);
							textView1.setVisibility(View.INVISIBLE);

							isPaused = false;
							isPlayed = false;
						}
						else if (action.equals("preparation") || action.equals("reconnecting")) {
							btn_play.setEnabled(false);
							btn_pause.setEnabled(false);
							btn_stop.setEnabled(true);

							btn_play.setClickable(false);
							btn_pause.setClickable(false);
							btn_stop.setClickable(true);

							progress.setVisibility(ProgressBar.VISIBLE);
							textView1.setVisibility(View.INVISIBLE);

							isPaused = false;
							isPlayed = false;
						}
					}
				}
			};

			registerReceiver(brService, filter);

			Intent serviceIntent = new Intent(this, PlayerService.class);

			if (!isServiceRunning(PlayerService.class)) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
					startForegroundService(serviceIntent);
				} else {
					startService(serviceIntent);
				}
			}
			bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);
		}

		if (myTimer == null) {
			myTimer = new Timer();
			final Handler uiHandler = new Handler();

			myTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					final String result = doLongAndComplicatedTask();
					uiHandler.post(new Runnable() {
						@Override
						public void run() {
							if (result != null)
								updateCurrentPlayedSong("<font color=\"#FF9900\">&nbsp;</font>" + result + "<font color=\"#FF9900\">&nbsp;</font>");
						}
					});
				}

				private String doLongAndComplicatedTask() {

					String currentPlayedSong = umpLnk;
					if (isPlayed) {
						try {
							URL url = new URL("https://umpako.com/get_umpako_id3.php?s=1&ni=1"); //http://umpako.com/get_umpako_id3.php?s=1&ni=1
							URLConnection conn = url.openConnection();
							InputStreamReader rd = new InputStreamReader(conn.getInputStream());
							StringBuilder allpage = new StringBuilder();
							int n = 0;
							char[] buffer = new char[2048];
							while (n >= 0) {
								n = rd.read(buffer, 0, buffer.length);
								if (n > 0) {
									allpage.append(buffer, 0, n);
								}
							}
							if (allpage.length() > 0) {
								currentPlayedSong = allpage.toString();
							}
						} catch (Exception e) {
							currentPlayedSong = umpLnk;
						}
					}
					return currentPlayedSong;
				}

				;
			}, 0L, 15L * 1000);

		}
    }

	
	private boolean isServiceRunning(Class<?> serviceClass) {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}

	/*
    @Override
    protected void onDestroy()
    {
		try {
			iService.UpdateSrv("cancel_notify");
		}
		catch (Exception e) {

		}

		super.onDestroy();
    	isDestroyed=true;
    	try
    	{
    		if(brService!= null){unregisterReceiver(brService);}
    	}
    	catch(Exception e)
    	{
    		
    	}
    	
    	try
    	{
    		if(mConnection!= null){unbindService(mConnection);}
    	}
    	catch(Exception e)
    	{
    		
    	}

		Intent serviceIntent = new Intent(this, PlayerService.class);
		stopService(serviceIntent);
    }
	 */

	/*
    @Override
    protected void onStop()
    {
    	super.onStop();
		SharedPreferences settings = getPreferences(0);
        SharedPreferences.Editor editor = settings.edit();
        editor.commit();
    }
	 */
  
    public void setsControls()
    {
    	btn_play.setEnabled(false);
		btn_pause.setEnabled(false);
		btn_stop.setEnabled(false);
		btn_play.setClickable(false);
		btn_pause.setClickable(false);
		btn_stop.setClickable(false);
		progress.setVisibility(ProgressBar.INVISIBLE);
		textView1.setVisibility(View.INVISIBLE);
    }
    
	public void onClick(View view) {
	
		boolean playEnabled=btn_play.isEnabled();
		boolean pauseEnabled=btn_pause.isEnabled();
		boolean stopEnabled=btn_stop.isEnabled();

		//try
		//{
			switch (view.getId()) {
			case R.id.imageButton2:
				setsControls();
				/*
				if(!isPaused) {
					progress.setVisibility(ProgressBar.VISIBLE);
				}
				*/
				updateSrv("play");
				break;
			case R.id.imageButton1:
				setsControls();
				updateSrv("pause");
				break;
			case R.id.imageButton3:
				setsControls();
				updateSrv("stop");
				break;
			default:
			}
			/*
		}
		catch(Exception e)
		{
			progress.setVisibility(ProgressBar.INVISIBLE);
			btn_play.setEnabled(playEnabled);
			btn_pause.setEnabled(pauseEnabled);
			btn_stop.setEnabled(stopEnabled);
			btn_play.setClickable(playEnabled);
			btn_pause.setClickable(pauseEnabled);
			btn_stop.setClickable(stopEnabled);
		}
		*/
	}

	private boolean updateSrv(String s)
	{
		try {
			if (iService != null) iService.UpdateSrv(s);
			return true;
		}
		catch (Exception e)
		{

		}
		return false;
	}
	
	private void init() {
				
		btn_play = (ImageButton)findViewById(R.id.imageButton2);
		btn_play.setOnClickListener(this);
		btn_play.setEnabled(false);
		btn_play.setClickable(false);
		
		btn_pause = (ImageButton)findViewById(R.id.imageButton1);
		btn_pause.setOnClickListener(this);
		btn_pause.setEnabled(false);
		btn_pause.setClickable(false);
		
		btn_stop = (ImageButton)findViewById(R.id.imageButton3);
		btn_stop.setOnClickListener(this);
		
		btn_stop.setEnabled(false);	
		btn_stop.setClickable(false);
		
		progress = (ProgressBar)findViewById(R.id.progressBar1);
		progress.setVisibility(ProgressBar.INVISIBLE);
		
		textView1=(TextView)findViewById(R.id.textView1);
		textView1.setLinksClickable(true);
		textView1.setMovementMethod(new LinkMovementMethod());
		textView1.setVisibility(View.INVISIBLE);
		
		umpLnk="<font color=\"#FF9900\">&nbsp;</font><a href=\"https://umpako.com\" target=\"_blank\">"+getResources().getString(R.string.umpako)+"</a><font color=\"#FF9900\">&nbsp;</font>";
		
		//isPaused = false;
		//isPlayed = false;
	}
	
	public void updateCurrentPlayedSong(String str)
	{
		textView1.setText(Html.fromHtml(str));
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		 super.onSaveInstanceState(outState);
		 
		 outState.putBoolean("btn_play", btn_play.isEnabled());
		 outState.putBoolean("btn_pause", btn_pause.isEnabled());
		 outState.putBoolean("btn_stop", btn_stop.isEnabled());
		 outState.putInt("progress", progress.getVisibility());

		 outState.putInt("textview", textView1.getVisibility());

		 outState.putBoolean("is_played", isPlayed);
		 outState.putBoolean("is_paused", isPaused);
	}
	 	 
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);

		boolean btn_play_b=savedInstanceState.getBoolean("btn_play");
		boolean btn_pause_b=savedInstanceState.getBoolean("btn_pause");
		boolean btn_stop_b=savedInstanceState.getBoolean("btn_stop");
		
		int progress_i=savedInstanceState.getInt("progress");
		
		btn_play.setEnabled(btn_play_b);
		btn_pause.setEnabled(btn_pause_b);
		btn_stop.setEnabled(btn_stop_b);
		btn_play.setClickable(btn_play_b);
		btn_pause.setClickable(btn_pause_b);
		btn_stop.setClickable(btn_stop_b);
		
		progress.setVisibility(progress_i);

		isPlayed=savedInstanceState.getBoolean("is_played");
		isPaused=savedInstanceState.getBoolean("is_paused");
		textView1.setVisibility(savedInstanceState.getInt("textview"));

		updateCurrentPlayedSong(umpLnk);
	}
	
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if(featureId == Window.FEATURE_ACTION_BAR && menu != null){
			if(menu.getClass().getSimpleName().equals("MenuBuilder")){
				try{
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				}
				catch(Exception e) {

				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}
    
    public void goToURL(String url)
    {
    	Uri uri = Uri.parse(url);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
		
    	int id=item.getItemId();
    		
    	if(id==R.id.menu_vk) {
    		goToURL("https://vk.com/umpako.netlabel");
    		return true;
    	}
    	
    	else if(id==R.id.menu_fb) {
    		goToURL("https://www.facebook.com/umpako.netlabel");
    		return true;
    	}
    	
    	else if(id==R.id.menu_tw) {
    		goToURL("https://x.com/umpako");
    		return true;
    	}
		else if(id==R.id.menu_yt) {
			goToURL("https://www.youtube.com/@umpako");
			return true;
		}
    	else if(id==R.id.menu_st) {
    		goToURL("https://umpako.com");
    		return true;
    	}
    	else if(id==R.id.menu_fav) {
    		goToURL("market://details?id=com.kurbetsoft.umpako");	
    		return true;
    	}
    	else if(id==R.id.menu_open) {
    		goToURL(URL/*+".m3u"*/);	
    		return true;
    	}
		else if(id==R.id.menu_exit) {
			if (iService!=null) {
				Intent serviceIntent=new Intent(this, PlayerService.class);
            	if(mConnection!=null) {
					unbindService(mConnection);
				}
				stopService(serviceIntent);
				iService = null;
       		}
			finish();
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				finishAffinity();
			}
			return true;
		}
		else return false;
	}
}
