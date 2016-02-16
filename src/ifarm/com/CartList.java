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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import ifarm.com.library.BaseFunctions;
import ifarm.com.library.CartFunctions;
import ifarm.com.library.PlantFunctions;
import ifarm.com.library.SettingHelper;
import ifarm.com.library.UserDBHandler;
import ifarm.com.library.UserFunctions;
import ifarm.com.universalimageloader.AbsListViewBaseActivity;
import ifarm.com.universalimageloader.ImageGridActivity;

public class CartList extends AbsListViewBaseActivity {
	
	Context context = this;
	DisplayImageOptions options;
	BaseFunctions bf;
	CartFunctions cf;
	UserFunctions uf;
	UserDBHandler db;
	SettingHelper st;
	JSONArray J;
	TextView txtTotal;
	Button btnPay;
	Intent intent = new Intent();
	//String[] imageUrls;
	//static String imgfolder = "/ifarm_api/android/product_img/";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bf = new BaseFunctions(context);
		cf = new CartFunctions(context);
		db = new UserDBHandler(context);
		st = new SettingHelper(context);
		uf = new UserFunctions(context);
		setContentView(R.layout.cart_list);
		txtTotal= (TextView)findViewById(R.id.txtTotal);
		btnPay = (Button)findViewById(R.id.btnPay);
		btnPay.setOnClickListener(btnlistener);
		listView = (ListView) findViewById(R.id.list);
		if(!uf.isUserLoggedIn()){
			intent.setClass(context, Login_main.class);
			intent.putExtra(INTENT_FROM, this.getClass().getSimpleName());
			startActivity(intent);
		}else{
			
			//start----------------------------------------------
		    // URL to the JSON data         
		    // The parsing of the xml data is done in a non-ui thread 
		    //DataLoaderTask listViewLoaderTask = new DataLoaderTask();
		    // Start parsing xml data
			DataLoaderTask listViewLoaderTask = new DataLoaderTask();
		    listViewLoaderTask.execute();      
		}
	}
	
	private Button.OnClickListener btnlistener = new Button.OnClickListener(){
		//@Override
		public void onClick(View v) {
			if(v.getId()==R.id.btnPay){
				DialogPayConfirm();
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
			
			String name = null,id = null,order_id,product_id,type,imgurl = null;
			//name=id=order_id,product_id,type,imgurl=null;
	        try {
	        	id = J.getJSONObject(position).getString("id");
				name = J.getJSONObject(position).getString("name");
				order_id = J.getJSONObject(position).getString("order_id");
				product_id = J.getJSONObject(position).getString("product_id");
				type = J.getJSONObject(position).getString("type");
				imgurl = cf.getImageUrl(J, position);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        // Special Case
	        
	        holder.txtId.setText(id);
	        
	        holder.txtName.setText(name);

			holder.txt_desc.setText(cf.getDescription(J, position));
			
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
	
	public void DialogCart(final int pos){
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		try {
			alertDialogBuilder.setTitle(J.getJSONObject(pos).getString("name"));
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		alertDialogBuilder
				.setPositiveButton(getString(R.string.modify),new DialogInterface.OnClickListener() {
					//long click cart list item
				public void onClick(DialogInterface dialog,int id) {
					//if modify clicked
					//--------------------------------------------------
					LayoutInflater layoutInflater = LayoutInflater.from(context);
					View promptView = layoutInflater.inflate(R.layout.prompts3, null);
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
					alertDialogBuilder.setView(promptView);
					final EditText qty = (EditText) promptView.findViewById(R.id.qty_input);
					setEditChangeListener(qty, pos);
					try {
						alertDialogBuilder.setTitle(J.getJSONObject(pos).getString("name"));
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					alertDialogBuilder
							.setPositiveButton(getString(R.string.modify), new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											String quantity;
											quantity = qty.getText().toString();
											if((quantity.length()==0)){
												Dialog(getString(R.string.error),getString(R.string.qty_input),false);
											}else{
												try {
													new CartActionAsyncTask().execute("modify",J.getJSONObject(pos).getString("id"),quantity);
												} catch (JSONException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
											}
										}
									});
					AlertDialog alertD = alertDialogBuilder.create();
					alertD.show();
					//--------------------------------------------------
				}
			  }).setNegativeButton(getString(R.string.delete),new DialogInterface.OnClickListener() {
				  // if delete clicked
				public void onClick(DialogInterface dialog,int id) {
					//-------------------------------------
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
					alertDialogBuilder.setTitle(getString(R.string.delete));
					alertDialogBuilder.setMessage(getString(R.string.r_u_sure))
					.setCancelable(false)
						.setPositiveButton(getString(R.string.yes),new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								try {
									new CartActionAsyncTask().execute("delete",J.getJSONObject(pos).getString("id"),"0");
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						  }).setNegativeButton(getString(R.string.no),new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
								}
							  });
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					//-------------------------------------
					
				}
			  });
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
    }
	
	private void setEditChangeListener(final EditText edt,final int position){
		edt.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				try {
					if(edt.length()!=0){
						Toast.makeText(context, "[NT "+(Integer.parseInt(edt.getText().toString())*Integer.parseInt(J.getJSONObject(position).getString("price")))+"]", Toast.LENGTH_SHORT).show();
						//alertDialogBuilder.setTitle(txtName.getText().toString()+"[NT"+(Integer.parseInt(qty.getText().toString())*Integer.parseInt(J.getJSONObject(position).getString("price")))+"]");
					}
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0,
					int arg1, int arg2, int arg3) {
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				
			}
			
		});
	}
	
	public void DialogPayConfirm(){
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		// set title
		alertDialogBuilder.setTitle(getString(R.string.pay));

		// set dialog message
		alertDialogBuilder.setMessage(getString(R.string.r_u_sure)).setCancelable(false).
				setPositiveButton(getString(R.string.yes),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					new CartActionAsyncTask().execute("pay",null,null);
				}
			  }).setNegativeButton(getString(R.string.no),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {

				}
			  });
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
    }
	
	public void reConnectDialog(){
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder
		.setTitle(getString(R.string.error_server))
		.setMessage(getString(R.string.reconnect)+"?")
		.setCancelable(false).
				setPositiveButton(getString(R.string.yes),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					new DataLoaderTask().execute();
				}
			  }).setNegativeButton(getString(R.string.no),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					finish();
				}
			  });
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
    }
	
	private class CartActionAsyncTask extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(context);
        String msg = "success";

        protected void onPreExecute() {
            this.dialog.setMessage(getString(R.string.loading));
            this.dialog.setCancelable(false);
            this.dialog.setIndeterminate(false);
            this.dialog.setTitle(getString(R.string.order));
            this.dialog.setIcon(R.drawable.loading);
            this.dialog.show();
        }
        
        @Override  
        protected void onProgressUpdate(String... values)  
        {  
            //set the current progress of the progress dialog  
            dialog.setMessage(values[0]);  
        }
        
        @Override
		protected String doInBackground(String... str) {
        	JSONObject jObj = null;
        		publishProgress(getString(R.string.connecting_server));
        			String method = str[0];
        			String order_id = str[1];
        			String amount = str[2];
        			try{
        				if(method.equals("modify")){
            				publishProgress(getString(R.string.modify));
                			jObj = cf.ModifyCrop(db.getEmail(),order_id,amount);
                        	Log.d("jObj", "Pass");
            			}else if(method.equals("delete")){
            				publishProgress(getString(R.string.delete));
                			jObj = cf.DeleteCrop(db.getEmail(),order_id);
            			}else if(method.equals("pay")){
            				publishProgress(getString(R.string.pay));
            				jObj = cf.OrderConfirm(db.getEmail());
            			}
        				if(jObj.getString("success") != null){
							Log.d("Success not null", "Pass");
						    String res = jObj.getString("success");
						    if(Integer.parseInt(res) == 1){
						    	publishProgress(getString(R.string.action_success));
						    	msg = method;
						    }else
						    	msg = getString(R.string.action_failed);
						}else
							msg = getString(R.string.action_failed);
        			}catch(Exception e){
        				return getString(R.string.error_server);
        			}
			return msg;
		}

        @Override
        protected void onPostExecute(String msg) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            txtTotal.setText(cf.getTotal(J));
            if(msg.equals("modify") || msg.equals("delete")){
            	DataLoaderTask listViewLoaderTask = new DataLoaderTask();
    		    listViewLoaderTask.execute();
            }else if(msg.equals("pay")){
            	Dialog(getString(R.string.pay),getString(R.string.pay_success),true);
            }else
            	Dialog(getString(R.string.error),msg,false);
            //((ListView) listView).setAdapter(new ItemAdapter(J));
        }
	}
	
	//start----------------------------------------------------------------------------------------
    /** AsyncTask to parse json data and load ListView */
    private class DataLoaderTask extends AsyncTask<String, String, String>{
    	private ProgressDialog dialog = new ProgressDialog(context);
    	
    	protected void onPreExecute() {
    		this.dialog.setTitle(getString(R.string.cart));
    		this.dialog.setCancelable(false);
            this.dialog.setIndeterminate(false);
            this.dialog.setMessage(getString(R.string.loading));
            if(getLanguage().equals("zh_TW")){
            	this.dialog.setIcon(R.drawable.btn_cropdetails_cart_c_a);
            }else{
            	this.dialog.setIcon(R.drawable.btn_cropdetails_cart_e_a);
            }
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
			JSONObject json = null;
				publishProgress(getString(R.string.cart_downloading));
				try{
					json = cf.GetOrders(db.getEmail());
					if(Integer.parseInt(json.getString("success"))==1){
						publishProgress(getString(R.string.downloaded));
						J = json.getJSONArray("orders");
						return "success";
					}else if(Integer.parseInt(json.getString("error"))==1){
						return getString(R.string.no_order);
					}else
						return getString(R.string.processing_order);
					
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
				if(J!=null){
					btnPay.setEnabled(true);
					txtTotal.setText(cf.getTotal(J));
					InitImgLoader();
					((ListView) listView).setAdapter(new ItemAdapter(J));
					listView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						    TextView tmptv = (TextView)view.findViewById(R.id.txtId);
						    String strid = tmptv.getText().toString();
							Log.d("txt_id:",strid);
						    int tempid = Integer.parseInt(strid);
						}
					});
					listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> adapter,
								View view, int position, long id) {
								DialogCart(position);
							return true;
						}
					});
				}//else{
					//Dialog(getString(R.string.cart),getString(R.string.no_order),true);
					//btnPay.setEnabled(false);
					//txtTotal.setText(getString(R.string.no_order));
					//}
					
				
			}else{
				if(str.equals(getString(R.string.error_server))){
					//Dialog(getString(R.string.error),str,true);
					reConnectDialog();
				}else{
					Dialog(getString(R.string.cart),str,true);
				}
			}
		}		
    }

}
