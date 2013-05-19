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

public class NewPositionAdater extends ArrayAdapter<PositionIntf> {
	private Context context;
	private int layoutId;
	private LayoutInflater inflater;
	
	public NewPositionAdater(Context context, int textViewResourceId,
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
			
			holder.nameLabel = (TextView) rowView.findViewById(R.id.LabelPosName);
			holder.countLabel = (TextView) rowView.findViewById(R.id.LabelPosCount);
			holder.whToLabel = (TextView) rowView.findViewById(R.id.LabelPosWhTo);
			holder.whFromLabel = (TextView) rowView.findViewById(R.id.LabelPosWhFrom);
			
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		
		holder.nameLabel.setText(position.getPositionGoods().toString());
		holder.countLabel.setText(String.valueOf(position.getPositionCount()).trim());
		holder.whToLabel.setText(position.getPositionToWarehouse().getWarehouseName().trim());
		holder.whFromLabel.setText(position.getPositionFromWarehouse().getWarehouseName().trim());
		
		return rowView;
	}
	
	private class ViewHolder {
		TextView nameLabel;
		TextView countLabel;
		TextView whToLabel;
		TextView whFromLabel;
	}

}
