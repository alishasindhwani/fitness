package transapps.gpxfitness.obj;

import transapps.gpxfitness.db.METAccessor;

import transapps.gpxfitness.gpx.GPXInfo;

public class GPSExercise extends Exercise {
	private GPXInfo info;
	private String startTime="";
	private String endTime="";
	public GPSExercise(GPXInfo i) {
		super("GPS", i.getDuration());
		this.info = i;
		// TODO Auto-generated constructor stub
	}
		
	@Override
	public int caloriesBurned() {
		return info.getCaloriesBurned();
	}
	
	public void setStartTime(String start)
	{
		startTime = start;
	}
	public void setEndTime(String end)
	{
		endTime = end;
	}
	public String getStartTime()
	{
		return startTime;
	}
	public String getEndTime()
	{
		return endTime;
	}
	public GPXInfo getInfo() {
		return this.info;
	}
	
	@Override
	public String getCategory() {
		return "GPS";
	}
}
