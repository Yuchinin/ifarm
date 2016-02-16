package ifarm.com;

import android.os.Bundle;
import android.widget.RadioGroup;

public class Admin_Setting extends Base {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.settings);
		
	}
	
    public void SetViewAndListener(){
    	RadioGroup rg;
    	rg = (RadioGroup)findViewById(R.id.rGroupUrl);
    	// Importing all assets like buttons, text fields
    	rg.setOnCheckedChangeListener(radioGlistener);
    }
    
    private RadioGroup.OnCheckedChangeListener radioGlistener = new RadioGroup.OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			//SharedPreferences settings = getSharedPreferences("settings",MODE_PRIVATE);
			// TODO Auto-generated method stub
			switch(checkedId){
			case R.id.rbtnSpring:
				break;
			case R.id.rbtnSummer:
				break;
			case R.id.rbtnAutumn:
				break;
			case R.id.rbtnWinter:
				break;
			}
			
		}
		
	};
}
