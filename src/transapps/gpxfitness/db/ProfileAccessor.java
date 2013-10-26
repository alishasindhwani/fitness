package transapps.gpxfitness.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ProfileAccessor {

	//NOTE:  this method /MUST/ be called prior to any other method in this class
	public static void createNewProfile(String username, int heightInInches, double weightInPounds,
			String sex, int age) {
		//store this stuff in a sqlite table
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		fitDB.execSQL("create table if not exists profile (username text, height int, weight real, sex text, age int)");
		ContentValues cv = new ContentValues();
		cv.put("username", username);
		cv.put("height", heightInInches);
		cv.put("weight", weightInPounds);
		cv.put("sex", sex);
		cv.put("age", age);
		fitDB.insert("profile", null, cv);
		fitDB.close();
	}
	
	public static boolean isProfileSet() {
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		Cursor c = fitDB.query("sqlite_master", new String[] {"name"}, "type = 'table' AND name = 'profile'",
				null, null, null, null, "1");
		if (c==null || !c.moveToFirst()) {
			c.close();
			fitDB.close();
			return false;
		}
		c.close();
		fitDB.close();
		return true;
	}
	
	public static void changeUsername(String username) {
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		ContentValues cv = new ContentValues();
		cv.put("username", username);
		fitDB.update("profile", cv, null, null);
		fitDB.close();
	}
	
	public static void changeHeight(int heightInInches) {
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		ContentValues cv = new ContentValues();
		cv.put("height", heightInInches);
		fitDB.update("profile", cv, null, null);
		fitDB.close();
	}
	
	public static void changeWeight(double weightInPounds) {
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		ContentValues cv = new ContentValues();
		cv.put("weight", weightInPounds);
		fitDB.update("profile", cv, null, null);
		fitDB.close();
	}
	
	public static void changeSex(String sex) {
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		ContentValues cv = new ContentValues();
		cv.put("sex", sex);
		fitDB.update("profile", cv, null, null);
		fitDB.close();
	}
	
	public static void changeAge(int age) {
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		ContentValues cv = new ContentValues();
		cv.put("age", age);
		fitDB.update("profile", cv, null, null);
		fitDB.close();
	}
	
	public static String getUsername() {
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		Cursor c = fitDB.query("profile", new String[] {"username"}, null, null, null, null, null, null);
		if (c==null || !c.moveToFirst()) throw new RuntimeException("Failed to find username!");
		String name = c.getString(0);
		c.close();
		fitDB.close();
		return name;
	}
	
	public static int getHeight() {
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		Cursor c = fitDB.query("profile", new String[] {"height"}, null, null, null, null, null, null);
		if (c==null || !c.moveToFirst()) throw new RuntimeException("Failed to find height!");
		int h = c.getInt(0);
		c.close();
		fitDB.close();
		return h;
	}
	
	public static double getWeight() {
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		Cursor c = fitDB.query("profile", new String[] {"weight"}, null, null, null, null, null, null);
		if (c==null || !c.moveToFirst()) throw new RuntimeException("Failed to find weight!");
		double w = c.getDouble(0);
		c.close();
		fitDB.close();
		return w;
	}
	
	public static String getSex() {
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		Cursor c = fitDB.query("profile", new String[] {"sex"}, null, null, null, null, null, null);
		if (c==null || !c.moveToFirst()) throw new RuntimeException("Failed to find sex!");
		String sex = c.getString(0);
		c.close();
		fitDB.close();
		return sex;
	}
	
	public static int getAge() {
		SQLiteDatabase fitDB = BasicFitnessDB.getDB();
		Cursor c = fitDB.query("profile", new String[] {"age"}, null, null, null, null, null, null);
		if (c==null || !c.moveToFirst()) throw new RuntimeException("Failed to find age!");
		int age = c.getInt(0);
		c.close();
		fitDB.close();
		return age;
	}
}
