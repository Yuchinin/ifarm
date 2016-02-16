package ifarm.com;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import ifarm.com.library.BaseFunctions;
import ifarm.com.library.SettingHelper;
import ifarm.com.library.UserDBHandler;
import ifarm.com.library.UserFunctions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

public abstract class Base extends Activity {
	//
	//protected ImageLoader imageLoader = ImageLoader.getInstance();
	//
	Context context = this;
	UserDBHandler db;
	SettingHelper st;
	protected static final int MENU_ABOUT = 1;
	//protected static final int MENU_SETTING = 2;
	//protected static final int MENU_QUIT = MENU_SETTING +1;
	protected static final int MENU_LOGOUT = 3;
	protected static final int MENU_USERLIST = 4;
	protected static final int MENU_CART = 5;
	protected static final int MENU_ORDERLIST = 6;
	protected static final int MENU_ACTIONLIST = 7;
	protected static final int MENU_DEVELOPER = 8;
	protected static final int MENU_MSGBOARD = 9;
	protected static final String INTENT_FROM = "RLF";

	
	BaseFunctions baseFunctions;

	@Override
    public boolean onPrepareOptionsMenu(Menu menu){
		if(menu.hasVisibleItems())
			menu.clear();
		Log.d("this.getClass().getSimpleName()", this.getClass().getSimpleName());
		db = new UserDBHandler(context);
		UserFunctions ufs = new UserFunctions(context);
		int lvl = 0;
		String name = null;
		//menu.add(0,MENU_QUIT,2,getString(R.string.quit)).setShortcut('2','e');
    	menu.add(0,MENU_ABOUT,0,getString(R.string.about)).setShortcut('0','a');
    	//menu.add(0,MENU_SETTING,1,getString(R.string.setting)).setShortcut('1','s');
    	if(ufs.isUserLoggedIn()){
    		Log.d("isUserLoggedIn","show logout menu");
			//Cursor c = db.get(1);
	    	lvl = db.getLevel();
	    	name = db.getName();
    		menu.add(0,MENU_LOGOUT,2,getString(R.string.logout)+" ["+name+"]").setShortcut('2','o');
    		menu.add(0,MENU_CART,4,getString(R.string.cart)).setShortcut('4','c');
    		menu.add(0,MENU_MSGBOARD,8,getString(R.string.msg_board)).setShortcut('8','m');
		}
    	if(lvl>=50){
    		//menu.add(0,MENU_USERLIST,3,getString(R.string.userlist)).setShortcut('3','u');
    		//menu.add(0,MENU_ORDERLIST,5,getString(R.string.orderlist)).setShortcut('5','s');
    		//menu.add(0,MENU_ACTIONLIST,6,getString(R.string.actionlist)).setShortcut('6','p');
    		menu.add(0,MENU_DEVELOPER,7,getString(R.string.developer)).setShortcut('7','d');
    	}
    	
    	return super.onCreateOptionsMenu(menu); 	
    }
    
