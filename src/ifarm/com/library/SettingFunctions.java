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
 
public class SettingFunctions {
	 
    private JSONParser jsonParser;
    private SettingHelper settings;
 
    private static String POST_tag = "POST";
    private static String GET_tag = "GET";
    
    Context myContext;
    // constructor
    public SettingFunctions(Context context){
    	this.myContext = context;
        jsonParser = new JSONParser();
        settings = new SettingHelper(myContext);
    }
    
    public JSONObject ChangeSetting(String table,String column,String value){
    	String URL = "http://"+settings.getIp()+settings.getRootFolder()+"setting_change.php";
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("table", table));
        params.add(new BasicNameValuePair("column", column));
        params.add(new BasicNameValuePair("value", value));
 
        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(URL, POST_tag, params);
        // return json
        Log.d(new Exception().getStackTrace()[0].getMethodName(),json.toString());
        return json;
    }
}
