
package ifarm.com;

import ifarm.com.library.SettingHelper;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Setting extends Activity {
/*	Context context = this;
	SettingHelper settings;;
	RadioGroup radioGUrl;
	RadioButton rbtnEmulator,rbtnDUC,rbtnOther;
	EditText edtOther;
	int SELECTED_URL;
	//String IP_KEY = "Ip Selected";
	//String IP_SAV = "Ip Address";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		SetViewAndListener();
		
		Log.d("Startup",settings.getIp());
		radioGUrl.check(settings.getSelectedIp());
		Log.d("After Startup",settings.getIp());
		edtOther.setEnabled(false);
		edtOther.setFocusableInTouchMode(false);
		if(radioGUrl.getCheckedRadioButtonId()==R.id.rbtnOther){
			Log.d("radioGUrl.getCheckedRadioButtonId()",settings.getIp());
			edtOther.setText(settings.getIp());
			edtOther.setEnabled(true);
			edtOther.setFocusableInTouchMode(true);
			Log.d("radioGUrl.getCheckedRadioButtonId() End",settings.getIp());
		}
		
		//String URL = getsettings.getString("IP_KEY", "empty");
		//Toast(URL);

	}

	public void SetViewAndListener() {
		settings = new SettingHelper(context);
		radioGUrl = (RadioGroup)findViewById(R.id.rGroupUrl);
		rbtnEmulator = (RadioButton)findViewById(R.id.rbtnEmulator);
		rbtnDUC = (RadioButton)findViewById(R.id.rbtnDUC);
		rbtnOther = (RadioButton)findViewById(R.id.rbtnOther);
		edtOther = (EditText)findViewById(R.id.edtOther);
		
		radioGUrl.setOnCheckedChangeListener(radioGlistener);
		edtOther.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				//SharedPreferences settings = getSharedPreferences("settings",MODE_PRIVATE);
				// TODO Auto-generated method stub
				if((radioGUrl.getCheckedRadioButtonId())==R.id.rbtnOther){
					settings.saveIp(edtOther.getText().toString());
					Log.d("Changing URL",settings.getIp());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
		});

	}
	
	private RadioGroup.OnCheckedChangeListener radioGlistener = new RadioGroup.OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			//SharedPreferences settings = getSharedPreferences("settings",MODE_PRIVATE);
			// TODO Auto-generated method stub
			switch(checkedId){
			case R.id.rbtnEmulator:
				settings.saveIp("10.0.2.2");
				settings.saveSelectedIp(checkedId);
				edtOther.setEnabled(false);
				edtOther.setFocusableInTouchMode(false);
				//edtOther.setInputType(InputType.TYPE_NULL);
				edtOther.setText(settings.getIp());
				Log.d("Emulator Selected! IP:",settings.getIp());
				break;
			case R.id.rbtnDUC:
				settings.saveIp("yuchinin.no-ip.info");
				edtOther.setEnabled(false);
				edtOther.setFocusableInTouchMode(false);
				//edtOther.setInputType(InputType.TYPE_NULL);
				settings.saveSelectedIp(checkedId);
				edtOther.setText(settings.getIp());
				Log.d("DUC Selected! IP:",settings.getIp());
				break;
			case R.id.rbtnOther:
				//settings.edit().putString(IP_KEY,"").commit();
				edtOther.setEnabled(true);
				edtOther.setFocusableInTouchMode(true);
				//edtOther.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
				settings.saveSelectedIp(checkedId);
				edtOther.setText(settings.getIp());
				Log.d("Other Selected! IP:",settings.getIp());
				break;
			}
			
		}
		
	};
	
	public void Toast(String string){
		Toast.makeText(context,string, Toast.LENGTH_SHORT).show();
	}

*/
}
