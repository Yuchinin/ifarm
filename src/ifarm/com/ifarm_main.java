package ifarm.com;

import org.json.JSONObject;

import ifarm.com.library.PlantFunctions;
import ifarm.com.library.SettingHelper;
import ifarm.com.library.UserDBHandler;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class ifarm_main extends Base {
	WebView engine;
	FrameLayout mWebContainer;
	SettingHelper settings;
	String loadURL;
	SettingHelper st;
	PlantFunctions pf;
	UserDBHandler db;
	Context ct = this;
	String plant_id,METHOD,title,msg,stat;
	TextView txtStatus;
	ImageButton imgbtnWatering,imgbtnWeeding,imgbtnFertilizing,imgbtnHarvesting,imgbtnSpray;
	//private MjpegView mv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ifarm_main);
		
		Bundle bundle = getIntent().getExtras();
		plant_id = bundle.getString("plant_id");
/*
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    //Your code goes here
                	mv.setSource(MjpegInputStream.read(loadURL));
                    Log.d("ifarm_main", "loadURL");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start(); 
	 
        //setContentView(mv);
        
        mv.setDisplayMode(MjpegView.SIZE_BEST_FIT);
 
        mv.showFps(true);
*/		
        //String url = engine.getUrl();
        
        //Dialog("Connected At",loadURL);
		//String title = bundle.getString("TITLE");
		
		//setTitle(title);
	}
	
	private void SetWebView(){
		Log.d("Cam Address", settings.getWebCamIp());
		Log.d("Port",  settings.getPort());
		loadURL = "http://"+settings.getWebCamIp()+":"+settings.getPort();
		//loadURL = "http://gamic.dnsalias.net:7001/img/video.mjpeg";
		
		engine = new WebView(ct);
		engine.setInitialScale(2);
        engine.getSettings().setLoadWithOverviewMode(true);
        engine.getSettings().setUseWideViewPort(true);
        engine.getSettings().setJavaScriptEnabled(true);
        engine.loadUrl(loadURL);
        
		//mv = new MjpegView(this);
		mWebContainer = (FrameLayout)findViewById(R.id.web_container);
        mWebContainer.addView(engine);
        Log.d("ifarm_main", "addView");
	}
	
	private void SetViewAndListener(){
		db = new UserDBHandler(ct);
		pf = new PlantFunctions(ct);
		settings = new SettingHelper(ct);
		//imgbtnLoosing = (ImageButton)findViewById(R.id.imgbtnLoosing);
		//imgbtnSowing = (ImageButton)findViewById(R.id.imgbtnSowing);
		txtStatus = (TextView)findViewById(R.id.txtStatus);
		imgbtnWatering = (ImageButton)findViewById(R.id.imgbtnWatering);
		imgbtnWeeding = (ImageButton)findViewById(R.id.imgbtnWeeding);
		imgbtnFertilizing = (ImageButton)findViewById(R.id.imgbtnFertilizing);
		imgbtnHarvesting = (ImageButton)findViewById(R.id.imgbtnHarvesting);
		imgbtnSpray = (ImageButton)findViewById(R.id.imgbtnSpray);
		
		//imgbtnLoosing.setOnClickListener(btntchlistener);
		//imgbtnSowing.setOnClickListener(btntchlistener);
		imgbtnWatering.setOnClickListener(btntchlistener);
		imgbtnWeeding.setOnClickListener(btntchlistener);
		imgbtnFertilizing.setOnClickListener(btntchlistener);
		imgbtnHarvesting.setOnClickListener(btntchlistener);
		imgbtnSpray.setOnClickListener(btntchlistener);
		
		if(getLanguage().equals("zh_TW")){
			Log.d("Language","TW");
			//imgbtnLoosing.setBackgroundResource(R.drawable.btn_chstatus_loosen_c);
			//imgbtnSowing.setBackgroundResource(R.drawable.btn_chstatus_sowing_c);
			imgbtnWatering.setBackgroundResource(R.drawable.btn_chstatus_watering_c);
			imgbtnWeeding.setBackgroundResource(R.drawable.btn_chstatus_weeding_c);
			imgbtnHarvesting.setBackgroundResource(R.drawable.btn_chstatus_harvest_c);
			imgbtnFertilizing.setBackgroundResource(R.drawable.btn_chstatus_fertilize_c);
			imgbtnSpray.setBackgroundResource(R.drawable.btn_chstatus_spray_c);
		}else{
			Log.d("Language","EN");
			//imgbtnLoosing.setBackgroundResource(R.drawable.btn_chstatus_loosen_e);
			//imgbtnSowing.setBackgroundResource(R.drawable.btn_chstatus_sowing_e);
			imgbtnWatering.setBackgroundResource(R.drawable.btn_chstatus_watering_e);
			imgbtnWeeding.setBackgroundResource(R.drawable.btn_chstatus_weeding_e);
			imgbtnHarvesting.setBackgroundResource(R.drawable.btn_chstatus_harvest_e);
			imgbtnFertilizing.setBackgroundResource(R.drawable.btn_chstatus_fertilize_e);
			imgbtnSpray.setBackgroundResource(R.drawable.btn_chstatus_spray_e);
		}
	}
	
	private ImageButton.OnClickListener btntchlistener = new ImageButton.OnClickListener(){
		@Override
		public void onClick(View v) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			if(v.getId()==R.id.imgbtnWatering){
				METHOD = getString(R.string.ac_water);
				title = getString(R.string.ac_water_str);
				alertDialogBuilder.setIcon(R.drawable.btn_watering_c_a);
			}else if(v.getId()==R.id.imgbtnWeeding){
				METHOD = getString(R.string.ac_weed);
				title = getString(R.string.ac_weed_str);
				alertDialogBuilder.setIcon(R.drawable.btn_weeding_c_a);
			}else if(v.getId()==R.id.imgbtnHarvesting){
				METHOD = getString(R.string.ac_harvest);
				title = getString(R.string.ac_harvest_str);
				alertDialogBuilder.setIcon(R.drawable.btn_harvest_c_a);
			}else if(v.getId()==R.id.imgbtnFertilizing){
				METHOD = getString(R.string.ac_fertilize);
				title = getString(R.string.ac_fertilize_str);
				alertDialogBuilder.setIcon(R.drawable.btn_fertilize_c_a);
			}else if(v.getId()==R.id.imgbtnSpray){
				METHOD = getString(R.string.ac_spray);
				title = getString(R.string.ac_spray_str);
				alertDialogBuilder.setIcon(R.drawable.btn_spray_c_a);
			}
			//--------------------------------------------------
			alertDialogBuilder.setTitle(title);
			alertDialogBuilder.setMessage(getString(R.string.r_u_sure)).setCancelable(false).
					setPositiveButton(getString(R.string.yes),new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						doAction();
					}
				  }).setNegativeButton(getString(R.string.no),new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.dismiss();
					}
				  });
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			//--------------------------------------------------
		}
	};
	
	private void doAction(){
		//====================================================
    	new Thread(new Runnable() {
            @Override
            public void run() {
                  try{
                	  JSONObject json = pf.doAction(db.getEmail(),plant_id, METHOD);
                	  if(Integer.parseInt(json.getString("success"))==1){
                		  msg = getString(R.string.action_success);
                	  }else if(Integer.parseInt(json.getString("success"))==2){
                		  msg = getString(R.string.action_previous_undone)+" ("+title+")";
                	  }else if(Integer.parseInt(json.getString("success"))==3){
                		  msg = "UPDATE products_order FAIL";
                	  }else if(Integer.parseInt(json.getString("success"))==4){
                		  msg = getString(R.string.crop)+" "+getString(R.string.immature);
                	  }
                	  runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            	Dialog(title,msg,false);
                            }
                        });
                     }catch(final Exception e){
                    	 e.printStackTrace();
                          runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Dialog(getString(R.string.error),getString(R.string.error_server),false);
                            }
                        });
                     }
            }
        }).start();
    	//==========================================================
	}
	
	private void getStat(){
		//====================================================
    	new Thread(new Runnable() {
            @Override
            public void run() {
                  try{
                	  stat = pf.getPlantStat(plant_id);
                	  runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            	txtStatus.setTextColor(Color.YELLOW);
                            	txtStatus.setText(stat);
                            }
                        });
                     }catch(final Exception e){
                    	 e.printStackTrace();
                          runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            	txtStatus.setTextColor(Color.RED);
                            	txtStatus.setText(getString(R.string.error_server));
                            }
                        });
                     }
            }
        }).start();
    	//==========================================================
	}

	public void onPause() {
        super.onPause();
        //mv.stopPlayback();
        engine.stopLoading();
}

	@Override
    protected void onDestroy() {
        super.onDestroy();
        mWebContainer.removeAllViews();
        //engine.destroy();
    }
	
	@Override
	public void onResume(){
		super.onResume();
		SetViewAndListener();
		SetWebView();
		getStat();
		
	}
}
