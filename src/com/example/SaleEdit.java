// Ronan Reilly 2012
package com.example;

/**
 * This class/Activity will be used to edit, create and update a sale in the data base.
 * This class extends the Android Activity class.
 * 
 */

// Imports needed to implement this class

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

public class SaleEdit extends Activity {

	// This variable will store the outcome of the check box selection.
	private String mMr_Mrs;
	// These variables store a check box in each of them.
	private CheckBox mCheckBox_Mr;
	private CheckBox mCheckBox_Mrs;
	private CheckBox mCheckBox_Miss;

	// Each of the variables below will be used to store the 
	// text from the text edit fields on the form for editing a sale.
	private EditText mCust_NameText;
	private EditText mApt_House_NumText;
	private EditText mTownText;
	private EditText mPh_NumText;
	private EditText mEmailText;

	// This variable will store the out come of the radio button selection.
	private String mEnergy_Type;
	// Each variable below will store the radio buttons from the form to edit a sale. 
	private RadioButton mRadio_GasR_Button;
	private RadioButton mRadio_ElecR_Button;

	// This variable will hold the outcome or rating from the rating bar.
	private String mRating;
	private RatingBar mRatingBar;

	// This variable will store the row id for rows in the database table.
	private Long mRowId;
	private SalesDbAdapter mDbHelper;

	/**
	 * The on create method is called when the activity is created. 
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// An instance of the SalesDbAdapter is initiated.
		mDbHelper = new SalesDbAdapter(this);
		mDbHelper.open();
		
		// The layout for the form is called form resources. 
		setContentView(R.layout.edit_sale);
		// The title of this activity is set.
		setTitle(R.string.edit_sale);

		// The check box is found in the resources/layout by its id.
		mCheckBox_Mr = (CheckBox) findViewById(R.id.checkbox);
		// An on click listener is set to the check box.
		mCheckBox_Mr.setOnClickListener(check_listener);
		// The check box is found in the resources/layout by its id.
		mCheckBox_Mrs = (CheckBox) findViewById(R.id.checkbox);
		// An on click listener is set to the check box.
		mCheckBox_Mrs.setOnClickListener(check_listener_two);
		// The check box is found in the resources/layout by its id.
		mCheckBox_Miss = (CheckBox) findViewById(R.id.checkbox);
		// An on click listener is set to the check box.
		mCheckBox_Miss.setOnClickListener(check_listener_three);

		// The layout for each text edit field in the edit sale form is found by id
		// and are cast to the correct view. 
		mCust_NameText = (EditText) findViewById(R.id.cust_name);
		mApt_House_NumText = (EditText) findViewById(R.id.apt_house_num);
		mTownText = (EditText) findViewById(R.id.town);
		mPh_NumText = (EditText) findViewById(R.id.ph_num);
		mEmailText = (EditText) findViewById(R.id.email);

		// The radio buttons layout is found in the resources/layout by its id.
		mRadio_GasR_Button = (RadioButton) findViewById(R.id.radio_gas);
		mRadio_ElecR_Button = (RadioButton) findViewById(R.id.radio_elec);
		// On click listeners are set to the radio buttons.
		mRadio_GasR_Button.setOnClickListener(radio_listener);
		mRadio_ElecR_Button.setOnClickListener(radio_listener);

		// The rating bars layout is found in the resources/layout by its id.
		mRatingBar = (RatingBar) findViewById(R.id.rating);
		
		// This is the method which manages changes made to the rating bar.
		// This method listens for any user interaction with the rating bar.
		mRatingBar
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						mRating = Float.toString(rating);
						Toast.makeText(SaleEdit.this, "New Rating: " + rating,
								Toast.LENGTH_SHORT).show();
					}
				});
		
		// The layout for the confirm button is found in the resources/layout by its id.
		Button confirmButton = (Button) findViewById(R.id.confirm);

		mRowId = (savedInstanceState == null) ? null
				: (Long) savedInstanceState
						.getSerializable(SalesDbAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(SalesDbAdapter.KEY_ROWID)
					: null;
		}
		
		// The populate fields method from below is called.
		populateFields();
		
		// This method listens for the users interaction with the confirm button which would
		// commit the sale new or updated to the database.
		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				setResult(RESULT_OK);
				finish();
			}

		});
	}

	private OnClickListener radio_listener = new OnClickListener() {
		public void onClick(View v) {

			RadioButton rb = (RadioButton) v;
			mEnergy_Type = rb.getText().toString();
			

		}
	};

	private OnClickListener check_listener = new OnClickListener() {
		public void onClick(View v) {
			CheckBox cb = (CheckBox) v;
			mMr_Mrs = cb.getText().toString();

		}
	};

	private OnClickListener check_listener_two = new OnClickListener() {
		public void onClick(View v) {
			CheckBox cb = (CheckBox) v;
			mMr_Mrs = cb.getText().toString();
		}
	};

	private OnClickListener check_listener_three = new OnClickListener() {
		public void onClick(View v) {
			CheckBox cb = (CheckBox) v;
			mMr_Mrs = cb.getText().toString();
		}
	};
	
	/**
	 * This method will be used to populate the fields of the sale.
	 */

