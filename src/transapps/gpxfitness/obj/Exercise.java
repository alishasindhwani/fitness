package transapps.gpxfitness.obj;

import java.util.Calendar;

import transapps.gpxfitness.db.METAccessor;
import transapps.gpxfitness.db.ProfileAccessor;

public class Exercise {
	private int duration; //in minutes
	private String type; //one of our MET exercises
	private long timehash;
	private Calendar cal = Calendar.getInstance();
	
	public Exercise(String type, int duration) {
		this.duration = duration;
		this.type = type;
		this.timehash = cal.getTime().getTime();
	}
	
	public Exercise(Exercise ex)
	{
		this.duration = ex.getDuration();
		this.type = ex.getType();
		this.timehash = ex.getTimehash();
	}
	
	public long getTimehash() {
		return this.timehash;
	}
	
	public void setTimehash(long hash) {
		this.timehash = hash; //NOTE: this method should ONLY be called when constructing from sqlite fields
	}
	
	public void setDuration(int duration)
	{
		this.duration = duration;
	}
	
	public int getDuration() {
		return this.duration;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getCategory() {
		String s = METAccessor.getCategory(this.type);
		if (s==null) return null;
		s = s.substring(0,1).toUpperCase() + s.substring(1);
		return s; 
	}
	
	public int caloriesBurned() {
		double met = METAccessor.getMET(type);
		double weightInPounds = ProfileAccessor.getWeight();
		double weightInKilos = weightInPounds*0.453592;
		double calories = (met * 3.5 * weightInKilos * this.duration) / 200; //met calorie-burning formula
		return (int) (calories+.5);
	}
}
