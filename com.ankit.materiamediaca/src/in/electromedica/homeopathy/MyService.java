package in.electromedica.homeopathy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
import com.parse.PushService;

public class MyService extends Service {
	public MyService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Parse.initialize(this, "Ma5ET71v6ghavJy2OWotdgLK4kBr4rwV5qhHZZxs",
				"5rFU1rvaiiXq11HMFJ9t7Bau8TclEcGJwq7WjM1B");

		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
		// Optionally enable public read access.
		// defaultACL.setPublicReadAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);
		PushService.subscribe(this, "Sanjivini", MainActivity.class);

		PushService.setDefaultPushCallback(this, MainActivity.class);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		return START_STICKY;
	}
}
