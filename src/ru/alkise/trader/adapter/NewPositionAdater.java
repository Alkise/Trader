package ru.alkise.trader.adapter;

import java.util.List;

import ru.alkise.trader.R;
import ru.alkise.trader.model.Position;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NewPositionAdater extends ArrayAdapter<Position> {
	private Context context;
	private int layoutId;
	private LayoutInflater inflater;
	
	public NewPositionAdater(Context context, int textViewResourceId,
			List<Position> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		layoutId = textViewResourceId;
	}
	
	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		View rowView = convertView;
		ViewHolder holder;
		Position position = getItem(pos);
		
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
		
		holder.nameLabel.setText(position.getGoods().toString());
		holder.countLabel.setText(String.valueOf(position.getCount()).trim());
		holder.whToLabel.setText(position.getWhTo().getDescr().trim());
		holder.whFromLabel.setText(position.getWhFrom().getDescr().trim());
		
		return rowView;
	}
	
	private class ViewHolder {
		TextView nameLabel;
		TextView countLabel;
		TextView whToLabel;
		TextView whFromLabel;
	}

}
