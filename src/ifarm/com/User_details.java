package ifarm.com;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ifarm.com.library.BaseFunctions;
import ifarm.com.library.UserDBHandler;
import ifarm.com.library.UserFunctions;
import ifarm.com.universalimageloader.BaseActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class User_details extends Base {
	Cursor c;
	private Button btnChangeInfo;
	private EditText edtName,edtEmail,edtPassword,edtLevel,edtPhone,edtAddress,edtBirthday;
	private RadioGroup rg;
	UserFunctions userFunctions;
	String name,email,password,level,phone,gender,address,birthday;
	
	// JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_SUCCESS_MSG = "success_msg";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_C_AT = "created_at";
    private static String KEY_U_AT = "updated_at";
    private static String KEY_LVL = "level";
    private static String KEY_TS = "timestamp";
    private static String KEY_PHONE = "phone";
    private static String KEY_GENDER = "gender";
    private static String KEY_ADDRESS = "address";
    private static String KEY_BIRTHDAY = "birthday";
    private String MSG_EMPTY_NAME;// = getString(R.string.empty_name);
    private String MSG_EMPTY_EMAIL;// = getString(R.string.empty_email);
    private String MSG_EMPTY_PASS;// = getString(R.string.empty_pass);
    private String MSG_EMPTY_LEVEL;// = getString(R.string.empty_passnew);
    private String MSG_EMPTY_PHONE;
    private String MSG_ERROR_INTERNET;// = getString(R.string.error_internet);
    private String MSG_ERROR_SERVER;// = getString(R.string.error_server);
    private String MSG_SUCCESS;// = getString(R.string.success_info);
	
	JSONArray J;
	Context context = this;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info);
		SetViewAndListener();
		
		Bundle bundle = getIntent().getExtras();
		int position = bundle.getInt("position");
		//id-=1;
		try {
			J = new JSONArray(bundle.getString("JSONARR"));
			name = J.getJSONObject(position).getString("name");
			email = J.getJSONObject(position).getString("email");
			level = J.getJSONObject(position).getString("level");
			phone = J.getJSONObject(position).getString("phone");
			address = J.getJSONObject(position).getString("address");
			birthday = J.getJSONObject(position).getString("birthday");
			gender = J.getJSONObject(position).getString("gender");
	        Log.d("name",name);
	        Log.d("email",email);
	        Log.d("level",level);
	        Log.d("phone",phone);
	        Log.d("address",address);
	        Log.d("birthday",birthday);
	        Log.d("gender",gender);
			edtName.setText(name);
	        edtEmail.setText(email);
	        edtLevel.setText(level);
	        edtPhone.setText(phone);
	        edtAddress.setText(address);
	        edtBirthday.setText(birthday);
	        if(gender.equals("F"))
	        	rg.check(R.id.rbtnFemale);
	        else
	        	rg.check(R.id.rbtnMale);
	        Log.d("gender", gender);
	        edtEmail.setEnabled(false);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		if(Integer.parseInt(edtLevel.getText().toString())>=99){
			edtName.setEnabled(false);
			edtEmail.setEnabled(false);
			edtLevel.setEnabled(false);
			edtPhone.setEnabled(false);
			rg.setEnabled(false);
			edtPassword.setEnabled(false);
			btnChangeInfo.setEnabled(false);
			edtAddress.setEnabled(false);
			edtBirthday.setEnabled(false);
		}
		
	}
	
    private void SetViewAndListener() {
    	MSG_EMPTY_NAME = getString(R.string.empty_name);
        MSG_EMPTY_EMAIL = getString(R.string.empty_email);
        MSG_EMPTY_PASS = getString(R.string.empty_pass);
        MSG_EMPTY_LEVEL = getString(R.string.empty_level);
        MSG_EMPTY_PHONE = getString(R.string.empty_phone);
        MSG_ERROR_INTERNET = getString(R.string.error_internet);
        MSG_ERROR_SERVER = getString(R.string.error_server);
        MSG_SUCCESS = getString(R.string.success_info);
        
    	btnChangeInfo = (Button)findViewById(R.id.btnChangeInfo);
    	edtName = (EditText)findViewById(R.id.edtName);
    	edtEmail = (EditText)findViewById(R.id.edtEmail);
    	edtPhone = (EditText)findViewById(R.id.edtPhone);
    	edtPassword = (EditText)findViewById(R.id.edtPassword);
    	edtLevel = (EditText)findViewById(R.id.edtLevel);
    	edtAddress = (EditText)findViewById(R.id.edtAddress);
    	edtBirthday = (EditText)findViewById(R.id.edtDate);
    	
    	rg = (RadioGroup)findViewById(R.id.rGroupReg);
    	
    	btnChangeInfo.setOnClickListener(btnlistener);
    	
    	userFunctions = new UserFunctions(context);
    	baseFunctions = new BaseFunctions(context);
    }
    
    private Button.OnClickListener btnlistener = new Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			UserDBHandler db = new UserDBHandler(context);
	        Cursor c = db.get(1);
	        String email_admin = c.getString(c.getColumnIndex("email"));
	        c.close();
	        db.close();
			String Errmsg = null;
			name = edtName.getText().toString();
			email = edtEmail.getText().toString();
			level = edtLevel.getText().toString();
			password = edtPassword.getText().toString();
			phone = edtPhone.getText().toString();
			address = edtAddress.getText().toString();
			birthday = edtBirthday.getText().toString();
			gender = "M";
			if(rg.getCheckedRadioButtonId()==R.id.rbtnFemale) 
				gender = "F";
			
			if(name.length()==0) Errmsg = MSG_EMPTY_NAME;
        	//if((email.length()==0)) Errmsg = MSG_EMPTY_EMAIL;
			else if(password.length()==0) Errmsg = MSG_EMPTY_PASS;
			else if(level.length()==0) Errmsg = MSG_EMPTY_LEVEL;
			else if(phone.length()==0) Errmsg = MSG_EMPTY_PHONE;
			
			if(Errmsg==null) {
				new ChangeInfo().execute(name,email,level,phone,gender,password,email_admin,address,birthday);
            } else {
            	Dialog(getString(R.string.error)+
            	" "+getString(R.string.change_info),Errmsg,false);
            }
			
	}
    	
    };
    
    private class ChangeInfo extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(context);
        private String msg;

        /** progress dialog to show user that the backup is processing. */
        /** application context. */

        protected void onPreExecute() {
            this.dialog.setMessage(getString(R.string.loading));
            this.dialog.show();
        }
        
        @Override
		protected String doInBackground(String... arg) {
        	String name = arg[0];
        	String email = arg[1];
        	String level = arg[2];
        	String phone = arg[3];
        	String gender = arg[4];
        	String password = arg[5];
        	String email_admin = arg[6];
        	String address = arg[7];
        	String birthday = arg[8];
        	
        	if(baseFunctions.isOnline()==false){
            	Log.d("isOnline", "false");
            	return MSG_ERROR_INTERNET;
            }
            else if(baseFunctions.isServerOnline()==false){
				Log.d("isServerOnline", "false");
				Log.d("doInBackground", "Pass");
				return MSG_ERROR_SERVER;
			}
			try {
				Log.d("Name to Server:", name);
				JSONObject jObj = userFunctions.AdminChangeDetails(
						name, email, level, phone, gender, password,email_admin,address,birthday);
		        if (jObj.getString(KEY_SUCCESS) != null) {
		            if(Integer.parseInt(jObj.getString(KEY_SUCCESS)) == 1){
		                msg = KEY_SUCCESS;
		                Log.d("msg", msg);
		            }else{
		            	return jObj.getString(KEY_ERROR_MSG);
		            }
		        }else{
		        	return "Failed to fetch data from server!";
		        }
		    } catch (JSONException e) {
		        return e.toString();
		    } catch (Exception e){
		    	return e.toString();
		    }
			return msg;
		}

        @Override
        protected void onPostExecute(String msg) {
        	edtPassword.setText("");
        	if(msg==KEY_SUCCESS){
            	Dialog(getString(R.string.change_info),MSG_SUCCESS,false);
            }else{
            	Dialog(getString(R.string.error)+" "+getString(R.string.change_info),msg,false);
            }
            
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

}
