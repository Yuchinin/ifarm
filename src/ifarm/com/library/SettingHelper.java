package ifarm.com.library;

import ifarm.com.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SettingHelper {
	protected Context mycontext;
	private SharedPreferences settings;
	private final String SETTING_NAME = "settings";
	
	private final String IP_SELECTED_KEY = "Ip Selected";
	private final String IP_SAV_KEY = "Ip Address";
	private final String IP_CAM_KEY = "webcam_ip";
	private final String VERSION_NAME = "Version Name";
	private final String VERSION_CODE = "Version Code";
	private final String PORT_SAV_KEY = "Port";
	private final String KEY_SEASON = "season";
	private final String ROOT_FOLDER = "/ifarm_api/android/";
	private final String APK_FOLDER = "/ifarm_api/android/apk/";
	private final String APK_NAME = "Ifarm.apk";
	
	public SettingHelper (Context context){
		this.mycontext = context;
		settings = mycontext.getSharedPreferences(SETTING_NAME,0);
	}
	//--------------------------Static Setting------------------------------
	public String getRootFolder(){
		return ROOT_FOLDER;
    }
	
	public String getAPKUrl(){
		return "adf.ly/aCVuX";
		//return getIp()+APK_FOLDER+APK_NAME;
	}
	
	public void saveIp(String value){
		settings.edit().putString(IP_SAV_KEY,value).commit();
    }
	
	public String getIp(){
		//return settings.getString(IP_SAV_KEY,"120.125.84.100");
		return settings.getString(IP_SAV_KEY,"10.0.3.2");
    }
	
	public void saveWebCamIp(String value){
		settings.edit().putString(IP_CAM_KEY,value).commit();
    }
	
	public String getWebCamIp(){
		return settings.getString(IP_CAM_KEY,"");
    }
	
	public void savePort(String value){
		settings.edit().putString(PORT_SAV_KEY,value).commit();
    }
	
	public String getPort(){
		return settings.getString(PORT_SAV_KEY,"8081");
    }
	
	public void saveSelectedIp(int value){
		settings.edit().putInt(IP_SELECTED_KEY,value).commit();
    }
	
	public String getCurrentSeason(){
		return settings.getString(KEY_SEASON,"");
	}
	
	public void saveCurrentSeason(String value){
		settings.edit().putString(KEY_SEASON,value).commit();
	}
	
	public String getLatestVersionName(){
		return settings.getString(VERSION_NAME,null);
	}
	
	public void saveLatestVersionName(String value){
		settings.edit().putString(VERSION_NAME,value).commit();
	}
	
	public String getLatestVersionCode(){
		return settings.getString(VERSION_CODE,"");
	}
	
	public void saveLatestVersionCode(String value){
		settings.edit().putString(VERSION_CODE,value).commit();
	}
	
	//public int getSelectedIp(){
		//return settings.getInt(IP_SELECTED_KEY,R.id.rbtnEmulator);
    //}
//------------------------------Static Setting End-----------------------------
	public void putString(String key, String _default){
		settings.edit().putString(key,_default).commit();
    }
	
	public String getString(String key, String _default){
		return settings.getString(key,_default);
    }
	
	public void putInt(String key, int _default){
		settings.edit().putInt(key,_default).commit();
    }
	
	public int getInt(String key, int _default){
		return settings.getInt(key, _default);
    }

}
