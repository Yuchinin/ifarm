package ifarm.com;

import ifarm.com.library.BaseFunctions;
import ifarm.com.library.BazaarFunctions;
import ifarm.com.library.MessageFunctions;
import ifarm.com.library.SettingHelper;
import ifarm.com.library.UserDBHandler;
import ifarm.com.library.UserFunctions;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MessageBoard extends AbsListViewBaseActivity {

	//DataLoaderTask listViewLoaderTask = new DataLoaderTask();
	DisplayImageOptions options;
	//String[] imageUrls;
	JSONArray J;
	SettingHelper settings;
	//String strUrl;
	Context context = this;
	UserFunctions uf;
	BaseFunctions bf;
	MessageFunctions mf;
	Button btnSend;
	EditText edtMsg;
	UserDBHandler db;
    //CountDownTimer cd_cart;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msg_board);
		
        //setAnimation();
    	
		//start----------------------------------------------
	    // URL to the JSON data         
	    // The parsing of the xml data is done in a non-ui thread 
	    //----------------------------------------------
	
	}
	
	public void setViewAndListener(){
		uf = new UserFunctions(context);
        bf = new BaseFunctions(context);
        db = new UserDBHandler(context);
        mf = new MessageFunctions(context);
		settings = new SettingHelper(context);

		listView = (ListView) findViewById(R.id.list);
		
		btnSend = (Button)findViewById(R.id.btnSend);
		edtMsg = (EditText)findViewById(R.id.edtMsg);
		
		//ibtn_cart = (ImageButton)findViewById(R.id.ibtn_ma);
		btnSend.setOnClickListener(btnlistener);
	}
	
	public Button.OnClickListener btnlistener = new Button.OnClickListener(){
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(edtMsg.getText().toString().length()>0){
				edtMsg.setEnabled(false);
				btnSend.setEnabled(false);
				new DataLoaderTask().execute("POST",edtMsg.getText().toString());
			}
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
			public TextView txt_msg,txtName,txtDate;
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
				view = getLayoutInflater().inflate(R.layout.item_list_image5, parent, false);
				holder = new ViewHolder();
				holder.txtName = (TextView) view.findViewById(R.id.txtName);
				holder.txt_msg = (TextView) view.findViewById(R.id.txt_msg);
				holder.txtDate = (TextView) view.findViewById(R.id.txtDate);
				holder.image = (ImageView) view.findViewById(R.id.image);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			
			String imgurl=null,name=null,data2=null,data3=null,data4=null,data5=null,level=null,updated_at=null;
	        try {
	        	name = J.getJSONObject(position).getString("name");
	        	data2 = J.getJSONObject(position).getString("imgurl");
		        data3 = J.getJSONObject(position).getString("msg");
		        data4 = J.getJSONObject(position).getString("email");
		        data5 = J.getJSONObject(position).getString("gender");
		        level = J.getJSONObject(position).getString("level");
		        updated_at = J.getJSONObject(position).getString("updated_at");
		        imgurl = uf.getImage(context, data4, Integer.parseInt(level), data5, data2);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        holder.txtName.setText(name);
			holder.txt_msg.setText(data3);
			holder.txtDate.setText(updated_at+"\t"+bf.getDayFromString(updated_at));
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
    	//private ProgressDialog dialog = new ProgressDialog(context);
    	
    	protected void onPreExecute() {
    		//this.dialog.setTitle(getString(R.string.bazaar));
    		//this.dialog.setTitle(" ");
            //this.dialog.setMessage(getString(R.string.loading));
            //this.dialog.setCancelable(false);
            //this.dialog.setIndeterminate(false);
            //this.dialog.setIcon(R.drawable.btn_chhome_market);
            //this.dialog.show();
        }
    	
    	//Update the progress  
        @Override  
        protected void onProgressUpdate(String... values)  
        {  
            //set the current progress of the progress dialog  
            //dialog.setMessage(values[0]);  
        }  

		@Override
		protected String doInBackground(String... str) {
				try{
					JSONObject json = null;
					if(str[0].equals("GET")){
						json = mf.GetMessage();
						
					}else if(str[0].equals("POST")){
						json = mf.PostMessage(str[1]);
					}
					if(Integer.parseInt(json.getString("success"))==1){
						J = json.getJSONArray("msgs");
						return "success";
					}
					return getString(R.string.action_failed);
				}catch(Exception e){
					e.printStackTrace();
					return getString(R.string.error_server);
				}
		}
		
        /** Invoked by the Android on "doInBackground" is executed */
		@Override
		protected void onPostExecute(String str) {
			//Log.d("onPostExecute", "1");
			//if (dialog.isShowing()) {
                //dialog.dismiss();
			//}
			//Log.d("onPostExecute", "2");
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
				edtMsg.setText("");
				
/*				listView.setOnItemClickListener(new OnItemClickListener() {
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
*/			}else{
				Log.d("msg", str);
				Dialog(getString(R.string.error),str,false);
			}
			btnSend.setEnabled(true);
			edtMsg.setEnabled(true);
			edtMsg.requestFocus();
		}		
		
    }
    //-----------------------------------------------------------------------------------
	
/*    public void setAnimation(){
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
*/    
    public void refreshList(){
	    // Start parsing xml data
    	new DataLoaderTask().execute("GET");	// Url,tableName      
    }
    
    @Override
	public void onResume()
	    {  // After a pause OR at startup
	    super.onResume();
	    //Refresh your stuff here
	    setViewAndListener();
	    refreshList();
	}
    
}
