package ifarm.com.library;

import ifarm.com.R;

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
 
public class UserFunctions {
	 
    private JSONParser jsonParser;
    private SettingHelper settings;
    private JSONArray J;
 
    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String changedetails_tag = "change_details";
    private static String adminchangedetails_tag = "admin_change_details";
    private static String POST_tag = "POST";
    private static String GET_tag = "GET";
    
    Context myContext;
    // constructor
    public UserFunctions(Context context){
    	this.myContext = context;
        jsonParser = new JSONParser();
        settings = new SettingHelper(myContext);
    }
    
    public String getIdentityString(Context ct, int lvl){
    	 String tmp = "[Error getIdentityString()]";
    	 if(lvl<5){
     		tmp = ct.getString(R.string.user_normal);
     	}else if(lvl>=10 && lvl<50){
     		tmp = ct.getString(R.string.user_farmer);
     	}else if(lvl>=50 && lvl<99){
     		tmp = ct.getString(R.string.user_basic);
     	}else if(lvl>=99){
     		tmp = ct.getString(R.string.user_admin);
     	}	
		return tmp;
    }
    
    public JSONArray GetAllUsers(){
    	String URL = 
    			"http://"+settings.getIp()+settings.getRootFolder()+"get_all_users.php";
    	
    	// Building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	// getting JSON string from URL
    	JSONObject json = jsonParser.makeHttpRequest(URL, GET_tag, params);
    	
    	try {
			J = json.getJSONArray("users");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return J;
    	
    }
 
    /**
     * function make Login Request
     * @param email
     * @param password
     * */
    public JSONObject loginUser(String email, String password){
    	String URL = 
    			"http://"+settings.getIp()+settings.getRootFolder();
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        
        //SettingHelper settings = new SettingHelper(getApplicationContext());
        //settings.getString(, "Url", "http://10.0.2.2/ifarm_api/");
        
        JSONObject json = jsonParser.makeHttpRequest(URL, POST_tag, params);

        Log.d("CurrentMethod", 
        		new Exception().getStackTrace()[0].getMethodName()+" json:"+json);
        
        return json;
    }
 
    /**
     * function make Login Request
     * @param name
     * @param email
     * @param password
     * */
    public JSONObject registerUser(
    		String name,String email,String pass,String phone,String gender,String addr,String birth){
    	String URL = 
    			"http://"+settings.getIp()+settings.getRootFolder();
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", pass));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("gender", gender));
        params.add(new BasicNameValuePair("address", addr));
        params.add(new BasicNameValuePair("birthday", birth));
 
        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(URL, POST_tag, params);
        
        Log.d("CurrentMethod", 
        		new Exception().getStackTrace()[0].getMethodName()+" json:"+json);
        return json;
    }
    
    /**
     * function make change pass Request
     * @param name
     * @param email
     * @param password
     * */
    public JSONObject ChangeDetails(
    		String name, String email, String password,
    		String newpassword,String phone,String gender,String addr,String birth){
    	String URL = "http://"+settings.getIp()+settings.getRootFolder();
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", changedetails_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("newpassword", newpassword));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("gender", gender));
        params.add(new BasicNameValuePair("address", addr));
        params.add(new BasicNameValuePair("birthday", birth));
 
        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(URL, POST_tag, params);
        
        Log.d(new Exception().getStackTrace()[0].getMethodName(),json.toString());
        return json;
    }
    
    public JSONObject AdminChangeDetails(
    		String name,String email,String level,String phone,String gender,
    		String password,String email_admin,String addr,String birth){
    	String URL = "http://"+settings.getIp()+settings.getRootFolder();
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", adminchangedetails_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("gender", gender));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("email_admin", email_admin));
        params.add(new BasicNameValuePair("address", addr));
        params.add(new BasicNameValuePair("birthday", birth));
 
        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(URL, POST_tag, params);
        // return json
        Log.d(new Exception().getStackTrace()[0].getMethodName(),json.toString());
        return json;
    }
    
    public String getImageUrl(String email){
    	String URL = "http://"+settings.getIp()+settings.getRootFolder();
    	String url = null;
    	JSONObject json = null;
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "get_imageurl"));
        params.add(new BasicNameValuePair("email", email));
        // getting JSON Object
        try {
        	json = jsonParser.makeHttpRequest(URL, POST_tag, params);
			url = json.getString("imageurl");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Log.d(new Exception().getStackTrace()[0].getMethodName(),json.toString());
    	return url;
    }
    
    public String getImage(Context context, String email,int lvl,String gender,String url){
    	SettingHelper settings = new SettingHelper(context);
    	 String imgurl;
    	
    	//int lvl = Integer.parseInt(level);
        String tmp = "http://"+settings.getIp()+settings.getRootFolder()+"pic/"+email+".png";
        if(url==null){
        	imgurl = getImageUrl(email);
        }else{
        	imgurl = url;
        }
        if((imgurl.equals("")) || (imgurl.length()==0)){
        	Log.d("Url Check","No pic found !");
        	Log.d("Url Check","Use Default Image!");
        	if(lvl<5){
        		if(gender.equals("M")){
        			imgurl = "drawable://" + R.drawable.male;
        		}else{
        			imgurl = "drawable://" + R.drawable.female;
        		}
        	}else if(lvl>=10 && lvl<50){
        		imgurl = "drawable://" + R.drawable.farmer;
        	}else if(lvl>=50 && lvl<99){
        		imgurl = "drawable://" + R.drawable.basic;
        	}else if(lvl>=99){
        		imgurl = "drawable://" + R.drawable.admin;
        	}	
        }else{
        	imgurl = tmp;
        }
		return imgurl;
    }
 
    /**
     * Function get Login status
     * */
    public boolean isUserLoggedIn(){
        UserDBHandler db = new UserDBHandler(myContext);
        if(db.getRowCount() > 0){
            // user logged in
            return true;
        }
        return false;
    }
 
    /**
     * Function to logout user
     * Reset Database
     * */
    public boolean logoutUser(){
        UserDBHandler db = new UserDBHandler(myContext);
        db.resetTables();
        return true;
    }
    
}
