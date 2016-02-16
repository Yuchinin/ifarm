package ifarm.com.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.util.Log;

public class JSONParser {
	 
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
 
    // constructor
    public JSONParser() {
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
 
    }
 
    public JSONObject makeHttpRequest(
    		String url, String method, List<NameValuePair> params) {
    	Log.d(new Exception().getStackTrace()[0].getMethodName(),url);
    	
    	//reset
    	is = null;jObj = null;json = "";
 
        // Making HTTP request
        try {
        	HttpParams httpParameters = new BasicHttpParams();
        	Log.e("httpParameters", "pass");
        	// Set the timeout in milliseconds until a connection is established.
        	// The default value is zero, that means the timeout is not used. 
        	int timeoutConnection = 10000;
        	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        	Log.e("timeoutConnection", "pass");
        	// Set the default socket timeout (SO_TIMEOUT) 
        	// in milliseconds which is the timeout for waiting for data.
        	int timeoutSocket = 10000;
        	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        	Log.e("timeoutSocket", "pass");
        	
        	// defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            Log.e("httpClient", "pass");
            //---<
            httpClient.setParams(httpParameters);
            Log.e("setParams", "pass");
            //--->
        	
            HttpResponse httpResponse = null;
            
        	if(method == "POST"){
        		//UrlEncodedFormEntity form;
        		//form = new UrlEncodedFormEntity(params);
                //form.setContentEncoding(HTTP.UTF_8);
                Log.e("http", method);
                //Log.d("form",form.toString());
        		HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
        		//httpPost.setEntity(form);
                httpResponse = httpClient.execute(httpPost);
        	}else if(method == "GET"){
                Log.e("httpGet", method);
        		String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);
                httpResponse = httpClient.execute(httpGet);
        	}
            
            //---<
            Log.e("httpResponse", "(");
            Log.e("httpResponse", httpResponse.toString());
            Log.e("httpResponse", ")");
            //--->
            
            HttpEntity httpEntity = httpResponse.getEntity();
            Log.e("httpEntity", httpEntity.toString());
            is = httpEntity.getContent();
            Log.e("is", is.toString());
            
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        	e.printStackTrace();
        }
 
        try {
        	if(is != null){
        		BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
        		Log.e("reader", "pass");
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "n");
                }
                is.close();
                json = sb.toString();
                //Log.e("JSON", json);
        	}
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
 
        // try parse the string to a JSON object
        try {
        	Log.e("json", json);
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        } catch (Exception e){
        	e.printStackTrace();
        }
        
        //if(jObj == null) Log.e("jObj", jObj.opt(json).toString());
        //Log.e("JSON Parser", "End");
        return jObj;
 
    }
}
