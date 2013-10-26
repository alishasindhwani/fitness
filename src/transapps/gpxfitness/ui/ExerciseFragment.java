package transapps.gpxfitness.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import transapps.gpxfitness.R;
import transapps.gpxfitness.db.HistoryAccessor;
import transapps.gpxfitness.db.METAccessor;
import transapps.gpxfitness.obj.ASet;
import transapps.gpxfitness.obj.Exercise;
import transapps.gpxfitness.obj.WeightExercise;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ExerciseFragment extends Fragment {

	private static final int ADDED_EXERCISE_CODE = 1616;
	private ArrayList<Exercise> exerciseList;
	private Calendar cal;
	private boolean IS_HISTORY_FRAGMENT = false;
	AddedExercisesAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_exercise, container, false);
		// add Exercise 
		cal = Calendar.getInstance();
		Bundle bundle = getArguments();
		if (bundle!=null && bundle.getInt("year")>0 && bundle.getInt("month")>0 && bundle.getInt("day")>0) {
			cal.set(bundle.getInt("year"), bundle.getInt("month")-1, bundle.getInt("day"));
			IS_HISTORY_FRAGMENT = true;
		}

		ListView listView = (ListView) rootView.findViewById(R.id.listView);
		exerciseList = new ArrayList<Exercise>();
		Exercise[] ex_array = HistoryAccessor.getBasicHistory(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		if(ex_array!=null) {
			for(Exercise lol: ex_array) {
				exerciseList.add(0,lol);
			}
		}
		Map<String, Integer> iconCollection = new LinkedHashMap<String, Integer>();
		List<String> categoryList =new ArrayList<String>();
		ExerciseListViewActivity.createCategoryList(categoryList);
		ExerciseListViewActivity.createIconCollection(categoryList, iconCollection);
		adapter = new AddedExercisesAdapter(getActivity(),R.layout.added_exercise_item, exerciseList,iconCollection);
		listView.setAdapter(adapter);

		if(IS_HISTORY_FRAGMENT == false)
		{
			rootView.findViewById(R.id.addexercise)
			.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					openExerciseListView();
				}
			});
			rootView.findViewById(R.id.plus)
			.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					openExerciseListView();
				}
			});
			listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Exercise obj = adapter.getExerciseItem(position);
					if(obj instanceof WeightExercise)
						editAlertDialogWeight(obj);
					else
						editAlertDialog(obj);
				}
			});
			listView.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int position, long id) {
					Exercise obj = adapter.getExerciseItem(position);
					deleteAlertDialog(obj, position);
					return false;
				}

			});
		}
		else{
			listView.setClickable(false);
			rootView.findViewById(R.id.addexercise).setVisibility(View.GONE);
			rootView.findViewById(R.id.plus).setVisibility(View.GONE);
		}

		return rootView;
	}
	public void openExerciseListView()
	{
		Intent intent = new Intent(getActivity(), ExerciseListViewActivity.class);
		startActivityForResult(intent,ADDED_EXERCISE_CODE);	
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (data!=null && requestCode == ADDED_EXERCISE_CODE) {

			if(resultCode == -1){      
				final String exerciseName=data.getStringExtra("exercise"); 
				Log.d("ALERT", METAccessor.getCategory(exerciseName));
				if(METAccessor.getCategory(exerciseName).equals("weight training"))
				{
					Log.d("ALERT", "realized it was a weight exercise");
					addAlertDialogWeight(exerciseName);
				}
				else{
					addAlertDialog(exerciseName);
				}
			}
			if (resultCode == 0) {    
				//Write your code if there's no result
			}
		}
	}
	public void deleteAlertDialog(final Exercise ex, final int position)
	{
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

		alert.setTitle("Delete " + ex.getType() + '?');
		alert.setCancelable(false);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				adapter.deleteExerciseItem(position);
				HistoryAccessor.deleteHistory(ex, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
				adapter.notifyDataSetChanged();	
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//  openExerciseListView();
			}
		});

		alert.show();	
	}

	public void editAlertDialog(final Exercise ex)
	{
		final Exercise old_ex = new Exercise(ex);
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.added_exercises_dialog);
		dialog.setTitle("Edit " + ex.getType() + '?');
		dialog.setCancelable(false);

		Button ok_button = (Button) dialog.findViewById(R.id.ok_button);
		ok_button.setText("Ok");
		Button cancel_button = (Button) dialog.findViewById(R.id.cancel_button);
		cancel_button.setText("Cancel");
		final EditText duration = (EditText) dialog.findViewById(R.id.duration_mins);
		duration.setText(String.valueOf(ex.getDuration()));
		ok_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String value = duration.getText().toString();
				if(value!=null && !value.equals("")  && !value.equals("0"))
				{
					if(value.length()>=5)
						value = ((String) value).substring(0,4);

					ex.setDuration(Integer.parseInt(value));
					ex.caloriesBurned();
					HistoryAccessor.updateHistory(old_ex, ex,  cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
					adapter.notifyDataSetChanged();		
				}
				dialog.dismiss();
			}
		});
		cancel_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	public void editAlertDialogWeight(final Exercise ex)
	{
		final Exercise old_ex = new Exercise(ex);
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.added_exercises_dialog_weights);
		dialog.setTitle("Edit " + ex.getType() + '?');
		dialog.setCancelable(true);

		Button ok_button = (Button) dialog.findViewById(R.id.ok_button);
		ok_button.setText("Ok");
		Button cancel_button = (Button) dialog.findViewById(R.id.cancel_button);
		cancel_button.setText("Cancel");
		final EditText duration = (EditText) dialog.findViewById(R.id.duration_mins);
		duration.setText(String.valueOf(ex.getDuration()));
		ListView listviewSET = (ListView) dialog.findViewById(R.id.listView);
		final ArrayList<ASet> setArrayList = ((WeightExercise) ex).getSets();

		final SetsAdapter setAdapter = new SetsAdapter(getActivity(), R.layout.set_item, setArrayList);
		listviewSET.setAdapter(setAdapter);

		TextView addset = (TextView) dialog.findViewById(R.id.addset);
		ImageView plus = (ImageView) dialog.findViewById(R.id.plus);
		addset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setArrayList.add(new ASet(0,0));
				setAdapter.notifyDataSetChanged();
			}

		});

		plus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setArrayList.add(new ASet(1,1));
				setAdapter.notifyDataSetChanged();
			}

		});

		ok_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String value = duration.getText().toString();
				if(value!=null && !value.equals("")  && !value.equals("0"))
				{
					if(value.length()>=5)
						value = ((String) value).substring(0,4);

					if(value.length()>=5)
						value = ((String) value).substring(0,4);
					ArrayList<ASet> validSets = new ArrayList<ASet>();
					for(ASet as: setArrayList){
						if(as.getLbs()!=0 && as.getReps()!=0)
							validSets.add(as);
					}
					((WeightExercise) ex).setSets(validSets);
					ex.setDuration(Integer.parseInt(value));
					ex.caloriesBurned();
					HistoryAccessor.updateHistory(old_ex, ex,  cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
					adapter.notifyDataSetChanged();		
				}
				dialog.dismiss();
			}
		});
		cancel_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	public void addAlertDialogWeight(final String exerciseName)
	{
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.added_exercises_dialog_weights);
		dialog.setTitle("Add " + exerciseName + '?');
		dialog.setCancelable(false);

		Button ok_button = (Button) dialog.findViewById(R.id.ok_button);
		ok_button.setText("Ok");
		Button cancel_button = (Button) dialog.findViewById(R.id.cancel_button);
		cancel_button.setText("Cancel");
		final EditText duration = (EditText) dialog.findViewById(R.id.duration_mins);

		ListView listviewSET = (ListView) dialog.findViewById(R.id.listView);
		final ArrayList<ASet> setArrayList = new ArrayList<ASet>();
		final SetsAdapter setAdapter = new SetsAdapter(getActivity(), R.layout.set_item, setArrayList);
		listviewSET.setAdapter(setAdapter);

		TextView addset = (TextView) dialog.findViewById(R.id.addset);
		ImageView plus = (ImageView) dialog.findViewById(R.id.plus);
		addset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setArrayList.add(new ASet(0,0));
				setAdapter.notifyDataSetChanged();
			}

		});

		plus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setArrayList.add(new ASet(1,1));
				setAdapter.notifyDataSetChanged();
			}

		});

		ok_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String value = duration.getText().toString();
				if(value!=null && !value.equals("")  && !value.equals("0"))
				{
					if(value.length()>=5)
						value = ((String) value).substring(0,4);

					if(value.length()>=5)
						value = ((String) value).substring(0,4);
					ArrayList<ASet> validSets = new ArrayList<ASet>();
					for(ASet as: setArrayList){
						if(as.getLbs()!=0 && as.getReps()!=0)
							validSets.add(as);
					}
					Exercise newex = new WeightExercise(exerciseName, Integer.parseInt(value), validSets);
					exerciseList.add(0,newex);
					HistoryAccessor.addHistory(newex, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
					adapter.notifyDataSetChanged();		
				}
				dialog.dismiss();
			}
		});
		cancel_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	public void addAlertDialog(final String exerciseName)
	{
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.added_exercises_dialog);
		dialog.setTitle("Edit " + exerciseName + '?');
		dialog.setCancelable(false);

		Button ok_button = (Button) dialog.findViewById(R.id.ok_button);
		ok_button.setText("Ok");
		Button cancel_button = (Button) dialog.findViewById(R.id.cancel_button);
		cancel_button.setText("Cancel");
		final EditText duration = (EditText) dialog.findViewById(R.id.duration_mins);

		ok_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String value = duration.getText().toString();
				if(value!=null && !value.equals("") && !value.equals("0"))
				{
					if(value.length()>=5)
						value = ((String) value).substring(0,4);
					Exercise newex = new Exercise(exerciseName, Integer.parseInt(value));
					exerciseList.add(0,newex);
					HistoryAccessor.addHistory(newex, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
					adapter.notifyDataSetChanged();	
				}
				dialog.dismiss();
			}
		});
		cancel_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
}