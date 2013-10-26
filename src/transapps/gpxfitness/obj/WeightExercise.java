package transapps.gpxfitness.obj;

import java.util.ArrayList;


public class WeightExercise extends Exercise {
	private ArrayList<ASet> sets = new ArrayList<ASet>();
	
	public WeightExercise(String type, int duration, ArrayList<ASet> sets) {
		super(type, duration);
		this.sets = sets;
	}
	
	public ArrayList<ASet> getSets() {
		if (this.sets.size()==0) return null;
		return this.sets;
	}
	public void setSets(ArrayList<ASet> sets)
	{
		this.sets = sets;
	}
	
	public void addSet(int weight, int reps) {
		ASet newSet = new ASet(reps, weight);
		this.sets.add(newSet);
	}
	
	public boolean deleteSet(int weight, int reps) {
		for (int i=0; i<this.sets.size(); i++) {
			ASet set = this.sets.get(i);
			if (set.getLbs()==weight && set.getReps()==reps) {
				this.sets.remove(i);
				return true;
			}
		}
		return false;
	}
}
