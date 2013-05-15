package ru.alkise.trader;

import java.util.ArrayList;
import java.util.List;

import ru.alkise.trader.model.OrderType;
import ru.alkise.trader.model.Position;
import ru.alkise.trader.model.Warehouse;
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
	private Position position;
	private TextView lblPosName;
	private TextView lblWhFrom;
	private EditText editCount;
	private Spinner spinnerWhTo;
	private ImageButton btnApprove;
	private ImageButton btnBack;
	private ArrayAdapter<Warehouse> whToAdapter;
	private OrderType docType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.position_edit_layout);

		activity = this;
		data = getIntent();
		docType = (OrderType) getIntent().getSerializableExtra("docType");
		position = (Position) data.getSerializableExtra("position");

		lblPosName = (TextView) findViewById(R.id.lblNomenclature);
		lblWhFrom = (TextView) findViewById(R.id.lblFromWarehouse);

		editCount = (EditText) findViewById(R.id.editCount);
		spinnerWhTo = (Spinner) findViewById(R.id.spinnerWhTo);

		btnApprove = (ImageButton) findViewById(R.id.btnApprove);
		btnBack = (ImageButton) findViewById(R.id.btnBack);

		if (position != null) {
			lblPosName.setText(position.getGoods().getDescr());
			lblWhFrom.setText(position.getWhFrom().getDescr());
			editCount.setText(String.valueOf(position.getCount()));
			editCount.selectAll();
		}

		List<Warehouse> warehouses = new ArrayList<Warehouse>(
				Warehouses.INSTANCE.getWarehousesList());
		whToAdapter = new ArrayAdapter<Warehouse>(this,
				android.R.layout.simple_spinner_dropdown_item, warehouses);
		spinnerWhTo.setAdapter(whToAdapter);
		spinnerWhTo.setSelection(whToAdapter.getPosition(position.getWhTo()));

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
						&& ((docType == OrderType.DEMAND) || (newCount <= position
								.getMaxCount()))) {
					Intent intent = new Intent();
					position.setCount(newCount);
					position.setWhTo((Warehouse) spinnerWhTo.getSelectedItem());
					intent.putExtra("pos", data.getIntExtra("pos", 0));
					intent.putExtra("whTo",
							(Warehouse) spinnerWhTo.getSelectedItem());
					intent.putExtra("count", newCount);
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
