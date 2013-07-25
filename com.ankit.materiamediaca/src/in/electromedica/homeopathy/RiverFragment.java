package in.electromedica.homeopathy;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.Ostermiller.util.Base64;

public class RiverFragment extends android.app.Fragment {
	private ListView listview;
	private DisplayMetrics metrics;
	private final int mode = 1;
	private ListView mListView;
	public Process rootProcess;
	public List<Map> data;
	public SimpleAdapter adapter;
	private final ArrayList<String> array_sort = new ArrayList<String>();
	private final ArrayList<String> array_sort_name = new ArrayList<String>();
	String build;
	String qNum, question, answer, points, latitude, longitute, hint;
	ProgressDialog pd;
	EditText search;
	WebView tv;
	DoPostRequestWCP d;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Retrieving the currently selected item number
		String position = getArguments().getString("position");

		// List of rivers
		String[] rivers = getResources().getStringArray(R.array.rivers);

		// Creating view correspoding to the fragment
		View v = inflater.inflate(R.layout.fragment_layout, container, false);

		String html = "<html><body>Loading Please Wait!</body></html>";
		String mime = "text/html";
		String encoding = "utf-8";

		tv = (WebView) v.findViewById(R.id.webView1);
		tv.getSettings().setJavaScriptEnabled(true);
		tv.loadDataWithBaseURL(null, html, mime, encoding, null);

		// Updating the action bar title
		getActivity().getActionBar().setTitle(position);

		byte[] encoded = Base64.encode(position.trim().getBytes());
		// new DoPostRequestWCP().execute(new String(encoded));
		d = new DoPostRequestWCP();
		d.execute(new String(encoded));

		return v;
	}

	private class DoPostRequestWCP extends AsyncTask<String, String, String> {

		private ProgressDialog pd;

		@Override
		protected void onPostExecute(final String result) {

			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {

					String mime = "text/html";
					String encoding = "utf-8";

					tv.getSettings().setJavaScriptEnabled(true);
					tv.loadDataWithBaseURL(null, result.replace("&9472;", ""),
							mime, encoding, null);
				}
			});

		}

		@Override
		protected void onPreExecute() {

			pd = ProgressDialog.show(getActivity(), "", "Sending Request ");
			pd.setCancelable(true);

			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub

			getActivity().getActionBar().setTitle(values[0]);
			super.onProgressUpdate(values);
		}

		@Override
		protected String doInBackground(String... params) {

			try {
				HttpClient client = new DefaultHttpClient();
				String getURL = "http://electromedica.in/homeopathy.php?line="
						+ params[0];

				// String getURL =
				// "http://flashracebits.webatu.com/questions.json";

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

					}

					// Log.d("xxx", build);

				}
				if (pd.isShowing())
					pd.dismiss();

				return build;

			} catch (Exception e) {
				// TODO: handle exception
			}
			return build;

		}

	}

	// @Override
	// public boolean onKey(View v, int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// if (d != null && d.getStatus() == Status.RUNNING) {
	// d.cancel(true);
	// }
	// getActivity().getFragmentManager().beginTransaction().remove(this)
	// .commit();
	// return true;
	// }
	// return false;
	// }

}
