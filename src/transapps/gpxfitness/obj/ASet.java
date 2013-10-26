package transapps.gpxfitness.obj;

import java.util.ArrayList;

public class ASet{
	private ArrayList<ArrayList<Integer>> sets = new ArrayList<ArrayList<Integer>>();

	private int reps = 0;
	private int lbs = 0;
	
	public ASet(int reps, int lbs) {
		this.reps = reps;
		this.lbs = lbs;
	}
	
	public void setReps(int reps)
	{
		this.reps = reps;
	}
	
	public void setLbs(int lbs)
	{
		this.lbs = lbs;
	}
	public int getReps()
	{
		return reps;
	}
	public int getLbs()
	{
		return lbs;
	}
}
