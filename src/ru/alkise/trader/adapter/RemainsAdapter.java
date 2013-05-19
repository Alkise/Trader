package ru.alkise.trader.adapter;

import java.util.List;

import ru.alkise.trader.R;
import ru.alkise.trader.model.PositionIntf;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RemainsAdapter extends ArrayAdapter<PositionIntf> {
	private Context context;
	private int layoutId;
	private LayoutInflater inflater;
	
	public RemainsAdapter(Context context, int textViewResourceId,
			List<PositionIntf> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		layoutId = textViewResourceId;
	}
	
	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		View rowView = convertView;
		ViewHolder holder;
		PositionIntf position = getItem(pos);
		
		if (rowView == null) {
			holder = new ViewHolder();
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(layoutId, parent, false);
			
			holder.countLabel = (TextView) rowView.findViewById(R.id.lbl_count_one_remain);
			holder.warehouseLabel = (TextView) rowView.findViewById(R.id.lbl_warehouse_one_remain);
			
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		
		holder.countLabel.setText(String.valueOf(position.getPositionCount()).trim());
		holder.warehouseLabel.setText(position.getPositionFromWarehouse().getWarehouseName().trim());
		
		return rowView;
	}
	
	private class ViewHolder {
		TextView warehouseLabel;
		TextView countLabel;
	}
}
