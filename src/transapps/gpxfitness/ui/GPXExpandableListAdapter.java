package transapps.gpxfitness.ui;

import java.util.List;

import transapps.gpxfitness.R;
import transapps.gpxfitness.gpx.GPXInfo;
import transapps.gpxfitness.obj.GPSExercise;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class GPXExpandableListAdapter extends BaseExpandableListAdapter {

	private Activity context;
	private List<GPSExercise> tracks;

	public GPXExpandableListAdapter(Activity context, List<GPSExercise> tracks) {
		this.context = context;
		this.tracks = tracks;
	}

	public Object getChild(int groupPosition, int childPosition) {
		return tracks.get(groupPosition).getInfo();
	}
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		//final String exercise_name = (String) getChild(groupPosition, childPosition);
		LayoutInflater inflater = context.getLayoutInflater();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.gpx_info, null);
		}
		final GPXInfo info = (GPXInfo) getChild(groupPosition, childPosition);

		TextView duration_txt_value = (TextView) convertView.findViewById(R.id.duration_txt_value);
		duration_txt_value.setText(info.getDuration() + " mins");
		
		TextView distance_txt_value = (TextView) convertView.findViewById(R.id.distance_txt_value);
		distance_txt_value.setText(Math.floor(info.getTotalDist() * 100) / 100 + " mi");
		
		TextView avg_txt_value = (TextView) convertView.findViewById(R.id.avgspeed_txt_value);
		avg_txt_value.setText(Math.floor(info.getAverageSpeed() * 100) / 100 + " mph");
		
		TextView maxspeed_txt_value = (TextView) convertView.findViewById(R.id.maxspeed_txt_value);
		maxspeed_txt_value.setText(Math.floor(info.getMaxSpeed() * 100) / 100 + " mph");
		
		TextView pace_txt_value = (TextView) convertView.findViewById(R.id.pace_txt_value);
		pace_txt_value.setText(Math.floor(info.getAveragePace() * 100) / 100 + " mins/mi");
		
		TextView maxelevation_txt_value = (TextView) convertView.findViewById(R.id.maxelevation_txt_value);
		maxelevation_txt_value.setText(info.getMaxElevation() + " m");
		
		TextView minelevation_txt_value = (TextView) convertView.findViewById(R.id.minelevation_txt_value);
		minelevation_txt_value.setText(info.getMinElevation() + " m");
		
		TextView grade_txt_value = (TextView) convertView.findViewById(R.id.grade_txt_value);
		grade_txt_value.setText(Math.floor(info.getAverageGrade() * 100) / 100 + " %");
		return convertView;
	}

	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	public Object getGroup(int groupPosition) {
		return tracks.get(groupPosition);
	}

	public int getGroupCount() {
		return tracks.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GPSExercise gpsex = (GPSExercise) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.gpx_info_title,
					null);
		}

		TextView title = (TextView) convertView.findViewById(R.id.gpx_title);
		title.setText("GPS Analysis from " + gpsex.getStartTime() + " to " + gpsex.getEndTime());
		//category_img.setImageResource(getIcon(groupPosition));
		return convertView;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
}