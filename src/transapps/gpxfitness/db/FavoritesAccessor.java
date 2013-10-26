package transapps.gpxfitness.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FavoritesAccessor {
	private static boolean created = false;

	public static void createFavoritesList() {
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		fitDB.execSQL("create table if not exists favorites (exercise text)");
		fitDB.close();
		created = true;
	}
	
	public static void addFavorite(String exercise) {
		if (!created) createFavoritesList();
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		ContentValues cv = new ContentValues();
		cv.put("exercise", exercise);
		fitDB.insert("favorites", null, cv);
		fitDB.close();
	}
	
	public static void removeFavorite(String exercise) {
		if (!created) createFavoritesList();
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		fitDB.delete("favorites", "exercise = ?", new String[] {exercise});
		fitDB.close();
	}
	
	public static String[] getFavorites() {
		if (!created) createFavoritesList();
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		Cursor c = fitDB.query("favorites", new String[] {"exercise"}, null, null, "exercise", null, "exercise", null);
		if (c==null || !c.moveToFirst()) {
			c.close();
			fitDB.close();
			return null;
		}
		String[] faves = new String[c.getCount()];
		int pos = 0;
		do {
			faves[pos] = c.getString(0);
			pos++;
		} while (c.moveToNext());
		c.close();
		fitDB.close();
		return faves;
	}
	
	public static boolean isFavorite(String exercise) {
		if (!created) createFavoritesList();
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		Cursor c = fitDB.query("favorites", new String[] {"exercise"}, "exercise = ?", new String[] {exercise}, "exercise", null, "exercise", "1");
		if (c==null || !c.moveToFirst()) {
			c.close();
			fitDB.close();
			return false;
		}
		c.close();
		fitDB.close();
		return true;
	}
}
