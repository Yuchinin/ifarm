package ifarm.com;

import java.io.IOException;

import ifarm.com.library.BaseFunctions;
import ifarm.com.library.FarmInfoFunctions;
import ifarm.com.library.SettingHelper;
import ifarm.com.library.UserDBHandler;
import ifarm.com.library.UserFunctions;
import ifarm.com.universalimageloader.ImageGridActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class Main extends Base {
	//A ProgressDialog object  
    private ProgressDialog progressDialog;
	final Context context = this;
	UserFunctions userFunctions;
	FarmInfoFunctions ff;
	UserDBHandler userdb = null;
	SQLiteDatabase db = null;
	SettingHelper settings;
	Intent intent;
	
	LinearLayout lnl;
	ImageButton ibtn_explore,ibtn_myifarm,ibtn_market;
	AnimationDrawable ad_explore ,ad_market ,ad_myifarm;  
    CountDownTimer cd_all;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                //WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		userFunctions = new UserFunctions(context);
		baseFunctions = new BaseFunctions(context);
		ff = new FarmInfoFunctions(context);
		//Initialize a LoadViewTask object and call the execute() method  
        new LoadViewTask().execute();
        //setContentView(R.layout.main);
        Log.d("Shouldbeafter", "SetViewAndListener");
        intent = new Intent();
	}
	
	@Override
	public void onBackPressed() {
		DialogFinishYesNo();
	}
	
	private ImageButton.OnClickListener btntchlistener = new ImageButton.OnClickListener(){
		@Override
		public void onClick(View v) {
			stopAnimation();
			// TODO Auto-generated method stub
			if(v.getId()==R.id.ibtn_myifarm){
				ibtn_myifarm.setImageResource(R.drawable.btn_home_myifarm_a);
				new MainAsyncTask().execute();
			}else {
				if(v.getId()==R.id.ibtn_market){
					ibtn_market.setImageResource(R.drawable.btn_home_market_a);
					intent.setClass(context,bazaar.class);
				}else if(v.getId()==R.id.ibtn_explore){
					ibtn_explore.setImageResource(R.drawable.btn_home_explore_a);
					intent.setClass(context,Explore_Crops.class);
				}
				startActivity(intent);
			}
		}
	};
	
	private void SetViewAndListener() {
		lnl = (LinearLayout)findViewById(R.id.lnl);
	    ibtn_explore = (ImageButton)findViewById(R.id.ibtn_explore);
	    ibtn_myifarm = (ImageButton)findViewById(R.id.ibtn_myifarm);
	    ibtn_market = (ImageButton)findViewById(R.id.ibtn_market);
	    setAnimation();
		ibtn_explore.setOnClickListener(btntchlistener);
		ibtn_myifarm.setOnClickListener(btntchlistener);
		ibtn_market.setOnClickListener(btntchlistener);
		Log.d("SetViewAndListener", "pass");
		lnl.setBackgroundResource(R.drawable.bg_home);
		ibtn_explore.setImageResource(R.drawable.btn_home_explore);
		ibtn_myifarm.setImageResource(R.drawable.btn_home_myifarm);
		ibtn_market.setImageResource(R.drawable.btn_home_market);
	}
	
	private void setAnimation(){
		int int_second = 1000*3;
		cd_all = new CountDownTimer(int_second,1000){         
            @Override
            public void onTick(long v) {
            	ibtn_explore.setImageResource(R.drawable.btn_home_explore);
            	ibtn_explore.setImageResource(R.drawable.effect_home_explore);  
            	ad_explore = (AnimationDrawable) ibtn_explore.getDrawable();  
            	ad_explore.start();
            	ibtn_market.setImageResource(R.drawable.btn_home_market);
            	ibtn_market.setImageResource(R.drawable.effect_home_market);  
            	ad_market = (AnimationDrawable) ibtn_market.getDrawable();  
            	ad_market.start();
            	ibtn_myifarm.setImageResource(R.drawable.btn_home_myifarm);
            	ibtn_myifarm.setImageResource(R.drawable.effect_home_myifarm);  
            	ad_myifarm = (AnimationDrawable) ibtn_myifarm.getDrawable();  
            	ad_myifarm.start();
            }
            @Override
            public void onFinish() {
            	this.start();
            }
        }.start();
	}
	
	public void stopAnimation(){
		cd_all.cancel();
	}
	
	private void resumeAnimation(){
		if(cd_all !=null){
			cd_all.start();
		}
	}
	
	private class MainAsyncTask extends AsyncTask<Void, String, String> {

        private ProgressDialog dialog = new ProgressDialog(context);
        String msg = "success";

        /** progress dialog to show user that the backup is processing. */
        /** application context. */

        protected void onPreExecute() {
            this.dialog.setMessage(getString(R.string.loading));
            this.dialog.setCancelable(false);
            this.dialog.setIndeterminate(false);
            //this.dialog.setTitle(getString(R.string.app_name));
            this.dialog.setTitle(" ");
            this.dialog.setIcon(R.drawable.btn_chhome_ifarm);
            this.dialog.show();
        }
        
        @Override  
        protected void onProgressUpdate(String... values)  
        {  
            //set the current progress of the progress dialog  
            dialog.setMessage(values[0]);  
        }
        
        @Override
		protected String doInBackground(Void... arg) {
        		publishProgress(getString(R.string.connecting_server));
        		//try{
        			publishProgress(getString(R.string.check_identity));
                    if(userFunctions.isUserLoggedIn()){
                    	publishProgress(getString(R.string.logon));
                    	Log.d("isUserLoggedIn", "true");
                    	intent.setClass(context, Dashboard.class);
                    }
                    else{
                    	publishProgress(getString(R.string.goto_login));
                    	Log.d("isUserLoggedIn", "false");
                    	intent.setClass(context, Login_main.class);
                    }
        		//} catch (Exception e){
            		//e.printStackTrace();
            		//return msg;
            	//}
			return msg;
		}

        @Override
        protected void onPostExecute(String msg) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if(!msg.equals("success"))
            	Dialog(getString(R.string.error),msg,false);
            else{
                startActivity(intent);
            }
        }
	}
	
	@Override
	public void onResume()
	    {  // After a pause OR at startup
	    super.onResume();
	    //Refresh your stuff here
	    resumeAnimation();
	}
	
	//To use the AsyncTask, it must be subclassed  
    private class LoadViewTask extends AsyncTask<Void, Integer, String> {
        //Before running code in separate thread  
        @Override  
        protected void onPreExecute()  
        {  
            //Create a new progress dialog  
            progressDialog = new ProgressDialog(context);  
            //Set the progress dialog to display a horizontal progress bar  
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
            //Set the dialog title to 'Loading...'  
            progressDialog.setTitle(getString(R.string.app_name));  
            //Set the dialog message to 'Loading application View, please wait...'  
            progressDialog.setMessage(getString(R.string.loadingmain));  
            //This dialog can't be canceled by pressing the back key  
            progressDialog.setCancelable(false);  
            //This dialog isn't indeterminate  
            progressDialog.setIndeterminate(false);  
            //The maximum number of items is 100  
            progressDialog.setMax(100);  
            //Set the current progress to zero  
            progressDialog.setProgress(0);  
            //Display the progress dialog
            progressDialog.setIcon(R.drawable.icon2);
            progressDialog.show();  
        }  
  
        //The code to be executed in a background thread.  
        @Override  
        protected String doInBackground(Void... params)  
        {  
            /* This is just a code that delays the thread execution 4 times, 
             * during 850 milliseconds and updates the current progress. This 
             * is where the code that is going to be executed on a background 
             * thread must be placed. 
             */  
            try{
                //Get the current thread's token  
                synchronized (this){  
                	//Initialize an integer (that will act as a counter) to zero  
                    int counter = 0;
                    publishProgress(counter);
                	settings = new SettingHelper(context);
                	counter+=20;
                	publishProgress(counter);
                	//settings.saveIp("120.125.84.100");
                	ff.getFarmInfo();
                	counter+=20;
                	publishProgress(counter);
                	userdb = new UserDBHandler(context);
                	counter+=20;
                	publishProgress(counter);
                	baseFunctions.isServerOnline();
                	counter+=20;
                	publishProgress(counter);
                	
                    //While the counter is smaller than four  
                    while(counter <= 100){  
                        //Wait 850 milliseconds  
                        this.wait(20);  
                        //Increment the counter  
                        counter++;  
                        //Set the current progress.  
                        //This value is going to be passed to the onProgressUpdate() method.  
                        publishProgress(counter);  
                    }
                }
                return "1";
            }catch (Exception e){
            	return getString(R.string.error_server);
            }  
        }  
  
        //Update the progress  
        @Override  
        protected void onProgressUpdate(Integer... values)  
        {  
            //set the current progress of the progress dialog  
            progressDialog.setProgress(values[0]);  
        }  
  
        //after executing the code in the thread  
        @Override  
        protected void onPostExecute(String result)  
        {  
            //close the progress dialog  
            progressDialog.dismiss();  
            
            //initialize the View  
            Log.d("dismiss", "pass");
            if(result.equals("1")){
            	if(Integer.parseInt(settings.getLatestVersionCode())>baseFunctions.GetVersionCode()){
                	DialogUpdateYesNo();
                }
            	//setContentView(R.layout.main);
                Log.d("setContentView", "pass");
                SetViewAndListener();
            }
            else
            	Dialog(getString(R.string.error),result,true);
            
        }  
    }
}
