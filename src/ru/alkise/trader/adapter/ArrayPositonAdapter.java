package ru.alkise.trader.adapter;

import java.util.ArrayList;
import java.util.List;

import ru.alkise.trader.R;
import ru.alkise.trader.model.Position;
import ru.alkise.trader.model.Warehouse;
import ru.alkise.trader.model.Warehouses;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class ArrayPositonAdapter extends ArrayAdapter<Position> {
	private ArrayAdapter<Warehouse> whToAdapter;
	private List<Warehouse> whList;
	private List<Position> positions;
	private int layoutResourceId;
	private LayoutInflater inflater;
	private Context context;

	public ArrayPositonAdapter(Context context, int textViewResourceId,
			List<Position> positions) {
		super(context, textViewResourceId, positions);
		this.positions = positions;
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
			holder.deleteButton = (ImageButton) rowView
					.findViewById(R.id.deleteButton);

			holder.deleteButton.setTag(position);

			if (position != null) {

				holder.deleteButton
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
								alertDialogBuilder.setNegativeButton(context.getString(R.string.cancel),
										new DialogInterface.OnClickListener() {

											public void onClick(DialogInterface p1, int p2) {
												p1.cancel();
											}

										}).setPositiveButton(context.getString(R.string.delete),
										new DialogInterface.OnClickListener() {

											public void onClick(DialogInterface p1, int p2) {
												System.out.println("Before : " + getCount());
												if (getCount() == 1) {
													clear();
												} else {
													Position position = getItem(pos);
													remove(position);
												}
												System.out.println("After : " + getCount());
											}
										}).setMessage("Delete this position?");
								
								AlertDialog deletingDialog = alertDialogBuilder.create();

								deletingDialog.show();
							}
						});

				holder.whToSpinner.setAdapter(whToAdapter);
				holder.whToSpinner
						.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> adapter,
									View arg1, int arg2, long arg3) {
								Position position = getItem(pos);
								position.setWhTo((Warehouse) adapter
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

		holder.whToSpinner.setSelection(whToAdapter.getPosition(position.getWhFrom()));

		return rowView;
	}

	class ViewHolder {
		TextView posNameView;
		EditText count;
		TextView whFromTextView;
		Spinner whToSpinner;
		ImageButton deleteButton;
	}

}
