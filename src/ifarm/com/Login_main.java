package ifarm.com;

import ifarm.com.library.BaseFunctions;
import ifarm.com.library.UserDBHandler;
import ifarm.com.library.UserFunctions;
import ifarm.com.universalimageloader.Constants.Extra;

import org.apache.http.NoHttpResponseException;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
 
 
public class Login_main extends Base {
	final Context context = this;
	String userid,password;
    Button btnLogin;
    Button btnLinkToRegister;
    EditText inputEmail;
    EditText inputPassword;
    TextView loginErrorMsg;
    UserFunctions uf;
    Setting settings;
    UserDBHandler db;
 
    // JSON Response node names
    private static String KEY_SUCCESS = "success";
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
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        
        SetViewAndListener();
        uf = new UserFunctions(context);
        baseFunctions = new BaseFunctions(context);
        Log.d("Login", "onCreate");
    }
    
    private Button.OnClickListener btnlistener = new Button.OnClickListener(){
		//@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//switch (v.getId()){
			if(v.getId()==R.id.btnLogin){
				//--------------------------------------------------
				LayoutInflater layoutInflater = LayoutInflater.from(context);
				
				View promptView = layoutInflater.inflate(R.layout.prompts2, null);

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

				// set prompts.xml to be the layout file of the alertdialog builder
				alertDialogBuilder.setView(promptView);
				
				final EditText name = (EditText) promptView.findViewById(R.id.email_input);

				final EditText pass = (EditText) promptView.findViewById(R.id.pass_input);

				// setup a dialog window
				alertDialogBuilder
						//.setCancelable(false)
						.setTitle(getString(R.string.userlogin))
						.setIcon(R.drawable.login_icon)
						.setPositiveButton(getString(R.string.login), new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										// get user input and set it to result
										userid = name.getText().toString();
										password = pass.getText().toString();
										if((userid.length()==0)){
											//dialog.dismiss();
											Dialog(getString(R.string.error),getString(R.string.empty_email),false);
										}else if((password.length()==0)) {
											Dialog(getString(R.string.error),getString(R.string.empty_pass),false);
											//dialog.dismiss();
										}else{
											new Login().execute(userid,password);	
										}
									}
								});

				// create an alert dialog
				AlertDialog alertD = alertDialogBuilder.create();

				alertD.show();
				//--------------------------------------------------
                
			}else if(v.getId()==R.id.btnLinkToRegisterScreen){
				String re_from = null;
				if(getIntent().getStringExtra(INTENT_FROM)!=null){
					re_from = getIntent().getStringExtra(INTENT_FROM);
					Log.d(INTENT_FROM, re_from);
				}
					
				Intent i = new Intent();
				if(re_from!=null)
					i.putExtra(INTENT_FROM, re_from);
				i.setClass(context, Register_main.class);
				startActivity(i);
                finish();
			}
		}
	};
    
    public void SetViewAndListener(){
    	db = new UserDBHandler(context);
    	// Importing all assets like buttons, text fields
        inputEmail = (EditText) findViewById(R.id.loginEmail);
        inputPassword = (EditText) findViewById(R.id.loginPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        loginErrorMsg = (TextView) findViewById(R.id.login_error);
        
        btnLogin.setOnClickListener(btnlistener);
        btnLinkToRegister.setOnClickListener(btnlistener);
    }
    
    private class Login extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(Login_main.this);

        /** progress dialog to show user that the backup is processing. */
        /** application context. */

        protected void onPreExecute() {
        	this.dialog.setCancelable(false);
            this.dialog.setIndeterminate(false);
            //this.dialog.setTitle(getString(R.string.app_name));
            this.dialog.setTitle(getString(R.string.login));
            this.dialog.setIcon(R.drawable.login_icon);
            this.dialog.setMessage(getString(R.string.loading));
            this.dialog.show();
        }
        
        @Override
        protected String doInBackground(String... params) {
        	JSONObject jObj;
        	Log.d("doInBackground", "Start");
        	String email = params[0];
        	Log.d("email", email);
        	String password = params[1];
        	Log.d("password", password);
        	
        	try{
        		jObj = uf.loginUser(email, password);
        	}catch(Exception e){
        		return getString(R.string.error_server);
        	}
            try {
		        if(Integer.parseInt(jObj.getString("success")) == 1){
		        	// user successfully logged in
		            // Store user details in SQLite Database
		            JSONObject jObj_user = jObj.getJSONObject("user");

		            // Clear all previous data in database
		            uf.logoutUser();
		            db.addUser(jObj_user.getString(KEY_NAME), 
		                	jObj_user.getString(KEY_EMAIL), 
		                	jObj.getString(KEY_UID), 
		                	jObj_user.getString(KEY_C_AT),
		                	jObj_user.getString(KEY_U_AT),
		                	jObj_user.getString(KEY_LVL),
		                	jObj_user.getString(KEY_TS),
		                	jObj_user.getString(KEY_PHONE),
		                	jObj_user.getString(KEY_GENDER),
		                	jObj_user.getString(KEY_ADDRESS),
		                	jObj_user.getString(KEY_BIRTHDAY));
		            db.close();
		        }else{
		            if(Integer.parseInt(jObj.getString("error"))==1){
		            	return getString(R.string.error_email);
		            }else if(Integer.parseInt(jObj.getString("error"))==2){
		            	return getString(R.string.error_emailorpass);
		            }
		        }
            } catch (Exception e){
			    return getString(R.string.error_server);
			}
            return KEY_SUCCESS;
        }

        @Override
        protected void onPostExecute(String msg) {
            
            if(msg==KEY_SUCCESS){
            	if(getIntent().getStringExtra(INTENT_FROM)!=null){
            		WelcomeDialog(getString(R.string.login),db.getName(),true);
            	}else
            		WelcomeDialog(getString(R.string.login),db.getName(),false);
            }else{
            	//loginErrorMsg.setText(msg);
            	Dialog(getString(R.string.error)+" "+getString(R.string.login),msg,false);
            }
            
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
         
        }

    }

}