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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
 
public class CartFunctions {
///*	 
    private JSONParser jsonParser;
    private SettingHelper settings;
 
    private static String POST_tag = "POST";
    private static String GET_tag = "GET";
    
    static String imgfolder = "/ifarm_api/android/product_img/";
    
    //String[] desc = {"name","price","location","season","plantable","dayuse","info"};
    
    Context ct;
    BaseFunctions bf;
    // constructor
    public CartFunctions(Context context){
    	this.ct = context;
        jsonParser = new JSONParser();
        settings = new SettingHelper(ct);
        bf = new BaseFunctions(ct);
    }
    
    public String getLanguage(){
    	return Locale.getDefault().toString();
    }
    
    public JSONObject GetAddress(String email){
    	String URL = "http://"+settings.getIp()+settings.getRootFolder()+"order_index.php";

    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", "get_address"));
    	params.add(new BasicNameValuePair("email", email));
    	// getting JSON string from URL
    	JSONObject json = jsonParser.makeHttpRequest(URL, POST_tag, params);

		return json;
    }
    
    public JSONObject PlantReady(String id){
    	String URL = "http://"+settings.getIp()+settings.getRootFolder()+"order_index.php";

    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", "PlantReady"));
    	params.add(new BasicNameValuePair("id", id));
    	// getting JSON string from URL
    	JSONObject json = jsonParser.makeHttpRequest(URL, POST_tag, params);
		return json;
    }
    
    public JSONObject GetPlantPrepare(){
    	String URL = "http://"+settings.getIp()+settings.getRootFolder()+"order_index.php";

    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", "getPlantPrepare"));
    	params.add(new BasicNameValuePair("lang",getLanguage()));
    	// getting JSON string from URL
    	JSONObject json = jsonParser.makeHttpRequest(URL, POST_tag, params);
		return json;
    }
    
    public JSONObject GetPlant(String email){
    	String URL = "http://"+settings.getIp()+settings.getRootFolder()+"order_index.php";

    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", "get_plant"));
    	params.add(new BasicNameValuePair("email", email));
    	params.add(new BasicNameValuePair("lang",getLanguage()));
    	// getting JSON string from URL
    	JSONObject json = jsonParser.makeHttpRequest(URL, POST_tag, params);
		return json;
    }
    
    public JSONObject GetOrders(String email){
    	String URL = "http://"+settings.getIp()+settings.getRootFolder()+"order_index.php";

    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", "get_orders"));
    	params.add(new BasicNameValuePair("buyer_email", email));
    	params.add(new BasicNameValuePair("lang",getLanguage()));
    	// getting JSON string from URL
    	JSONObject json = jsonParser.makeHttpRequest(URL, POST_tag, params);
		return json;
    }
    
    public JSONObject GetOrderWithId(String order_id){
    	String URL = "http://"+settings.getIp()+settings.getRootFolder()+"order_index.php";
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", "get_orders_with_id"));
    	params.add(new BasicNameValuePair("order_id", order_id));
    	params.add(new BasicNameValuePair("lang",getLanguage()));
    	// getting JSON string from URL
    	JSONObject json = jsonParser.makeHttpRequest(URL, POST_tag, params);

		return json;
    }
    
