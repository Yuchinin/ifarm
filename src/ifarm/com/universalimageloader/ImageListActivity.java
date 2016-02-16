package ifarm.com.universalimageloader;

import ifarm.com.R;
import ifarm.com.library.SettingHelper;
import ifarm.com.library.UserFunctions;
import ifarm.com.universalimageloader.Constants.Extra;

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

public class ImageListActivity extends AbsListViewBaseActivity {

	DisplayImageOptions options;
	String[] imageUrls;
	JSONArray J;
	SettingHelper settings;
	String strUrl;
	Context context = this;
	UserFunctions userFunctions;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings = new SettingHelper(context);
		Log.d("debug", "1");
		strUrl = "http://"+settings.getIp()+settings.getRootFolder()+"get_all_users.php";
		Log.d("debug", "2");
		setContentView(R.layout.ac_image_list);
		listView = (ListView) findViewById(android.R.id.list);
		//start----------------------------------------------
	    // URL to the JSON data         
	    // The parsing of the xml data is done in a non-ui thread 
	    DataLoaderTask listViewLoaderTask = new DataLoaderTask();
	    // Start parsing xml data
	    listViewLoaderTask.execute(strUrl,"users");	// Url,tableName             
	    //----------------------------------------------
	}

	@Override
	public void onBackPressed() {
		AnimateFirstDisplayListener.displayedImages.clear();
		super.onBackPressed();
	}

	private void startImagePagerActivity(int position) {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		//intent.putExtra(Extra.IMAGES, imageUrls);
		
		intent.putExtra(Extra.IMAGES, imageUrls);
		intent.putExtra(Extra.IMAGE_POSITION, position);
		startActivity(intent);
	}

	class ItemAdapter extends BaseAdapter {
		JSONArray J;
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
		
		private ItemAdapter(JSONArray JSONARR){
			J = JSONARR;
		}

		private class ViewHolder {
			public TextView txt_desc,txtName;
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
				holder.txtName = (TextView) view.findViewById(R.id.txtName);
				holder.txt_desc = (TextView) view.findViewById(R.id.txt_desc);
				holder.image = (ImageView) view.findViewById(R.id.image);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			
			String name = null,email=null,level=null,imgurl=null,gender=null;
	        try {
				name = J.getJSONObject(position).getString("name");
				email = J.getJSONObject(position).getString("email");
		        level = J.getJSONObject(position).getString("level");
		        gender = J.getJSONObject(position).getString("gender");
		        imgurl = imageUrls[position];
		        String tmp = "http://"+settings.getIp()+settings.getRootFolder()+"pic/";
		        Log.d("gettmp",tmp);
		        Log.d("imgurl",imgurl);
		        if((imgurl.equals(tmp)) || (imgurl.length()==0)){
		        	Log.d("ifimgurl","No pic found !");
		        	if(Integer.parseInt(level)<10){
		        		if(gender.equals("M")){
		        			imgurl = "drawable://" + R.drawable.male;
		        		}else{
		        			imgurl = "drawable://" + R.drawable.female;
		        		}
		        	}else if(Integer.parseInt(level)>=10 && Integer.parseInt(level)<50){
		        		imgurl = "drawable://" + R.drawable.farmer;
		        	}else if(Integer.parseInt(level)>=50 && Integer.parseInt(level)<99){
		        		imgurl = "drawable://" + R.drawable.basic;
		        	}else if(Integer.parseInt(level)>=99){
		        		imgurl = "drawable://" + R.drawable.admin;
		        	}	
		        }
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        
	        holder.txtName.setText(name);

			holder.txt_desc.setText(
					getString(R.string.email)+":"+email +
					"\n" + getString(R.string.level)+":"+level+
					"\n" + getString(R.string.gender)+":"+gender);		

			imageLoader.displayImage(imgurl, holder.image, options, animateFirstListener);

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
            this.dialog.setMessage("Downloading from: "+strUrl);
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
			String tmpStr = "success";
			userFunctions = new UserFunctions(context);
			try{
				publishProgress("Downloading Data...");
				// Download JsonData and Convert to Json Array
	        	J = userFunctions.GetAllUsers();
	        	//[0]=Url;[1]=tablename
	        	publishProgress("Download Completed !");
	        	imageUrls = new String[J.length()];
	        	publishProgress("Adding Image Url...");
	        	for(int x = 0;x<J.length();x++){
					Log.d("for try["+x+"]", J.getJSONObject(x).getString("name")); 
					Log.d("So imgurl["+x+"]=", J.getJSONObject(x).getString("imgurl"));
					imageUrls[x] = "http://"+settings.getIp()+settings.getRootFolder()+"pic/"+
					J.getJSONObject(x).getString("imgurl");
					Log.d("imageurl", imageUrls[x]);
					publishProgress("Adding Image Url... \n"+(J.length())+" more Remaining");
				}
	        }catch(Exception e){
	        	Log.d("JSON Exception1",e.toString());
	        	return e.toString();
	        }
			return tmpStr;
		}
		
        /** Invoked by the Android on "doInBackground" is executed */
		@Override
		protected void onPostExecute(String str) {
			Log.d("onPostExecute", "1");
			if (dialog.isShowing()) {
                dialog.dismiss();
			}
			Log.d("onPostExecute", "2");
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
						startImagePagerActivity(position);
					}
				});
			}
			else{
					finish();
				}
		}		
    }
    //-----------------------------------------------------------------------------------
	
}