package ifarm.com;

import ifarm.com.library.CartFunctions;
import ifarm.com.library.PlantFunctions;
import ifarm.com.library.SettingHelper;
import ifarm.com.universalimageloader.AbsListViewBaseActivity;

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
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PlantReadyList extends AbsListViewBaseActivity {

	DisplayImageOptions options;
	//String[] imageUrls;
	JSONArray J;
	SettingHelper settings;
	Context context = this;
	CartFunctions cf;
	PlantFunctions pf;
	int position_to;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	    //----------------------------------------------
	}

	@Override
	public void onBackPressed() {
		AnimateFirstDisplayListener.displayedImages.clear();
		super.onBackPressed();
	}

	class ItemAdapter extends BaseAdapter {
		JSONArray J;
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
		
		private ItemAdapter(JSONArray JSONARR){
			J = JSONARR;
		}

		private class ViewHolder {
			public TextView txt_desc,txtName,txtId;
			public ImageView image;
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
				view = getLayoutInflater().inflate(R.layout.item_list_image0, parent, false);
				holder = new ViewHolder();
				holder.txtId = (TextView) view.findViewById(R.id.txtId);
				holder.txtName = (TextView) view.findViewById(R.id.txtName);
				holder.txt_desc = (TextView) view.findViewById(R.id.txt_desc);
				holder.image = (ImageView) view.findViewById(R.id.image);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			
			String name,id,status,imgurl,email;
			name=id=status=imgurl=email=null;
	        try {
	        	id = J.getJSONObject(position).getString("id");
				name = J.getJSONObject(position).getString("name");
				email = J.getJSONObject(position).getString("buyer_email");
				status = J.getJSONObject(position).getString("status");
		        imgurl = pf.getImageUrl(J, position,"product_id");
		        //J.getJSONObject(position).getString("imgurl");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        // Special Case
	        
	        holder.txtId.setText(id);
	        
	        holder.txtName.setText("(Id:"+id+")"+name);

			holder.txt_desc.setText(getString(R.string.email)+":"+email+"\n"+cf.getStatus(status));
			
			InitImgLoader();

			imageLoader.displayImage(imgurl, holder.image, options, animateFirstListener);
			
			//if(pf.getCamTF(J, position)==false)
				//holder.txtName.setTextColor(Color.RED);
			return view;
		}
	}
	
	private void InitImgLoader(){
		if(imageLoader.isInited())
			;
		else{
			imageLoader.init(ImageLoaderConfiguration.createDefault(context));
			options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.loading)
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.icon1)
			.cacheInMemory()
			.cacheOnDisc()
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
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
	
	public void DialogPlantYesNo(){
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle(getString(R.string.done)+" ?");
		alertDialogBuilder.setMessage(getString(R.string.r_u_sure)).setCancelable(false).
				setPositiveButton(getString(R.string.yes),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					//gotoCam();
					new PlantReadyTask().execute();
				}
			  }).setNegativeButton(getString(R.string.no),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.dismiss();
				}
			  });
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
    }
	
	//start----------------------------------------------------------------------------------------
    /** AsyncTask to parse json data and load ListView */
    private class DataLoaderTask extends AsyncTask<String, String, String>{
    	private ProgressDialog dialog = new ProgressDialog(context);
    	
    	protected void onPreExecute() {
    		this.dialog.setCancelable(false);
    		this.dialog.setIndeterminate(false);
    		//this.dialog.setTitle(getString(R.string.plant));
            this.dialog.setIcon(R.drawable.icon1);
            this.dialog.show();
        }
    	 
        @Override  
        protected void onProgressUpdate(String... values)  
        {  
            dialog.setMessage(values[0]);
        }  

		@Override
		protected String doInBackground(String... str) {
			JSONObject json = null;
				publishProgress(getString(R.string.downloading));
				try{
					json = cf.GetPlantPrepare();
					if(Integer.parseInt(json.getString("success"))==1){
						J = json.getJSONArray("plants");
						return "success";
					}else{
						return getString(R.string.noplant);
					}
				}catch(Exception e){
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
				InitImgLoader();
				((ListView) listView).setAdapter(new ItemAdapter(J));
				
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						position_to = position;
						DialogPlantYesNo();
					}
				});
			}else{
				if(str.equals(getString(R.string.error_server)))
	            	Dialog(getString(R.string.error),str,true);
	            else{
	            	Dialog(getString(R.string.ready_plant),getString(R.string.nocrop),true);
	            }
			}
		}		
    }
    //-----------------------------------------------------------------------------------
	
  //start----------------------------------------------------------------------------------------
    /** AsyncTask to parse json data and load ListView */
    private class PlantReadyTask extends AsyncTask<String, String, String>{
    	private ProgressDialog dialog = new ProgressDialog(context);
    	
    	protected void onPreExecute() {
    		this.dialog.setCancelable(false);
    		this.dialog.setIndeterminate(false);
    		this.dialog.setTitle(getString(R.string.action));
            this.dialog.setIcon(R.drawable.icon1);
            this.dialog.show();
        }
    	 
        @Override  
        protected void onProgressUpdate(String... values)  
        {  
            dialog.setMessage(values[0]);
        }  

		@Override
		protected String doInBackground(String... str) {
			JSONObject json = null;
				//publishProgress(getString(R.string.act));
				try{
					String plant_id = J.getJSONObject(position_to).getString("id");
					json = cf.PlantReady(plant_id);
					if(Integer.parseInt(json.getString("success"))==1){
						return "success";
					}else{
						return getString(R.string.action_failed);
					}
				}catch(Exception e){
					return getString(R.string.error_server);
				}
		}
		
		@Override
		protected void onPostExecute(String str) {
			
			if (dialog.isShowing()) {
                dialog.dismiss();
			}
			if(str.equals("success")){
				DataLoaderTask listViewLoaderTask = new DataLoaderTask();
    		    listViewLoaderTask.execute();
				Dialog(getString(R.string.plant),getString(R.string.action_success),false);
			}else{
				Dialog(getString(R.string.error),str,true);
			}
		}		
    }
    //-----------------------------------------------------------------------------------
    @Override
	public void onResume(){
		super.onResume();
		settings = new SettingHelper(context);
		pf = new PlantFunctions(context);
		cf = new CartFunctions(context);
		
		setContentView(R.layout.ac_image_list);
		listView = (ListView) findViewById(android.R.id.list);
		
	    DataLoaderTask listViewLoaderTask = new DataLoaderTask();
	    listViewLoaderTask.execute();
		
	}
}