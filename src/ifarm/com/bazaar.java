package ifarm.com;

import ifarm.com.library.BaseFunctions;
import ifarm.com.library.BazaarFunctions;
import ifarm.com.library.SettingHelper;
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
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class bazaar extends AbsListViewBaseActivity {

	DisplayImageOptions options;
	//String[] imageUrls;
	JSONArray J;
	SettingHelper settings;
	//String strUrl;
	Context context = this;
	UserFunctions uf;
	BaseFunctions bf;
	BazaarFunctions bzf;
	ImageButton ibtn_cart; 
    AnimationDrawable ad_cart;  
    CountDownTimer cd_cart;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.market);
		uf = new UserFunctions(context);
        bf = new BaseFunctions(context);
        bzf = new BazaarFunctions(context);
		settings = new SettingHelper(context);

		listView = (ListView) findViewById(R.id.list);
		
		ibtn_cart = (ImageButton)findViewById(R.id.ibtn_ma);
        ibtn_cart.setOnClickListener(btnlistener);
        setAnimation();
    	
		//start----------------------------------------------
	    // URL to the JSON data         
	    // The parsing of the xml data is done in a non-ui thread 
	    DataLoaderTask listViewLoaderTask = new DataLoaderTask();
	    // Start parsing xml data
	    listViewLoaderTask.execute();	// Url,tableName             
	    //----------------------------------------------
	
	}
	
	public ImageButton.OnClickListener btnlistener = new ImageButton.OnClickListener(){
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			stopAnimation();
			ibtn_cart.setImageResource(R.drawable.btn_market_about_a);
			Intent intent = new Intent();
			intent.setClass(context, FarmDetails.class);
			startActivity(intent);
		}

	};
	
	@Override
	public void onBackPressed() {
		AnimateFirstDisplayListener.displayedImages.clear();
		super.onBackPressed();
	}

	class ItemAdapter extends BaseAdapter {
		JSONArray J;
		private ImageLoadingListener animateFirstListener = 
				new AnimateFirstDisplayListener();
		
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
				view = getLayoutInflater().inflate(R.layout.item_list_image4, parent, false);
				holder = new ViewHolder();
				holder.txtName = (TextView) view.findViewById(R.id.txtName);
				holder.txt_desc = (TextView) view.findViewById(R.id.txt_desc);
				holder.image = (ImageView) view.findViewById(R.id.image);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			
			String imgurl=null,name=null,data2=null,data3=null;
	        try {
				name = J.getJSONObject(position).getString("name");
		        data2 = J.getJSONObject(position).getString("rate");
		        data3 = J.getJSONObject(position).getString("desc");
		        imgurl = "http://"+settings.getIp()+
		        		bzf.GetImageFolder()+
		        		J.getJSONObject(position).getString("uid")+
		        		"."+J.getJSONObject(position).getString("imgtype");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        
	        holder.txtName.setText(name);

			holder.txt_desc.setText(
					getString(R.string.desc)+":"+data3 +
					"\n" + getString(R.string.rating)+":"+data2);
			Log.d("displayImage", "1");			

			imageLoader.displayImage(imgurl, holder.image, options, animateFirstListener);
			Log.d("displayImage", "2");

			return view;
		}
	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = 
				Collections.synchronizedList(new LinkedList<String>());

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
	
	//start--------------------------------------------------------------

    /** AsyncTask to parse json data and load ListView */
    private class DataLoaderTask extends AsyncTask<String, String, String>{
    	private ProgressDialog dialog = new ProgressDialog(context);
    	
    	protected void onPreExecute() {
    		//this.dialog.setTitle(getString(R.string.bazaar));
    		this.dialog.setTitle(" ");
            this.dialog.setMessage(getString(R.string.loading));
            this.dialog.setCancelable(false);
            this.dialog.setIndeterminate(false);
            this.dialog.setIcon(R.drawable.btn_chhome_market);
            this.dialog.show();
        }
    	
    	//Update the progress  
        @Override  
        protected void onProgressUpdate(String... values)  
        {  
            //set the current progress of the progress dialog  
            dialog.setMessage(values[0]);  
        }  

		@Override
		protected String doInBackground(String... str) {
				publishProgress(getString(R.string.downloading));
				// Download JsonData and Convert to Json Array
				try{
					J = bzf.GetAllBazaars();
				}catch(Exception e){
					return getString(R.string.error_server);
				}
	        	publishProgress(getString(R.string.downloaded));
			return "success";
		}
		
        /** Invoked by the Android on "doInBackground" is executed */
		@Override
		protected void onPostExecute(String str) {
			Log.d("onPostExecute", "1");
			if (dialog.isShowing()) {
                dialog.dismiss();
			}
			Log.d("onPostExecute", "2");
			if(str.equals("success")){
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
					public void onItemClick(
							AdapterView<?> parent, View view, int position, long id) {
						try {
							startWeb(J.getJSONObject(position).getString("url"));
						} catch (Exception e) {
							e.printStackTrace();
							Dialog(getString(R.string.error),e.toString(),false);
						}
					}
				});
			}
			else{
				Log.d("msg", str);
				Dialog(getString(R.string.error),str,true);
				}
		}		
		
    }
    //-----------------------------------------------------------------------------------
	
    public void setAnimation(){
    	cd_cart = new CountDownTimer(2000,1000){         
            @Override
            public void onTick(long v) {
            	ibtn_cart.setImageResource(R.drawable.btn_market_about);
            	ibtn_cart.setImageResource(R.drawable.effect_market_about);  
            	ad_cart = (AnimationDrawable) ibtn_cart.getDrawable();  
            	ad_cart.start();
            }
             @Override
            public void onFinish() {
            	this.start();
            }
        }.start();
    }
    
    public void stopAnimation(){
    	cd_cart.cancel();
    }
    
    public void resumeAnimation(){
    	if(cd_cart!=null){
    		cd_cart.start();
    	}
    }
    
    @Override
	public void onResume()
	    {  // After a pause OR at startup
	    super.onResume();
	    //Refresh your stuff here
	    resumeAnimation();
	}
    
}
