package ifarm.com;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.R.color;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import ifarm.com.PlantListSP.ItemAdapter;
import ifarm.com.library.BaseFunctions;
import ifarm.com.library.CartFunctions;
import ifarm.com.library.PlantFunctions;
import ifarm.com.library.SettingHelper;
import ifarm.com.library.UserDBHandler;
import ifarm.com.library.UserFunctions;
import ifarm.com.universalimageloader.AbsListViewBaseActivity;
import ifarm.com.universalimageloader.ImageGridActivity;

public class AllActionList extends AbsListViewBaseActivity {
	
	Context context = this;
	DisplayImageOptions options;
	//CartFunctions cf;
	PlantFunctions pf;
	//UserFunctions uf;
	UserDBHandler db;
	//SettingHelper st;
	JSONArray J;
	Intent intent = new Intent();
	String METHOD,pid;
	int pos;
	//LinearLayout lnl;
	//Button btnOrderList,btnPlantList;
	ItemAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//cf = new CartFunctions(context);
		pf = new PlantFunctions(context);
		db = new UserDBHandler(context);
		//st = new SettingHelper(context);
		//uf = new UserFunctions(context);
		
		//lnl = (LinearLayout)findViewById(R.id.lnl);
		//lnl.setBackgroundResource(R.drawable.bg_history_close);
		//listView = (ListView) findViewById(R.id.list);
		//DataLoaderTask listViewLoaderTask = new DataLoaderTask();
		//listViewLoaderTask.execute();
	}
	
	class ItemAdapter extends BaseAdapter {
		JSONArray J;
		
		private ItemAdapter(JSONArray JSONARR){
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
			String id,plant_id = null,email,action_request = null,created_at = null,updated_at,incharge_by,status = null;
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
			holder.txtName.setText(created_at);
			holder.txtOther.setText(pf.getStatus(status));
			holder.txtId.setTextColor(Color.WHITE);
	        holder.txtDate.setTextColor(Color.WHITE);
			holder.txtName.setTextColor(Color.WHITE);
			holder.txtOther.setTextColor(Color.WHITE);
			return view;
		}
	}
	
	public void showOrderDetails(int position){
		
	}
	
	//start-----------------------------------------------------------------
    /** AsyncTask to parse json data and load ListView */
    private class DataLoaderTask extends AsyncTask<String, String, String>{
    	private ProgressDialog dialog = new ProgressDialog(context);
    	
    	protected void onPreExecute() {
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
						json = pf.getPlantList(str[0], null);
						if(Integer.parseInt(json.getString("success"))==1){
							J = json.getJSONArray("plants");
							publishProgress(getString(R.string.downloaded));
							return "success";
						}else
							return getString(R.string.no_order);
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
					adapter = new ItemAdapter(J);
					((ListView) listView).setAdapter(adapter);
					listView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?>
						parent, View view, int position, long id) {
							pos = position;
							doneDialog();
						}
					});
				}else{
					Dialog(getString(R.string.error),
							getString(R.string.no_order),true);
				}
			}else{
				if(str.equals(getString(R.string.error_server))){
					reConnectDialog();
				}else{
					Dialog(getString(R.string.cart),str,true);
				}
			}
		}		
    }
    
    private class PlantDone extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(context);

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
        		pid = J.getJSONObject(pos).getString("id");
        		Log.d("test", db.getEmail()+"/"+pid+"/"+arg[0]);
        		JSONObject json = pf.doAction(db.getEmail(), pid, arg[0]);
        		if(Integer.parseInt(json.getString("success"))==1){
        			return "success";
        		}else
        			return getString(R.string.action_failed);
        	}catch(Exception e){
        		e.printStackTrace();
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
            	onResume();
            	Dialog(getString(R.string.action),getString(R.string.action_success),false);
            }
        }
	}
    
    public void doneDialog(){
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder
		.setTitle(getString(R.string.action))
		.setMessage(getString(R.string.done)+" ?")
		.setCancelable(false).
				setPositiveButton(getString(R.string.yes),
						new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					METHOD = "DONE";
					new PlantDone().execute(METHOD);
				}
			  }).setNegativeButton(getString(R.string.no),
					  new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					//finish();
				}
			  });
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
    }
    
    public void reConnectDialog(){
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder
		.setTitle(getString(R.string.error_server))
		.setMessage(getString(R.string.reconnect)+"?")
		.setCancelable(false).
				setPositiveButton(getString(R.string.yes),
						new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					METHOD = "UNDONE";
					new DataLoaderTask().execute(METHOD);
				}
			  }).setNegativeButton(getString(R.string.no),
					  new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					finish();
				}
			  });
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
    }
    
    @Override
	public void onResume()
	    {  // After a pause OR at startup
	    super.onResume();
			setContentView(R.layout.ac_image_list);
			listView = (ListView) findViewById(android.R.id.list);
			METHOD = "UNDONE";
			new DataLoaderTask().execute(METHOD);
	}

}
