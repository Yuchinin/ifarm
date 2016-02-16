package ifarm.com;
 
import ifarm.com.library.Base64;
import ifarm.com.library.BitmapDownloader;
import ifarm.com.library.SettingHelper;
import ifarm.com.library.UserDBHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
 
public class UploadImage extends Activity {
    InputStream inputStream;
    String the_string_response;
    int contentLength;
    String res;
    Bitmap bitmap;
    Button btn_Capture,btn_Rotate,btn_Upload;
    String email;
    ArrayList<NameValuePair> nameValuePairs;
    Context context = this;
    ProgressDialog dialog;
    UserDBHandler db;
    SettingHelper settings;
    
    private static final int TAKE_PICTURE = 0;
    private Uri mUri;
    private Bitmap mPhoto;
    
        @Override
    public void onCreate(Bundle icicle) {
            super.onCreate(icicle);
            setContentView(R.layout.imageupload);
            
            SetViewAndListener();
            
            Intent intent = getIntent();
            email = intent.getExtras().getString("email");
        }
        
        private void SetViewAndListener(){
        	db = new UserDBHandler(context);
        	settings = new SettingHelper(context);
        	btn_Capture = (Button)findViewById(R.id.btn_Capture);
        	btn_Rotate = (Button)findViewById(R.id.btn_Rotate);
        	btn_Upload = (Button)findViewById(R.id.btn_Upload);
        	
        	btn_Capture.setOnClickListener(btnlistener);
        	btn_Rotate.setOnClickListener(btnlistener);
        	btn_Upload.setOnClickListener(btnlistener);
        	
        	btn_Rotate.setEnabled(false);
        	btn_Upload.setEnabled(false);
        	//====================================================
        	new Thread(new Runnable() {
	            @Override
	            public void run() {
	                  try{
	                	  runOnUiThread(new Runnable() {
	                            @Override
	                            public void run() {
	                            	Toast.makeText(context, "Trying to get Image from Server...", Toast.LENGTH_LONG).show();                          
	                            }
	                        });
	                	  mPhoto = BitmapDownloader
	                		.getBitmapFromURL("http://"+settings.getIp()
	                		+"/ifarm_api/ANDROID/pic/"+db.getEmail()+".png");
	                	  runOnUiThread(new Runnable() {
	                            @Override
	                            public void run() {
	                            	if(mPhoto!=null){
	                            		((ImageView)findViewById(R.id.imgV))
		    	                	  	.setImageBitmap(mPhoto);
	                            		btn_Rotate.setEnabled(true);
	                                	btn_Upload.setEnabled(true);
		                            	Toast.makeText(context, "Image Downloaded", Toast.LENGTH_LONG).show();
	                            	}
	                            }
	                        });
	                     }catch(final Exception e){
	                    	 e.printStackTrace();
	                          runOnUiThread(new Runnable() {
	                            @Override
	                            public void run() {
	                                Toast.makeText(context, "Failed to get Image from Server", Toast.LENGTH_LONG).show();                              
	                            }
	                        });
	                     }
	            }
	        }).start();
        	//==========================================================
        }
        
