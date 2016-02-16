package ifarm.com.library;

import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
 
public class BazaarFunctions {
	 
    private JSONParser jsonParser;
    private SettingHelper settings;
 
    private static String POST_tag = "POST";
    private static String GET_tag = "GET";
    private static String imgfolder = "/ifarm_api/Android/bazaar_img/";
    
    Context myContext;
    // constructor
    public BazaarFunctions(Context context){
    	this.myContext = context;
        jsonParser = new JSONParser();
        settings = new SettingHelper(myContext);
    }
    
    public String GetImageFolder() {
    	return imgfolder;
    }
    
    public JSONArray GetAllBazaars(){
    	String GetAllURL = "http://"+settings.getIp()+settings.getRootFolder()+"get_all_bazaars.php";
    	JSONArray J = null;
    	// Building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	// getting JSON string from URL
    	
		try {
			JSONObject json = jsonParser.makeHttpRequest(GetAllURL, GET_tag, params);
			J = json.getJSONArray("bazaars");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return J;
    }

}
