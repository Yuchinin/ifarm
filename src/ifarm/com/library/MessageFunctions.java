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
 
public class MessageFunctions {
	 
    private JSONParser jsonParser;
    private UserDBHandler db;
    private SettingHelper settings;
 
    private static String POST_tag = "POST";
    private static String GET_tag = "GET";
    
    Context myContext;
    // constructor
    public MessageFunctions(Context context){
    	this.myContext = context;
        jsonParser = new JSONParser();
        db = new UserDBHandler(context);
        settings = new SettingHelper(myContext);
    }
   
    
    public JSONObject PostMessage(String msg){
    	String URL = "http://"+settings.getIp()+settings.getRootFolder()+"msg_board.php";
    	JSONObject json = null;
    	// Building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", "post"));
    	params.add(new BasicNameValuePair("email", db.getEmail()));
    	params.add(new BasicNameValuePair("msg", msg));
		json = jsonParser.makeHttpRequest(URL, POST_tag, params);
		return json;
    }
    
    public JSONObject GetMessage(){
    	String URL = "http://"+settings.getIp()+settings.getRootFolder()+"msg_board.php";
    	// Building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", "get"));
    	JSONObject json = jsonParser.makeHttpRequest(URL, POST_tag, params);
		return json;
    }

}
