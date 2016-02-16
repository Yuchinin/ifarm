package ifarm.com.library;

import ifarm.com.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
 
public class BaseFunctions {
	 
    private JSONParser jsonParser;
    private SettingHelper settings;
 
    private static String getSETTING_tag = "get_setting";
    private static String POST_tag = "POST";
    private static String GET_tag = "GET";
    
    Context ct;
    // constructor
    public BaseFunctions(Context context){
    	this.ct = context;
        jsonParser = new JSONParser();
        settings = new SettingHelper(ct);
    }
    
    public String GetVersionName(){
    	try {
			return ct.getPackageManager().getPackageInfo(ct.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
    }
    
    public int GetVersionCode(){
    	try {
			return ct.getPackageManager().getPackageInfo(ct.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return -1;
		}
    }
    public String basicCheck() {
    	String tmp = "success";
    	if(!isOnline()) {
    		tmp = ct.getString(R.string.error_internet);
    		//Dialog(ct.getString(R.string.error),tmp);
    		return tmp;
    	}
    	if(!isServerOnline()) {
    		tmp = ct.getString(R.string.error_server);
    		//Dialog(ct.getString(R.string.error),tmp);
    		return tmp;
    	}
		return tmp;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
            (ConnectivityManager) ct.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected() && netInfo.isAvailable()) {
            return true;
        }
        return false;
    }
    
    public boolean isServerOnline() {
    	JSONObject json;
    	String season = null;
    	String ver_name = null;
    	String ver_code = null;
    	String webcam_ip = null;
    	String port = null;
    	
    	Log.d("isServerOnline", "Initial");
    	String URL = "http://"+settings.getIp()+settings.getRootFolder()+"get_setting.php";
    	Log.d("isServerOnline:", URL);
    	//--
    	// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", getSETTING_tag));

        //String onlinekey = "0";
        try {
        	json = jsonParser.makeHttpRequest(URL, POST_tag, params);
        	season = json.getString("season");
        	ver_name = json.getString("version_name");
        	ver_code = json.getString("version_code");
        	webcam_ip = json.getString("webcam_ip");
        	port = json.getString("port");
        	settings.saveCurrentSeason(season);
        	settings.saveLatestVersionName(ver_name);
        	settings.saveLatestVersionCode(ver_code);
        	settings.saveWebCamIp(webcam_ip);
        	Log.d("saveCamIp",webcam_ip);
        	Log.d("getCamIp",settings.getWebCamIp());
        	settings.savePort(port);
        	Log.d("Season Now", season);
            return true;
        	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
    }
    
    public void Dialog(String title,String msg){
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ct);
		alertDialogBuilder.setTitle(title);
		alertDialogBuilder.setMessage(msg).setCancelable(false)
			.setPositiveButton(ct.getString(R.string.dismiss),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.dismiss();
				}
			  });
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
    }
    
    public String getDayFromString(String indate){
    	SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
		try {
			date = inFormat.parse(indate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        SimpleDateFormat outFormat = new SimpleDateFormat("EEE");
        String goal = outFormat.format(date); 
        return goal;
    }
 
}
