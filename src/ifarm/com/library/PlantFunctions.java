package ifarm.com.library;

import ifarm.com.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.content.Context;
import android.util.Log;
 
public class PlantFunctions {
///*	 
    private JSONParser jsonParser;
    private SettingHelper settings;
    private static String POST_tag = "POST";
    private static String GET_tag = "GET";
    
    String[] desc = {"name","price","location","season","plantable","dayuse","info"};
    static String imgfolder = "/ifarm_api/android/product_img/";
    
    Context ct;
    // constructor
    public PlantFunctions(Context context){
    	this.ct = context;
        jsonParser = new JSONParser();
        settings = new SettingHelper(ct);
    }
    
    public String getPlantStat(String plant_id){
    	String status = ct.getString(R.string.status)+":";
    	String URL = "http://"+settings.getIp()+settings.getRootFolder()+"order_index.php";
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", "getPlantStat"));
    	params.add(new BasicNameValuePair("plant_id", plant_id));
    	try{
    		JSONObject json = jsonParser.makeHttpRequest(URL, POST_tag, params);
        	if(json.getString("status").equals("MATURE")){
        		status += ct.getString(R.string.mature);
        	}else if(json.getString("status").equals("DEATH")){
        		status += ct.getString(R.string.death);
        	}else{
        		status += ct.getString(R.string.immature);
        	}
    	}catch(Exception e){
    		status += ct.getString(R.string.error_server);
    	}
    	return status;
    }
    
    public String getStatus(String status){
    	String tmp = ct.getString(R.string.status)+":";
    	if(status.equals("DONE")) tmp += ct.getString(R.string.done);
    	else if(status.equals("PENDING")) tmp += ct.getString(R.string.pending);
    	else tmp += ct.getString(R.string.undone);
    	return tmp;
    }
    
    public String getRequest(String action_request){
    	String tmp = ct.getString(R.string.action)+":";
    	if(action_request.equals("PLANT")) tmp += ct.getString(R.string.ac_plant_str);
    	else if(action_request.equals("WATER")) tmp += ct.getString(R.string.ac_water_str);
    	else if(action_request.equals("SPRAY")) tmp += ct.getString(R.string.ac_spray_str);
    	else if(action_request.equals("WEED")) tmp += ct.getString(R.string.ac_weed_str);
    	else if(action_request.equals("HARVEST")) tmp += ct.getString(R.string.ac_harvest_str);
    	else if(action_request.equals("FERTILIZE")) tmp += ct.getString(R.string.ac_fertilize_str);
    	return tmp;
    }
    
    public String getLanguage(){
    	return Locale.getDefault().toString();
    }
    
    public String getSeasonString(String seasoncode){
    	String tmp = null;
    	String season = seasonToUpperCase(seasoncode);
    	Log.d("season to upppercase", season);
    	if(season.equals("SPR")) tmp = ct.getString(R.string.spring);
    	else if(season.equals("SUM")) tmp = ct.getString(R.string.summer);
    	else if(season.equals("AUT")) tmp = ct.getString(R.string.autumn);
    	else if(season.equals("WIN")) tmp = ct.getString(R.string.winter);
    	else if(season.equals("ALL")) tmp = ct.getString(R.string.season4);
    	else tmp = ct.getString(R.string.unknown);
		return tmp;
    }
    
    public String seasonToUpperCase(String seasoncode){
    	String season = seasoncode.toUpperCase();
		return season;
    }
    
    public String getPrice(String price){
    	if(price.equals("0")||price.isEmpty())
    		return "-";
    	else
    		return "NT"+price;
    }
    
    public String getLocation(String locationcode){
    	if(locationcode.equals("0"))
    		return ct.getString(R.string.all_tw);
    	else if(locationcode.equals("1"))
    		return ct.getString(R.string.north);
    	else if(locationcode.equals("2"))
    		return ct.getString(R.string.mid);
    	else if(locationcode.equals("3"))
    		return ct.getString(R.string.south);
    	else if(locationcode.length()>1)
    		return locationcode;
    	else
    		return ct.getString(R.string.unknown);
    }
    
    public Boolean getPlantableTF(String plantable,String season){
    	Log.d("plantable", plantable);
    	Log.d("season", season);
    	Log.d("processSeasonCode(season)", seasonToUpperCase(season));
    	Log.d("settings.getCurrentSeason())", settings.getCurrentSeason());
    	if(plantable.equals("1") && 
    			(seasonToUpperCase(season).equals(settings.getCurrentSeason())
    					|| seasonToUpperCase(season).equals("ALL"))){
    		Log.d("getPlantableTF", "true");
    		return true;
    	}
    	else{
    		Log.d("getPlantableTF", "false");
    		return false;
    	}
    }
    
