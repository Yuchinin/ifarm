package ifarm.com;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class Explore_Crops extends Base {
	
	Context context = this;
	LinearLayout lnl;
	ImageButton ibtnHint;
	Boolean Hint = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.explore_main);
		
		SetViewAndListener();
		//Intent intent = this.getIntent();
		//Bundle bundle = intent.getExtras();
		
		//String title = bundle.getString("TITLE");
		//setTitle(title);
	}
	
	private void SetViewAndListener() {
		Button btnSpring,btnSummer,btnAutumn,btnWinter;
		
		btnSpring = (Button)findViewById(R.id.btnSpring);
		btnSummer = (Button)findViewById(R.id.btnSummer);
		btnAutumn = (Button)findViewById(R.id.btnAutumn);
		btnWinter = (Button)findViewById(R.id.btnWinter);
		ibtnHint = (ImageButton)findViewById(R.id.ibtn_Hint);
		
		btnSpring.setOnClickListener(btnlistener);
		btnSummer.setOnClickListener(btnlistener);
		btnAutumn.setOnClickListener(btnlistener);
		btnWinter.setOnClickListener(btnlistener);
		ibtnHint.setOnClickListener(btnlistener);
	}
	
	private Button.OnClickListener btnlistener = new Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int tmp = v.getId();
			Intent intent = new Intent(context,PlantListSP.class);
			intent.putExtra("exploreOrplant", "explore");
			if(tmp==R.id.btnSpring){
				lnl.setBackgroundResource(R.drawable.bg_explore_spr);
				intent.putExtra("season", "SPR");
			}else if(tmp==R.id.btnSummer){
				lnl.setBackgroundResource(R.drawable.bg_explore_sum);
				intent.putExtra("season", "SUM");
			}else if(tmp==R.id.btnAutumn){
				lnl.setBackgroundResource(R.drawable.bg_explore_aut);
				intent.putExtra("season", "AUT");
			}else if(tmp==R.id.btnWinter){
				lnl.setBackgroundResource(R.drawable.bg_explore_win);
				intent.putExtra("season", "WIN");
			}else if(tmp==R.id.ibtn_Hint){
				if(Hint==false){
					lnl.setBackgroundResource(R.drawable.bg_explore_help);
					Hint = true;
				}else{
					lnl.setBackgroundResource(R.drawable.bg_explore);
					Hint = false;
				}
			}
			if(tmp!=R.id.ibtn_Hint)
				startActivity(intent);
			
			//Obsolete--------------------------------
/*			switch (v.getId()){
			case R.id.btnSpring:
				Intent intentNorth = new Intent();
				intentNorth.setClass(context,farm_list.class);
				Bundle bundleNorth = new Bundle();
				bundleNorth.putString("AREA", "N");
				intentNorth.putExtras(bundleNorth);
				startActivity(intentNorth);
				break;
			case R.id.btnSummer:
				Intent intentMid = new Intent();
				intentMid.setClass(context,farm_list.class);
				Bundle bundleMid = new Bundle();
				bundleMid.putString("AREA", "M");
				intentMid.putExtras(bundleMid);
				startActivity(intentMid);
				break;
			case R.id.btnAutumn:
				Intent intentSouth = new Intent();
				intentSouth.setClass(context,farm_list.class);
				Bundle bundleSouth = new Bundle();
				bundleSouth.putString("AREA", "S");
				intentSouth.putExtras(bundleSouth);
				startActivity(intentSouth);
				break;
			
			}
*/		}
		
	};
	
	@Override
	public void onResume() {  // After a pause OR at startup
	    super.onResume();
	    lnl = (LinearLayout)findViewById(R.id.lnl);
	    lnl.setBackgroundResource(R.drawable.bg_explore);
	    //Refresh your stuff here
	    
	}
}
