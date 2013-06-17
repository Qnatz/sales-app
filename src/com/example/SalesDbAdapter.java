// Ronan Reilly 2012
package com.example;

/**
 *  This class will be used to access the devices SQLite database, create a database, add a 
 *  table and manage the data for each sale to be stored, it will also allow us 
 *  to up date entries made into the table.
 */

// Imports needed for the implementation of this class.

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SalesDbAdapter {
	
	/**
	 * These constant definitions will be used to access the
	 * associated fields in the database.
	 */
	
	public static final String KEY_MR_MRS = "mr_mrs";
	public static final String KEY_CUST_NAME = "cust_name";
	public static final String KEY_APT_HOUSE_NUM = "apt_house_num";
	public static final String KEY_TOWN = "town";
	public static final String KEY_PH_NUM = "ph_num";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_ENERGY_TYPE = "energy_type";
	public static final String KEY_RATING = "rating";
	public static final String KEY_ROWID = "_id";

	// The tag for this class.
	private static final String TAG = "SalesDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	/**
	 * Create our Database with columns as specified in the constants at the beginning
	 * of this class using a database creation string, each constant will be used a s a column in
	 * the table. 
	 */
	
	private static final String DATABASE_CREATE = "create table sales (_id integer primary key autoincrement, "
			+ " mr_mrs text not null, cust_name text not null, apt_house_num text not null, town text not null, ph_num text not null, email text not null, energy_type text not null, rating text not null);";

	// The database is named "data".
	private static String DATABASE_NAME = "data";
	// The table to be used is called "sales".
	private static String DATABASE_TABLE = "sales";
	// Setting the version of the database to be used.
	private static final int DATABASE_VERSION = 2;

	// Setting a variable up to store the context for communicating with the Android OS.
	private final Context mCtx;

	/**
	 * The constructor for this class takes a context which will give us the  
	 * ability to communicate with the Android OS when required.This context will 
	 * be implemented by the activity class and will be passed from there
	 * when needed.
	 */
	
	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		/**
		 * Here the database is created using the database creation string defined earlier on in 
		 * the class.
		 */
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}
		
		/**
		 * This method checks what version database is being used if it is below version 2
		 * it will be updated to version 2 as is stored in the variable DATABASE_VERSION.
		 * */
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version" + oldVersion + "to"
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS sales");
			onCreate(db);

		}
	}

	/**
	 * Constructor that takes the context to allow the database to be opened &
	 * created.
	 */

	public SalesDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	/**
	 * This method  calls on an instance of the DatabaseHelper which is the locally implemented
	 * SQLiteOpenHelper. The getWritableDatabase() is called on the database helper which manages the
	 * creating and opening of the database. 
	 * 
	 * Open the sales database if it cannot be opened, try to create a new database or
	 * if a new data base cannot be created, throw an exception to signal failure.
	 * @return this -> a self reference allowing for this to be chained in a
	 * initialization call.
	 * @throws SQLException this is if database could not be either opened or created.
	 */
	
	public SalesDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * This method closes the database and allows for the release of any 
	 * resources used in the connection.
	 */
	
	public void close() {
		mDbHelper.close();
	}

	/**
	 * Create a new sale using the details provided. If the sale is created
	 * return the rowId for the sale, other wise return a-1 to indicate failure
	 * to insert a sale.
	 * @param cust_name the customers name of the sale
	 * @param apt_house_num the customers house number of the sale
	 * @param town the town info of customer sale
	 * @param ph_num the customers phone number of the sale
	 * @param email the customers email of the sale
	 * @param energy_type the type of energy of the sale  
	 *  @param date the date of the sale           
	 */
	
	public long createSale(String mr_mrs, String cust_name,
			String apt_house_num, String town, String ph_num,
			String email, String energy_type, String rating) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_MR_MRS, mr_mrs);
		initialValues.put(KEY_CUST_NAME, cust_name);
		initialValues.put(KEY_APT_HOUSE_NUM, apt_house_num);
		initialValues.put(KEY_TOWN, town);
		initialValues.put(KEY_PH_NUM, ph_num);
		initialValues.put(KEY_EMAIL, email);
		initialValues.put(KEY_ENERGY_TYPE, energy_type);
		initialValues.put(KEY_RATING, rating);

		return mDb.insert(DATABASE_TABLE, null, initialValues);

	}

	/**
	 * Delete the note with the given rowId
	 * @param rowId id of note to be deleted
	 * @return true if row is deleted, false if otherwise
	 */

	public boolean deleteSale(long rowId) {
		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * This method sends a query to return a cursor over all of the sales in the database.
	 * First the name of the database is queried which is "sales". Next an array containing strings
	 * for each column that is needed is queried.The null values are where are where selections can 
	 * be put on the data we want to pull from the table which would be selectionArgs, groupBy, having 
	 * & orderBy. The null values mean that all the data is required and no grouping is needed.
	 * 
	 * A cursor is used to allow Android to use resources more efficiently than
	 * would be the case if a collection of rows where returned.
	 * 
	 * @return Cursor over all sales
	 * 
	 */

	public Cursor fetchallSales() {
		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_MR_MRS,
				KEY_CUST_NAME, KEY_APT_HOUSE_NUM, KEY_TOWN,
				KEY_PH_NUM, KEY_EMAIL, KEY_ENERGY_TYPE, KEY_RATING }, null,
				null, null, null, null);
	}

	/**
	 * This method will get a sale where the rows id is given.
	 * The first parameter is set true so that only one row will be returned.
	 * Strings are then passed that correspond to the column names in the sales table.
	 * Fourth is the selection parameter which will search for the row in the table
	 * where the row id is the same to that which has been passed in.
	 * The null vales are for grouping which are left null as the data is required in its default 
	 * order.
	 * 
	 * Returns a cursor positioned at the sale of the given rowId.
	 * @param rowId id of sale to retrieve.
	 * @param Cursor positioned to the sale matched if it is found.
	 * @throws SQLException if note could not be found.
	 * 
	 */

	public Cursor fetchSale(long rowId) throws SQLException {

		Cursor mCursor =

		mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_MR_MRS,
				KEY_CUST_NAME, KEY_APT_HOUSE_NUM, KEY_TOWN,
				KEY_PH_NUM, KEY_EMAIL, KEY_ENERGY_TYPE, KEY_RATING}, KEY_ROWID
				+ "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	/**
	 * Update the sale where the row id and table fields are provided. The sale to be updated is
	 * specified using the rowId and the values passed in which correspond to the columns
	 * of the table.
	 * 
	 * 
	 * @param rowId id of sale to be updated
	 * @param cust_name the customer name to set sales customer name to
	 * @param apt_house_num the customer house number to set address for the sale
	 * @param town info to set town for the sale
	 * @param ph_num the phone number to set the phone number of the sale too
	 * @param email the email of the sale to be changed
	 * @param the energy type to be updated too
	 * @param rating the rating to be changed in the sale 
	 * @ return true if the sale was correctly updated or false other wise
	 */

	public boolean updateSale(long rowId, String mr_mrs, String cust_name,
			String apt_house_num, String town, String ph_num,
			String email, String energy_type, String rating) {
		ContentValues args = new ContentValues();
		args.put(KEY_MR_MRS, mr_mrs);
		args.put(KEY_CUST_NAME, cust_name);
		args.put(KEY_APT_HOUSE_NUM, apt_house_num);
		args.put(KEY_TOWN, town);
		args.put(KEY_PH_NUM, ph_num);
		args.put(KEY_EMAIL, email);
		args.put(KEY_ENERGY_TYPE, energy_type);
		args.put(KEY_RATING, rating);

		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;

	}
}
