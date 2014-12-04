package com.foodorder.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.foodorder.beans.AppConstants;
import com.foodorder.beans.ApplicationData;
import com.foodorder.beans.FoodListsViewImage;
import com.foodorder.beans.MenuModel;
import com.foodorder.client.R;
import com.foodorder.utils.LogInOut;

public class ShoppingCartActivity extends Activity {

	private ArrayList<MenuModel> menuList;
	private ListView listView;
	private MyBaseAdapter myBaseAdapter;
	static String path = AppConstants.path;
	//private Intent intentViewCart;
	private Intent intentConfirmPage;
	//private Bundle b;
	private Button btnOrderConfirm;
	private HashMap<String, String> currentOrderline;
	private Menu menu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_cart);
		//setTitle("Shopping Cart");

		listView = (ListView) findViewById(R.id.listView1);
		menuList = ApplicationData.getCartList();
		currentOrderline = ApplicationData.getOrderLine();
		myBaseAdapter = new MyBaseAdapter();
		listView.setAdapter(myBaseAdapter);
		btnOrderConfirm = (Button) findViewById(R.id.btnOrderConfirm);
	}
	
	private void setConfirmButton(){
		setConfirmButton(ApplicationData.getUser()!=null);
	}
	
	private void setConfirmButton(boolean isLogin){
		if(isLogin){
			//btnOrderConfirm.setText(getString(R.string.btnCheckout));
			btnOrderConfirm.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if ((menuList != null) && (menuList.size() > 0)) {
						intentConfirmPage = new Intent(ShoppingCartActivity.this, OrderConfirmActivity.class);
						intentConfirmPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
						intentConfirmPage.putExtra("OrderConfirm", "Confirm order page loaded successfully");
						startActivity(intentConfirmPage);
					}
				}
			});
		}else{
			//btnOrderConfirm.setText(getString(R.string.login));
			btnOrderConfirm.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					AlertDialog.Builder adbLogin = new AlertDialog.Builder( ShoppingCartActivity.this);
					adbLogin.setTitle("Login required");
					adbLogin.setMessage("Login required to proceed.\nDo you want to login?");
					adbLogin.setPositiveButton("Ok",
							new AlertDialog.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									ApplicationData.setUser(null);
						            Intent loginIntent = new Intent(ShoppingCartActivity.this, LoginActivity.class);
						            startActivity(loginIntent);
								}
							});
					adbLogin.setNegativeButton("Cancel", null);
					adbLogin.show();
				}
			});
		}
		if(menuList == null){
			btnOrderConfirm.setEnabled(false);
		}else if(menuList.size() <= 0){
			btnOrderConfirm.setEnabled(false);
		}else{
			btnOrderConfirm.setEnabled(true);
		}
			
	}
	
	private void setLogInOutUI(){
		if(ApplicationData.getUser()!=null){
			LogInOut.setLogin(true, menu);
			setConfirmButton(true);
		}else{
			LogInOut.setLogin(false, menu);
			setConfirmButton(false);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.e("ShoppingCartActivity", "onResume()");
		menuList = ApplicationData.getCartList();
		currentOrderline = ApplicationData.getOrderLine();
		setLogInOutUI();
		
		// Toast.makeText(this, "onResume()", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.menu = menu;
		getMenuInflater().inflate(R.menu.rest_list, menu);
		if(ApplicationData.getUser()!=null){
			LogInOut.setLogin(true, menu);
		}else{
			LogInOut.setLogin(false, menu);
		}
		MenuItem itemLogin = menu.findItem(R.id.action_cart);
		itemLogin.setVisible(false);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		LogInOut.handleOptionItem(this, item);
		return super.onOptionsItemSelected(item);
	}

	class MyBaseAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if(menuList == null)
				return 0;
			return menuList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		boolean tryParseDouble(String value)  
		{  
		     try  
		     {  
		    	 Double.parseDouble(value);  
		         return true;  
		      } catch(NumberFormatException nfe)  
		      {  
		          return false;  
		      }  
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (menuList == null) {
				return convertView;
			}
			final int pos = position;
			@SuppressWarnings("static-access")
			final View view = convertView.inflate(ShoppingCartActivity.this,
					R.layout.subcate_listview_cart, null);
			Object obj = menuList.get(position);
			ImageView menu_item_image = (ImageView) view.findViewById(R.id.img);
			TextView menu_item_title = (TextView) view.findViewById(R.id.info);
			final TextView total = (TextView) view.findViewById(R.id.totalPrice);
			//TextView finalTotal = (TextView) view.findViewById(R.id.total);
			ImageButton btnRemove = (ImageButton) view.findViewById(R.id.btnRemove);
			//ImageButton updateBtn = (ImageButton) view.findViewById(R.id.btnUpdate);
			
			ImageButton btnAdd = (ImageButton) view.findViewById(R.id.btnAdd);
			ImageButton btnReduce = (ImageButton) view.findViewById(R.id.btnReduce);

			final TextView txtQuantity = (TextView) view.findViewById(R.id.txtQty1);
			
			final AlertDialog.Builder adb = new AlertDialog.Builder( ShoppingCartActivity.this);
			adb.setTitle("Delete?");
			adb.setMessage("Are you sure you want to delete this item?");
			final int positionToRemove = pos;
			adb.setPositiveButton("Ok",
					new AlertDialog.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							ArrayList<MenuModel> listMenuApp = ApplicationData.getCartList();
							MenuModel aMenu = menuList.get(positionToRemove);
							listMenuApp.remove(aMenu);
							ApplicationData.setCartList(listMenuApp);

							//HashMap<String, String> currentOrderline = ApplicationData.getOrderLine();
							currentOrderline.remove(aMenu.getMenuid());
							ApplicationData.setOrderLineList(currentOrderline);

							notifyDataSetChanged();
							setConfirmButton();
						}
					});
			
			btnAdd.setOnClickListener(new AdapterView.OnClickListener() {
				@Override
				public void onClick(View v) {
					Set<String> keys = currentOrderline.keySet();
					String itemId = null;
					MenuModel aMenu = menuList.get(positionToRemove);
					for (String key : keys) {
						if (aMenu.getMenuid() == key)
							itemId = key;
					}
					int qty = Integer.parseInt(currentOrderline.get(itemId) );
				
					if (!currentOrderline.isEmpty()) {
						if (currentOrderline.containsKey(aMenu.getMenuid())) {
							currentOrderline.remove(aMenu.getMenuid());
						}
					}
					txtQuantity.setText(Integer.toString(++qty));
					currentOrderline.put(aMenu.getMenuid(), txtQuantity.getText().toString());
					ApplicationData.setOrderLineList(currentOrderline);
					notifyDataSetChanged();
					
				}
			});
			
			btnReduce.setOnClickListener(new AdapterView.OnClickListener() {
				@Override
				public void onClick(View view) {
					Set<String> keys = currentOrderline.keySet();
					String itemId = null;
					MenuModel aMenu = menuList.get(positionToRemove);
					for (String key : keys) {
						if (aMenu.getMenuid() == key)
							itemId = key;
					}
					int qty = Integer.parseInt(currentOrderline.get(itemId) );
					
					if(qty == 1){
						adb.setNegativeButton("Cancel", null);
						adb.show();
					}else{
						if (!currentOrderline.isEmpty()) {
							if (currentOrderline.containsKey(aMenu.getMenuid())) {
								currentOrderline.remove(aMenu.getMenuid());
							}
						}
						txtQuantity.setText(Integer.toString(--qty));
						currentOrderline.put(aMenu.getMenuid(), txtQuantity.getText().toString());
						ApplicationData.setOrderLineList(currentOrderline);
						notifyDataSetChanged();
					}
				}
			});
			
			if (txtQuantity != null) {
				Set<String> keys = currentOrderline.keySet();
				String itemId = null;
				MenuModel aModel = menuList.get(position);

				for (String key : keys) {
					if (aModel.getMenuid() == key)
						itemId = key;
				}
				txtQuantity.setText(currentOrderline.get(itemId));
				final MenuModel aMenuPrice = (MenuModel) obj;
				int qty = Integer.parseInt(currentOrderline.get(itemId));
				float price = 0.0f;
				if(aMenuPrice.getPrice()!=null)
				{
					price = Float.parseFloat(aMenuPrice.getPrice());
							//Double.parseDouble(aMenuPrice.getPrice());
				}
				
				String calcTotal = String.format(Locale.CANADA, "%.2f" , qty*price);
						//Float.toString(qty*price);
						//String.valueOf(calcTotal);
				total.setText("Total: $" + calcTotal);				
				
			}
			
			
			
			// update a new quantity
			/*
			updateBtn.setOnClickListener(new AdapterView.OnClickListener() {
				public void onClick(View v) {
					// int position = (Integer)v.getTag();
					MenuModel aMenu = menuList.get(pos);
					HashMap<String, String> currentOrderline = ApplicationData.getOrderLine();
					if (!currentOrderline.isEmpty()) {
						if (currentOrderline.containsKey(aMenu.getMenuid())) {
							currentOrderline.remove(aMenu.getMenuid());
						}
					}
					if(txtQuantity.getText().toString() == null 
							|| txtQuantity.getText().toString().equals("")
							|| Integer.parseInt(txtQuantity.getText().toString()) <= 0 ){
						adb.setNegativeButton("Cancel", new AlertDialog.OnClickListener() {
								public void onClick(DialogInterface dialog,int which) {
									txtQuantity.setText("1");
								}
							});
						adb.show();
					}
					else{
						currentOrderline.put(aMenu.getMenuid(), txtQuantity.getText().toString());
						ApplicationData.setOrderLineList(currentOrderline);
						notifyDataSetChanged();
					}
				}
			});
			*/
			btnRemove.setTag(0);
			btnRemove.setOnClickListener(new AdapterView.OnClickListener() {
				public void onClick(View v) {
					adb.setNegativeButton("Cancel", null);
					adb.show();
				}

			});

			if (obj instanceof MenuModel) {
				final MenuModel aMenuItem = (MenuModel) obj;
				menu_item_title.setText("Name: " + aMenuItem.getName() + "\n"
						+ "Unit Price: $" + aMenuItem.getPrice());

				menu_item_image.setTag(aMenuItem.getPic());
				if (menuList.get(position).getPic() != null
						&& !menuList.get(position).getPic().equals("")) {
					try {
						new FoodListsViewImage(ShoppingCartActivity.this)
								.loadingImage(menuList.get(position).getPic(),
										menu_item_image, R.drawable.computer,
										listView);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					menu_item_image.setImageResource(R.drawable.computer);
				}

			} else {
			}
			return view;
		}
	}

	/*
	 * public static Bitmap getPicByPath(String picName) { picName =
	 * picName.substring(picName.lastIndexOf("/") + 1); String filePath = path +
	 * picName; Bitmap bitmap = BitmapFactory.decodeFile(filePath); return
	 * bitmap;
	 * 
	 * }
	 */
}
