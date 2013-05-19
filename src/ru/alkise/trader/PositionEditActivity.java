package ru.alkise.trader;

import java.util.ArrayList;
import java.util.List;

import ru.alkise.trader.model.DocumentType;
import ru.alkise.trader.model.Order;
import ru.alkise.trader.model.PositionIntf;
import ru.alkise.trader.model.WarehouseIntf;
import ru.alkise.trader.model.Warehouses;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PositionEditActivity extends Activity {
	private Activity activity;
	private Intent data;
	private PositionIntf position;
	private TextView lblPosName;
	private TextView lblWhFrom;
	private EditText editCount;
	private Spinner spinnerWhTo;
	private ImageButton btnApprove;
	private ImageButton btnBack;
	private ArrayAdapter<WarehouseIntf> whToAdapter;
	private DocumentType docType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.position_edit_layout);

		activity = this;
		data = getIntent();
		docType = (DocumentType) getIntent().getSerializableExtra(Order.ORDER_TYPE);
		position = (PositionIntf) data.getSerializableExtra(PositionIntf.TABLE_NAME);

		lblPosName = (TextView) findViewById(R.id.lblNomenclature);
		lblWhFrom = (TextView) findViewById(R.id.lblFromWarehouse);

		editCount = (EditText) findViewById(R.id.editCount);
		spinnerWhTo = (Spinner) findViewById(R.id.spinnerWhTo);

		btnApprove = (ImageButton) findViewById(R.id.btnApprove);
		btnBack = (ImageButton) findViewById(R.id.btnBack);

		if (position != null) {
			lblPosName.setText(position.getPositionGoods().getGoodsName());
			lblWhFrom.setText(position.getPositionFromWarehouse().getWarehouseName());
			editCount.setText(String.valueOf(position.getPositionCount()));
			editCount.selectAll();
		}

		List<WarehouseIntf> warehouses = new ArrayList<WarehouseIntf>(
				Warehouses.INSTANCE.getWarehousesList());
		whToAdapter = new ArrayAdapter<WarehouseIntf>(this,
				android.R.layout.simple_spinner_dropdown_item, warehouses);
		spinnerWhTo.setAdapter(whToAdapter);
		spinnerWhTo.setSelection(whToAdapter.getPosition(position.getPositionToWarehouse()));

		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});

		btnApprove.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				double newCount = Double.parseDouble(String.valueOf(editCount
						.getText()));
				if ((newCount >= 1.0)
						&& ((docType == DocumentType.DEMAND) || (newCount <= position
								.getPositionGoods().getGoodsRemains()))) {
					Intent intent = new Intent();
					position.setPositionCount(newCount);
					position.setPositionToWarehouse((WarehouseIntf) spinnerWhTo.getSelectedItem());
					intent.putExtra(PositionIntf.POSITION_CODE, data.getIntExtra(PositionIntf.POSITION_CODE, 0));
					intent.putExtra(PositionIntf.POSITION_TO_WAREHOUSE,
							(WarehouseIntf) spinnerWhTo.getSelectedItem());
					intent.putExtra(PositionIntf.POSITION_COUNT, newCount);
					setResult(RESULT_OK, intent);
					finish();
				} else {
					Toast.makeText(activity, "Count error", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
	}
}
