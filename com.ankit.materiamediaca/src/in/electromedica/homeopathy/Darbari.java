package in.electromedica.homeopathy;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Darbari extends ListFragment {
	// private static final List<Map<String, String>> items = new
	// ArrayList<Map<String, String>>();
	private static final String[] keys = { "line1", "line2" };
	private static final int[] controlIds = { android.R.id.text1,
			android.R.id.text2 };
	private static final String TAG = null;
	public ListView mListView;
	public List<Map> data;
	public SimpleAdapter adapter;
	private final ArrayList<String> array_sort = new ArrayList<String>();
	String build;
	String qNum, question, answer, points, latitude, longitute, hint, remedy2,
			remedy3, remedy1;
	ProgressDialog pd;
	EditText search;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.url_list, container, false);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		getActivity().getFragmentManager().putFragment(outState, TAG, this);
	}

	public void onRetoreInstanceState(Bundle inState) {
		getActivity().getFragmentManager().getFragment(inState, TAG);
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

				HttpClient client = new DefaultHttpClient();
				String getURL = "http://electromedica.in/darbari.php";//

				HttpGet get = new HttpGet(getURL);
				HttpResponse responseGet = client.execute(get);
				HttpEntity resEntityGet = responseGet.getEntity();

				if (resEntityGet != null) {

					InputStream instream = resEntityGet.getContent();
					BufferedReader str = new BufferedReader(
							new InputStreamReader(instream));

					String ans = new String("");
					build = new String("");
					while ((ans = str.readLine()) != null) {
						build = build + ans;
						// Log.d("g", build);

					}

					JSONObject jobj = new JSONObject(build);
					JSONArray arr = jobj.getJSONArray("questions");
					String arrlen = Integer.toString(arr.length());
					// Log.d(
					for (int i = 0; i < arr.length(); i++) {
						JSONObject qs = arr.getJSONObject(i);

						qNum = qs.getString("E");// nick &points
						remedy1 = qs.getString("one");
						remedy2 = qs.getString("two");
						remedy3 = qs.getString("three");

						Map<String, String> map = new HashMap<String, String>();
						map.put("line1", !qNum.isEmpty() ? qNum : "Unknown");
						map.put("line2", (!remedy1.isEmpty() ? "\nRemedy 1\n"
								+ remedy1 : "")
								+ (!remedy2.isEmpty() ? "\nRemedy 2\n"
										+ remedy2 : "")
								+

								(!remedy3.isEmpty() ? "\nRemedy 3\n" + remedy3
										: ""));

						items.add(map);

						listtemp.add(map);

					}

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