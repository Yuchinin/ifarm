package ifarm.com;

import ifarm.com.R;
import ifarm.com.library.SettingHelper;
import ifarm.com.library.UserFunctions;
import ifarm.com.universalimageloader.AbsListViewBaseActivity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class UserList extends AbsListViewBaseActivity {

	DisplayImageOptions options;
	//String[] imageUrls;
	JSONArray J;
	SettingHelper settings;
	Context context = this;
	UserFunctions userFunctions;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings = new SettingHelper(context);
		setContentView(R.layout.ac_image_list);
		listView = (ListView) findViewById(android.R.id.list);
		//start----------------------------------------------
	    // URL to the JSON data         
	    // The parsing of the xml data is done in a non-ui thread 
	    DataLoaderTask listViewLoaderTask = new DataLoaderTask();
	    // Start parsing xml data
	    listViewLoaderTask.execute();
	    //----------------------------------------------
	}

	@Override
	public void onBackPressed() {
		AnimateFirstDisplayListener.displayedImages.clear();
		super.onBackPressed();
	}
	
	private void detailActivity(int position) {
		Intent intent = new Intent(context, User_details.class);
		intent.putExtra("JSONARR",J.toString());
		//intent.putExtra("id", id);
		intent.putExtra("position", position);
		Log.d("position put in:", Integer.toString(position));
		Log.d("JSONARR put in:", J.toString());
		startActivity(intent);
	}

	class ItemAdapter extends BaseAdapter {
		JSONArray J;
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
		
		private ItemAdapter(JSONArray JSONARR){
			J = JSONARR;
		}

		private class ViewHolder {
			public TextView txt_desc,txtName,txt_id;
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
				view = getLayoutInflater().inflate(R.layout.item_list_image, parent, false);
				holder = new ViewHolder();
				holder.txt_id = (TextView) view.findViewById(R.id.txtId);
				holder.txtName = (TextView) view.findViewById(R.id.txtName);
				holder.txt_desc = (TextView) view.findViewById(R.id.txt_desc);
				holder.image = (ImageView) view.findViewById(R.id.image);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			
			String txt_id,name,email,level,gender,phone,imgurl;
			txt_id=name=email=level=gender=phone=imgurl=null;
	        try {
	        	txt_id = J.getJSONObject(position).getString("uid");
				name = J.getJSONObject(position).getString("name");
				email = J.getJSONObject(position).getString("email");
		        level = J.getJSONObject(position).getString("level");
		        gender = J.getJSONObject(position).getString("gender");
		        phone = J.getJSONObject(position).getString("phone");
		        imgurl = J.getJSONObject(position).getString("imgurl");
		      //----------------------------------------------------
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	      //----------------------------------------------------
	        holder.txt_id.setText(txt_id);
	        
	        holder.txtName.setText(name);

			holder.txt_desc.setText(
					getString(R.string.email)+":"+email +
					"\n" + getString(R.string.level)+":"+level+
					"\n" + getString(R.string.gender)+":"+gender+
					"\n" + getString(R.string.phone)+":"+phone);	

			imageLoader.displayImage(userFunctions.getImage(context,email,Integer.parseInt(level),gender,imgurl), holder.image, options, animateFirstListener);

			return view;
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
	
    /** AsyncTask to parse json data and load ListView */
    private class DataLoaderTask extends AsyncTask<String, String, String>{
    	private ProgressDialog dialog = new ProgressDialog(context);
    	
    	protected void onPreExecute() {
            this.dialog.setMessage(getString(R.string.loading));
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
			String tmpStr = "success";
			userFunctions = new UserFunctions(context);
			// Download JsonData and Convert to Json Array
			try{
				J = userFunctions.GetAllUsers();
			}catch(Exception e){
				return getString(R.string.error_server);
			}
	        publishProgress(getString(R.string.downloaded));
	        
			return tmpStr;
		}
		
        /** Invoked by the Android on "doInBackground" is executed */
		@Override
		protected void onPostExecute(String str) {
			if (dialog.isShowing()) {
                dialog.dismiss();
			}
			if(str=="success"){
				imageLoader.init(ImageLoaderConfiguration.createDefault(context));
				
				options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error)
				.cacheInMemory()
				.cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(20))
				.build();
				
				((ListView) listView).setAdapter(new ItemAdapter(J));
				
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						//startImagePagerActivity(position);
						TextView tmptv = (TextView)view.findViewById(R.id.txtId);
					    //String strid = tmptv.getText().toString();
						//Log.d("txt_id:",strid);
					    //int tempid = Integer.parseInt(strid);
					    detailActivity(position);
					    Log.d("txt_id:",Integer.toString(position));
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