	private void populateFields() {
		// Checks if row is selected or if it is a new sale being created. 
		if (mRowId != null) {
			// The fetch sale method from the SalesDbAdapter class is called
			// to find the right sale to edit in the database.
			Cursor sale = mDbHelper.fetchSale(mRowId);
			startManagingCursor(sale);

			mMr_Mrs = sale.getString(sale
					.getColumnIndexOrThrow(SalesDbAdapter.KEY_MR_MRS));
			if (mMr_Mrs.equalsIgnoreCase("Mr")) {
				mCheckBox_Mr.setChecked(true);

			}
			mMr_Mrs = sale.getString(sale
					.getColumnIndexOrThrow(SalesDbAdapter.KEY_MR_MRS));
			if (mMr_Mrs.equalsIgnoreCase("Mrs")) {
				mCheckBox_Mrs.setChecked(true);

			}
			mMr_Mrs = sale.getString(sale
					.getColumnIndexOrThrow(SalesDbAdapter.KEY_MR_MRS));
			if (mMr_Mrs.equalsIgnoreCase("Miss")) {
				mCheckBox_Miss.setChecked(true);

			}

			mCust_NameText.setText(sale.getString(sale
					.getColumnIndexOrThrow(SalesDbAdapter.KEY_CUST_NAME)));
			mApt_House_NumText.setText(sale.getString(sale
					.getColumnIndexOrThrow(SalesDbAdapter.KEY_APT_HOUSE_NUM)));
			mTownText.setText(sale.getString(sale
					.getColumnIndexOrThrow(SalesDbAdapter.KEY_TOWN)));
			mPh_NumText.setText(sale.getString(sale
					.getColumnIndexOrThrow(SalesDbAdapter.KEY_PH_NUM)));
			mEmailText.setText(sale.getString(sale
					.getColumnIndexOrThrow(SalesDbAdapter.KEY_EMAIL)));

			mEnergy_Type = sale.getString(sale
					.getColumnIndexOrThrow(SalesDbAdapter.KEY_ENERGY_TYPE));
			if (mEnergy_Type.equalsIgnoreCase("gas sale")) {
				mRadio_GasR_Button.toggle();
			}
			if (mEnergy_Type.equalsIgnoreCase("electricity sale")) {
				mRadio_ElecR_Button.toggle();
			}

			mRating = sale.getString(sale
					.getColumnIndexOrThrow(SalesDbAdapter.KEY_RATING));
		}
	}

	/**
	 * This method is used by the OS if the activity may might be stopped.
	 * It will be called to store any state needed to restart the activity
	 * in the same condition as it were when it was stopped.  
	 */
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(SalesDbAdapter.KEY_ROWID, mRowId);
	}

	/**
	 * This method is called when the activity is ended. It is used to save the 
	 * sale in question to the database.
	 */
	
	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}
	/**
	 * This method will call the populateField() method to pull the sale form the database 
	 * and populate the fields of the sale.
	 */

	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}
	
	
	/**
	 * This method is used to put the data from the from into the data base. 
	 */
	
	private void saveState() {
		
		// These variables will store the data from the fields on the form 
		// and are converted to strings.
		String cust_name = mCust_NameText.getText().toString();
		String apt_house_num = mApt_House_NumText.getText().toString();
		String town = mTownText.getText().toString();
		String ph_num = mPh_NumText.getText().toString();
		String email = mEmailText.getText().toString();
		String rating = mRating.toString();

		// A check is performed to see if a new note is to be created.
		if (mRowId == null) {
			// The createSale() method is called from the salesDbAdapter
			// class and the data form the edit sale form is pushed into the database 
			// in a new row.
			long id = mDbHelper.createSale(mMr_Mrs, cust_name, apt_house_num,
					town, ph_num, email, mEnergy_Type, rating);
			if (id > 0) {
				mRowId = id;
			}
			// If there is a row id passed into this method the updatSale()
			// method is called and the sale at the given row id is updated with the values filled in by the user in the 
			// form for this activity.
		} else {
			mDbHelper.updateSale(mRowId, mMr_Mrs, cust_name, apt_house_num,
					town, ph_num, email, mEnergy_Type, rating);
		}
	}

}
