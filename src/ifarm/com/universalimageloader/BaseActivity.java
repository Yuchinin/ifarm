package ifarm.com.universalimageloader;

import ifarm.com.Base;
import ifarm.com.R;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class BaseActivity extends Base {

	protected ImageLoader imageLoader = ImageLoader.getInstance();
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.item_clear_memory_cache:
				imageLoader.clearMemoryCache();
				return true;
			case R.id.item_clear_disc_cache:
				imageLoader.clearDiscCache();
				return true;
			default:
				return false;
		}
	}
*/
	@Override
	protected void onDestroy() {
		Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
	    super.onDestroy();
	    if (imageLoader.isInited()) {
	    	imageLoader.clearDiscCache();
	    	imageLoader.clearMemoryCache();
	    	imageLoader.destroy();
	    }
	}
}
