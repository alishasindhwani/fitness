package transapps.gpxfitness.ui;

import java.util.ArrayList;
import java.util.Map;

import transapps.gpxfitness.R;
import transapps.gpxfitness.db.METAccessor;
import transapps.gpxfitness.obj.ASet;
import transapps.gpxfitness.obj.Exercise;
import transapps.gpxfitness.obj.WeightExercise;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AddedExercisesAdapter extends ArrayAdapter<Exercise> {
	Context context;
	int rowResourceId;
	private Map<String, Integer> iconCollection;
	ArrayList<Exercise> data;

	public AddedExercisesAdapter(Context context, int rowResourceId, ArrayList<Exercise> data, Map<String, Integer> iconCollection)
	{
		super(context, rowResourceId, data);
		this.context = context;
		this.rowResourceId=rowResourceId;
		this.data = data;
		this.iconCollection=iconCollection;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(rowResourceId, parent, false);
		Exercise ex_obj = data.get(position);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.category_img_added);
		TextView category = (TextView) rowView.findViewById(R.id.category_added);
		TextView exercise = (TextView) rowView.findViewById(R.id.exercise_added);
		TextView minutes = (TextView) rowView.findViewById(R.id.minutes_added);
		TextView calories = (TextView) rowView.findViewById(R.id.calories_burned);
		TextView sets = (TextView) rowView.findViewById(R.id.Sets);
		exercise.setText(ex_obj.getType());
		String category_text = METAccessor.getCategory(ex_obj.getType());
		category_text= category_text.substring(0,1).toUpperCase() + category_text.substring(1);
		category.setText(category_text);
		minutes.setText(ex_obj.getDuration() + " mins");
		calories.setText(ex_obj.caloriesBurned() + " Calories");
		imageView.setImageResource(iconCollection.get(category_text));
		
		if(ex_obj instanceof WeightExercise)
		{
			 ArrayList<ASet> set_arraylist = ((WeightExercise) ex_obj).getSets();
			 sets.setText("");
			 for(int i=0; i<set_arraylist.size(); i++){
				 ASet a = set_arraylist.get(i);
				 sets.append(a.getReps() + " reps at " + a.getLbs() + " lbs");
				 if(i!=set_arraylist.size()-1)
					 sets.append("\n");
			 }
			 
		}
		else
			sets.setVisibility(View.GONE);
		return rowView;
	}
	
	public Exercise getExerciseItem(int position)
	{
		return data.get(position);
	}
	public void deleteExerciseItem(int position)
	{
		data.remove(position);
	}
}