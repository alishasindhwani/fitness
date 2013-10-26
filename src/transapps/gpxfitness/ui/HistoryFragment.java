package transapps.gpxfitness.ui;

import java.util.ArrayList;

import transapps.gpxfitness.R;
import transapps.gpxfitness.db.HistoryAccessor;
import transapps.gpxfitness.obj.History;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class HistoryFragment extends Fragment {
    private static final int CONTENT_VIEW_ID = 10101010;
	private HistoryItemAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.history, container, false);
		ArrayList<History> histories = new ArrayList<History>();
		String[][] date_cals = HistoryAccessor.getDatesList();
		if(date_cals!=null)
		{
			for(int i=0; i<date_cals.length; i++)
			{
				histories.add(new History(date_cals[i][0], date_cals[i][1]));
			}	
			ListView listview = (ListView) rootView.findViewById(R.id.history_listview);
			
			adapter = new HistoryItemAdapter(getActivity(), R.layout.history_item, histories);
			adapter.setHistoryFragment(this);

			listview.setAdapter(adapter);
		}
		return rootView;
	}

}

class HistoryItemAdapter extends ArrayAdapter<History> {
	Context context;
	int rowResourceId;
	ArrayList<History> data;
	HistoryFragment frag;
	int containerID;
	
	public HistoryItemAdapter(Context context, int rowResourceId, ArrayList<History> data)
	{
		super(context, rowResourceId, data);
		this.context = context;
		this.rowResourceId=rowResourceId;
		this.data=data;
	}
	
	public void setContainer(int id) {
		this.containerID = id;
	}

	public void setHistoryFragment(HistoryFragment f) {
		this.frag = f;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(rowResourceId, parent, false);
		TextView date = (TextView) rowView.findViewById(R.id.date);
		TextView calories = (TextView) rowView.findViewById(R.id.total_calories);
		date.setText(data.get(position).getDate());
		calories.setText(data.get(position).getTotalCal() + " calories");
		final FragmentManager manager = frag.getFragmentManager();

		final ImageView list = (ImageView) rowView.findViewById(R.id.his_list);
        list.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("hist", "show me the list");
				
				Bundle b = new Bundle();
				b.putInt("year", data.get(position).getYear());
				b.putInt("month", data.get(position).getMonth());
				b.putInt("day", data.get(position).getDay());
				// Create new fragment and transaction
				Fragment summary = new ExerciseFragment(); //FIXME: this shouldn't include functionality to add new stuff!
				summary.setArguments(b);
				FragmentTransaction transaction = manager.beginTransaction();

				// Replace whatever is in the fragment_container view with this fragment,
				// and add the transaction to the back stack
				transaction.replace(R.id.history_container, summary);
				transaction.show(summary);
				transaction.addToBackStack(null);

				// Commit the transaction
				transaction.commit();
			}
        });
		
		return rowView;
	}
}