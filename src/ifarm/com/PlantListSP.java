package ifarm.com;

import ifarm.com.library.BaseFunctions;
import ifarm.com.library.PlantFunctions;
import ifarm.com.library.SettingHelper;
import ifarm.com.library.UserDBHandler;
import ifarm.com.library.UserFunctions;
import ifarm.com.universalimageloader.AbsListViewBaseActivity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

public class PlantListSP extends AbsListViewBaseActivity {

	ItemAdapter adapter;
	DisplayImageOptions options;
	//String[] imageUrls;
	JSONArray J;
	SettingHelper settings;
	Context context = this;
	PlantFunctions plantfunctions;
	BaseFunctions baseFunctions;
	UserFunctions uf;
	String seasoncode = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings = new SettingHelper(context);
		plantfunctions = new PlantFunctions(context);
		baseFunctions = new BaseFunctions(context);
		uf = new UserFunctions(context);
		
		setContentView(R.layout.ac_image_list);
		listView = (ListView) findViewById(android.R.id.list);
		try{
			Log.d("seasoncodetry", seasoncode);
			Bundle bundle = getIntent().getExtras();
			String season = bundle.getString("season");
			
			if(season.equals("current")){	// From Explore Crop
				Log.d("plantSP", "Current Crop");
				seasoncode = "current";
				ChangeBackground(settings.getCurrentSeason());}
			else if(season.equals("all")){// From
				Log.d("plantSP", "All Crop");
				seasoncode = "all";
				//--------------------------
				if(uf.isUserLoggedIn()){
					UserDBHandler db = new UserDBHandler(context);
					if(db.getLevel()>=50){
						Dialog(getString(R.string.error),getString(R.string.mode)+getString(R.string.developer),false);
					}
				}
				//--------------------------
				ChangeBackground(settings.getCurrentSeason());}
			else {
				Log.d("plantSP", "Explore Crop");
				seasoncode = bundle.getString("season");
				ChangeBackground(seasoncode);}
			
		}catch(Exception e){
			//e.printStackTrace();
		}
		
		
		//start----------------------------------------------
	    // URL to the JSON data         
	    // The parsing of the xml data is done in a non-ui thread 
	    DataLoaderTask listViewLoaderTask = new DataLoaderTask();
	    // Start parsing xml data
	    Log.d("seasoncodeout", seasoncode);
	    listViewLoaderTask.execute(seasoncode);           
	    //----------------------------------------------
	}
	
	private void ChangeBackground(String seasoncode){
		if(seasoncode.equals("SPR"))
			//listView.setBackground(context.getResources().getDrawable(R.drawable.spring));
			listView.setBackgroundResource(R.drawable.bg_spring);
		if(seasoncode.equals("SUM"))
			listView.setBackgroundResource(R.drawable.bg_summer);
		if(seasoncode.equals("AUT"))
			listView.setBackgroundResource(R.drawable.bg_autumn);
		if(seasoncode.equals("WIN"))
			listView.setBackgroundResource(R.drawable.bg_winter);
	}

	@Override
	public void onBackPressed() {
		AnimateFirstDisplayListener.displayedImages.clear();
		super.onBackPressed();
	}
	
	private void detailActivity(int id,int position) {
		Intent intent = new Intent(this, Plant_details.class);
		intent.putExtra("J",J.toString());
		intent.putExtra("position", position);
		intent.putExtra("id", id);
		Log.d("position put in:", Integer.toString(position));
		startActivity(intent);
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
				//view = getLayoutInflater().inflate(R.layout.item_list_image, parent, false);
				view = getLayoutInflater().inflate(R.layout.ac_image_grid, parent, false);
				holder = new ViewHolder();
				holder.txtId = (TextView) view.findViewById(R.id.txtId);
				holder.txtName = (TextView) view.findViewById(R.id.txtName);
				holder.txt_desc = (TextView) view.findViewById(R.id.txt_desc);
				holder.image = (ImageView) view.findViewById(R.id.image);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			
			String name,uid,imgurl;
			name=uid=imgurl=null;
	        try {
	        	uid = J.getJSONObject(position).getString("uid");
				name = J.getJSONObject(position).getString("name");
		        imgurl = plantfunctions.getImageUrl(J, position,"uid");
		        //J.getJSONObject(position).getString("imgurl");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        // Special Case
	        
	        holder.txtId.setText(uid);
	        
	        holder.txtName.setText(name);

			holder.txt_desc.setText(plantfunctions.getDescriptionLight(position, J));
			
			InitImgLoader();

			imageLoader.displayImage(imgurl, holder.image, options, animateFirstListener);

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
	
	//start----------------------------------------------------------------------------------------
    /** AsyncTask to parse json data and load ListView */
    private class DataLoaderTask extends AsyncTask<String, String, String>{
    	private ProgressDialog dialog = new ProgressDialog(context);
    	
    	protected void onPreExecute() {
    		this.dialog.setCancelable(false);
    		this.dialog.setIndeterminate(false);
    		Log.d("Season pic Start",seasoncode);
    		String tmp = seasoncode;
    		if(!tmp.equals("SPR")&&!tmp.equals("SUM")&&!tmp.equals("AUT")&&!tmp.equals("WIN"))
    			tmp = settings.getCurrentSeason();
    		if(getIntent().getStringExtra("exploreOrplant")!=null)
    			if(getIntent().getStringExtra("exploreOrplant").equals("plant"))
        			this.dialog.setTitle(getString(R.string.plant));
        		else{
        			if(tmp.equals("SPR")) this.dialog.setTitle(getString(R.string.spring));
        			if(tmp.equals("SUM")) this.dialog.setTitle(getString(R.string.summer));
        			if(tmp.equals("AUT")) this.dialog.setTitle(getString(R.string.autumn));
        			if(tmp.equals("WIN")) this.dialog.setTitle(getString(R.string.winter));
        			//this.dialog.setTitle(getString(R.string.explore_crops));
        		}
        			
    		Log.d("Season pic",tmp);
            this.dialog.setMessage(getString(R.string.loading));
            if(tmp.equals("SPR")) this.dialog.setIcon(R.drawable.loading_spring);
            if(tmp.equals("SUM")) this.dialog.setIcon(R.drawable.loading_summer);
            if(tmp.equals("AUT")) this.dialog.setIcon(R.drawable.loading_autumn);
            if(tmp.equals("WIN")) this.dialog.setIcon(R.drawable.loading_winter);
            //this.dialog.setIcon(R.drawable.loading);
            this.dialog.show();
        }
    	
    	//Update the progress  
        @Override  
        protected void onProgressUpdate(String... values)  
        {  
            //set the current progress of the progress dialog
            dialog.setMessage(values[0]);
            
        }  

    	//JSONObject jObject;
    	
    	// Doing the parsing of xml data in a non-ui thread 
		@Override
		protected String doInBackground(String... str) {
				publishProgress(getString(R.string.downloading));
				// Download JsonData and Convert to Json Array
				try{
				if(str[0].equals("current")){
					Log.d("Crop trigger","current");
					J = plantfunctions.GetCurrentProducts().getJSONArray("products");
					return "success";}
				else if(str[0].equals("all")) { // all
					Log.d("Crop trigger","all");
					J = plantfunctions.GetAllProducts().getJSONArray("products");
					return "success";}
				else {
					Log.d("Crop trigger","season");
					J = plantfunctions.GetSeasonProducts(str[0]).getJSONArray("products");
					return "success";}
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
				//((ListView) listView).setAdapter(new ItemAdapter(J));
				
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						//startImagePagerActivity(position);
						//detailActivity(position);
						//View parentView = (View) view.getParent();
					    //String strid = ((TextView) parentView.findViewById(R.id.txt_id)).getText().toString();
					    TextView tmptv = (TextView)view.findViewById(R.id.txtId);
					    String strid = tmptv.getText().toString();
						Log.d("txt_id:",strid);
					    int tempid = Integer.parseInt(strid);
					    detailActivity(tempid,position);
					    Log.d("position:",Integer.toString(position));
					    Log.d("long:",Long.toString(id));
					}
				});
			}
			else{
					Dialog(getString(R.string.error),str,true);
				}
		}		
    }
    //-----------------------------------------------------------------------------------
	
}