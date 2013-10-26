package transapps.gpxfitness.ui;

import java.util.List;
import java.util.Map;

import transapps.gpxfitness.R;
import transapps.gpxfitness.db.FavoritesAccessor;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Activity context;
	private Map<String, List<String>> exercises;
	private List<String> categories;
	private Map<String, Integer> icons;

	public ExpandableListAdapter(Activity context, List<String> categories,
			Map<String, List<String>> exercises, Map<String, Integer> icons) {
		this.context = context;
		this.exercises = exercises;
		this.categories = categories;
		this.icons = icons;
	}

	public Object getChild(int groupPosition, int childPosition) {
		return exercises.get(categories.get(groupPosition)).get(childPosition);
	}
	public int getIcon(int groupPosition) {
		return icons.get(categories.get(groupPosition));
	}
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final String exercise_name = (String) getChild(groupPosition, childPosition);
		LayoutInflater inflater = context.getLayoutInflater();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.child_item, null);
		}

		TextView exercise = (TextView) convertView.findViewById(R.id.exercise);
		//Typeface tf = Typeface.createFromAsset(context.getAssets(),"SourceSansPro-Regular.ttf");

		//item.setTypeface(tf);
		final ImageView star = (ImageView) convertView.findViewById(R.id.star);
		if(FavoritesAccessor.isFavorite(exercise_name))
		{
			star.setImageResource(R.drawable.ic_star);
		}
		else{
			star.setImageResource(R.drawable.ic_star_outline);
		}
		star.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if(FavoritesAccessor.isFavorite(exercise_name))
				{
					star.setImageResource(R.drawable.ic_star_outline);
					FavoritesAccessor.removeFavorite(exercise_name);
				}
				else{
					star.setImageResource(R.drawable.ic_star);
					FavoritesAccessor.addFavorite(exercise_name);
				}
			}
		});

		exercise.setText(exercise_name);
		return convertView;
	}

	public int getChildrenCount(int groupPosition) {
		return exercises.get(categories.get(groupPosition)).size();
	}

	public Object getGroup(int groupPosition) {
		return categories.get(groupPosition);
	}

	public int getGroupCount() {
		return categories.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String category_name = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.group_item,
					null);
		}

		TextView category = (TextView) convertView.findViewById(R.id.category);
		ImageView category_img = (ImageView) convertView.findViewById(R.id.category_img);
		category.setText(category_name);
		category_img.setImageResource(getIcon(groupPosition));
		return convertView;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}