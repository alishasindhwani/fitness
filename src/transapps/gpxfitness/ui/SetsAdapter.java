package transapps.gpxfitness.ui;

import java.util.ArrayList;

import transapps.gpxfitness.R;
import transapps.gpxfitness.obj.ASet;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

public class SetsAdapter extends ArrayAdapter<ASet> {
	Context context;
	int rowResourceId;
	ArrayList<ASet> data;

	public SetsAdapter(Context context, int rowResourceId, ArrayList<ASet> data)
	{
		super(context, rowResourceId, data);
		this.context = context;
		this.rowResourceId=rowResourceId;
		this.data=data;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(rowResourceId, parent, false);
		final ASet sets = data.get(position);
		EditText et_reps = (EditText) rowView.findViewById(R.id.reps_ct);
		if(sets.getReps()!=0)
			et_reps.setText(String.valueOf(sets.getReps()));
		
		et_reps.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            String reps_str = s.toString();
            if(reps_str!=null && reps_str.length()!=0 && !reps_str.equals(""))
            	sets.setReps(Integer.parseInt(reps_str));
            }
        });
		EditText et_lbs = (EditText) rowView.findViewById(R.id.lbs_ct);
		if(sets.getLbs()!=0)
			et_lbs.setText(String.valueOf(sets.getLbs()));
		et_lbs.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            String lbs_str = s.toString();
            if(lbs_str!=null && lbs_str.length()!=0 && !lbs_str.equals(""))
            	sets.setLbs(Integer.parseInt(lbs_str));
            }
        });
		
		return rowView;
	}
}