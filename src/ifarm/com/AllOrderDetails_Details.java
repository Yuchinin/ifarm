package ifarm.com;

import ifarm.com.library.CartFunctions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class AllOrderDetails_Details extends Base{
	Context ct = this;
	CartFunctions cf;
	JSONArray J;
	ListView listView;
	DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_image_list);
		
		listView = (ListView) findViewById(android.R.id.list);
		
		cf = new CartFunctions(ct);
		
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		
		String order_id = bundle.getString("order_id");
		
		new DownloadData().execute(order_id);
	}
	
	private class DownloadData extends AsyncTask<String, String, String> {

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
        	JSONObject json;
        	try{
        		json = cf.GetOrderWithId(arg[0]);
        		if(Integer.parseInt(json.getString("success"))==1){
        			J = json.getJSONArray("orders");
        			return "success";
        		}else{
        			return getString(R.string.no_order);
        		}
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
            	Dialog(getString(R.string.error),msg,true);
            else{
            	//txtDetails.setText(txtDetails.getText().toString()+cf.getTotal(J));
            	InitImgLoader();
				((ListView) listView).setAdapter(new ItemAdapter(J));
            }
        }
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
				view = getLayoutInflater().inflate(R.layout.item_list_image, parent, false);
				holder = new ViewHolder();
				holder.txtId = (TextView) view.findViewById(R.id.txtId);
				holder.txtName = (TextView) view.findViewById(R.id.txtName);
				holder.txt_desc = (TextView) view.findViewById(R.id.txt_desc);
				holder.image = (ImageView) view.findViewById(R.id.image);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			
			String name = null,id = null,product_id,order_id,type,imgurl = null;
	        try {
	        	id = J.getJSONObject(position).getString("id");
				name = J.getJSONObject(position).getString("name");
				order_id = J.getJSONObject(position).getString("order_id");
				product_id = J.getJSONObject(position).getString("product_id");
				type = J.getJSONObject(position).getString("type");
				imgurl = cf.getImageUrl(J, position);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
	        holder.txtId.setText(id);
	        holder.txtName.setText(name);
			holder.txt_desc.setText(cf.getDescription(J, position));
			InitImgLoader();
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
	
	private void InitImgLoader(){
		if(imageLoader.isInited())
			;
		else{
			imageLoader.init(ImageLoaderConfiguration.createDefault(ct));
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

}
