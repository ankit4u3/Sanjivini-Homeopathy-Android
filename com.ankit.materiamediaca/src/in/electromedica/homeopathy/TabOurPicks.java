package in.electromedica.homeopathy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListFragment;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class TabOurPicks extends ListFragment {
	// private static final List<Map<String, String>> items = new
	// ArrayList<Map<String, String>>();
	private static final String[] keys = { "line1", "line2" };
	private static final int[] controlIds = { android.R.id.text1,
			android.R.id.text2 };

	//
	// static {
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("line1", "Title One");
	// map.put("line2", "Subtitle One");
	// items.add(map);
	// map = new HashMap<String, String>();
	// map.put("line1", "Title Two");
	// map.put("line2", "Subtitle Two");
	// items.add(map);
	// map = new HashMap<String, String>();
	// map.put("line1", "Title Three");
	// map.put("line2", "Subtitle Three");
	// items.add(map);
	// map = new HashMap<String, String>();
	// map.put("line1", "Title Four");
	// map.put("line2", "Subtitle Four");
	// items.add(map);
	// map = new HashMap<String, String>();
	// map.put("line1", "Title Five");
	// map.put("line2", "Subtitle Five");
	// items.add(map);
	// }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.url_list, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//
		new SimpleTestAsync().execute();
		//
		// ListAdapter adapter = new SimpleAdapter(getActivity(), items,
		// android.R.layout.simple_list_item_2, keys, controlIds);
		// setListAdapter(adapter);
	}

	class SimpleTestAsync extends AsyncTask<String, String, String> {
		List<Map> listtemp = new ArrayList<Map>(); //
		Map<String, String> map = new HashMap<String, String>();
		List<Map<String, String>> items = new ArrayList<Map<String, String>>();

		// ALL THE DATA FETCHED WILL BE SAVED IN THIS LIST MAP OF ARRAYLIST TYPE

		@Override
		protected String doInBackground(String... params) {
			try {

				String[] strFields = {
						android.provider.CallLog.Calls.CACHED_NAME,
						android.provider.CallLog.Calls.NUMBER,
						android.provider.CallLog.Calls.DATE,
						android.provider.CallLog.Calls.TYPE };
				String strOrder = android.provider.CallLog.Calls.DATE + " DESC";

				Cursor mCallCursor = getActivity().getContentResolver().query(
						android.provider.CallLog.Calls.CONTENT_URI, strFields,
						null, null, strOrder);

				if (mCallCursor.moveToFirst()) {

					do {

						boolean missed = mCallCursor.getInt(mCallCursor
								.getColumnIndex(CallLog.Calls.TYPE)) == CallLog.Calls.OUTGOING_TYPE;

						if (missed) {

							String name = mCallCursor.getString(mCallCursor
									.getColumnIndex(CallLog.Calls.CACHED_NAME));

							String number = mCallCursor.getString(mCallCursor
									.getColumnIndex(CallLog.Calls.NUMBER));

							String time = mCallCursor.getString(mCallCursor
									.getColumnIndex(CallLog.Calls.DATE));

							Map<String, String> map = new HashMap<String, String>();
							map.put("line1", !name.isEmpty() ? name : "Unknown");
							map.put("line2", number);
							items.add(map);

						}

					} while (mCallCursor.moveToNext());

				}

			}

			catch (Exception e) {
				// TODO: handle exception
			}
			return null;

		}

		@Override
		protected void onPostExecute(String result) {

			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {

					ListAdapter adapter = new SimpleAdapter(getActivity(),
							items, android.R.layout.simple_list_item_2, keys,
							controlIds);
					setListAdapter(adapter);

				}
			});

		}

	}
}