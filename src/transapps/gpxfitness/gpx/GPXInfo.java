package transapps.gpxfitness.gpx;

public class GPXInfo {
	private double totalDist; //in miles
	private int caloriesBurned;
	private double maxSpeed; //in mph
	private double maxElev; //in meters above sea level
	private double minElev; //in meters above sea level
	private double avgGrade; //in degrees? percent?
	private int duration; //in minutes
	
	public GPXInfo() {
		this.totalDist = 0;
		this.caloriesBurned = 0;
		this.maxSpeed = 0;
		this.maxElev = 0;
		this.minElev = 0;
		this.avgGrade = 0;
		this.duration = 0;
	}
	
	public GPXInfo(double dist, int cals, double speed, double max, double min, double grade, int dur) {
		this.totalDist = dist;
		this.caloriesBurned = cals;
		this.maxSpeed = speed;
		this.maxElev = max;
		this.minElev = min;
		this.avgGrade = grade;
		this.duration = dur;
	}
	
	public void addToDistance(double miles) {
		this.totalDist += miles;
	}
	
	public void addToBurnedCalories(int calories) {
		this.caloriesBurned += calories;
	}
	
	public void setBurnedCalories(int calories) {
		this.caloriesBurned = calories;
	}
	
	public void setMaxSpeed(double mph) {
		this.maxSpeed = mph;
	}
	
	public void setMaxElevation(double meters) {
		this.maxElev = meters;
	}
	
	public void setMinElevation(double meters) {
		this.minElev = meters;
	}
	
	public void setAverageGrade(double grade) {
		this.avgGrade = grade;
	}
	
	public void setDuration(int minutes) {
		this.duration = minutes;
	}
	
	public double getTotalDist() {
		return this.totalDist;
	}
	
	public int getCaloriesBurned() {
		return this.caloriesBurned;
	}
	
	public double getAverageSpeed() {
		return this.totalDist/(this.duration/60.0);
	}
	
	public double getMaxSpeed() {
		return this.maxSpeed;
	}
	
	public double getAveragePace() {
		return this.duration/this.totalDist;
	}
	
	public double getMaxElevation() {
		return this.maxElev;
	}
	
	public double getMinElevation() {
		return this.minElev;
	}
	
	public double getAverageGrade() {
		return this.avgGrade;
	}
	
	public int getDuration() {
		return this.duration;
	}
}