	@Override
    public boolean onOptionsItemSelected(MenuItem item){
		String todo = 
				"(Deprecated) \n"+
				"Crop Data Collect \n"+
				"Timer Function \n"+
				"Google Map Api \n"+
				"Animation Research\n"+
				"";
		String wip = 
				"(Deprecated) \n"+
				"WebView Capture \n"+
				"SuperUser Management \n"+
				"Order Function \n"+
				"Splash Screen \n"+
				"";
		String done = 
				"(Deprecated) \n"+
				"(0.1 Alpha) Basic Interface\n"+
				"(0.1 Alpha) Bundle Transfer\n"+
				"(0.1 Alpha) Database Configuration\n"+
				"(0.1 Alpha) Database Sample\n"+
				"(0.1 Alpha) Database Linkup\n"+
				"(0.2 Alpha) Database Upgrade Method\n"+
				"(0.2 Alpha) Database Memory Leak\n"+
				"(0.3) ifarm_api\n"+
				"(0.3.1) Tweak ifarm_api\n"+
				"(0.3.1) Check Network Status\n"+
				"(0.3.1) Check Server Status\n"+
				"(0.4) Tweak Interface\n"+
				"(0.4.1) Tweak Check Server Status \n"+
				"(0.5) U Farm I Farm Added \n"+
				"(0.5.1) String Tweak \n"+
				"(0.5.2) Setting Page on menu \n"+
				"(0.5.2) Dynamic Ip Change \n"+
				"(0.5.3) Fix Memory Leak in Webview \n"+
				"(0.6) Asynctask Implement \n"+
				"(0.6) Dialog Implement \n"+
				"(0.6) Loading Screen Implement \n"+
				"(0.7) Fix Login & Register Error \n"+
				"(0.7) LoginCheckTask Implement \n"+
				"(0.7) Connection to Server Require - U Farm I Farm \n"+
				"(0.8) Web Server Upload/Download Image \n"+
				"(0.8) Camera Function \n";
		
    	switch (item.getItemId()){
	    	case MENU_ABOUT:
	    		Intent intentAbout = new Intent();
	    		intentAbout.setClass(getApplicationContext(),about.class);
				Bundle bundle = new Bundle();
				bundle.putString("TODO", todo);
				bundle.putString("WIP", wip);
				bundle.putString("DONE", done);
				intentAbout.putExtras(bundle);
				startActivity(intentAbout);
	    		break;
	    	//case MENU_SETTING:
	    		//Dialog(getString(R.string.error),"Function Obsolete",false);
	    		//Intent intentSetting = new Intent();
	    		//intentSetting.setClass(getApplicationContext(),Setting.class);
	    		//startActivity(intentSetting);
		    	//break;
	    	case MENU_LOGOUT:
	    		DialogLogoutYesNo();
	    		Log.d("current class", this.getClass().getSimpleName());
				break;
	    	case MENU_USERLIST:
	    		Intent intent1 = new Intent();
	    		intent1.setClass(context,UserList.class);
	    		startActivity(intent1);
	    		break;
	    	case MENU_CART:
	    		Intent intent2 = new Intent();
	    		intent2.setClass(context,CartList.class);
	    		startActivity(intent2);
	    		break;
	    	case MENU_ORDERLIST:
	    		Intent intent3 = new Intent();
	    		intent3.setClass(context,AllOrderList.class);
	    		startActivity(intent3);
	    		break;
	    	case MENU_ACTIONLIST:
	    		Intent intent4 = new Intent();
	    		intent4.setClass(context,AllActionList.class);
	    		startActivity(intent4);
	    		break;
	    	case MENU_DEVELOPER:
	    		Intent intent5 = new Intent();
	    		intent5.setClass(context,Developer.class);
	    		startActivity(intent5);
	    		break;
	    	case MENU_MSGBOARD:
	    		Intent intent6 = new Intent();
	    		intent6.setClass(context,MessageBoard.class);
	    		startActivity(intent6);
	    		break;
    	}
    	return super.onOptionsItemSelected(item);   			
	}
	