    public JSONArray GetAllOrders(){
    	String URL = "http://"+settings.getIp()+settings.getRootFolder()+"order_index.php";
    	JSONArray J = null;
    	// Building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", "get_all_orders"));
    	params.add(new BasicNameValuePair("lang",getLanguage()));
    	// getting JSON string from URL
    	JSONObject json = jsonParser.makeHttpRequest(URL, POST_tag, params);
		try {
			J = json.getJSONArray("orders");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return J;
    }
    
    public JSONObject GetAllOrdersWithEmail(String email){
    	String URL = "http://"+settings.getIp()+settings.getRootFolder()+"order_index.php";
    	// Building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", "get_all_orders_with_email"));
    	params.add(new BasicNameValuePair("email", email));
    	params.add(new BasicNameValuePair("lang",getLanguage()));
    	// getting JSON string from URL
    	JSONObject json = jsonParser.makeHttpRequest(URL, POST_tag, params);

		return json;
    }
    
    public JSONArray GetOrdersDetails(String order_id){
    	String URL = "http://"+settings.getIp()+settings.getRootFolder()+"order_index.php";
    	JSONArray J = null;
    	// Building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", "get_order_details"));
    	params.add(new BasicNameValuePair("order_id", order_id));
    	params.add(new BasicNameValuePair("lang",getLanguage()));
    	// getting JSON string from URL
    	JSONObject json = jsonParser.makeHttpRequest(URL, POST_tag, params);
		try {
			J = json.getJSONArray("order_details");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return J;
    }
    
    public JSONObject SignOrder(String order_id, String email, String password){
    	String URL = "http://"+settings.getIp()+settings.getRootFolder()+"order_index.php";
    	// Building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", "sign"));
    	params.add(new BasicNameValuePair("order_id", order_id));
    	params.add(new BasicNameValuePair("email", email));
    	params.add(new BasicNameValuePair("password", password));
    	// getting JSON string from URL
    	JSONObject json = jsonParser.makeHttpRequest(URL, POST_tag, params);
		
		return json;
    }
    
    public JSONObject PayOrder(String order_id){
    	String URL = "http://"+settings.getIp()+settings.getRootFolder()+"order_index.php";
    	// Building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", "pay"));
    	params.add(new BasicNameValuePair("order_id", order_id));
    	// getting JSON string from URL
    	JSONObject json = jsonParser.makeHttpRequest(URL, POST_tag, params);
		
		return json;
    }
    
    public String getImageUrl(JSONArray J,int position){
    	String url = null;
		try {
			url = "http://"+settings.getIp()+imgfolder+
					J.getJSONObject(position).getString("product_id")+"."+J.getJSONObject(position).getString("imgtype");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return url;
    }
    
    public String getTypeString(String type){
    	if(type.equals("BUY")) return ct.getString(R.string.order);
    	else return ct.getString(R.string.plant);
    }
    
    public String getOrderIdString(String order_id){
    	return ct.getString(R.string.order_id)+":\n"+order_id;
    }
    
    public String getStatus(String status){
    	String tmp = ct.getString(R.string.status)+":";
    	if(status.equals("PENDING")) tmp += ct.getString(R.string.haven_pay);
    	else if(status.equals("PROCESSING")) tmp += ct.getString(R.string.payed);
    	else if(status.equals("UNPAID")) tmp += ct.getString(R.string.haven_verify);
    	else if(status.equals("PAID")) tmp += ct.getString(R.string.already_paid);
    	else if(status.equals("PLANT_PENDING")) tmp += ct.getString(R.string.pending);
    	else if(status.equals("PLANT_READY")) tmp += ct.getString(R.string.ready);
    	else if(status.equals("PLANT_PLANTED")) tmp += ct.getString(R.string.planted);
    	else if(status.equals("PLANT_UNDONE")) tmp += ct.getString(R.string.undone);
    	else if(status.equals("PLANT_DONE")) tmp += ct.getString(R.string.done);
    	return tmp;
    }
    
    public String getDescription(JSONArray J,int position){
    	String tmp = null;
    	try {
			tmp = ct.getString(R.string.type)+":"+getTypeString(J.getJSONObject(position).getString("type"))+"\n"+
					ct.getString(R.string.quantity)+":"+J.getJSONObject(position).getString("amount")+"\n"+
					ct.getString(R.string.price)+":"+J.getJSONObject(position).getString("price");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return tmp;
    }
    
    public String getUserInfo(String order_id,String buyer_name,String updated_at){
    	String tmp = null;
    	tmp = ct.getString(R.string.order_id)+":"+order_id+"\n"+
				ct.getString(R.string.name)+":"+buyer_name+"\n"+
				ct.getString(R.string.date)+":"+updated_at+" ("+bf.getDayFromString(updated_at)+")"+"\n";
    	return tmp;
    }
    
    public String getTotal(JSONArray J){
    	int tmpint = 0;
    		for(int x=0;x<J.length();x++){
    			try {
    				tmpint += Integer.parseInt(J.getJSONObject(x).getString("amount"))*
    						Integer.parseInt(J.getJSONObject(x).getString("price"));
    			}catch (Exception e) {
    				return e.toString();
    			}
    		}
    		return ct.getString(R.string.total)+"NT"+Integer.toString(tmpint);
    }
    
    public String getTotalString(String total){
    	return ct.getString(R.string.total)+"NT"+total;
    }
    
    public JSONObject PlantCrop(String buyer_email, String product_id){
    	String url = "http://"+settings.getIp()+settings.getRootFolder()+"order_index.php";
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "plant"));
        params.add(new BasicNameValuePair("buyer_email", buyer_email));
        params.add(new BasicNameValuePair("product_id", product_id));
 
        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(url, POST_tag, params);
        // return json
        return json;
    }
    
    public JSONObject OrderCrop(String buyer_email, String product_id, String amount){
    	String url = "http://"+settings.getIp()+settings.getRootFolder()+"order_index.php";
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "order"));
        params.add(new BasicNameValuePair("buyer_email", buyer_email));
        params.add(new BasicNameValuePair("product_id", product_id));
        params.add(new BasicNameValuePair("amount", amount));
 
        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(url, POST_tag, params);
        // return json
        return json;
    }
    
    public JSONObject ModifyCrop(String buyer_email,String id,String amount){
    	String url = "http://"+settings.getIp()+settings.getRootFolder()+"order_index.php";
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "modify"));
        params.add(new BasicNameValuePair("buyer_email", buyer_email));
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("amount", amount));
 
        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(url, POST_tag, params);
        // return json
        return json;
    }
    
    public JSONObject DeleteCrop(String buyer_email,String id){
    	String url = "http://"+settings.getIp()+settings.getRootFolder()+"order_index.php";
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "delete"));
        params.add(new BasicNameValuePair("buyer_email", buyer_email));
        params.add(new BasicNameValuePair("id", id));
 
        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(url, POST_tag, params);
        // return json
        return json;
    }
    
    public JSONObject OrderConfirm(String buyer_email){
    	String url = "http://"+settings.getIp()+settings.getRootFolder()+"order_index.php";
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "confirm"));
        params.add(new BasicNameValuePair("buyer_email", buyer_email));
 
        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(url, POST_tag, params);
        // return json
        return json;
    }
//*/
}
