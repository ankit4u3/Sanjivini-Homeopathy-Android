package in.electromedica.homeopathy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Embroyo extends ListFragment {
	// private static final List<Map<String, String>> items = new
	// ArrayList<Map<String, String>>();
	private static final String[] keys = { "line1", "line2" };
	private static final int[] controlIds = { android.R.id.text1,
			android.R.id.text2 };
	private static final String TAG = "symp";
	boolean mDualPane;
	int mCurCheckPosition = 0;
	public List<Map> data;
	public SimpleAdapter adapter;
	private final ArrayList<String> array_sort = new ArrayList<String>();

	private final ArrayList<String> itemname = new ArrayList<String>();
	private final ArrayList<String> itemdesc = new ArrayList<String>();
	String build;
	String qNum, question, answer, points, latitude, longitute, hint, remedy2,
			remedy3, remedy1;
	public ProgressDialog pd;
	EditText search;
	ListView listView;

	@Override
	public void onSaveInstanceState(Bundle outState) {
		getActivity().getFragmentManager().putFragment(outState, TAG, this);
	}

	public void onRetoreInstanceState(Bundle inState) {
		getActivity().getFragmentManager().getFragment(inState, TAG);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.url_list, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		final FragmentManager fragmentManager = getFragmentManager();

		listView = (ListView) getActivity().findViewById(android.R.id.list);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				FragmentTransaction ft = fragmentManager.beginTransaction();
				HTMLViewerCast rFragment = new HTMLViewerCast();
				Bundle data = new Bundle();
				data.putString("position", itemdesc.get(arg2));
				data.putString("positiont", itemname.get(arg2));
				rFragment.setArguments(data);
				ft.replace(R.id.content_frame, rFragment);
				ft.addToBackStack(null);
				ft.commit();

			}

		});
		int i = listView.getCount();
		if (i <= 0) {
			pd = ProgressDialog.show(getActivity(), "", "Loading...");
			pd.setCancelable(true);
			//
			new SimpleTestAsync().execute();
		}
		//

		if (savedInstanceState != null) {
			// Restore last state for checked position.
			mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
		}
	}

	public void write2sd(String s) {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED)) {

			return;
		}

		File sdcard = Environment.getExternalStorageDirectory();
		FileWriter fileWriter = null;
		FileReader fileReader = null;
		char[] buf = null;
		try {
			File newfile = new File(sdcard.getAbsolutePath() + "/embroyo.txt");
			fileWriter = new FileWriter(newfile);
			fileWriter.write(s);
			fileReader = new FileReader(newfile);
			buf = new char[50];
			fileReader.read(buf);

		} catch (Exception e) {
			Log.e("PATH ERROR", e.toString());
		} finally {
			try {
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void ShowDialogForResults(String result) {
		WindowManager wm = (WindowManager) getActivity()
				.getApplicationContext().getSystemService(
						Context.WINDOW_SERVICE);

		final Dialog dialog = new Dialog(getActivity());
		String html = result;
		String mime = "text/html";
		String encoding = "utf-8";
		dialog.setTitle("Symptomes");
		ScrollView sv = new ScrollView(getActivity());
		LinearLayout ll = new LinearLayout(getActivity());
		ll.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		// layoutParams.setMargins(30, 20, 30, 0);
		layoutParams.height = getResources().getDisplayMetrics().heightPixels;
		layoutParams.width = getResources().getDisplayMetrics().widthPixels;
		TextView tv = new TextView(getActivity());
		WebView v = new WebView(getActivity());
		v.getSettings().setJavaScriptEnabled(true);
		v.loadDataWithBaseURL(null, html, mime, encoding, null);

		tv.setText(result);

		// v.setPadding(4, 0, 4, 10);
		// ll.addView(v, layoutParams);
		sv.addView(v, layoutParams);
		// ll.addView(tv, layoutParams);
		dialog.setContentView(sv);

		dialog.show();
	}

	class SimpleTestAsync extends AsyncTask<String, String, String> {
		List<Map> listtemp = new ArrayList<Map>(); //
		Map<String, String> map = new HashMap<String, String>();
		List<Map<String, String>> items = new ArrayList<Map<String, String>>();

		// ALL THE DATA FETCHED WILL BE SAVED IN THIS LIST MAP OF ARRAYLIST TYPE

		@Override
		protected String doInBackground(String... params) {

			try {

				InputStream is = getActivity().getAssets().open(
						"Medica/embroyo.txt");
				// InputStream instream = resEntityGet.getContent();
				BufferedReader str = new BufferedReader(new InputStreamReader(
						is));

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
					remedy1 = qs.getString("H");
					String input = remedy1;
					Source htmlSource = new Source(input);
					Segment htmlSeg = new Segment(htmlSource, 0, input.length());
					Renderer htmlRend = new Renderer(htmlSeg);

					Map<String, String> map = new HashMap<String, String>();
					map.put("line1", !qNum.isEmpty() ? qNum : "Unknown");
					map.put("line2", "");

					itemname.add(!qNum.isEmpty() ? qNum : "Unknown");
					itemdesc.add(remedy1);
					// (!remedy2.isEmpty() ? "\nRemedy 1\n"
					// + remedy2 : ""));

					items.add(map);
					listtemp.add(map);

				}

			}

			// }

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
							items, android.R.layout.two_line_list_item, keys,
							controlIds);
					setListAdapter(adapter);

				}
			});
			if (pd.isShowing())
				pd.dismiss();
		}

	}

}