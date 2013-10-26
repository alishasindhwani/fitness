package transapps.gpxfitness.db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import transapps.gpxfitness.gpx.GPXInfo;
import transapps.gpxfitness.obj.ASet;
import transapps.gpxfitness.obj.Exercise;
import transapps.gpxfitness.obj.GPSExercise;
import transapps.gpxfitness.obj.WeightExercise;
import transapps.pli.helper.TimeConversion;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class HistoryAccessor {
	private static boolean created = false;
	private static final int empty = -666;
	private static final Calendar cal = Calendar.getInstance();

	private static void createHistoryList() {
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		fitDB.execSQL("create table if not exists history (exercise text, date int, duration int, " +
				"calories int, reps int, weight int," +
				"GPXdistance real, GPXmaxspeed real, GPXmaxelev real, GPXminelev real, GPXavggrade real," +
				"timehash int, GPXstart text, GPXend text)");
		fitDB.execSQL("create table if not exists dateslist (date text, totalcalories int)");
		fitDB.close();
		created = true;
	}
	
	public static void addHistory(Exercise exercise, int year, int month, int day) {
		if (exercise instanceof GPSExercise) addGPXHistory(((GPSExercise) exercise).getInfo(), year, month, day, exercise.getTimehash(),
				((GPSExercise) exercise).getStartTime(), ((GPSExercise) exercise).getEndTime());
		else if (exercise instanceof WeightExercise) addWeightsHistory(exercise.getType(), exercise.getDuration(),
				exercise.caloriesBurned(), ((WeightExercise) exercise).getSets(), 
				year, month, day, exercise.getTimehash());
		else addBasicHistory(exercise.getType(), exercise.getDuration(), exercise.caloriesBurned(), year, month, day, exercise.getTimehash());
	}

	private static void addGPXHistory(GPXInfo info, int year, int month, int day, long timehash, String start, String end) {
		if (!created) createHistoryList();
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		ContentValues cv = new ContentValues();
		cv.put("exercise", "GPS");
		cv.put("date", toUTCMillis(year, month, day)); 
		cv.put("duration", info.getDuration());
		cv.put("calories", info.getCaloriesBurned());
		cv.put("GPXdistance", info.getTotalDist());
		cv.put("GPXmaxspeed", info.getMaxSpeed());
		cv.put("GPXmaxelev", info.getMaxElevation());
		cv.put("GPXminelev", info.getMinElevation());
		cv.put("GPXavggrade", info.getAverageGrade());
		cv.put("timehash", timehash);
		cv.put("GPXstart", start);
		cv.put("GPXend", end);
		
		//we don't use the 'reps' or 'weight' column, so use the empty field code
		cv.put("reps", empty); 
		cv.put("weight", empty);

		long code = fitDB.insert("history", null, cv);	
		if (code==-1) Log.e("History Accessor", "Failed to insert GPS History!");
		fitDB.close();
		addToTodaysCalories(info.getCaloriesBurned(), year, month, day);
	}

	
	private static void addWeightsHistory(String exercise, int durationInMinutes, int calories, 
			ArrayList<ASet> sets, int year, int month, int day, long timehash) {
		if (!created) createHistoryList();
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		for (ASet set : sets) {
			ContentValues cv = new ContentValues();
			cv.put("exercise", exercise);
			cv.put("date", toUTCMillis(year, month, day)); 
			cv.put("duration", durationInMinutes);
			cv.put("calories", calories);
			cv.put("reps", set.getReps());
			cv.put("weight", set.getLbs());
			cv.put("timehash", timehash);
	
			
			//we don't use the gpx fields, so use the empty field code
			cv.put("GPXdistance", empty);
			cv.put("GPXmaxspeed", empty);
			cv.put("GPXmaxelev", empty);
			cv.put("GPXminelev", empty);
			cv.put("GPXavggrade", empty);
			cv.put("GPXstart", empty);
			cv.put("GPXend", empty);
			
			long code = fitDB.insert("history", null, cv);
			if (code==-1) Log.e("History Accessor", "Failed to insert weight History!");
		}
		fitDB.close();
		addToTodaysCalories(calories, year, month, day);
	}
	
	private static void addBasicHistory(String exercise, int durationInMinutes, int calories, int year, int month, 
			int day, long timehash) {
		if (!created) createHistoryList();
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		ContentValues cv = new ContentValues();
		cv.put("exercise", exercise);
		cv.put("date", toUTCMillis(year, month, day)); 
		cv.put("duration", durationInMinutes);
		cv.put("calories", calories);
		cv.put("timehash", timehash);

		
		//we don't use the gpx fields, reps, or weight, so use the empty field code
		cv.put("reps", empty);
		cv.put("weight", empty);
		cv.put("GPXdistance", empty);
		cv.put("GPXmaxspeed", empty);
		cv.put("GPXmaxelev", empty);
		cv.put("GPXminelev", empty);
		cv.put("GPXavggrade", empty);
		cv.put("GPXstart", empty);
		cv.put("GPXend", empty);

		long code = fitDB.insert("history", null, cv);
		if (code==-1) Log.e("History Accessor", "Failed to insert basic History!");

		fitDB.close();
		addToTodaysCalories(calories, year, month, day);
	}
	
	public static boolean updateHistory(Exercise old, Exercise updated, int year, int month, int day) {
		if (old instanceof WeightExercise && updated instanceof WeightExercise) {
			Log.i("prettyDate", "time to update a WeightExercise");
			return updateWeightHistory((WeightExercise)old, (WeightExercise)updated, year, month, day);
		}
		else if (!(old instanceof GPSExercise || updated instanceof GPSExercise)) {
			Log.i("prettyDate", "time to update a basic exercise");
			return updateBasicHistory(old, updated, year, month, day);
		}
		else {
			Log.i("prettyDate", "WAAAAT");
			return false;
		}
	}
	
	private static boolean updateWeightHistory(WeightExercise old, WeightExercise updated, int year, int month, int day) {
		deleteWeightHistory(old, year, month, day);
		if (updated.getSets()!=null){
				addWeightsHistory(updated.getType(), updated.getDuration(),
					updated.caloriesBurned(), updated.getSets(), 
					year, month, day, updated.getTimehash());
			return true;
		}
		return false;
	}
	
	private static boolean updateBasicHistory(Exercise old, Exercise updated, int year, int month, int day) {
		if (!created) createHistoryList();
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		Cursor c = fitDB.query("history", new String[] {"exercise"}, 
				"timehash = ?", new String[] {String.valueOf(old.getTimehash())}, null, null, null, "1");
		if (c==null || !c.moveToFirst()) {
			Log.i("prettyDate", "ERROR: couldn't find this basic history exercise with timehash "+old.getTimehash());
			c.close();
			fitDB.close();
			return false;
		}
		c.close();
	
		ContentValues changes = new ContentValues();
		changes.put("duration", updated.getDuration());
		changes.put("calories", updated.caloriesBurned());
		
		int code = fitDB.update("history", changes, "timehash = ?", new String[] {String.valueOf(old.getTimehash())});
		Log.i("prettyDate", "updated exercise! "+code+" rows changed?");
		fitDB.close();
		addToTodaysCalories(updated.caloriesBurned()-old.caloriesBurned(), year, month, day);
		return true;
	}
	
	private static void addToTodaysCalories(int newCalories, int year, int month, int day) {
		if (!created) createHistoryList();
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		Cursor c = fitDB.query("dateslist", new String[] {"date", "totalcalories"}, "date = ?", 
				new String[] {String.valueOf(toUTCMillis(year, month, day))}, "date", null, null, "1");
		ContentValues newTotal = new ContentValues();
		newTotal.put("date", toUTCMillis(year, month, day));
		if (c!=null && c.moveToFirst()) { //there exists an entry for this date, update it
			Log.i("prettyDate", "update an existing day");
			int currentCalories = c.getInt(1);
			newTotal.put("totalcalories", (currentCalories+newCalories));
			int code = fitDB.update("dateslist", newTotal, "date = ?", new String[] {String.valueOf(toUTCMillis(year, month, day))});
			Log.i("prettyDate", "adding calories "+newCalories+", "+code+" rows updated?");
		}
		else { //there is no entry yet for this date, make one
			Log.i("prettyDate", "adding calories to a new day");
			newTotal.put("totalcalories", newCalories);
			fitDB.insert("dateslist", null, newTotal);
		}
		c.close();
		fitDB.close();
	}
	
	public static void deleteHistory(Exercise exercise, int year, int month, int day) {
		Log.i("prettyDate", "preparing to delete an exercise");
		if (exercise instanceof GPSExercise) deleteGPXHistory((GPSExercise)exercise, year, month, day);
		else if (exercise instanceof WeightExercise) deleteWeightHistory((WeightExercise)exercise, year, month, day);
		else deleteBasicHistory(exercise, year, month, day);
	}
	
	private static void deleteBasicHistory(Exercise old, int year, int month, int day) {
		if (!created) createHistoryList();
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		int code = fitDB.delete("history", "timehash = ?", new String[] {String.valueOf(old.getTimehash())});
		Log.i("prettyDate", "trying to delete basic ex with timehash "+old.getTimehash()+", deleted "+code+" rows?");
		fitDB.close();
		addToTodaysCalories(-old.caloriesBurned(), year, month, day);
	}
	
	private static void deleteWeightHistory(WeightExercise old, int year, int month, int day) {
		if (!created) createHistoryList();
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		int code = fitDB.delete("history", "timehash = ?", new String[] {String.valueOf(old.getTimehash())});
		Log.i("prettyDate", "trying to delete weight ex with timehash "+old.getTimehash()+", deleted "+code+" rows?");
		fitDB.close();
		addToTodaysCalories(-old.caloriesBurned(), year, month, day);
	}
	
	private static void deleteGPXHistory(GPSExercise old, int year, int month, int day) {
		if (!created) createHistoryList();
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		int code = fitDB.delete("history", "timehash = ?", new String[] {String.valueOf(old.getTimehash())});
		Log.i("prettyDate", "trying to delete gps ex with timehash "+old.getTimehash()+", deleted "+code+" rows?");
		fitDB.close();
		addToTodaysCalories(-old.caloriesBurned(), year, month, day);
	}
	
	public static Exercise[] getBasicHistory(int year, int month, int day) {
		Exercise[] all = getHistory(year, month, day);
		ArrayList<Exercise> basic = new ArrayList<Exercise>();
		if (all==null) return null;
		for (Exercise e : all) {
			if (!(e instanceof GPSExercise)) basic.add(e);
		}
		return basic.toArray(new Exercise[0]);
	}
	
	public static GPSExercise[] getGPXHistory(int year, int month, int day) {
		Exercise[] all = getHistory(year, month, day);
		if (all==null) return null;
		ArrayList<GPSExercise> gpx = new ArrayList<GPSExercise>();
		for (Exercise e : all) {
			if ((e instanceof GPSExercise)) gpx.add((GPSExercise) e);
		}
		return gpx.toArray(new GPSExercise[0]);
	}
	
	public static Exercise[] getHistory(int year, int month, int day) {
		if (!created) createHistoryList();
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		String[] columns = new String[] {"exercise", "duration", "calories", "reps", "weight", 
				"GPXdistance", "GPXmaxspeed", "GPXmaxelev", "GPXminelev", "GPXavggrade", "timehash", "GPXstart", "GPXend"};
		Cursor c = fitDB.query("history", columns, "date = ?", new String[] {String.valueOf(toUTCMillis(year, month, day))}, null, null, null);
		if (c==null || !c.moveToFirst()) {
			c.close();
			fitDB.close();
			return null;
		}
		ArrayList<Exercise> hist = new ArrayList<Exercise>();
		HashMap<Long, WeightExercise> weights = new HashMap<Long, WeightExercise>();

		do {
			String type = c.getString(0);
			int duration = c.getInt(1);
			int calories = c.getInt(2);
			int reps = c.getInt(3);
			int weight = c.getInt(4);
			double GPXdist = c.getDouble(5);
			double GPXmaxspeed = c.getDouble(6);
			double GPXmaxelev = c.getDouble(7);
			double GPXminelev = c.getDouble(8);
			double GPXgrade = c.getDouble(9);
			long timehash = c.getLong(10);
			String GPXstart = c.getString(11);
			String GPXend = c.getString(12);
			
			if (type.equals("GPS")) { //this is a gps-analysis exercise
				if (!(GPXdist==empty || GPXmaxspeed==empty || GPXmaxelev==empty 
						|| GPXminelev==empty || GPXgrade==empty || GPXstart=="-666" || GPXend=="-666" ))  {
					GPXInfo info = new GPXInfo();
					info.setDuration(duration);
					info.setMaxElevation(GPXmaxelev);
					info.setMinElevation(GPXminelev);
					info.setMaxSpeed(GPXmaxspeed);
					info.setAverageGrade(GPXgrade);
					info.setBurnedCalories(calories);
					GPSExercise ex = new GPSExercise(info);
					ex.setStartTime(GPXstart);
					ex.setEndTime(GPXend);
					ex.setTimehash(timehash);
					hist.add(ex);
					}
			}
			else if (reps!=empty && weight!=empty) { //this is a weightlifting exercise
				ASet set = new ASet(reps, weight);
				if (weights.containsKey(timehash)) {
					//this set belongs to an existing exercise
					weights.get(timehash).addSet(weight, reps);
				}
				else { //make a new exercise with this set
					ArrayList<ASet> sets = new ArrayList<ASet>();
					sets.add(set);
					WeightExercise w = new WeightExercise(type, duration, sets);
					w.setTimehash(timehash);
					weights.put(timehash, w);
					hist.add(w);
				}
			}
			else { //this is a basic exercise, ignore all extra fields
				Exercise e = new Exercise(type, duration);
				e.setTimehash(timehash);
				hist.add(e);
			}
		} while (c.moveToNext());
		c.close();
		fitDB.close();
		return hist.toArray(new Exercise[0]);
	}
	
	public static String[][] getDatesList() {
		if (!created) createHistoryList();
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		Cursor c = fitDB.query("dateslist", new String[] {"date", "totalcalories"}, null, null, null, null, "date", "100");
		if (c==null || !c.moveToFirst()) {
			c.close();
			fitDB.close();
			return null; //no history exists yet
		}
		String[][] days = new String[c.getCount()][2];
		int pos = 0;
		do {
			days[pos] = new String[] {prettyDate(c.getLong(0)), Integer.toString(c.getInt(1))};
			pos++;
		} while (c.moveToNext());
		c.close();
		fitDB.close();
		return days;
	}
	
	public static int getTotalCalories(int year, int month, int day) {
		if (!created) createHistoryList();
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		Cursor c = fitDB.query("dateslist", new String[] {"totalcalories"}, "date = ?", 
				new String[] {String.valueOf(toUTCMillis(year, month, day))}, null, null, "date", "100");
		if (c==null || !c.moveToFirst()) {
			c.close();
			fitDB.close();
			return 0; //no history exists yet
		}
		int cals = c.getInt(0);
		c.close();
		fitDB.close();
		return cals;
	}
	
	public static long toUTCMillis(int year, int month, int day) {
		Calendar temp = Calendar.getInstance();
		temp.setTimeInMillis(0);
		temp.set(year, month, day, 0, 0, 0);
		return temp.getTime().getTime();
	}
	
	//mm/dd/yyyy
	public static String prettyDate(long millis) {
		String abbr = TimeConversion.dateStringOfTimeInMillis(millis);
		int month = Integer.parseInt(abbr.substring(0, 2), 10);
		String mo = "";
		switch (month) {
		case 1: mo = "January";
		break;
		case 2: mo = "February";
		break;
		case 3: mo = "March";
		break;
		case 4: mo = "April";
		break;
		case 5: mo = "May";
		break;
		case 6: mo = "June";
		break;
		case 7: mo = "July";
		break;
		case 8: mo = "August";
		break;
		case 9: mo = "September";
		break;
		case 10: mo = "October";
		break;
		case 11: mo = "November";
		break;
		case 12: mo = "December";
		break;
		}
		String day = abbr.substring(3,5);
		String year = abbr.substring(6);
		return mo+" "+day+", "+year;
	}
}
