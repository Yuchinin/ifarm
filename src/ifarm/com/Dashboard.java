package ifarm.com;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
 
import ifarm.com.library.UserDBHandler;
import ifarm.com.library.UserFunctions;
 
public class Dashboard extends Base {
    UserFunctions userFunctions;
    //TextView txtUser;
	ImageButton btnPlant,btnStatus,btnHistory, btnMyInfo;
	AnimationDrawable ad_plant ,ad_status ,ad_history ,ad_info;  
    CountDownTimer cd_plant ,cd_status ,cd_history ,cd_info;
	final Context context = this;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dashboard);
        Log.d("Dashboard","SetViewAndListener");
        SetViewAndListener();
        Log.d("this.getClass().getSimpleName()", this.getClass().getSimpleName());
    }
    
    private void setAnimation(){
    	cd_plant = new CountDownTimer(2000,1000){         
            @Override
            public void onTick(long v) {
            	if(getLanguage().equals("zh_TW")){
            		btnPlant.setImageResource(R.drawable.btn_myifarm_plant_c);
                	btnPlant.setImageResource(R.drawable.effect_myifarm_plant_c);  
            	}else{
            		btnPlant.setImageResource(R.drawable.btn_myifarm_plant_e);
                	btnPlant.setImageResource(R.drawable.effect_myifarm_plant_e);
            	}
            	
            	ad_plant = (AnimationDrawable) btnPlant.getDrawable();  
            	ad_plant.start();
            }
            @Override
            public void onFinish() {
            	this.start();
            }
        }.start();
        
        cd_status = new CountDownTimer(3000,1000){         
            @Override
            public void onTick(long v) {
            	if(getLanguage().equals("zh_TW")){
            		btnStatus.setImageResource(R.drawable.btn_myifarm_status_c);
            		btnStatus.setImageResource(R.drawable.effect_myifarm_status_c);  
            	}else{
            		btnStatus.setImageResource(R.drawable.btn_myifarm_status_e);
            		btnStatus.setImageResource(R.drawable.effect_myifarm_status_e);
            	}
            	
            	ad_status = (AnimationDrawable) btnStatus.getDrawable();  
            	ad_status.start();
            }
            @Override
            public void onFinish() {
            	this.start();
            }
        }.start();
        
        cd_history = new CountDownTimer(2500,1000){         
            @Override
            public void onTick(long v) {
            	if(getLanguage().equals("zh_TW")){
            		btnHistory.setImageResource(R.drawable.btn_myifarm_history_c);
            		btnHistory.setImageResource(R.drawable.effect_myifarm_history_c);  
            	}else{
            		btnHistory.setImageResource(R.drawable.btn_myifarm_history_e);
            		btnHistory.setImageResource(R.drawable.effect_myifarm_history_e);
            	}
            	
            	ad_history = (AnimationDrawable) btnHistory.getDrawable();  
            	ad_history.start();
            }
            @Override
            public void onFinish() {
            	this.start();
            }
        }.start();
        
        cd_info = new CountDownTimer(4000,1000){         
            @Override
            public void onTick(long v) {
            	if(getLanguage().equals("zh_TW")){
            		btnMyInfo.setImageResource(R.drawable.btn_myifarm_info_c);
            		btnMyInfo.setImageResource(R.drawable.effect_myifarm_info_c);  
            	}else{
            		btnMyInfo.setImageResource(R.drawable.btn_myifarm_info_e);
            		btnMyInfo.setImageResource(R.drawable.effect_myifarm_info_e);
            	}
            	
            	ad_info = (AnimationDrawable) btnMyInfo.getDrawable();  
            	ad_info.start();
            }
            @Override
            public void onFinish() {
            	this.start();
            }
        }.start();
        
    }
    
	private void resumeAnimation(){
		if(cd_plant !=null && cd_status !=null && cd_history !=null && cd_info !=null){
			cd_plant.start();
			cd_status.start();
			cd_history.start();
			cd_info.start();
		}
	}
	
	private void stopAnimation(){
			cd_plant.cancel();
			cd_status.cancel();
			cd_history.cancel();
			cd_info.cancel();
	}
    
    private Button.OnClickListener btnlistener = new Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			setImageBorder(v);
				Intent intent = new Intent();
				if(v.getId()==R.id.imgbtnplant){
					userFunctions = new UserFunctions(context);
			        UserDBHandler db = new UserDBHandler(context);
			        
					intent.setClass(context,PlantListSP.class);
					intent.putExtra("exploreOrplant", "plant");
					if(db.getLevel()>=50)
						intent.putExtra("season", "all");
					else
						intent.putExtra("season", "current");
					db.close();
				}else if(v.getId()==R.id.imgbtnstatus){
					//intent.setClass(context,ifarm_main.class);
					intent.setClass(context,PlantListSP1.class);
				}else if(v.getId()==R.id.imgbtnhistory){
					intent.putExtra(INTENT_FROM, this.getClass().getSimpleName());
					intent.setClass(context,AllOrderList.class);
				}else if(v.getId()==R.id.imgbtninfo){
					intent.setClass(context,MyInfo.class);
				}
				startActivity(intent);
		}
    };
    
    public void setImageBorder(View v){
    	stopAnimation();
    	if(getLanguage().equals("zh_TW")){
    		if(v.getId()==R.id.imgbtnplant)
    			btnPlant.setImageResource(R.drawable.btn_myifarm_plant_c_a);
    		else if(v.getId()==R.id.imgbtnstatus)
    			btnStatus.setImageResource(R.drawable.btn_myifarm_status_c_a);
    		else if(v.getId()==R.id.imgbtnhistory)
    			btnHistory.setImageResource(R.drawable.btn_myifarm_history_c_a);
    		else if(v.getId()==R.id.imgbtninfo)
    			btnMyInfo.setImageResource(R.drawable.btn_myifarm_info_c_a);	
		}else{
			if(v.getId()==R.id.imgbtnplant)
    			btnPlant.setImageResource(R.drawable.btn_myifarm_plant_e_a);
    		else if(v.getId()==R.id.imgbtnstatus)
    			btnStatus.setImageResource(R.drawable.btn_myifarm_status_e_a);
    		else if(v.getId()==R.id.imgbtnhistory)
    			btnHistory.setImageResource(R.drawable.btn_myifarm_history_e_a);
    		else if(v.getId()==R.id.imgbtninfo)
    			btnMyInfo.setImageResource(R.drawable.btn_myifarm_info_e_a);
		}
    }
    
	public void SetViewAndListener() {
		btnPlant = (ImageButton)findViewById(R.id.imgbtnplant);
		btnStatus = (ImageButton)findViewById(R.id.imgbtnstatus);
		btnHistory = (ImageButton)findViewById(R.id.imgbtnhistory);
		btnMyInfo = (ImageButton)findViewById(R.id.imgbtninfo);
		
		btnPlant.setOnClickListener(btnlistener);
		btnStatus.setOnClickListener(btnlistener);
		btnHistory.setOnClickListener(btnlistener);
		btnMyInfo.setOnClickListener(btnlistener);
		
		Log.d("Language",getLanguage());
		
		setAnimation();
	}
	
	@Override
	public void onResume()
	    {  // After a pause OR at startup
	    super.onResume();
	    resumeAnimation();
	    //Refresh your stuff here
	    /*UserDBHandler db = new UserDBHandler(context);
        Cursor c = db.get(1);
        //txtUser.setText(c.getString(c.getColumnIndex("name")));
        c.close();
        db.close();
        */
	     }
}
