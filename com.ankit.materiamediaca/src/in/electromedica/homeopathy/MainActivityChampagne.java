package in.electromedica.homeopathy;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MainActivityChampagne {

	public static final String KEY_NAME = "title";
	public static final String KEY_NOTES = "lastcall";

	private static final String DATABASE_NAME = "Champagne.db";
	private static final String DATABASE_TABLE = "champagneTable";
	private static final int DATABASE_VERSION = 1;

	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;

	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub\
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + KEY_NAME
					+ " TEXT NOT NULL);");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXIST " + DATABASE_TABLE);
			onCreate(db);

		}

	}

	public MainActivityChampagne(Context c) {
		ourContext = c;

	}

	public MainActivityChampagne open() {
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;

	}

	public void close() {
		ourHelper.close();

	}

	public long createEntry(String titles, String notes) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, titles);
		return ourDatabase.insert(DATABASE_TABLE, null, cv);

	}

	public ArrayList<String> getData() {
		// TODO Auto-generated method stub
		String[] columns = new String[] { KEY_NAME };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null,
				null, null);
		String result = "";
		int iNotes = c.getColumnIndex(KEY_NAME);
		ArrayList<String> string_array = new ArrayList<String>();
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = c.getString(iNotes) + "\n";
			string_array.add(result);
			Log.d("Sqllite", result);
		}

		return string_array;

	}

	public void printlog() {
		Cursor c = ourDatabase.rawQuery("SELECT * FROM champagneTable", null);

		if (c != null) {
			if (c.moveToFirst()) {
				do {
					String dir = c.getString(c.getColumnIndex("title"));
					Log.d("Sqllite", dir);
				} while (c.moveToNext());
			}
		}
	}

	public void updateEntry(String thetitles, String thenotes) {
		// TODO Auto-generated method stub
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(KEY_NAME, thetitles);
		/* cvUpdate.put(KEY_NOTES, thenotes); */
		/*
		 * ourDatabase.update(DATABASE_TABLE, cvUpdate, KEY_NAME + "=" +
		 * thetitles + KEY_NOTES + "=" + thenotes, null);
		 */
		ourDatabase.update(DATABASE_TABLE, cvUpdate, KEY_NAME + "='"
				+ thetitles + "'", null);

	}

}