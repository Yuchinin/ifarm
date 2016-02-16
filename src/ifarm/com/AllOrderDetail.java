package ifarm.com;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import ifarm.com.PlantListSP.ItemAdapter;
import ifarm.com.library.CartFunctions;
import ifarm.com.library.FarmInfoFunctions;
import ifarm.com.library.UserDBHandler;
import ifarm.com.universalimageloader.AbsListViewBaseActivity;

public class AllOrderDetail extends AbsListViewBaseActivity{
	Context ct = this;
	CartFunctions cf;
	UserDBHandler db;
	JSONArray J;
	TextView txtDetails;
	String address,order_id,farm_info,from;
	Button btnSignOrder,btnList,btnRes_Pay;
	FarmInfoFunctions ff;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_details);
		
		SetViewAndListener();
	}
	
	public void SetViewAndListener(){
		cf = new CartFunctions(ct);
		db = new UserDBHandler(ct);
		ff = new FarmInfoFunctions(ct);
		txtDetails = (TextView)findViewById(R.id.txtDetails);
		btnSignOrder = (Button)findViewById(R.id.btnSign);
		btnList = (Button)findViewById(R.id.btnList);
		btnRes_Pay = (Button)findViewById(R.id.btnRes_Pay);
		btnList.setOnClickListener(btnlistener);
		btnRes_Pay.setOnClickListener(btnlistener);
		btnSignOrder.setOnClickListener(btnlistener);
		
		Bundle bundle = getIntent().getExtras();
		order_id = bundle.getString("order_id");
		String status = bundle.getString("status");
		String buyer_name = bundle.getString("buyer_name");
		String updated_at = bundle.getString("updated_at");
		String address = bundle.getString("address");
		String total = bundle.getString("total");
		from = bundle.getString("from");
		btnRes_Pay.setVisibility(Button.GONE);
		btnSignOrder.setVisibility(Button.GONE);
		if(status.equals("PROCESSING"))
			btnRes_Pay.setVisibility(Button.VISIBLE);
		else if(status.equals("UNPAID")){
			btnRes_Pay.setVisibility(Button.GONE);
			if(db.getLevel()>50){
				btnSignOrder.setVisibility(Button.VISIBLE);
			}
		}
		txtDetails.setText(cf.getUserInfo(order_id, buyer_name, updated_at)+
				getString(R.string.address)+":"+address+"\n"+cf.getTotalString(total));
		if(from.equals("ORDER_LIST_USER"))
			new FarmInfoDownload().execute();
	}
	
	public void showLists(){
		Intent intent = new Intent();
		intent.setClass(ct, AllOrderDetails_Details.class);
		intent.putExtra("order_id", order_id);
		startActivity(intent);
	}
	
	private Button.OnClickListener btnlistener = new Button.OnClickListener(){
		//@Override
		public void onClick(View v) {
			if(v.getId()==R.id.btnSign){
				SignDialog();
			}else if(v.getId()==R.id.btnList){
				showLists();
			}else if(v.getId()==R.id.btnRes_Pay){
				new PayOrder().execute();
			}else {
				
			}
		}
	};
	
	public void SignDialog(){
		//--------------------------------------------------
		LayoutInflater layoutInflater = LayoutInflater.from(ct);
		View promptView = layoutInflater.inflate(R.layout.prompts1, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ct);
		alertDialogBuilder.setView(promptView);
		final EditText pass = (EditText) promptView.findViewById(R.id.pass_input);
		alertDialogBuilder
				.setTitle(getString(R.string.verify))
				.setPositiveButton(getString(R.string.verify), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								String password;
								password = pass.getText().toString();
								if((password.length()==0)){
									Dialog(getString(R.string.error),getString(R.string.pass_input),false);
								}else{
									new SignOrder().execute(password);
								}
							}
						});
		AlertDialog alertD = alertDialogBuilder.create();
		alertD.show();
		//--------------------------------------------------
	}
	
	private class PayOrder extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(ct);

        protected void onPreExecute() {
            this.dialog.setMessage(getString(R.string.loading));
            this.dialog.setCancelable(false);
            this.dialog.setIndeterminate(false);
            //this.dialog.setTitle(getString(R.string.app_name));
            //this.dialog.setTitle(" ");
            //this.dialog.setIcon(R.drawable.btn_chhome_ifarm);
            this.dialog.show();
        }
        
        @Override
		protected String doInBackground(String... arg) {
        	try{
        		JSONObject json = cf.PayOrder(order_id);
        		if(Integer.parseInt(json.getString("success"))==1){
        			return "success";
        		}else 
        			return getString(R.string.action_failed);
        	}catch(Exception e){
        		return getString(R.string.error_server);
        	}
		}

        @Override
        protected void onPostExecute(String msg) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if(!msg.equals("success"))
            	Dialog(getString(R.string.error),msg,false);
            else{
            	Dialog(getString(R.string.pay),getString(R.string.already_paid),false);
            	btnRes_Pay.setVisibility(Button.INVISIBLE);
            }
        }
	}
	
	private class SignOrder extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(ct);

        protected void onPreExecute() {
            this.dialog.setMessage(getString(R.string.loading));
            this.dialog.setCancelable(false);
            this.dialog.setIndeterminate(false);
            //this.dialog.setTitle(getString(R.string.app_name));
            //this.dialog.setTitle(" ");
            //this.dialog.setIcon(R.drawable.btn_chhome_ifarm);
            this.dialog.show();
        }
        
        @Override
		protected String doInBackground(String... arg) {
        	try{
        		JSONObject json = cf.SignOrder(order_id,db.getEmail(),arg[0]);
        		if(Integer.parseInt(json.getString("success"))==1){
        			return "success";
        		}else if(Integer.parseInt(json.getString("error"))==1){
        			return getString(R.string.action_failed);
        		}else
        			return getString(R.string.error_emailorpass);
        	}catch(Exception e){
        		return getString(R.string.error_server);
        	}
		}

        @Override
        protected void onPostExecute(String msg) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if(!msg.equals("success"))
            	Dialog(getString(R.string.error),msg,false);
            else{
            	Dialog(getString(R.string.verify),getString(R.string.verify_success),true);
            }
        }
	}
	
	private class FarmInfoDownload extends AsyncTask<String, Integer, Integer> {

        private ProgressDialog dialog = new ProgressDialog(ct);

        protected void onPreExecute() {
            this.dialog.setMessage(getString(R.string.loading));
            this.dialog.setCancelable(false);
            this.dialog.setIndeterminate(false);
            //this.dialog.setTitle(getString(R.string.app_name));
            //this.dialog.setTitle(" ");
            //this.dialog.setIcon(R.drawable.btn_chhome_ifarm);
            this.dialog.show();
        }
        
        @Override
		protected Integer doInBackground(String... arg) {
        	//JSONObject json = null;
        	try{
        		//json = ff.getFarmInfo();
        		ff.getFarmInfo();
        		farm_info = "\n"+
        			getString(R.string.vendor_name)+":"+ff.getVendor()+"\n"+
        			getString(R.string.bank)+":"+ff.getBankName()+"\n"+
        			getString(R.string.vendor_account)+":"+ff.getBankAccount();
        		return 1;
        	}catch(Exception e){
        		return 0;
        	}
		}

        @Override
        protected void onPostExecute(Integer value) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if(value==1){
            	txtDetails.append(farm_info);
            }else{
            	Dialog(getString(R.string.error),getString(R.string.msg_fail_get_vendor),false);
            }
            
        }
	}
}
