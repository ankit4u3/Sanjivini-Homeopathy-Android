package in.electromedica.homeopathy;

import android.content.Context;
import android.widget.ArrayAdapter;

public class MyListAdapter extends ArrayAdapter<String> {

	public MyListAdapter(Context context, String[] proMenu) {
		// TODO Auto-generated constructor stub
		super(context, android.R.layout.simple_list_item_1);
	}
}