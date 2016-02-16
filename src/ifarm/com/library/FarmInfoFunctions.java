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
 
public class FarmInfoFunctions {
	 
    private JSONParser jsonParser;
    private SettingHelper settings;
 
    private static String get_tag = "get_farm_info";
    private static String POST_tag = "POST";
    private static String GET_tag = "GET";
    private static String name_key = "name";
    private static String phone_key = "phone";
    private static String address_key = "address";
    private static String email_key = "email";
    private static String info_key = "info";
    private static String vendor_key = "vendor_name";
    private static String bank_key = "bank_name";
    private static String bankac_key = "bank_acc";
    
    Context ct;
    // constructor
    public FarmInfoFunctions(Context context){
    	this.ct = context;
        jsonParser = new JSONParser();
        settings = new SettingHelper(ct);
    }
    
    public JSONObject getFarmInfo() {
    	JSONObject json = null;
    	String URL = "http://"+settings.getIp()+settings.getRootFolder()+"get_farm_info.php";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_tag));
        try {
        	json = jsonParser.makeHttpRequest(URL, POST_tag, params);
        	settings.putString(name_key, json.getString(name_key));
        	settings.putString(phone_key, json.getString(phone_key));
        	settings.putString(address_key, json.getString(address_key));
        	settings.putString(email_key, json.getString(email_key));
        	settings.putString(info_key, json.getString(info_key));
        	settings.putString(vendor_key, json.getString(vendor_key));
        	settings.putString(bank_key, json.getString(bank_key));
        	settings.putString(bankac_key, json.getString(bankac_key));
		} catch (Exception e){
			e.printStackTrace();
		}
        return json;
    }
    
    public String getName(){
    	return settings.getString(name_key, null);
    }
    
    public String getPhone(){
    	return settings.getString(phone_key, null);
    }
    
    public String getAddress(){
    	return settings.getString(address_key, null);
    }
    
    public String getEmail(){
    	return settings.getString(email_key, null);
    }
    
    public String getInfo(){
    	return settings.getString(info_key, null);
    }
    
    public String getVendor(){
    	return settings.getString(vendor_key, null);
    }
    
    public String getBankName(){
    	return settings.getString(bank_key, null);
    }
    
    public String getBankAccount(){
    	return settings.getString(bankac_key, null);
    }
}
