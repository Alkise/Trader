package ru.alkise.trader.model.store;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class SharedPreferencesSettingsSaver implements SettingsSaverIntf {
	private static final String DEFAULT_STRING = "";

	private SharedPreferences sp;
	private Editor e;

	public SharedPreferencesSettingsSaver(Context context) {
		sp = context.getSharedPreferences(SETTINGS_FILE_NAME,
				Context.MODE_PRIVATE);
		e = sp.edit();
	}

	@Override
	public boolean commit() {
		return e.commit();
	}

	@Override
	public void saveStringParam(EditText editText) {
		e.putString(String.valueOf(editText.getTag()),
				String.valueOf(editText.getText()));
	}

	@Override
	public void saveIntParam(EditText editText) {
		e.putInt(String.valueOf(editText.getTag()),
				Integer.valueOf(editText.getText().toString()));
	}

	@Override
	public void loadStringParam(EditText editText) {
		editText.setText(sp.getString(String.valueOf(editText.getTag()),
				DEFAULT_STRING));
	}

	@Override
	public void loadIntParam(EditText editText) {
		editText.setText(String.valueOf(sp.getInt((String) editText.getTag(), 0)));
	}

	@Override
	public void saveStringParams(List<EditText> editTextList) {
		for (EditText edit : editTextList) {
			saveStringParam(edit);
		}
	}

	@Override
	public void saveIntParams(List<EditText> editTextList) {
		for (EditText edit : editTextList) {
			saveIntParam(edit);
		}
	}

	@Override
	public void loadStringParams(List<EditText> editTextList) {
		for (EditText edit : editTextList) {
			loadStringParam(edit);
		}
	}

	@Override
	public void loadIntParams(List<EditText> editTextList) {
		for (EditText edit : editTextList) {
			loadIntParam(edit);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void saveStringSetParams(ListView listView) {
		ArrayAdapter<String> listAdapter = (ArrayAdapter<String>) listView.getAdapter();
		Set<String> elements = new HashSet<String>();
		for (int i = 0; i < listAdapter.getCount(); i++) {
			elements.add(listAdapter.getItem(i));
		}
		e.putStringSet(String.valueOf(listView.getTag()), elements);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadStingSetParams(ListView listView) {
		ArrayAdapter<String> listAdapter = (ArrayAdapter<String>) listView.getAdapter();
		Set<String> elements = new HashSet<String>();
		elements = sp.getStringSet(String.valueOf(listView.getTag()), elements);
		listAdapter.addAll(elements);
	}

}
