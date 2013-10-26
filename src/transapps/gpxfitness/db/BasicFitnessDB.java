package transapps.gpxfitness.db;

import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class BasicFitnessDB {
	private static final String fitnessDBPath = Environment.getExternalStorageDirectory().getPath()+"/mystuff/";
	private static final String fitnessDBName = "fitness.sqlite";

	public static SQLiteDatabase getDB() {
		try {
			SQLiteDatabase fitDB = SQLiteDatabase.openDatabase(fitnessDBPath+fitnessDBName, null, SQLiteDatabase.OPEN_READWRITE);
			if (!fitDB.isOpen()) return makeDB();
			return fitDB;
		} catch (SQLiteCantOpenDatabaseException e) {
			return makeDB();
		}
	}
	
	public static SQLiteDatabase makeDB() {
		SQLiteDatabase fitDB = SQLiteDatabase.openOrCreateDatabase(fitnessDBPath+fitnessDBName, null);
		fitDB.execSQL("CREATE TABLE IF NOT EXISTS android_metadata (locale TEXT DEFAULT 'en_US')");
		fitDB.execSQL("INSERT INTO android_metadata VALUES('en_US')");
		fitDB.execSQL("CREATE TABLE IF NOT EXISTS metdata (_id INTEGER PRIMARY KEY, category TEXT, exercise TEXT, met NUMERIC)");
		fitDB.execSQL("INSERT INTO metdata VALUES(1,'military','obstacle course',5)");
		fitDB.execSQL("INSERT INTO metdata VALUES(2,'conditioning','stationary bike, moderate effort',6.5)");
		fitDB.execSQL("INSERT INTO metdata VALUES(3,'conditioning','stationary bike, vigorous effort',11)");
		fitDB.execSQL("INSERT INTO metdata VALUES(4,'conditioning','push-ups, moderate effort',4)");
		fitDB.execSQL("INSERT INTO metdata VALUES(5,'conditioning','sit-ups, moderate effort',4)");
		fitDB.execSQL("INSERT INTO metdata VALUES(6,'conditioning','pull-ups, moderate effort',4)");
		fitDB.execSQL("INSERT INTO metdata VALUES(7,'conditioning','calisthenics, moderate effort',4)");
		fitDB.execSQL("INSERT INTO metdata VALUES(8,'conditioning','push-ups, vigorous effort',8)");
		fitDB.execSQL("INSERT INTO metdata VALUES(9,'conditioning','sit-ups, vigorous effort',8)");
		fitDB.execSQL("INSERT INTO metdata VALUES(10,'conditioning','pull-ups, vigorous effort',8)");
		fitDB.execSQL("INSERT INTO metdata VALUES(11,'conditioning','jumping jacks',8)");
		fitDB.execSQL("INSERT INTO metdata VALUES(12,'conditioning','calisthenics, vigorous effort',8)");
		fitDB.execSQL("INSERT INTO metdata VALUES(13,'conditioning','circuit traning, moderate effort',4.3)");
		fitDB.execSQL("INSERT INTO metdata VALUES(14,'conditioning','circuit training, vigorous effort',8)");
		fitDB.execSQL("INSERT INTO metdata VALUES(15,'conditioning','elliptical trainer',5)");
		fitDB.execSQL("INSERT INTO metdata VALUES(16,'conditioning','resistance training, vigorous effort',6)");
		fitDB.execSQL("INSERT INTO metdata VALUES(17,'conditioning','resistance training, moderate effort',4)");
		fitDB.execSQL("INSERT INTO metdata VALUES(18,'conditioning','climbing stairs',9)");
		fitDB.execSQL("INSERT INTO metdata VALUES(19,'conditioning','rope jumping',11)");
		fitDB.execSQL("INSERT INTO metdata VALUES(20,'conditioning','rowing machine, vigorous effort',10)");
		fitDB.execSQL("INSERT INTO metdata VALUES(21,'conditioning','rowing machine, moderate effort',6)");
		fitDB.execSQL("INSERT INTO metdata VALUES(22,'conditioning','yoga',3)");
		fitDB.execSQL("INSERT INTO metdata VALUES(23,'conditioning','stretching, mild',2.3)");
		fitDB.execSQL("INSERT INTO metdata VALUES(24,'conditioning','pilates',3)");
		fitDB.execSQL("INSERT INTO metdata VALUES(25,'conditioning','upper body exercise',3.5)");
		fitDB.execSQL("INSERT INTO metdata VALUES(26,'military','rifle shooting, lying down',2.3)");
		fitDB.execSQL("INSERT INTO metdata VALUES(27,'military','rifle shooting, kneeling or standing',2.5)");
		fitDB.execSQL("INSERT INTO metdata VALUES(28,'occupational','carpentry',4.3)");
		fitDB.execSQL("INSERT INTO metdata VALUES(29,'occupational','construction, moderate effort',4.5)");
		fitDB.execSQL("INSERT INTO metdata VALUES(30,'occupational','construction, vigorous effort',6)");
		fitDB.execSQL("INSERT INTO metdata VALUES(31,'occupational','carrying heavy loads',8)");
		fitDB.execSQL("INSERT INTO metdata VALUES(32,'occupational','manual labor, moderate effort',4.5)");
		fitDB.execSQL("INSERT INTO metdata VALUES(33,'occupational','manual labor, vigorous effort',6.5)");
		fitDB.execSQL("INSERT INTO metdata VALUES(34,'occupational','masonry',4.3)");
		fitDB.execSQL("INSERT INTO metdata VALUES(35,'occupational','shoveling',7.8)");
		fitDB.execSQL("INSERT INTO metdata VALUES(36,'running','4 mph (15 min/mile)',6)");
		fitDB.execSQL("INSERT INTO metdata VALUES(37,'running','5 mph (12 min/mile)',8.3)");
		fitDB.execSQL("INSERT INTO metdata VALUES(38,'running','6 mph (10 min/mile)',9.8)");
		fitDB.execSQL("INSERT INTO metdata VALUES(39,'running','7 mph (8.5 min/mile)',11)");
		fitDB.execSQL("INSERT INTO metdata VALUES(40,'running','8 mph (7.5 min/mile)',11.8)");
		fitDB.execSQL("INSERT INTO metdata VALUES(41,'running','9 mph (6.5 min/mile)',12.8)");
		fitDB.execSQL("INSERT INTO metdata VALUES(42,'running','10 mph (6 min/mile)',14.5)");
		fitDB.execSQL("INSERT INTO metdata VALUES(43,'running','11 mph (5.5 min/mile)',16)");
		fitDB.execSQL("INSERT INTO metdata VALUES(44,'running','12 mph (5 min/mile)',19)");
		fitDB.execSQL("INSERT INTO metdata VALUES(45,'running','13 mph (4.6 min/mile)',19.8)");
		fitDB.execSQL("INSERT INTO metdata VALUES(46,'running','14 mph (4.3 min/mile)',23)");
		fitDB.execSQL("INSERT INTO metdata VALUES(47,'sports','basketball',6.5)");
		fitDB.execSQL("INSERT INTO metdata VALUES(48,'sports','bowling',3)");
		fitDB.execSQL("INSERT INTO metdata VALUES(49,'sports','boxing',7.8)");
		fitDB.execSQL("INSERT INTO metdata VALUES(50,'conditioning','punching bag',5.5)");
		fitDB.execSQL("INSERT INTO metdata VALUES(51,'sports','football',8)");
		fitDB.execSQL("INSERT INTO metdata VALUES(52,'sports','playing catch',2.5)");
		fitDB.execSQL("INSERT INTO metdata VALUES(53,'sports','golf',4.8)");
		fitDB.execSQL("INSERT INTO metdata VALUES(54,'sports','martial arts, slow pace',5.3)");
		fitDB.execSQL("INSERT INTO metdata VALUES(55,'sports','martial arts, moderate pace',10.3)");
		fitDB.execSQL("INSERT INTO metdata VALUES(56,'sports','raquetball',7)");
		fitDB.execSQL("INSERT INTO metdata VALUES(57,'sports','rock climbing',6)");
		fitDB.execSQL("INSERT INTO metdata VALUES(58,'sports','rugby',7)");
		fitDB.execSQL("INSERT INTO metdata VALUES(59,'sports','soccer',8)");
		fitDB.execSQL("INSERT INTO metdata VALUES(60,'sports','baseball',5)");
		fitDB.execSQL("INSERT INTO metdata VALUES(61,'sports','table tennis',4)");
		fitDB.execSQL("INSERT INTO metdata VALUES(62,'sports','tennis',7.3)");
		fitDB.execSQL("INSERT INTO metdata VALUES(63,'sports','volleyball',4)");
		fitDB.execSQL("INSERT INTO metdata VALUES(64,'sports','wrestling',6)");
		fitDB.execSQL("INSERT INTO metdata VALUES(65,'occupational','driving a vehicle',2.5)");
		fitDB.execSQL("INSERT INTO metdata VALUES(66,'military','marching, moderate speed, no pack',4.5)");
		fitDB.execSQL("INSERT INTO metdata VALUES(67,'military','maching rapidly, no pack',8)");
		fitDB.execSQL("INSERT INTO metdata VALUES(68,'walking','less than 2 mph (more than 30 min/mile), no incline',2)");
		fitDB.execSQL("INSERT INTO metdata VALUES(69,'walking','2 mph (30 min/mile), no incline',2.8)");
		fitDB.execSQL("INSERT INTO metdata VALUES(70,'walking','2.5 mph (24 min/mile), no incline',3)");
		fitDB.execSQL("INSERT INTO metdata VALUES(71,'walking','3 mph (20 min/mile), no incline',3.5)");
		fitDB.execSQL("INSERT INTO metdata VALUES(72,'walking','3.5 mph (17 min/mile), no incline',4.3)");
		fitDB.execSQL("INSERT INTO metdata VALUES(73,'walking','4 mph (15 min/mile), no incline',5)");
		fitDB.execSQL("INSERT INTO metdata VALUES(74,'walking','4.5 mph (13 min/mile), no incline',7)");
		fitDB.execSQL("INSERT INTO metdata VALUES(75,'walking','5 mph (12 min/mile), no incline',8.3)");
		fitDB.execSQL("INSERT INTO metdata VALUES(76,'military','marching, with pack',7.8)");
		fitDB.execSQL("INSERT INTO metdata VALUES(77,'walking','3 mph, uphill (1-5% grade)',5.3)");
		fitDB.execSQL("INSERT INTO metdata VALUES(78,'walking','3 mph, uphill (6-15% grade)',8)");
		fitDB.execSQL("INSERT INTO metdata VALUES(79,'weight training','squats',6)");
		fitDB.execSQL("INSERT INTO metdata VALUES(80,'weight training','leg press',4.5)");
		fitDB.execSQL("INSERT INTO metdata VALUES(81,'weight training','deadlift',6)");
		fitDB.execSQL("INSERT INTO metdata VALUES(82,'weight training','leg extension',4)");
		fitDB.execSQL("INSERT INTO metdata VALUES(83,'weight training','leg curl',4)");
		fitDB.execSQL("INSERT INTO metdata VALUES(84,'weight training','calf raise',4)");
		fitDB.execSQL("INSERT INTO metdata VALUES(85,'weight training','bench press',5)");
		fitDB.execSQL("INSERT INTO metdata VALUES(86,'weight training','snatch',5)");
		fitDB.execSQL("INSERT INTO metdata VALUES(87,'weight training','chest fly',5.5)");
		fitDB.execSQL("INSERT INTO metdata VALUES(88,'weight training','lat pull down',4.5)");
		fitDB.execSQL("INSERT INTO metdata VALUES(89,'weight training','seated row',4)");
		fitDB.execSQL("INSERT INTO metdata VALUES(90,'weight training','military press',5.5)");
		fitDB.execSQL("INSERT INTO metdata VALUES(91,'weight training','shrugs',5)");
		fitDB.execSQL("INSERT INTO metdata VALUES(92,'weight training','tricep extensions',4)");
		fitDB.execSQL("INSERT INTO metdata VALUES(93,'weight training','bicep curls',4)");
		fitDB.execSQL("INSERT INTO metdata VALUES(94,'weight training','clean',5.5)");
		fitDB.execSQL("INSERT INTO metdata VALUES(95,'weight training','clean and jerk',6)");
		return fitDB;
	}
	
}