	public void Dialog(String title,String msg,final Boolean finish){
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle(title);
		if(title.equals(getString(R.string.error)))
			alertDialogBuilder.setIcon(R.drawable.icon_error);
		alertDialogBuilder.setMessage(msg).setCancelable(false)
			.setPositiveButton(getString(R.string.dismiss),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.dismiss();
					if(finish==true){
						finish();
					}
				}
			  });
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
    }
    
    public void DialogFinishYesNo(){
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle(getString(R.string.quit));
		alertDialogBuilder.setMessage(getString(R.string.r_u_sure)).setCancelable(false).
				setPositiveButton(getString(R.string.yes),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.dismiss();
					finish();
				}
			  }).setNegativeButton(getString(R.string.no),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.dismiss();
				}
			  });
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
    }
    
    public void startWeb(String url) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse("http://"+url));
		startActivity(i);
	}
    
    public void DialogUpdateYesNo(){
    	st = new SettingHelper(context);
    	baseFunctions = new BaseFunctions(context);
    	String local_ver_name = baseFunctions.GetVersionName();
    	int local_ver_code = baseFunctions.GetVersionCode();
    	String server_ver_name = st.getLatestVersionName();
    	int server_ver_code = Integer.parseInt(st.getLatestVersionCode());
    	String tmp = "\n"+context.getString(R.string.verName)+local_ver_name+"\n"+
    			context.getString(R.string.verCode)+local_ver_code+"\n"+
    			context.getString(R.string.verNameLatest)+server_ver_name+"\n"+
    			context.getString(R.string.verCodeLatest)+server_ver_code;
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle(getString(R.string.update));
		
		alertDialogBuilder.setMessage(getString(R.string.update_msg)+tmp).setCancelable(false).
				setPositiveButton(getString(R.string.yes),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					
					startWeb(st.getAPKUrl());
				}
			  }).setNegativeButton(getString(R.string.no),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					
				}
			  });
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
    }
    
    public void DialogLogoutYesNo(){
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		// set title
		alertDialogBuilder.setTitle(getString(R.string.logout));

		// set dialog message
		alertDialogBuilder.setMessage(getString(R.string.r_u_sure)).setCancelable(false).
				setPositiveButton(getString(R.string.yes),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					// current activity
					Log.d("current class", context.getClass().getSimpleName());
					//---------------------
					Intent intent = new Intent(context, Main.class);

	                // Close all views before launching Dashboard
	                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                startActivity(intent);
	                UserFunctions ufs = new UserFunctions(context);
		    		ufs.logoutUser();
	             // Close Login Screen
	                finish();
					//----------------------
		    		//if(!context.getClass().getSimpleName().equals("Main")){
		    			//Log.d("current class", context.getClass().getSimpleName());
		    			//finish();
		    		//}
						
				}
			  }).setNegativeButton(getString(R.string.no),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					// current activity
					dialog.dismiss();
					//finish();
				}
			  });
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
    }
    
    public void WelcomeDialog(String title,String name,final Boolean finish){
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		// set title
		alertDialogBuilder.setTitle(title);

		// set dialog message
		alertDialogBuilder.setMessage(getString(R.string.welcomeback)+name).setCancelable(false)
			.setPositiveButton(getString(R.string.dismiss),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.dismiss();
					//Bundle bundle = getIntent().getExtras();
					if(finish==true){
						//Log.d(REQUEST_LOGIN_FROM, getIntent().getStringExtra(REQUEST_LOGIN_FROM));
						//if(getIntent().getStringExtra(REQUEST_LOGIN_FROM).equals("Plant_details"))
							finish();
					}else{
						Intent intent = new Intent();
	            		// Launch Dashboard Screen
						db = new UserDBHandler(context);
						if(db.getLevel()>=10){
							intent.setClass(context, Developer.class);
						}else{
							intent.setClass(context, Dashboard.class);
						}
		                startActivity(intent);
		                finish();
	            	}
				}
			  });
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
    }
    
    public String getLanguage(){
    	return Locale.getDefault().toString();
    }
    
    public void showDatePickerDialog(View v){
    	Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(v.getContext(), datePickerListener, 
                year-18, month,day);
        dialog.show();
    }
    
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			//Do whatever you want
			Calendar c = Calendar.getInstance();
			int nyear = c.get(Calendar.YEAR);
			if((nyear-year)>=18){
				String tmp = Integer.toString(month+1)+"-"
						+Integer.toString(day)+"-"
						+Integer.toString(year);
				EditText date = (EditText)findViewById(R.id.edtDate);
				// Parse the input date
				SimpleDateFormat fmt = new SimpleDateFormat("MM-dd-yyyy");
				Date inputDate = null;
				try {
					inputDate = fmt.parse(tmp);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Create the MySQL datetime string
				fmt = new SimpleDateFormat("yyyy-MM-dd");
				String dateString = fmt.format(inputDate);
				//date.setText(String.valueOf(year) + "/" + String.valueOf(month+1) + "/" + String.valueOf(day));
				date.setText(dateString);
			}else{
				Dialog(getString(R.string.age_check),getString(R.string.require_adult),false);
			}
				
		}
    };
    
    @Override
	public void onResume() {  // After a pause OR at startup
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
	    super.onResume();
	    //Refresh your stuff here
	    
	     }
	
}
