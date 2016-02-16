package ifarm.com;

import ifarm.com.library.UserDBHandler;
import ifarm.com.library.UserFunctions;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

public class Developer extends Activity {
	TextView txtName;
	Button btn1,btn2,btn3,btn4;
	UserFunctions uf;
	UserDBHandler db;
	Intent intent = new Intent();
	Context ct = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.developer);
		
		SetViewAndListener();
	}
	
	public void SetViewAndListener(){
		uf = new UserFunctions(ct);
		db = new UserDBHandler(ct);
		txtName = (TextView)findViewById(R.id.txtName);
		btn1 = (Button)findViewById(R.id.btn1);
		btn2 = (Button)findViewById(R.id.btn2);
		btn3 = (Button)findViewById(R.id.btn3);
		btn4 = (Button)findViewById(R.id.btn4);
		btn1.setOnClickListener(btnlistener);
		btn2.setOnClickListener(btnlistener);
		btn3.setOnClickListener(btnlistener);
		btn4.setOnClickListener(btnlistener);
		
		txtName.setText(uf.getIdentityString(ct, db.getLevel()));
	}
	
	private Button.OnClickListener btnlistener = new Button.OnClickListener(){
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btn1){
				intent.setClass(ct, UserList.class);
			}else if(v.getId()==R.id.btn2){
				intent.setClass(ct, AllOrderList.class);
			}else if(v.getId()==R.id.btn3){
				intent.setClass(ct, AllActionList.class);
			}else if(v.getId()==R.id.btn4){
				intent.setClass(ct, PlantReadyList.class);
			}
			startActivity(intent);
		}
	};

}
