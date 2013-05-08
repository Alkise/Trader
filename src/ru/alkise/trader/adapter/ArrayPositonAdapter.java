package ru.alkise.trader.adapter;

import java.util.ArrayList;
import java.util.List;

import ru.alkise.trader.R;
import ru.alkise.trader.model.Position;
import ru.alkise.trader.model.Warehouse;
import ru.alkise.trader.model.Warehouses;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ArrayPositonAdapter extends ArrayAdapter<Position> {
	private ArrayAdapter<Warehouse> whToAdapter;
	private List<Warehouse> whList;
	private int layoutResourceId;
	private LayoutInflater inflater;
	private Context context;

	public ArrayPositonAdapter(Context context, int textViewResourceId,
			List<Position> positions) {
		super(context, textViewResourceId, positions);
		this.context = context;

		layoutResourceId = textViewResourceId;

		whList = new ArrayList<Warehouse>(
				Warehouses.INSTANCE.getWarehousesList());
		whToAdapter = new ArrayAdapter<Warehouse>(context,
				android.R.layout.simple_spinner_dropdown_item, whList);
	}

	@Override
	public View getView(final int pos, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Position position = getItem(pos);
		View rowView = convertView;

		if (rowView == null) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(layoutResourceId, parent, false);
			holder = new ViewHolder();

			holder.posNameView = (TextView) rowView
					.findViewById(R.id.posNameView);

			holder.count = (EditText) rowView.findViewById(R.id.countEdit);

			holder.whFromTextView = (TextView) rowView
					.findViewById(R.id.whFromView);

			holder.whToSpinner = (Spinner) rowView
					.findViewById(R.id.whToSpinner);
			holder.deleteButton = (Button) rowView
					.findViewById(R.id.deleteButton);

			holder.deleteButton.setTag(position);

			if (position != null) {

				holder.deleteButton
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								Position position = getItem(pos);
								remove(position);
							}
						});

				holder.whToSpinner.setAdapter(whToAdapter);
				holder.whToSpinner
						.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> adapter,
									View arg1, int arg2, long arg3) {
								Position position = getItem(pos);
								position.setWhTo(
												(Warehouse) adapter
														.getSelectedItem());
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub

							}

						});

				rowView.setTag(holder);
			}
		} else {
			holder = (ViewHolder) rowView.getTag();
		}

		holder.posNameView.setText(position.getGoods().toString());
		holder.posNameView.setTextColor(Color.BLACK);

		holder.count.setText(String.valueOf(position.getCount()));
		holder.count.setTextColor(Color.BLACK);
		holder.count.setTextSize(16);

		holder.whFromTextView.setText(position.getWhFrom().toString());
		holder.whFromTextView.setTextColor(Color.BLACK);
		holder.whFromTextView.setTextSize(16);

		holder.whToSpinner.setSelection(whList.indexOf(position.getWhTo()));

		return rowView;
	}

	class ViewHolder {
		TextView posNameView;
		EditText count;
		TextView whFromTextView;
		Spinner whToSpinner;
		Button deleteButton;
	}

}
