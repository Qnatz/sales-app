// Ronan Reilly 2012
package com.example;

/*
 * This class is the Activity which will display the list of sales added to the database.
 * It will extend the ListActivity ensuring that the class inherits from the ListActivity
 * class.
 * 
 */

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class SALESAPPActivity extends ListActivity {
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	
	// This is the menu position constant.
	private static final int INSERT_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;
	
	// This field will store the instance of SalesdDbAdapter to be used in this class. 
	private SalesDbAdapter mDbHelper;

	/**
	 * This method is called when the Activity is created.
	 * The layout for the list is called in this is called
	 * and an instance of the SalesDbAdapter is set up to access
	 * sales data and populate the list with titles (customers names) from sales in the database.
	 */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Here the savedInstanceState is passed into the super on create.
		super.onCreate(savedInstanceState);
		// Here the layout for the list is called from the resources/layout.
		setContentView(R.layout.sales_list);
		// A SalesDbAdapter instance is created and assigned to the mDbHelper field.
		mDbHelper = new SalesDbAdapter(this);
		// the Database is opened or created.
		mDbHelper.open();
		// the fillData() method is called to populate the ListView with the data from the database.
		fillData();
		registerForContextMenu(getListView());
	}
	
	/**
	 * This method will use the SimpleCursorAdapter which will get a cursor from the 
	 * database and attaches it to the fields from the layout. these fields are the row elements of the list
	 * used to populate the list with data from the database.
	 * 
	 * The entries from the database need to be mapped from the customers name
	 * field that has been returned in the cursor to the text1 resource. The first array
	 * (from) contains a list of the table columns to be mapped and the second array (int)contains references to views 
	 * that will bind the data to (R.idtext1 TextView).
	 * 
	 */

	private void fillData() {
		
		// The fetch all sales method is called from the SalesDbAdapter class.
		Cursor salesCursor = mDbHelper.fetchallSales();
		// Activity method allows android to take care of the cursor is called on the sales cursor.
		startManagingCursor(salesCursor);

		// Create an array to specify the fields we want to display in the list
		// customer name and sale type.
		String[] from = new String[] { SalesDbAdapter.KEY_ENERGY_TYPE, SalesDbAdapter.KEY_CUST_NAME};

		// An array of the fields we want to bind those fields to (in this
		// case just text1)
		int[] to = new int[] { R.id.text1, R.id.text2};

		// A simple cursor adapter is created and set it to display.
		SimpleCursorAdapter sales = new SimpleCursorAdapter(this,
				R.layout.sales_row, salesCursor, from, to);
		setListAdapter(sales);
	}

	/**
	 * This method creates the Add item button that can be used 
	 * when the menu button is pressed on the device. It will be 
	 * assigned to the first position in the menu.
	 */
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Here the menu item is added to the menu.
		// Arguments passed are a group identifier, an ID (INSERT_ID), the order of the form and the resource of the string.
		menu.add(0, INSERT_ID, 0, R.string.menu_insert);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case INSERT_ID:
			createSale();
			return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}
	
	/**
	 * This method will allow users to delete a note from the database.
	 * Each item in the list view will be registered and defined for the the context menu in order
	 * for it to be able to be selected.
	 * 
	 */

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// The view that has been triggered for the menu is passed.
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}
	/**
	 * This method will manage the "Add Sale" menu item.
	 * When selected this method is called and the item ID is gotten using
	 * item.getId() -> (INSERT_ID).
	 * 
	 */

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			// Adapter context info is retrieved and passed to the delete sale method form the SalesDbAdapter class.
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
			.getMenuInfo();
			// Delete the sale at the given id.
			mDbHelper.deleteSale(info.id);
			// List is populated again.
			fillData();
			return true;
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * This method will create an intent to create a new sale in the SaleEdit class. 
	 * The intent calls the Sale Edit class. 
	 */
	
	private void createSale() {
		// For the intent to communicate with the OS to direct requests a context is given (this).
		Intent i = new Intent(this, SaleEdit.class);
		
		startActivityForResult(i, ACTIVITY_CREATE);
	}

	/**
	 * This method is called when the user selects an item from the list.
	 * The are four parameters passed into this method, the ListView object it was invoked by, the view inside this list view
	 * the position in the list and the row's id for the clicked on item.
	 * The position that the user selected is used to get the data from the correct
	 * row  and it is bundled into the SaleEdit Activity.
	 */
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Intent is created to edit a sale using the SaleEdit class.
		Intent i = new Intent(this, SaleEdit.class);
		// Data is put into the extras Bundle which will be passed to the SaleEdit activity.
		i.putExtra(SalesDbAdapter.KEY_ROWID, id);
		// Intent is passed to activity for result.  
		startActivityForResult(i, ACTIVITY_EDIT);
	}
	
	/**
	 * This method will update its view of the data that 
	 * has been changed in the SaleEdit class.
	 */

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		fillData();
	}
}