    public Boolean getCamTF(JSONArray J,int position){
    	try {
			if(J.getJSONObject(position).getString("status").equals("PENDING") || 
				J.getJSONObject(position).getString("status").equals("PROCESSING") ||
				J.getJSONObject(position).getString("status").equals("PAID") ||
				J.getJSONObject(position).getString("status").equals("PLANT_PENDING")){
				return false;
			}else{
				return true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
    }
    
    public String getImageUrl(JSONArray J,int position,String type){
    	String url = null;
		try {
			if(J.getJSONObject(position).getString("imgtype").equals("url")){
				url = J.getJSONObject(position).getString("imgurl");
			}else{
				url = "http://"+settings.getIp()+imgfolder+
						J.getJSONObject(position).getString(type)+"."+J.getJSONObject(position).getString("imgtype");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return url;
    }
    
    public String getPlantableString(String b){
    	if(b.equals("1")) return ct.getString(R.string.yes);
    	else return ct.getString(R.string.no);
    }
    
    private String getDesc(String value){
    	if(value.length()>0){
    		return value;
    	}else{
    		return ct.getString(R.string.unknown);
    	}
    }
    
    public String getDescription(int position, JSONArray J) {
    	String tmp = "";
		try {
			tmp += 
					ct.getString(R.string.price)+":"+getPrice(J.getJSONObject(position).getString(desc[1]))+"\n"+
					ct.getString(R.string.location)+":"+getLocation(J.getJSONObject(position).getString(desc[2]))+"\n"+
					ct.getString(R.string.season)+":"+getSeasonString(J.getJSONObject(position).getString(desc[3]))+"\n"+
					ct.getString(R.string.plantable)+":"+getPlantableString(J.getJSONObject(position).getString(desc[4]))+"\n"+
					ct.getString(R.string.dayuse)+":"+J.getJSONObject(position).getString(desc[5])+"\n"+
					ct.getString(R.string.desc)+":"+getDesc(J.getJSONObject(position).getString(desc[6]));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tmp;
    }
    
    public String getDescriptionLight(int position, JSONArray J) {
    	String tmp = null;
		try {
			tmp = ct.getString(R.string.price)+":"+getPrice(J.getJSONObject(position).getString(desc[1]));//+"\n"+
					//ct.getString(R.string.location)+":"+J.getJSONObject(position).getString(desc[2])+"\n"+
					//ct.getString(R.string.season)+":"+J.getJSONObject(position).getString(desc[3])+"\n"+
					//ct.getString(R.string.plantable)+":"+J.getJSONObject(position).getString(desc[4])+"\n"+
					//ct.getString(R.string.dayuse)+":"+J.getJSONObject(position).getString(desc[5]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//+"\n"+
				//ct.getString(R.string.desc)+":"+J.getJSONObject(position).getString(desc[6]);
		return tmp;
    }
    
    public JSONObject doAction(String email, String id, String action_request){
    	String URL = "http://"+settings.getIp()+settings.getRootFolder()+"order_index.php";
    	//JSONArray J = null;
    	// Building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", "doAction"));
    	params.add(new BasicNameValuePair("email", email));
    	params.add(new BasicNameValuePair("plant_id", id));
    	params.add(new BasicNameValuePair("action_request", action_request));
    	// getting JSON string from URL
    	JSONObject json = jsonParser.makeHttpRequest(URL, POST_tag, params);

		return json;
    }
    
    public JSONObject getPlantList(String method, String email){
    	String URL = "http://"+settings.getIp()+settings.getRootFolder()+"order_index.php";
    	//JSONArray J = null;
    	// Building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", "getPlantList"));
    	params.add(new BasicNameValuePair("method", method));
    	if(email!=null){
    		params.add(new BasicNameValuePair("email", email));
    	}
    	// getting JSON string from URL
    	JSONObject json = jsonParser.makeHttpRequest(URL, POST_tag, params);

		return json;
    }
    
    public JSONObject GetAllProducts(){
    	String URL = "http://"+settings.getIp()+settings.getRootFolder()+"get_products.php";
    	//JSONArray J = null;
    	// Building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", "all"));
    	params.add(new BasicNameValuePair("lang",getLanguage()));
    	Log.d("getallLanguage", getLanguage());
    	// getting JSON string from URL
    	JSONObject json = jsonParser.makeHttpRequest(URL, POST_tag, params);

		return json;
    }
    
    public JSONObject GetCurrentProducts(){
    	String URL = "http://"+settings.getIp()+settings.getRootFolder()+"get_products.php";
    	//JSONArray J = null;
    	// Building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", "current"));
    	params.add(new BasicNameValuePair("lang",getLanguage()));
    	Log.d("getcurrentLanguage", getLanguage());
    	// getting JSON string from URL
    	JSONObject json = jsonParser.makeHttpRequest(URL, POST_tag, params);

		return json;
    }
    
    public JSONObject GetSeasonProducts(String season){
    	String URL = "http://"+settings.getIp()+settings.getRootFolder()+"get_products.php";
    	//JSONArray J = null;
    	// Building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", "season"));
    	params.add(new BasicNameValuePair("season", season));
    	params.add(new BasicNameValuePair("lang",getLanguage()));
    	Log.d("getseasonLanguage", getLanguage());
    	// getting JSON string from URL
    	JSONObject json = jsonParser.makeHttpRequest(URL, POST_tag, params);

		return json;
    }
//*/
}
