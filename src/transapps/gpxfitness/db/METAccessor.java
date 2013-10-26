package transapps.gpxfitness.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class METAccessor {
	
	public static String[] getCategories() {
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		Cursor cats = fitDB.query("metdata", new String[] {"category"}, null, null, "category", null, "category", null);
		if (cats==null || !cats.moveToFirst()) {
			cats.close();
			fitDB.close();
			Log.d("META", "Category cursor was empty!");
			return null;
		}
		String[] categories = new String[cats.getCount()];
		int pos = 0;
		do {
			categories[pos] = cats.getString(0);
			pos++;
		} while (cats.moveToNext());
		cats.close();
		fitDB.close();
		return categories;
	}
	
	public static String[] getExercises(String category) {
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		Cursor ex = fitDB.query("metdata", new String[] {"exercise"}, "category = ?", new String[] {category}, "exercise", null, null);
		if (ex==null || !ex.moveToFirst()) {
			ex.close();
			fitDB.close();
			Log.d("META", "Exercise cursor was empty!");
			return null;
		}
		String[] exercises = new String[ex.getCount()];
		int pos = 0;
		do {
			exercises[pos] = ex.getString(0);
			pos++;
		} while (ex.moveToNext());
		ex.close();
		fitDB.close();
		if (category.equals("running")) {
			//the top five entries need to go at the bottom, sqlite query mis-sorts the double-digit numbers
			String[] temp = exercises.clone();
			exercises[0] = temp[5];
			exercises[1] = temp[6];
			exercises[2] = temp[7];
			exercises[3] = temp[8];
			exercises[4] = temp[9];
			exercises[5] = temp[10];
			exercises[6] = temp[0];
			exercises[7] = temp[1];
			exercises[8] = temp[2];
			exercises[9] = temp[3];
			exercises[10] = temp[4];
		}
		return exercises;
	}
	
	public static Double getMET(String exercise) {
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		Cursor met =  fitDB.query("metdata", new String[] {"met"}, "exercise = ?", new String[] {exercise}, null, null, null, "1");
		if (met==null || !met.moveToFirst()) {
			met.close();
			fitDB.close();
			return null;
		}
		double metVal = met.getDouble(0);
		met.close();
		fitDB.close();
		return metVal;
	}
	
	public static String getCategory(String exercise) {
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		Cursor category =  fitDB.query("metdata", new String[] {"category"}, "exercise = ?", new String[] {exercise}, null, null, null, "1");
		if (category==null || !category.moveToFirst()) {
			category.close();
			fitDB.close();
			return null;
		}
		String cat = category.getString(0);
		category.close();
		fitDB.close();
		return cat;
	}
}