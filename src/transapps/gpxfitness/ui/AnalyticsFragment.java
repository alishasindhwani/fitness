package transapps.gpxfitness.ui;

import java.util.Calendar;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import transapps.gpxfitness.R;
import transapps.gpxfitness.db.HistoryAccessor;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class AnalyticsFragment extends Fragment {
	private int max=0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.analytics_layout, container, false);
		LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.chart);
		GraphicalView l = ChartFactory.getLineChartView(getActivity(), getDemoDataset(), getDemoRenderer());
		layout.addView(l);
		return rootView;
	}

	private XYMultipleSeriesDataset getDemoDataset() {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		final int nr = 30;
		Calendar cal = Calendar.getInstance();

		int month = cal.get(Calendar.MONTH) +1;
		int daysInMonth = 30;
		int year = cal.get(Calendar.YEAR);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		boolean leapYear = false;
		if(year%4==0 && year%100!=0 || year%4==0 && year%400==0)
			leapYear = true;
		if (month == 4 || month == 6 || month == 9 || month == 11)
			daysInMonth = 30;
		else 
			if (month == 2) 
				daysInMonth = (leapYear) ? 29 : 28;
			else 
				daysInMonth = 31;
		for (int i = 0; i <1 ; i++) {
	        XYSeries series = new XYSeries("");
	        for (int k = 1; k <= day; k++) {
	        	int calories = HistoryAccessor.getTotalCalories(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), k);
	            series.add(k, calories);
	            if(calories>max)
	            	max = calories;
	        }
	        dataset.addSeries(series);
	    }
		return dataset;
	}

	private XYMultipleSeriesRenderer getDemoRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(30);
		renderer.setChartTitleTextSize(50);
		renderer.setLabelsTextSize(30);
		renderer.setLegendTextSize(30);
		renderer.setPointSize(5f);
		renderer.setShowLegend(false);
		int[] margins = renderer.getMargins();
		margins[0]=(int) renderer.getLabelsTextSize() + 20;
		margins[1]=(int) renderer.getLabelsTextSize() + 20;
		margins[2] = (int) renderer.getLabelsTextSize() + 20;
		margins[3] = (int) renderer.getLabelsTextSize() + 20;
		renderer.setMargins(margins);
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.parseColor("#FF71B544"));
		r.setPointStyle(PointStyle.CIRCLE);
		r.setFillPoints(true);
		renderer.addSeriesRenderer(r);
		setChartSettings(renderer);
		return renderer;
	}
	private void setChartSettings(XYMultipleSeriesRenderer renderer) {
		renderer.setChartTitle("Monthly Calories");
		renderer.setXTitle("Date");
		renderer.setYTitle("Calories");
		renderer.setXLabels(10);
		renderer.setXLabelsPadding(10);
		renderer.setYLabelsPadding(20);
		renderer.setYLabelsAlign(Align.LEFT);
		renderer.setApplyBackgroundColor(false);
		renderer.setRange(new double[] {0,30,0,max+20});
		renderer.setFitLegend(true);
		renderer.setAxesColor(Color.BLACK);
		renderer.setShowGrid(true);
		renderer.setPanEnabled(false, false);
		renderer.setZoomRate(0.2f);
		renderer.setZoomEnabled(false, false);
	}
}