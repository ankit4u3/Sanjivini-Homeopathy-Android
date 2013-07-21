package in.electromedica.homeopathy;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyCustomReceiver extends BroadcastReceiver {
	public MyCustomReceiver() {
	}

	private static final String TAG = "MyCustomReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		String name = null;
		String item = null;
		try {

			String action = intent.getAction();
			String channel = intent.getExtras().getString("com.parse.Channel");
			if (channel != null) {
				JSONObject json = new JSONObject(intent.getExtras().getString(
						"com.parse.Data"));

				Log.d(TAG, "got action " + action + " on channel " + channel
						+ " with:");
				Iterator itr = json.keys();
				while (itr.hasNext()) {
					String key = (String) itr.next();
					Log.d(TAG, "..." + key + " => " + json.getString(key));
					name = json.getString("title");
					item = json.getString("news");
				}

				// String str = channel; // Data you want to send
				// Intent intent1 = new Intent(context, MainActivity.class);
				// intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// intent1.putExtra("channel", str); // here you will add the
				// data
				// // into
				// // intent to pass bw
				// // activites
				// intent1.putExtra("name", name);
				// intent1.putExtra("item", item);
				//
				// context.startActivity(intent1);
			} else {

			}

		} catch (JSONException e) {
			Log.d(TAG, "JSONException: " + e.getMessage());
		}
	}
}
