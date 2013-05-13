package ru.alkise.trader.adapter;

import java.util.List;

import ru.alkise.trader.R;
import ru.alkise.trader.model.Goods;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FindingGoodsAdapter extends ArrayAdapter<Goods> {
	private Context context;
	private int layoutId;
	private LayoutInflater inflater;

	public FindingGoodsAdapter(Context context, int textViewResourceId,
			List<Goods> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		layoutId = textViewResourceId;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		View rowView = convertView;
		ViewHolder holder;
		Goods goods = getItem(pos);

		if (rowView == null) {
			holder = new ViewHolder();
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(layoutId, parent, false);

			holder.nameLabel = (TextView) rowView
					.findViewById(R.id.lbl_name_goods_remain);
			holder.countLabel = (TextView) rowView
					.findViewById(R.id.lbl_count_goods_remain);

			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}

		holder.nameLabel.setText(String.valueOf(goods.getCode()) + " " + goods.getDescr().trim());
		holder.countLabel.setText(context.getString(R.string.rests) + ": "
				+ String.valueOf(goods.getCount()).trim());

		return rowView;
	}

	private class ViewHolder {
		TextView nameLabel;
		TextView countLabel;
	}

}
