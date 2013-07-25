package in.electromedica.homeopathy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class HTMLViewerCast extends Fragment {
	private static final String TAG = "web";
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
	private LinearLayout layout;

	private AdView adView;

	private AdRequest adRequest;

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

		// Retrieving the currently selected item number
		String position = getArguments().getString("position");
		String positiont = getArguments().getString("positiont");
		// List of rivers
		String[] rivers = getResources().getStringArray(R.array.rivers);
		getActivity().getActionBar().setTitle(positiont);
		// Creating view correspoding to the fragment
		View v = inflater.inflate(R.layout.fragment_layout, container, false);

		String html = "<html><body> Loading Please Wait !</body></html>";
		String mime = "text/html";
		String encoding = "UTF-8";

		tv = (WebView) v.findViewById(R.id.webView1);
		tv.getSettings().setJavaScriptEnabled(true);

		tv.loadDataWithBaseURL(null, position.toString(), mime, encoding, null);

		return v;
	}

}
