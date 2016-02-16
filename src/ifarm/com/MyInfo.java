package ifarm.com;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import ifarm.com.library.BaseFunctions;
import ifarm.com.library.BitmapDownloader;
import ifarm.com.library.SettingHelper;
import ifarm.com.library.UserDBHandler;
import ifarm.com.library.UserFunctions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MyInfo extends Base {
	Cursor c;
	private Button btnChangeInfo;
	private EditText edtName,edtEmail,edtPasswordNew,edtPhone,edtAddress,edtBirthday;
	private RadioGroup rg;
	private ImageView info_pic;
	UserFunctions userFunctions;
	UserDBHandler db;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	ImageLoadingListener animateFirstListener;
	SettingHelper settings;
	// this context will use when we work with Alert Dialog
	final Context context = this;
	String gender_tmp;
	boolean changeTF = false;
	
	String name,email,password,passwordnew,phone,gender,address,birthday;
	int level;
	
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
    private String MSG_EMPTY_PASSNEW;// = getString(R.string.empty_passnew);
    private String MSG_EMPTY_PHONE;
    private String MSG_ERROR_INTERNET;// = getString(R.string.error_internet);
    private String MSG_ERROR_SERVER;// = getString(R.string.error_server);
    private String MSG_SUCCESS;// = getString(R.string.success_info);
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.my_info);
        Log.d("setContentView", "Pass");
        SetViewAndListener();
        Log.d("SetViewAndListener", "Pass");
        db = new UserDBHandler(context);
        edtName.setText(db.getName());
        edtEmail.setText(db.getEmail());
        edtPhone.setText(db.getPhone());
        edtAddress.setText(db.getAddress());
        edtBirthday.setText(db.getBirthday());
        if(db.getGender().equals("M"))
        	rg.check(R.id.rbtnMale);
        else
        	rg.check(R.id.rbtnFemale);
        
        Log.d("gender", db.getGender());
        edtEmail.setEnabled(false);
        //--------------------
        new DownloadData().execute();
        //--------------------
        
    }
    
    @Override
	public void onBackPressed() {
		if(!edtName.getText().toString().equals(db.getName())){
			changeTF = true;
		}if(!edtEmail.getText().toString().equals(db.getEmail())){
			changeTF = true;
		}if(!edtPhone.getText().toString().equals(db.getPhone())){
			changeTF = true;
		}if(!edtAddress.getText().toString().equals(db.getAddress())){
			changeTF = true;
		}if(!edtBirthday.getText().toString().equals(db.getBirthday())){
			changeTF = true;
		}
		if(rg.getCheckedRadioButtonId()==R.id.rbtnMale){
			gender_tmp = "M";
		}else{
			gender_tmp = "F";
		}
		if(!db.getGender().equals(gender_tmp)){
			changeTF = true;
		}
		if(changeTF==true){
			OnBackDialog();
		}else{
			finish();
		}
	}
    
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
    
    private void SetViewAndListener() {
    	MSG_EMPTY_NAME = getString(R.string.empty_name);
        MSG_EMPTY_EMAIL = getString(R.string.empty_email);
        MSG_EMPTY_PASS = getString(R.string.empty_pass);
        MSG_EMPTY_PASSNEW = getString(R.string.empty_passnew);
        MSG_EMPTY_PHONE = getString(R.string.empty_phone);
        MSG_ERROR_INTERNET = getString(R.string.error_internet);
        MSG_ERROR_SERVER = getString(R.string.error_server);
        MSG_SUCCESS = getString(R.string.success_info);
        
    	btnChangeInfo = (Button)findViewById(R.id.btnChangeInfo);
    	edtName = (EditText)findViewById(R.id.edtName);
    	edtEmail = (EditText)findViewById(R.id.edtEmail);
    	edtPhone = (EditText)findViewById(R.id.edtPhone);
    	edtPasswordNew = (EditText)findViewById(R.id.edtPasswordNew);
    	edtAddress = (EditText)findViewById(R.id.edtAddress);
    	edtBirthday = (EditText)findViewById(R.id.edtDate);
    	info_pic = (ImageView)findViewById(R.id.info_pic);
    	
    	rg = (RadioGroup)findViewById(R.id.rGroupReg);
    	
    	btnChangeInfo.setOnClickListener(btnlistener);
    	animateFirstListener = new AnimateFirstDisplayListener();
    	info_pic.setOnClickListener(btnlistener);
    	
    	userFunctions = new UserFunctions(context);
    	baseFunctions = new BaseFunctions(context);
    	settings = new SettingHelper(context);
    }
    
    private Button.OnClickListener btnlistener = new Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Button b = (Button)v;
			if(v.getId()==R.id.btnChangeInfo){
				String Errmsg = null;
				name = edtName.getText().toString();
				email = edtEmail.getText().toString();
				//password = edtPassword.getText().toString();
				passwordnew = edtPasswordNew.getText().toString();
				phone = edtPhone.getText().toString();
				address = edtAddress.getText().toString();
				birthday = edtBirthday.getText().toString();
				gender = "M";
				if(rg.getCheckedRadioButtonId()==R.id.rbtnFemale) 
					gender = "F";
			
				if(name.length()==0) Errmsg = MSG_EMPTY_NAME;
				//if((email.length()==0)) Errmsg = MSG_EMPTY_EMAIL;
				//else if(password.length()==0) Errmsg = MSG_EMPTY_PASS;
				//else if(passwordnew.length()==0) Errmsg = MSG_EMPTY_PASSNEW;
				else if(phone.length()==0) Errmsg = MSG_EMPTY_PHONE;
			
				if(Errmsg==null) {
					//------------------------------------------------------------
					// get prompts.xml view
					LayoutInflater layoutInflater = LayoutInflater.from(context);
					
					View promptView = layoutInflater.inflate(R.layout.prompts1, null);

					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

					// set prompts.xml to be the layout file of the alertdialog builder
					alertDialogBuilder.setView(promptView);

					final EditText input = (EditText) promptView.findViewById(R.id.pass_input);

					// setup a dialog window
					alertDialogBuilder
							//.setCancelable(false)
							.setTitle(getString(R.string.change_info))
							.setIcon(R.drawable.login_icon1)
							.setPositiveButton(getString(R.string.change_info), new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											// get user input and set it to result
											password = input.getText().toString();
											if(password.length()==0){
												dialog.dismiss();
												Dialog(getString(R.string.error),MSG_EMPTY_PASS,false);
											}else{
												new ChangeInfo().execute(name,email,password,passwordnew,phone,gender,address,birthday);
											}
										}
									});
							//.setNegativeButton(getString(R.string.no),
								//	new DialogInterface.OnClickListener() {
									//	public void onClick(DialogInterface dialog,	int id) {
										//	dialog.cancel();
										//}
									//});

					// create an alert dialog
					AlertDialog alertD = alertDialogBuilder.create();

					alertD.show();
					//----------------------------------------------------------
					//new ChangeInfo().execute(name,email,password,passwordnew,phone,gender,address,birthday);
				} else {
					Dialog(getString(R.string.error)+
						" "+getString(R.string.change_info),Errmsg,false);
				}
			}
			else if(v.getId()==R.id.info_pic){
		        	Intent intent = new Intent();
		        	intent.setClass(context,UploadImage.class);
		        	intent.putExtra("email", email);
		        	startActivity(intent);
			}
	}
    };
    
    public void OnBackDialog(){
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder
		.setTitle(getString(R.string.error))
		.setMessage(getString(R.string.reminder1))
		.setCancelable(false).
				setPositiveButton(getString(R.string.yes),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					finish();
				}
			  }).setNegativeButton(getString(R.string.no),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					
				}
			  });
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
    }
    
    private class ChangeInfo extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(context);
        //private String msg;

        /** progress dialog to show user that the backup is processing. */
        /** application context. */

        protected void onPreExecute() {
            this.dialog.setMessage(getString(R.string.loading));
            this.dialog.show();
        }
        
        @Override
		protected String doInBackground(String... arg) {
        	JSONObject jObj;
        	String name = arg[0];
        	String email = arg[1];
        	String password = arg[2];
        	String passwordnew = arg[3];
        	String phone = arg[4];
        	String gender = arg[5];
        	String address = arg[6];
        	String birthday = arg[7];
			
			Log.d("Name to Server:", name);
			try {
				jObj = userFunctions.ChangeDetails(
						name,email,password,passwordnew,phone,gender,address,birthday);
		        if(Integer.parseInt(jObj.getString(KEY_SUCCESS)) == 1){
		            UserDBHandler db = new UserDBHandler(context);
		            JSONObject jObj_user = jObj.getJSONObject("user");
		            Log.d("Name from Server:", jObj_user.getString(KEY_NAME));

	                // Clear all previous data in database
	                userFunctions.logoutUser();
	                Log.d("logoutUser", "Pass");
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
	                Log.d("Name in local db:", db.getName());
	                return KEY_SUCCESS;
	            }else{
	            	if(Integer.parseInt(jObj.getString("error"))==1){
	            		return getString(R.string.error_email);
	            	}else if(Integer.parseInt(jObj.getString("error"))==2){
	            		return getString(R.string.action_failed);
	            	}else {
	            		return getString(R.string.error_emailorpass);
	            	}
	            }
		    }catch (Exception e){
		    	return getString(R.string.error_server);
		    }
		}

        @Override
        protected void onPostExecute(String msg) {
        	if(msg==KEY_SUCCESS){
            	Dialog(getString(R.string.change_info),MSG_SUCCESS,true);
            }else{
            	Dialog(getString(R.string.error)+" "+getString(R.string.change_info),msg,false);
            }
            
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
    
    private class DownloadData extends AsyncTask<Void, String, String> {        
        @Override
		protected String doInBackground(Void... arg) {
        		publishProgress(getString(R.string.connecting_server));
        		email = db.getEmail();
                //level = c.getString(c.getColumnIndex("level"));
                level = db.getLevel();
                gender = db.getGender(); 
                try{
                	return userFunctions.getImage(context,email,level,gender,null);
                }catch(Exception e){
                	return null;
                }
		}

        @Override
        protected void onPostExecute(String msg) {
        	if(msg==null){
        		
        	}else{
        		if(imageLoader.isInited())
            		;
            	else{
            		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            		
            		options = new DisplayImageOptions.Builder()
            		.showStubImage(R.drawable.ic_stub)
            		.showImageForEmptyUri(R.drawable.ic_empty)
            		.showImageOnFail(R.drawable.ic_error)
            		.cacheInMemory()
            		.cacheOnDisc()
            		.displayer(new RoundedBitmapDisplayer(20))
            		.build();
            	}
            		imageLoader.displayImage(msg, info_pic, options, animateFirstListener);
        	}
        }
	}
    
    @Override
	public void onResume() {  // After a pause OR at startup
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
	    super.onResume();
	    new DownloadData().execute();
	     }
}