        private Button.OnClickListener btnlistener = new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			if (v.getId()== R.id.btn_Capture) {
	        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	        File f = new File(Environment.getExternalStorageDirectory(),  "photo.jpg");
	        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
	        mUri = Uri.fromFile(f);
	        startActivityForResult(i, TAKE_PICTURE);
	        } else if (v.getId()==R.id.btn_Rotate) {
	        	if (mPhoto!=null) {
	        		Matrix matrix = new Matrix();
	        		matrix.postRotate(90);
	        		mPhoto = Bitmap.createBitmap(mPhoto , 0, 0, mPhoto.getWidth(), mPhoto.getHeight(), matrix, true);
	        		((ImageView)findViewById(R.id.imgV)).setImageBitmap(mPhoto);
	            }
	        } else if(v.getId()==R.id.btn_Upload){
	        	dialog = new ProgressDialog(context);
	        	dialog.setMessage("Uploading Image...");
	        	//dialog.setIcon(mPhoto.get);
	            dialog.show();
	        	if (bitmap != null) {
	            	Toast.makeText(UploadImage.this, "bitmap recycle", Toast.LENGTH_LONG).show();
	                //bitmap.recycle();
	            	bitmap = null;
	              }
	 
	            //bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.icon1);
                bitmap = mPhoto;
	            ByteArrayOutputStream stream = new ByteArrayOutputStream();
	            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
	            byte [] byte_arr = stream.toByteArray();
	            String image_str = Base64.encodeBytes(byte_arr);
	            nameValuePairs = new  ArrayList<NameValuePair>();
	 
	            nameValuePairs.add(new BasicNameValuePair("image",image_str));
	            nameValuePairs.add(new BasicNameValuePair("email",email));
	            
	             Thread t = new Thread(new Runnable() {
	            @Override
	            public void run() {
	                  try{
	                         HttpClient httpclient = new DefaultHttpClient();
	                         HttpPost httppost = new HttpPost("http://120.125.84.100/Ifarm_API/ANDROID/upload_image.php");
	                         httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	                         HttpResponse response = httpclient.execute(httppost);
	                         the_string_response = convertResponseToString(response);
	                         runOnUiThread(new Runnable() {
	                                 
	                                @Override
	                                public void run() {
	                                	
	                                	dialog.setMessage("Response " + the_string_response);
	                                	
	                                	//dialog.dismiss();
	                                    //Toast.makeText(UploadImage.this, "Response " + the_string_response, Toast.LENGTH_LONG).show();                          
	                                }
	                            });
	                          
	                     }catch(final Exception e){
	                          runOnUiThread(new Runnable() {
	                             
	                            @Override
	                            public void run() {
	                            	dialog.setMessage("ERROR " + e.getMessage());
	                                Toast.makeText(UploadImage.this, "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();                              
	                            }
	                        });
	                           System.out.println("Error in http connection "+e.toString());
	                     }
	            }
	        });
	         t.start();
	        }
				//----------------------------------------------------------------
				
				
			}
        	
        };
        
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
               switch (requestCode) {
               case TAKE_PICTURE:
                   if (resultCode == Activity.RESULT_OK) {
                	   //dialog = ProgressDialog.show(context, "", "Uploading file...", true);
                	   
                       getContentResolver().notifyChange(mUri, null);
                       ContentResolver cr = getContentResolver();
                       try {
                           mPhoto = android.provider.MediaStore.Images.Media.getBitmap(cr, mUri);
                        ((ImageView)findViewById(R.id.imgV)).setImageBitmap(mPhoto);
                        btn_Rotate.setEnabled(true);
                        btn_Upload.setEnabled(true);
                        //---------------------------------------------------
                        
                       } catch (Exception e) {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                          }
                       //dialog.dismiss();
                     }
               }
        }
 
        public String convertResponseToString(HttpResponse response) throws IllegalStateException, IOException{
 
             res = "";
             StringBuffer buffer = new StringBuffer();
             inputStream = response.getEntity().getContent();
             contentLength = (int) response.getEntity().getContentLength(); //getting content length…..
              runOnUiThread(new Runnable() {
             
            @Override
            public void run() {
            	dialog.setMessage("contentLength : " + contentLength);
                //Toast.makeText(UploadImage.this, "contentLength : " + contentLength, Toast.LENGTH_LONG).show();                     
            }
        });
          
             if (contentLength < 0){
             }
             else{
                    byte[] data = new byte[512];
                    int len = 0;
                    try
                    {
                        while (-1 != (len = inputStream.read(data)) )
                        {
                            buffer.append(new String(data, 0, len)); //converting to string and appending  to stringbuffer…..
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    try
                    {
                        inputStream.close(); // closing the stream…..
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    res = buffer.toString();     // converting stringbuffer to string…..
 
                    runOnUiThread(new Runnable() {
                     
                    @Override
                    public void run() {
                    	dialog.setMessage("Result : " + res);
                    	dialog.dismiss();
                       //Toast.makeText(UploadImage.this, "Result : " + res, Toast.LENGTH_LONG).show();
                    }
                });
                    //System.out.println("Response => " +  EntityUtils.toString(response.getEntity()));
             }
             return res;
        }
}
