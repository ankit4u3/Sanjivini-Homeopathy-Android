package in.electromedica.homeopathy;

import java.io.BufferedReader;
import java.io.File;
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

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class MainActivity extends Activity {
	public static final String TAG = null;
	public ListView mListView;
	public List<Map> data;
	public SimpleAdapter adapter;
	private final ArrayList<String> array_sort = new ArrayList<String>();
	String build;
	String qNum, question, answer, points, latitude, longitute, hint;
	ProgressDialog pd;
	EditText search;
	public Boolean ret = false;
	public ProgressBar pbc;
	// Within which the entire activity is enclosed
	DrawerLayout mDrawerLayout;

	// ListView represents Navigation Drawer
	ListView mDrawerList;
	// ActionBarDrawerToggle indicates the presence of Navigation Drawer in the
	// action bar
	ActionBarDrawerToggle mDrawerToggle;

	// Title of the action bar
	String mTitle = "";

	private Process rootProcess;
	RiverFragment rFragment;
	private LinearLayout layout;

	private AdView adView;

	private AdRequest adRequest;
	MainActivityChampagne info = new MainActivityChampagne(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// create new tabs and set up the titles of the tabs

		mTitle = (String) getTitle();

		// Getting reference to the DrawerLayout
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerList = (ListView) findViewById(R.id.drawer_list);

		// Getting reference to the ActionBarDrawerToggle
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			/** Called when drawer is closed */
			@Override
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();

			}

			/** Called when a drawer is opened */
			@Override
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle("Select Options");
				invalidateOptionsMenu();
			}

		};

		// Setting DrawerToggle on DrawerLayout
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// Creating an ArrayAdapter to add items to the listview mDrawerList
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getBaseContext(), R.layout.drawer_list_item, getResources()
						.getStringArray(R.array.rivers));

		// Setting the adapter on mDrawerList
		mDrawerList.setAdapter(adapter);

		// Enabling Home button
		getActionBar().setHomeButtonEnabled(true);

		// Enabling Up navigation
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// if (isServiceRunning("com.ankit.whocalledpro.MyService") == false) {
		// startService(new Intent(this, MyService.class));
		// }
		// Setting item click listener for the listview mDrawerList
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// Getting an array of rivers
				String[] rivers = getResources().getStringArray(R.array.rivers);

				// Currently selected river
				mTitle = rivers[position];

				// Creating a fragment object

				FragmentManager fragmentManager = getFragmentManager();

				// Creating a fragment transaction
				FragmentTransaction ft = fragmentManager.beginTransaction();
				switch (position) {

				case 0:
					Uri uri = Uri.parse("market://details?id="
							+ getPackageName());
					Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
					try {
						startActivity(goToMarket);
					} catch (ActivityNotFoundException e) {
						Toast.makeText(getApplicationContext(),
								"Market Connot Locate the Package ",
								Toast.LENGTH_LONG).show();
					}
					mDrawerLayout.closeDrawer(mDrawerList);
					break;
				case 1:
					Howtouse htu = new Howtouse();
					ft.setCustomAnimations(android.R.animator.fade_in,
							android.R.animator.fade_out);
					ft.replace(R.id.content_frame, htu);
					ft.addToBackStack(null);
					ft.commit();
					mDrawerLayout.closeDrawer(mDrawerList);
					break;

				case 2:
					Symptome sSymptome = new Symptome();
					ft.setCustomAnimations(android.R.animator.fade_in,
							android.R.animator.fade_out);
					ft.replace(R.id.content_frame, sSymptome);
					ft.addToBackStack(null);
					ft.commit();
					mDrawerLayout.closeDrawer(mDrawerList);
					break;
				//
				case 3:
					Embroyo emb = new Embroyo();
					ft.setCustomAnimations(android.R.animator.fade_in,
							android.R.animator.fade_out);
					ft.replace(R.id.content_frame, emb);
					ft.addToBackStack(null);
					ft.commit();
					mDrawerLayout.closeDrawer(mDrawerList);
					break;
				case 4:
					DarbariList dar = new DarbariList();
					ft.setCustomAnimations(android.R.animator.fade_in,
							android.R.animator.fade_out);
					ft.replace(R.id.content_frame, dar);
					ft.addToBackStack(null);
					ft.commit();
					mDrawerLayout.closeDrawer(mDrawerList);
					break;
				case 5:

					mDrawerLayout.closeDrawer(mDrawerList);
					break;

				default:
					if (isNetworkOnline() == true) {
						// Bundle bundle = new Bundle();
						// bundle.putString("edttext", mTitle);
						RiverFragment rFragment = new RiverFragment();
						Bundle data = new Bundle();
						info.open();
						info.createEntry(mTitle, "Last Logged");
						info.close();

						data.putString("position", mTitle);
						rFragment.setArguments(data);
						ft.setCustomAnimations(android.R.animator.fade_in,
								android.R.animator.fade_out);
						ft.replace(R.id.content_frame, rFragment);
						ft.addToBackStack(null);
						ft.commit();

						mDrawerLayout.closeDrawer(mDrawerList);
						break;

					} else {
						Toast.makeText(getApplicationContext(),
								"Error in Network", Toast.LENGTH_SHORT).show();
					}

					mDrawerLayout.closeDrawer(mDrawerList);
					break;
				}

			}
		});
		mDrawerLayout.openDrawer(mDrawerList);
		layout = (LinearLayout) findViewById(R.id.ad_holder);
		adView = new AdView(MainActivity.this, AdSize.BANNER, "a151c0a237afe98");
		layout.addView(adView);
		adRequest = new AdRequest();
		adView.loadAd(adRequest);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	/** Handling the touch event of app icon */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/** Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the drawer is open, hide action items related to the content view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean isNetworkOnline() {
		boolean status = false;
		try {
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getNetworkInfo(0);
			if (netInfo != null
					&& netInfo.getState() == NetworkInfo.State.CONNECTED) {
				status = true;
			} else {
				netInfo = cm.getNetworkInfo(1);
				if (netInfo != null
						&& netInfo.getState() == NetworkInfo.State.CONNECTED)
					status = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return status;

	}

	@Override
	protected void onResume() {
		SharedPreferences prefs = this.getSharedPreferences("confirmRegSyp", 0);
		boolean runOnce = prefs.getBoolean("ranOnce", false);
		{
			SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean("ranOnce", false);
			editor.commit();
		}
		super.onResume();
	}

	public void ShowDialogForResults(String result) {
		WindowManager wm = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);

		final Dialog dialog = new Dialog(MainActivity.this);
		String html = result;
		String mime = "text/html";
		String encoding = "utf-8";
		dialog.setTitle(mTitle);
		ScrollView sv = new ScrollView(MainActivity.this);
		LinearLayout ll = new LinearLayout(MainActivity.this);
		ll.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		// layoutParams.setMargins(30, 20, 30, 0);
		layoutParams.height = getResources().getDisplayMetrics().heightPixels;
		layoutParams.width = getResources().getDisplayMetrics().widthPixels;
		TextView tv = new TextView(MainActivity.this);
		WebView v = new WebView(MainActivity.this);
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

	private boolean isServiceRunning(String serviceName) {
		// serviceName == "com.ankit.whocalledpro.MyService"
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceName.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	public Animation inFromRightAnimation() {
		Animation inFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromRight.setDuration(800);
		inFromRight.setInterpolator(new AccelerateInterpolator());
		return inFromRight;
	}

	public Animation outToRightAnimation() {
		Animation outtoLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoLeft.setDuration(800);
		outtoLeft.setInterpolator(new AccelerateInterpolator());
		return outtoLeft;
	}

	public static void clearApplicationData(Context context) {
		File cache = context.getCacheDir();
		File appDir = new File(cache.getParent());
		if (appDir.exists()) {
			String[] children = appDir.list();
			for (String s : children) {
				File f = new File(appDir, s);
				if (deleteDir(f))
					Log.i("deleting",
							String.format("**************** DELETED -> (%s) "
									+ "*******************",
									f.getAbsolutePath()));

			}
		}
	}

	private static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	private class DoPostRequestWCP extends AsyncTask<String, String, String> {

		private ProgressDialog pd;

		@Override
		protected void onPostExecute(String result) {
			ShowDialogForResults(result);

			super.onPostExecute(result);

		}

		@Override
		protected void onPreExecute() {

			pd = ProgressDialog.show(MainActivity.this, "", "Sending Request ");
			pd.setCancelable(true);

			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub

			// super.onProgressUpdate(values);
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

					Log.d("xxx", build);

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

}
