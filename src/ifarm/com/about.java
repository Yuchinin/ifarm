package ifarm.com;

import ifarm.com.library.BaseFunctions;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class about extends Activity {
	TextView txtTodo,txtWip,txtDone,txtVer;
	ScrollView sVParent,sV1,sV2,sV3;
	BaseFunctions bf;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		bf = new BaseFunctions(this);
		
		SetViewAndListener();
		
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		
		String Todo = bundle.getString("TODO");
		String Wip = bundle.getString("WIP");
		String Done = bundle.getString("DONE");
		
		//sVParent.setOnTouchListener(ScrollViewOnTouchListener);
		
		txtVer.setText(getString(R.string.verName)+bf.GetVersionName()+" \n"+ getString(R.string.verCode) +bf.GetVersionCode());
		
		txtTodo.setText(Todo);
		txtWip.setText(Wip);
		txtDone.setText(Done);
		
		sVParent.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.v("P","PARENT TOUCH");
				// TODO Auto-generated method stub
				findViewById(R.id.sV1).getParent().requestDisallowInterceptTouchEvent(false);
				return false;
			}
        });
        sV1.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event)
            {
                Log.v("C1","CHILD TOUCH");
                 //  Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        sV2.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event)
            {
                Log.v("C2","CHILD TOUCH");
                 //  Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        sV3.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event)
            {
                Log.v("C3","CHILD TOUCH");
                 //  Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        
       /// private ScrollView.OnTouchListener ScrollViewOnTouchListener = new ScrollView.OnTouchListener
	}
	
	public void SetViewAndListener(){
		txtTodo = (TextView)findViewById(R.id.txtTodo);
		txtWip = (TextView)findViewById(R.id.txtWip);
		txtDone = (TextView)findViewById(R.id.txtDone);
		txtVer = (TextView)findViewById(R.id.txtVer);
		sVParent = (ScrollView)findViewById(R.id.sVParent);
		sV1 = (ScrollView)findViewById(R.id.sV1);
		sV2 = (ScrollView)findViewById(R.id.sV2);
		sV3 = (ScrollView)findViewById(R.id.sV3);
	}

}
