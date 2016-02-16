package ifarm.com;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import ifarm.com.library.BaseFunctions;
import ifarm.com.library.CartFunctions;
import ifarm.com.library.PlantFunctions;
import ifarm.com.library.SettingHelper;
import ifarm.com.library.UserDBHandler;
import ifarm.com.library.UserFunctions;
import ifarm.com.universalimageloader.BaseActivity;
import ifarm.com.universalimageloader.ImagePagerActivity;
import ifarm.com.universalimageloader.Constants.Extra;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Plant_details extends BaseActivity {

	Context context = this;
	//String[] imageUrls;
	int id,position;
	TextView txtName,txtDesc;
	ImageButton btnPlant,btnOrder,btnUp,btnDown,btnCart;
	LinearLayout rl_de;
	PlantFunctions pf;
	BaseFunctions bf;
	UserFunctions uf;
	CartFunctions cf;
	UserDBHandler db;
	Intent intent;
	SettingHelper settings;
	Gallery gallery;
	JSONArray J;
	String[] desc = {"name","price","location","season","plantable","dayuse","info"};
	String name,plantable,season;
    AnimationDrawable ad_cart;  
    CountDownTimer cd_cart;

	DisplayImageOptions options;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crop_details);
		
		SetViewAndListener();
		//SetChange(position);
	}

	private void SetViewAndListener() {
		Bundle bundle = getIntent().getExtras();
		//imageUrls = bundle.getStringArray(Extra.IMAGES);
		id = bundle.getInt("id");
		position = bundle.getInt("position");
		try {
			J = new JSONArray(bundle.getString("J"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		settings = new SettingHelper(context);
		pf = new PlantFunctions(context);
		uf = new UserFunctions(context);
		bf = new BaseFunctions(context);
		cf = new CartFunctions(context);
		db = new UserDBHandler(context);
		intent = new Intent();
		
		btnPlant = (ImageButton)findViewById(R.id.btnPlant);
		btnOrder = (ImageButton)findViewById(R.id.btnOrder);
		btnUp = (ImageButton)findViewById(R.id.btnUp);
		btnDown = (ImageButton)findViewById(R.id.btnDown);
		btnCart = (ImageButton)findViewById(R.id.btnCart);
		//txt_id = (TextView)findViewById(R.id.txtId);
		txtName = (TextView)findViewById(R.id.txtName);
		txtDesc = (TextView)findViewById(R.id.txtDesc);
		rl_de = (LinearLayout)findViewById(R.id.rl_de);
		
		gallery = (Gallery) findViewById(R.id.gallery);
		gallery.setAdapter(new ImageGalleryAdapter());
		
		btnPlant.setOnClickListener(btnlistener);
		btnOrder.setOnClickListener(btnlistener);
		btnUp.setOnClickListener(btnlistener);
		btnDown.setOnClickListener(btnlistener);
		btnCart.setOnClickListener(btnlistener);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int positions, long ids) {
				position = positions;
				SetChange(positions);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}			
		});
		gallery.setSelection(position,true);
		//txt_id.setText(Integer.toString(id));
		setAnimation();
	}
	
	public void setAnimation(){
		if(getLanguage().equals("zh_TW")){   
			
	        cd_cart = new CountDownTimer(2000,1000){         
	            @Override
	            public void onTick(long v) {
	            	btnCart.setImageResource(R.drawable.btn_cropdetails_cart_c);
	            	btnCart.setImageResource(R.drawable.effect_cropdetails_cart_c);  
	            	ad_cart = (AnimationDrawable) btnCart.getDrawable();  
	            	ad_cart.start();
	            }
	             @Override
	            public void onFinish() {
	            	this.start();
	            }
	        }.start();
		}else{
			cd_cart = new CountDownTimer(2000,1000){         
	            @Override
	            public void onTick(long v) {
	            	btnCart.setImageResource(R.drawable.btn_cropdetails_cart_e);
	            	btnCart.setImageResource(R.drawable.effect_cropdetails_cart_e);  
	            	ad_cart = (AnimationDrawable) btnCart.getDrawable();  
	            	ad_cart.start();
	            }
	             @Override
	            public void onFinish() {
	            	this.start();
	            }
	        }.start();
		}
	}
	
	public void resumeAnimation(){
		if(ad_cart!=null){
			ad_cart.start();
		}
	}
	
	public void stopAnimation(){
		cd_cart.cancel();
	}
	
	@Override
	public void onResume()
	    {  // After a pause OR at startup
	    super.onResume();
	    //Refresh your stuff here
	    resumeAnimation();
	}
	
	private void SetChange(int p){
		try {
			name = J.getJSONObject(p).getString("name");
			plantable = J.getJSONObject(p).getString("plantable");
			season = J.getJSONObject(p).getString("season");
			txtName.setText(name);
			txtDesc.setText(pf.getDescription(p, J));
			//btnPlant.setEnabled(pf.getPlantableTF(J.getJSONObject(p).getString("plantable"),J.getJSONObject(p).getString("season")));
			gallery.setSelection(p,true);
			ChangeBackground(pf.seasonToUpperCase(season));
			if(position==0){
				//btnUp.setEnabled(false);
				btnUp.setVisibility(Button.INVISIBLE);
				btnDown.setVisibility(Button.VISIBLE);
				//btnDown.setEnabled(true);
			}else if(position==J.length()-1){
				//btnUp.setEnabled(true);
				//btnDown.setEnabled(false);
				btnUp.setVisibility(Button.VISIBLE);
				btnDown.setVisibility(Button.INVISIBLE);
			}else{
				//btnUp.setEnabled(true);
				//btnDown.setEnabled(true);
				btnUp.setVisibility(Button.VISIBLE);
				btnDown.setVisibility(Button.VISIBLE);
			}
			if(J.length()<=1){
				btnUp.setVisibility(Button.INVISIBLE);
				btnDown.setVisibility(Button.INVISIBLE);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void ChangeBackground(String seasoncode){
		String tmp = seasoncode.toUpperCase();
		if(!tmp.equals("SPR")&&!tmp.equals("SUM")&&!tmp.equals("AUT")&&!tmp.equals("WIN")){
			Log.d("Season Code Exception",tmp);
			tmp = settings.getCurrentSeason();
			Log.d("Get Current Season",tmp);
		}
		if(tmp.equals("SPR")){
			Log.d("Background",tmp);
			rl_de.setBackgroundResource(R.drawable.bg_cropdetails_spring);
		}else if(tmp.equals("SUM")){
			Log.d("Background",tmp);
			rl_de.setBackgroundResource(R.drawable.bg_cropdetails_summer);
		}else if(tmp.equals("AUT")){
			Log.d("Background",tmp);
			rl_de.setBackgroundResource(R.drawable.bg_cropdetails_autumn);
		}else if(tmp.equals("WIN")){
			Log.d("Background",tmp);
			rl_de.setBackgroundResource(R.drawable.bg_cropdetails_winter);
		}
	}
	
	private void InitImgLoader(){
		if(imageLoader.isInited())
			;
		else{
			imageLoader.init(ImageLoaderConfiguration.createDefault(context));
			options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.ic_stub)
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.ic_error)
			.cacheInMemory()
			.cacheOnDisc()
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
		}
	}
	
	private void NeedLoginDialog(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle(getString(R.string.error));
		alertDialogBuilder.setMessage(getString(R.string.require_login)).setCancelable(false).
				setPositiveButton(getString(R.string.yes),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					stopAnimation();
					intent.setClass(context, Login_main.class);
					intent.putExtra(INTENT_FROM, this.getClass().getSimpleName());
					startActivity(intent);
				}
			  }).setNegativeButton(getString(R.string.no),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					
				}
			  });
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
	}
	
	private Button.OnClickListener btnlistener = new Button.OnClickListener(){
		//@Override
		public void onClick(View v) {
			if(v.getId()==R.id.btnPlant){
				if(!uf.isUserLoggedIn()){
					NeedLoginDialog();
				}else{
					if(pf.getPlantableTF(plantable, season)==false){
						Dialog(getString(R.string.error), getString(R.string.unplantable), false);
					}else{
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
						alertDialogBuilder.setTitle(getString(R.string.plant));
						alertDialogBuilder.setMessage(getString(R.string.r_u_sure)).setCancelable(false).
								setPositiveButton(getString(R.string.yes),new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									try {
										new OrderAsyncTask().execute("plant",J.getJSONObject(position).getString("uid"),null);
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
					}
				}
			}else if(v.getId()==R.id.btnOrder){
				if(!uf.isUserLoggedIn()){
					stopAnimation();
					intent.setClass(context, Login_main.class);
					intent.putExtra(INTENT_FROM, this.getClass().getSimpleName());
					startActivity(intent);
				}else{
					if(pf.getPlantableTF(plantable, season)==false){
						Dialog(getString(R.string.error), getString(R.string.unplantable), false);
					}else{
					LayoutInflater layoutInflater = LayoutInflater.from(context);
					View promptView = layoutInflater.inflate(R.layout.prompts3, null);
					final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
					alertDialogBuilder.setView(promptView);
					final EditText qty = (EditText) promptView.findViewById(R.id.qty_input);
					setEditChangeListener(qty);
					alertDialogBuilder
							//.setCancelable(false)
							.setTitle(txtName.getText().toString())
							//.setIcon(R.drawable.login_icon)
							.setPositiveButton(getString(R.string.order), new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											String quantity;
											quantity = qty.getText().toString();
											if((quantity.length()==0)){
												Dialog(getString(R.string.error),getString(R.string.qty_input),false);
											}else{
												try {
													new OrderAsyncTask().execute("order",J.getJSONObject(position).getString("uid"),quantity);
												} catch (JSONException e) {
													e.printStackTrace();
												}
											}
										}
									});
					AlertDialog alertD = alertDialogBuilder.create();
					alertD.show();
					}
				}
			}else if(v.getId()==R.id.btnUp){
				if(position-1>=0){
					position-=1;
					SetChange(position);
				}
			}else if(v.getId()==R.id.btnDown){
				if(position+1<=J.length()-1){
					position+=1;
					SetChange(position);
				}
			}else if(v.getId()==R.id.btnCart){
				stopAnimation();
				intent.setClass(context, CartList.class);
				startActivity(intent);
			}
		}
	};
	
	private void setEditChangeListener(final EditText edt){
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
	
	private void startImagePagerActivity(int position) {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		//intent.putExtra(Extra.IMAGES, imageUrls);
		intent.putExtra(Extra.IMAGE_POSITION, position);
		startActivity(intent);
	}

	private class ImageGalleryAdapter extends BaseAdapter {
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
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = (ImageView) convertView;
			if (imageView == null) {
				imageView = (ImageView) getLayoutInflater().inflate(R.layout.item_gallery_image, parent, false);
			}
			String tmpurl = null;
			try {
				tmpurl = pf.getImageUrl(J, position,"uid");
			} catch (Exception e) {
				e.printStackTrace();
			}
			InitImgLoader();
			imageLoader.displayImage(tmpurl, imageView, options);
			return imageView;
		}
	}
	
	private class OrderAsyncTask extends AsyncTask<String, String, String> {
        private ProgressDialog dialog = new ProgressDialog(context);

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
        		String method,product_id,amount;
        		method = str[0];
        		product_id = str[1];
        		amount = str[2];
        		try{
        			publishProgress(getString(R.string.cart_adding));
        			if(method.equals("plant")){
        				jObj = cf.PlantCrop(db.getEmail(), product_id);
        			}else if(method.equals("order")){
        				jObj = cf.OrderCrop(db.getEmail(), product_id, amount);
        			}
        			if(Integer.parseInt(jObj.getString("success")) == 1){
				    	publishProgress(getString(R.string.cart_adding_completed));
				    	return "success";
				    }else if(Integer.parseInt(jObj.getString("error")) == 2){
				    	return getString(R.string.processing_order);
				    }else
				    	return getString(R.string.action_failed);
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
            	Dialog(getString(R.string.error),msg,false);
            else{
            	Dialog(getString(R.string.order),getString(R.string.cart_adding_completed),false);
            }
        }
	}
}