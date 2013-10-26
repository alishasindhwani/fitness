package transapps.gpxfitness.ui;

import java.util.Calendar;
import java.util.HashMap;

import transapps.gpxfitness.R;
import transapps.gpxfitness.db.HistoryAccessor;
import transapps.gpxfitness.obj.Exercise;
import transapps.gpxfitness.piechart.PieChart;
import transapps.gpxfitness.piechart.PieChart.OnSelectedLisenter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DailySummaryFragment extends Fragment {
	public static final String ARG_SECTION_NUMBER = "section_number";
	
	public String stringDate(Calendar now) {
		int dow = now.get(Calendar.DAY_OF_WEEK);
		String week = "";
		switch(dow) {
		case Calendar.MONDAY: week="Monday, ";
		break;
		case Calendar.TUESDAY: week="Tuesday, ";
		break;
		case Calendar.WEDNESDAY: week="Wednesday, ";
		break;
		case Calendar.THURSDAY: week="Thursday, ";
		break;
		case Calendar.FRIDAY: week="Friday, ";
		break;
		case Calendar.SATURDAY: week="Saturday, ";
		break;
		case Calendar.SUNDAY: week="Sunday, ";
		break;
		}
		int mon = now.get(Calendar.MONTH);
		String month = "";
		switch(mon) {
		case Calendar.JANUARY: month="January ";
		break;
		case Calendar.FEBRUARY: month="February ";
		break;
		case Calendar.MARCH: month="March ";
		break;
		case Calendar.APRIL: month="April ";
		break;
		case Calendar.MAY: month="May ";
		break;
		case Calendar.JUNE: month="June ";
		break;
		case Calendar.JULY: month="July ";
		break;
		case Calendar.AUGUST: month="August ";
		break;
		case Calendar.SEPTEMBER: month="September ";
		break;
		case Calendar.OCTOBER: month="October ";
		break;
		case Calendar.NOVEMBER: month="November ";
		break;
		case Calendar.DECEMBER: month="December ";
		break;
		}
		return week+month+now.get(Calendar.DAY_OF_MONTH)+", "+now.get(Calendar.YEAR);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_daily_summary, container, false);
        Bundle bundle = getArguments(); 
        Calendar now = Calendar.getInstance();
        if (bundle!=null && bundle.getInt("year")>0 && bundle.getInt("month")>0 && bundle.getInt("day")>0) {
        	now.set(bundle.getInt("year"), bundle.getInt("month")-1, bundle.getInt("day"));	
        }

		Log.d("TEXT", "ON CREATE WAS CALLED");
		final TextView category = (TextView) rootView.findViewById(R.id.category);
		final TextView selected = (TextView) rootView.findViewById(android.R.id.text1);

		final TextView today = (TextView) rootView.findViewById(R.id.workout_summary);
		String date = stringDate(now);
		today.setText(date);

		final TextView cals = (TextView) rootView.findViewById(R.id.calories_burned);
		int burned = HistoryAccessor.getTotalCalories(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
		cals.setText(burned + " Calories Burned");

		final PieChart pie = (PieChart) rootView.findViewById(R.id.pieChart);

		Exercise[] exercises = HistoryAccessor.getHistory(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
		int totalCals = HistoryAccessor.getTotalCalories(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
		HashMap<Exercise, Float> dat = new HashMap<Exercise, Float>();

		if (exercises==null) dat=null;
		else {
			for (Exercise e: exercises) {
				Log.i("PIE", "getting calories for exercise "+e.getType());
				dat.put(e, ((float) (100*e.caloriesBurned()/(1.0*totalCals))));
				Log.i("PIE", "adding percentage: "+(100*e.caloriesBurned()/(1.0*totalCals)));
			}
		}

		try {
			// setting data
			pie.setAdapter(dat);

			// setting a listener 
			pie.setOnSelectedListener(new OnSelectedLisenter() {
				@Override
				public void onSelected(Exercise iSelectedExercise) {
					if(iSelectedExercise!=null)
					{
						if (!iSelectedExercise.getType().equals("empty")) {
							category.setText(iSelectedExercise.getCategory()+": "+iSelectedExercise.getType());
							selected.setText(iSelectedExercise.getDuration()+" minutes\n"+iSelectedExercise.caloriesBurned()+" calories burned");

						}
						else selected.setText("no exercises have been added");
					}
					else {
						selected.setText("");
						category.setText("");
					}
				}
			});  
		} catch (Exception e) {
			if (e.getMessage().equals(PieChart.ERROR_NOT_EQUAL_TO_100)){
				Log.e("kenyang","percentage is not equal to 100");
			}
		}
		return rootView;

	}
}