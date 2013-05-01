package ru.alkise.trader.adapter;

import java.util.ArrayList;

import ru.alkise.trader.R;
import ru.alkise.trader.model.Order;
import ru.alkise.trader.model.Position;
import ru.alkise.trader.model.Warehouse;
import ru.alkise.trader.model.Warehouses;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class PositionAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<Warehouse> whToList;
	private ArrayAdapter<Warehouse> whToAdapter;

	public PositionAdapter(Activity activity) {
		inflater = (LayoutInflater) activity.getApplication().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		whToList = new ArrayList<Warehouse>(Warehouses.INSTANCE.getWarehousesList());
		whToAdapter = new ArrayAdapter<Warehouse>(activity, android.R.layout.simple_dropdown_item_1line, whToList){
			
		};
		whToAdapter.setDropDownViewResource(R.layout.spinner_custom_item);
	}

	@Override
	public int getCount() {
		return Order.INSTANCE.getPositions().size();
	}

	@Override
	public Object getItem(int arg0) {
		return Order.INSTANCE.getPositions().get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return Order.INSTANCE.getPositions().get(arg0).getId();
	}

	@Override
	public View getView(final int pos, View view, ViewGroup parent) {
		ViewHolder holder;
		View modifiedView = view;
		if (modifiedView == null) {
			modifiedView = inflater.inflate(R.layout.position_layout, null);
			holder = new ViewHolder();
			holder.posNameView = (TextView) modifiedView
					.findViewById(R.id.posNameView);

			holder.count = (EditText) modifiedView.findViewById(R.id.countEdit);

			holder.whFromSpinner = (TextView) modifiedView
					.findViewById(R.id.whFromView);

			holder.whToSpinner = (Spinner) modifiedView
					.findViewById(R.id.whToSpinner);

			Position position = Order.INSTANCE.getPositions().get(pos);

			if (position != null) {
				holder.posNameView.setText(position.getGoods().toString());
				holder.posNameView.setTextColor(Color.BLACK);

				holder.count.setText(String.valueOf(position.getCount()));
				holder.count.setTextColor(Color.BLACK);

				holder.whFromSpinner.setText(position.getWhFrom().toString());
				holder.whFromSpinner.setTextColor(Color.BLACK);
				
				holder.whToSpinner.setAdapter(whToAdapter);
				holder.whToSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> adapter, View arg1,
							int arg2, long arg3) {
						Order.INSTANCE.getPositions().get(pos).setWhTo((Warehouse)adapter.getSelectedItem());
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
				
				});
				holder.whToSpinner.setSelection(whToList.indexOf(position.getWhFrom()));
			}

		} else {
			holder = (ViewHolder) modifiedView.getTag();
		}
		return modifiedView;
	}

	class ViewHolder {
		TextView posNameView;
		EditText count;
		TextView whFromSpinner;
		Spinner whToSpinner;
		Button deleteButton;
	}
}
