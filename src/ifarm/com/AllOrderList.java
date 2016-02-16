package ifarm.com;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import ifarm.com.library.BaseFunctions;
import ifarm.com.library.CartFunctions;
import ifarm.com.library.PlantFunctions;
import ifarm.com.library.SettingHelper;
import ifarm.com.library.UserDBHandler;
import ifarm.com.library.UserFunctions;
import ifarm.com.universalimageloader.AbsListViewBaseActivity;

public class AllOrderList extends AbsListViewBaseActivity {
	
	Context context = this;
	DisplayImageOptions options;
	BaseFunctions bf;
	CartFunctions cf;
	PlantFunctions pf;
	UserFunctions uf;
	UserDBHandler db;
	SettingHelper st;
	JSONArray J;
	Intent intent = new Intent();
	String order_id,buyer_name,updated_at,address,status,total,METHOD;
	LinearLayout lnl;
	Button btnOrderList,btnPlantList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bf = new BaseFunctions(context);
		cf = new CartFunctions(context);
		pf = new PlantFunctions(context);
		db = new UserDBHandler(context);
		st = new SettingHelper(context);
		uf = new UserFunctions(context);
		
		//lnl = (LinearLayout)findViewById(R.id.lnl);
		//lnl.setBackgroundResource(R.drawable.bg_history_close);
		//listView = (ListView) findViewById(R.id.list);
		//DataLoaderTask listViewLoaderTask = new DataLoaderTask();
		//listViewLoaderTask.execute();
	}
	
	private Button.OnClickListener btnlistener = new Button.OnClickListener(){
		@Override
		public void onClick(View v) {
			CloseBook();
			if(v.getId()==R.id.btnOrder){
				btnOrderList.setBackgroundResource(R.drawable.history_list_line);
				btnPlantList.setBackgroundResource(0);
				METHOD = "ORDER_LIST_USER";
				new DataLoaderTask().execute(METHOD);
			}else if(v.getId()==R.id.btnPlant){
				btnOrderList.setBackgroundResource(0);
				btnPlantList.setBackgroundResource(R.drawable.history_list_line);
				METHOD = "PLANT_LIST_USER";
				new DataLoaderTask().execute(METHOD);
			}
		}
	};
	
	private void CloseBook(){
		listView.setVisibility(ListView.INVISIBLE);
		lnl.setBackgroundResource(R.drawable.bg_history_close);
		btnOrderList.setVisibility(Button.INVISIBLE);
		btnPlantList.setVisibility(Button.INVISIBLE);
	}
	
	private void OpenBook(){
		listView.setVisibility(ListView.VISIBLE);
		lnl.setBackgroundResource(R.drawable.bg_history_open);
		btnOrderList.setVisibility(Button.VISIBLE);
		btnPlantList.setVisibility(Button.VISIBLE);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	class ItemAdapter extends BaseAdapter {
		JSONArray J;
		
		private ItemAdapter(JSONArray JSONARR){
			J = JSONARR;
		}

		private class ViewHolder {
			public TextView txtDate,txtName,txtId;
		}

		@Override
		public int getCount() {
			return J.length();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				if(getIntent().getStringExtra(INTENT_FROM)!=null){
					view = getLayoutInflater().inflate(R.layout.item_list_image2, parent, false);
				}else{
					view = getLayoutInflater().inflate(R.layout.item_list_image1, parent, false);
				}
				holder = new ViewHolder();
				holder.txtId = (TextView) view.findViewById(R.id.txtId);
				holder.txtName = (TextView) view.findViewById(R.id.txtName);
				holder.txtDate = (TextView) view.findViewById(R.id.txtDate);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
	        try {
	        	order_id = J.getJSONObject(position).getString("order_id");
	        	buyer_name = J.getJSONObject(position).getString("buyer_name");
				updated_at = J.getJSONObject(position).getString("updated_at");
				address = J.getJSONObject(position).getString("address");
				status = J.getJSONObject(position).getString("status");
				total = J.getJSONObject(position).getString("total");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
	        holder.txtId.setText(cf.getOrderIdString(order_id));
	        if(getIntent().getStringExtra(INTENT_FROM)!=null){ //history
	        	holder.txtDate.setText(cf.getStatus(status)+"\n"+cf.getTotalString(total));
	        }else{
	        	holder.txtDate.setText(getString(R.string.name)+":"+buyer_name);
	        }
			holder.txtName.setText(updated_at+" ("+bf.getDayFromString(updated_at)+")");
			return view;
		}
	}
	
	class ItemAdapter1 extends BaseAdapter {
		JSONArray J;
		
		private ItemAdapter1(JSONArray JSONARR){
			J = JSONARR;
		}

		private class ViewHolder {
			public TextView txtDate,txtName,txtId,txtOther;
		}

		@Override
		public int getCount() {
			return J.length();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = getLayoutInflater().inflate(R.layout.item_list_image3, parent, false);
				holder = new ViewHolder();
				holder.txtId = (TextView) view.findViewById(R.id.txtId);
				holder.txtName = (TextView) view.findViewById(R.id.txtName);
				holder.txtDate = (TextView) view.findViewById(R.id.txtDate);
				holder.txtOther = (TextView) view.findViewById(R.id.txtOther);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			String id,plant_id = null,action_request = null,created_at = null,status = null;
	        try {
	        	id = J.getJSONObject(position).getString("id");
	        	plant_id = J.getJSONObject(position).getString("plant_id");
	        	created_at = J.getJSONObject(position).getString("created_at");
	        	action_request = J.getJSONObject(position).getString("action_request");
				status = J.getJSONObject(position).getString("status");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
	        holder.txtId.setText(plant_id);
	        holder.txtDate.setText(pf.getRequest(action_request));
			holder.txtName.setText(created_at+"\n("+bf.getDayFromString(created_at)+")");
			holder.txtOther.setText(pf.getStatus(status));
			return view;
		}
	}
	
	public void showOrderDetails(int position){
		try{
			status = J.getJSONObject(position).getString("status");
			buyer_name = J.getJSONObject(position).getString("buyer_name");
			updated_at = J.getJSONObject(position).getString("updated_at");
			address = J.getJSONObject(position).getString("address");
			order_id = J.getJSONObject(position).getString("order_id");
			total = J.getJSONObject(position).getString("total");
		}catch(Exception e){
			
		}
		intent.setClass(context, AllOrderDetail.class);
		intent.putExtra("status", status);
		intent.putExtra("order_id", order_id);
		intent.putExtra("buyer_name", buyer_name);
		intent.putExtra("updated_at", updated_at);
		intent.putExtra("address", address);
		intent.putExtra("total", total);
		if(METHOD.equals("ORDER_LIST_USER"))
			intent.putExtra("from", METHOD);
		else
			intent.putExtra("from", "ORDER_LIST_ALL");
		Log.d("order_id", order_id);
		Log.d("buyer_name", buyer_name);
		Log.d("updated_at", updated_at);
		Log.d("address", address);
		Log.d("total", total);
		startActivity(intent);
	}
	
	//start----------------------------------------------------------------------------------------
    /** AsyncTask to parse json data and load ListView */
    private class DataLoaderTask extends AsyncTask<String, String, String>{
    	private ProgressDialog dialog = new ProgressDialog(context);
    	
    	protected void onPreExecute() {
    		if(getIntent().getStringExtra(INTENT_FROM)!=null){
    			CloseBook();
    		}
    		//this.dialog.setTitle(getString(R.string.order));
    		this.dialog.setCancelable(false);
            this.dialog.setIndeterminate(false);
            this.dialog.setMessage(getString(R.string.loading));
            this.dialog.setIcon(R.drawable.loading);
            //this.dialog.show();
        }
    	
        @Override  
        protected void onProgressUpdate(String... values)  
        {  
            dialog.setMessage(values[0]);
        }  

		@Override
		protected String doInBackground(String... str) {
				publishProgress(getString(R.string.downloading));
				JSONObject json = null;
				try{
					if(str[0].equals("ORDER_LIST_USER")){
						//listView.setBackgroundResource(R.drawable.bg_history_close);
						json = cf.GetAllOrdersWithEmail(db.getEmail());
						if(Integer.parseInt(json.getString("success"))==1){
							J = json.getJSONArray("orders");
							publishProgress(getString(R.string.downloaded));
							return "success";
						}else
							return getString(R.string.no_order);
					}else if(str[0].equals("PLANT_LIST_USER")){
						//listView.setBackgroundResource(R.drawable.bg_history_close);
						json = pf.getPlantList("HISTORY", db.getEmail());
						if(Integer.parseInt(json.getString("success"))==1){
							J = json.getJSONArray("plants");
							publishProgress(getString(R.string.downloaded));
							return str[0];
						}else
							return getString(R.string.no_order);
					}else{
						J = cf.GetAllOrders();
						publishProgress(getString(R.string.downloaded));
						return "success";
					}
				}catch(Exception e){
					e.printStackTrace();
					return getString(R.string.error_server);
				}
		}
		
        /** Invoked by the Android on "doInBackground" is executed */
		@Override
		protected void onPostExecute(String str) {
			
			if (dialog.isShowing()) {
                dialog.dismiss();
			}
			if(str.equals("success")){
				if(J!=null){
					((ListView) listView).setAdapter(new ItemAdapter(J));
					if(getIntent().getStringExtra(INTENT_FROM)!=null){
						OpenBook();
					}
					listView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						    //TextView tmptv = (TextView)view.findViewById(R.id.txtId);
							if(METHOD.equals("PLANT_LIST_USER")){
								
							}else{
								showOrderDetails(position);
							}
						}
					});
				}else{
					Dialog(getString(R.string.error),getString(R.string.no_order),true);
				}
			}else if(str.equals("PLANT_LIST_USER")){
				if(J!=null){
					((ListView) listView).setAdapter(new ItemAdapter1(J));
					OpenBook();
				}else{
					Dialog(getString(R.string.error),getString(R.string.no_order),true);
				}
			}else{
				if(str.equals(getString(R.string.error_server))){
					reConnectDialog();
				}else{
					Dialog(getString(R.string.error),str,true);
				}
			}
		}		
    }
    
    public void reConnectDialog(){
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder
		.setTitle(getString(R.string.error_server))
		.setMessage(getString(R.string.reconnect)+"?")
		.setCancelable(false).
				setPositiveButton(getString(R.string.yes),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					if(METHOD.equals("ORDER_LIST_USER")){
						METHOD = "ORDER_LIST_USER";
						new DataLoaderTask().execute(METHOD);
					}else if(METHOD.equals("PLANT_LIST_USER")){
						METHOD = "PLANT_LIST_USER";
						new DataLoaderTask().execute(METHOD);
					}else if(METHOD.equals("ORDER_LIST_ALL")){
						METHOD = "ORDER_LIST_ALL";
						new DataLoaderTask().execute(METHOD);
					}
				}
			  }).setNegativeButton(getString(R.string.no),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					finish();
				}
			  }).show();
    }
    
    @Override
	public void onResume()
	    {  // After a pause OR at startup
	    super.onResume();
	    //Refresh your stuff here
	    if(getIntent().getStringExtra(INTENT_FROM)!=null){
			setContentView(R.layout.history_list);
			listView = (ListView) findViewById(R.id.list);
			lnl = (LinearLayout)findViewById(R.id.lnl);
			btnOrderList = (Button)findViewById(R.id.btnOrder);
			btnPlantList = (Button)findViewById(R.id.btnPlant);
			
			btnOrderList.setOnClickListener(btnlistener);
			btnPlantList.setOnClickListener(btnlistener);
			METHOD = "ORDER_LIST_USER";
			new DataLoaderTask().execute(METHOD);
		}else{
			setContentView(R.layout.ac_image_list);
			listView = (ListView) findViewById(android.R.id.list);
			METHOD = "ORDER_LIST_ALL";
			new DataLoaderTask().execute(METHOD);
		}
	}

}
