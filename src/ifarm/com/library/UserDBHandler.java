package ifarm.com.library;

import java.io.IOException;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class UserDBHandler extends SQLiteOpenHelper {
	
	Context context;
 
	//public SQLiteDatabase dbg = null;
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 14;
    
    //private int dbVer;
 
    // Database Name
    private static final String DATABASE_NAME = "ifarm_user";
 
    // Login table name
    private static final String TABLE_LOGIN = "login";
    
    // User table name
    private static final String TABLE_USER = "user";
    
  //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/ifarm.com/databases/";
 
    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_C_AT = "created_at";
    private static final String KEY_U_AT = "updated_at";
    private static final String KEY_LVL = "level";
    private static final String KEY_TS = "timestamp";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_BIRTHDAY = "birthday";
    
    String TABLE_TEMPLATE = "("
			//+ KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_EMAIL + " TEXT UNIQUE,"
            + KEY_UID + " TEXT,"
            + KEY_C_AT + " TEXT,"
            + KEY_U_AT + " TEXT,"
            + KEY_LVL + " INTEGER,"
            + KEY_TS + " TIMESTAMP,"
            + KEY_PHONE + " TEXT,"
            + KEY_GENDER + " TEXT,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_BIRTHDAY + " DATE" +
            		")";
	
    String CREATE_LOGIN_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_LOGIN + TABLE_TEMPLATE;
    
    
    String COLUMN[] = {KEY_ID,KEY_NAME,KEY_EMAIL,KEY_UID,KEY_C_AT,KEY_U_AT,KEY_LVL,KEY_TS,KEY_PHONE,KEY_GENDER,KEY_ADDRESS,KEY_BIRTHDAY};
 
    public UserDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        //this.onCreate(getWritableDatabase());
        checkDataBase();
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
    		//Log.d(DATABASE_NAME,"Database found!");
    		Log.d(DATABASE_NAME,"Create Table!");
    		
            //String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + TABLE_TEMPLATE;
            //db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
            db.execSQL(CREATE_LOGIN_TABLE);
            //db.execSQL(CREATE_USER_TABLE);
            db.setVersion(DATABASE_VERSION);
            //if(db.getVersion()<DATABASE_VERSION){
				//Log.d(DATABASE_NAME, "New Version Found!");
				//Log.d(DATABASE_NAME, "Begin Upgrading Database...");
				//onUpgrade(checkDB,checkDB.getVersion(),DATABASE_VERSION);
			//}
            //db.close();
    }
    
    private void checkDataBase(){
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
    	SQLiteDatabase checkDB = null;
    	try{
    		String myPath = DB_PATH + DATABASE_NAME;
    		//checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    		checkDB = SQLiteDatabase.openOrCreateDatabase(myPath, null);
    		
    		//dbg = this.getReadableDatabase();
    		if(checkDB.getVersion()<DATABASE_VERSION){
			Log.d(DATABASE_NAME, "New Version Found!");
			Log.d(DATABASE_NAME, "Begin Upgrading Database...");
			onUpgrade(checkDB,checkDB.getVersion(),DATABASE_VERSION);
    		}else{
    			
    		}
    		
    		Log.d(DATABASE_NAME, "Checked ver = "+checkDB.getVersion());
    		Log.d(DATABASE_NAME, "Current ver = "+DATABASE_VERSION);
    		
    			//dbVer = checkDB.getVersion();	// get readable database version
    	}catch(SQLiteException e){
    		//database does't exist yet.
    	}
    	if(checkDB != null){
    		checkDB.close();
    	}
    	//return checkDB != null ? true : false;
    }
    
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        //context.deleteDatabase(DATABASE_NAME);
    	//deleteDatabase();
        // Create tables again
        //String myPath = DB_PATH + DATABASE_NAME;
        //db = SQLiteDatabase.openOrCreateDatabase(myPath, null);
        //onCreate(getWritableDatabase());
        this.onCreate(db);
        //db.setVersion(DATABASE_VERSION);
        
        //Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
		//if (newVersion > oldVersion) {
			//db.getVersion();
			Log.d(DATABASE_NAME,"New Version: "+newVersion+" > Old Version: "+oldVersion);
			Log.d(DATABASE_NAME,"Updating Database: "+DATABASE_NAME);
			//context.deleteDatabase(DATABASE_NAME);
			//try {
				//deleteDatabase();
			//} catch (SQLException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			//} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			//}
			
			// Create tables again
			//onCreate(db);
		//}
    }
 
    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email, String uid, String created_at, String updated_at
    		, String level, String timestamp, String phone,String g,String addr,String birth) {
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // Email
        values.put(KEY_C_AT, created_at); // Created At
        values.put(KEY_U_AT, updated_at);
        values.put(KEY_LVL, level);
        values.put(KEY_TS, timestamp);
        values.put(KEY_PHONE, phone);
        values.put(KEY_GENDER, g);
        values.put(KEY_ADDRESS, addr);
        values.put(KEY_BIRTHDAY, birth);
 
        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
    }
 
    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails(){
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
        	for(int x=1;x<COLUMN.length;x++){
        		user.put(COLUMN[x], cursor.getString(cursor.getColumnIndex(COLUMN[x])));
        	}
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }
 
    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
 
        // return row count
        return rowCount;
    }
    
    public String getUid(){
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
    	String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        String temp = cursor.getString(cursor.getColumnIndex(KEY_UID));
        db.close();
        return temp;
    }
    
    public String getName(){
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
        Cursor c = this.get(1);
        String temp = c.getString(c.getColumnIndex(KEY_NAME));
        c.close();
        return temp;
    }
    
    public String getEmail(){
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
        Cursor c = this.get(1);
        String temp = c.getString(c.getColumnIndex(KEY_EMAIL));
        c.close();
        return temp;
    }
    
    public int getLevel(){
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
    	Cursor c = this.get(1);
        int temp = c.getInt(c.getColumnIndex(KEY_LVL));
        c.close();
        return temp;
    }
    
    public String getGender(){
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
    	Cursor c = this.get(1);
    	String temp = c.getString(c.getColumnIndex(KEY_GENDER));
    	c.close();
        return temp;
    }
    
    public String getPhone(){
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
    	Cursor c = this.get(1);
    	String temp = c.getString(c.getColumnIndex(KEY_PHONE));
    	c.close();
        return temp;
    }
    
    public String getAddress(){
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
    	Cursor c = this.get(1);
    	String temp = c.getString(c.getColumnIndex(KEY_ADDRESS));
    	c.close();
        return temp;
    }
    
    public String getBirthday(){
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
    	Cursor c = this.get(1);
    	String temp = c.getString(c.getColumnIndex(KEY_BIRTHDAY));
    	c.close();
        return temp;
    }
 
    /**
     * Recreate database
     * Delete all tables and create them again
     **/
    public void resetTables(){
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }
    
	public Cursor get(long rowId) throws SQLException{
		Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCursor = db.query(TABLE_LOGIN, COLUMN,KEY_ID + "=" + rowId,null,null,null,null,null);
		if (mCursor != null){
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public Cursor getAll(){
		Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCursor = db.query(TABLE_LOGIN, COLUMN, null, null, null, null, null);
		return mCursor;
	}
 
}
