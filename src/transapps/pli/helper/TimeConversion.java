/**
 * @author tnguyen
 */
package transapps.pli.helper;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeConversion {

    private final String TAG = TimeConversion.class.getSimpleName();

	private static final String strDateFormat_MMddyyyy = "MM/dd/yyyy";
	private static final String strDateFormat_MMddyy = "MM/dd/yy";
	private static final String strTimeFormat_HHmmss = "HH:mm:ss";
	private static final String strTimeFormat_HHmm = "HH:mm";
	private static final String strTimeFormat_mmss = "mm:ss";
	
	private static final String xmlSchemaDateTimeFormatString = "yyyy'-'MM'-'dd'T'HH:mm:ss'Z'";
	static SimpleDateFormat dateFormat_MMddyyyy = new SimpleDateFormat(strDateFormat_MMddyyyy);
	static SimpleDateFormat timeFormat_HHmmss = new SimpleDateFormat(strTimeFormat_HHmmss);
	static SimpleDateFormat timeFormat_mmss = new SimpleDateFormat(strTimeFormat_mmss);
	static SimpleDateFormat dateTimeFormat_MMddyyyy_HHmmss = new SimpleDateFormat(
			strDateFormat_MMddyyyy + " " + strTimeFormat_HHmmss);
	static SimpleDateFormat dateTimeFormat_MMddyy_HHmmss = new SimpleDateFormat(
			strDateFormat_MMddyy + " " + strTimeFormat_HHmmss);
	static SimpleDateFormat dateTimeFormat_MMddyy_HHmm = new SimpleDateFormat(
			strDateFormat_MMddyy + " " + strTimeFormat_HHmm);
	static SimpleDateFormat xmlSchemaDateTimeFormat = new SimpleDateFormat(
			xmlSchemaDateTimeFormatString);

	private static final long millisInSec = 1000;
	private static final long millisInMin = 60000;
	private static final long millisInHour = 3600000;
	private static final long millisInDay = 86400000;

	static DecimalFormat secondsFormat3 = new DecimalFormat("00.000");
	static DecimalFormat secondsFormat2 = new DecimalFormat("00.00");
	static DecimalFormat secondsFormat1 = new DecimalFormat("00.0");
	static DecimalFormat secondsFormat0 = new DecimalFormat("00");
	static DecimalFormat twoDigitsFormat = new DecimalFormat("00");

	/**
	 * Returns date of the given time in millis in the format MM/dd/yyyy
	 * 
	 * @param t
	 * @return
	 */
	public static String dateStringOfTimeInMillis(long t) {
		return dateFormat_MMddyyyy.format(new Date(t));
	}

	/**
	 * Returns time of the given time in millis in the format HH:mm:ss
	 * 
	 * @param t
	 * @return
	 */
	public static String timeStringOfTimeInMillis(long t) {
		long minutes = t / millisInMin;
		if (minutes >= 60) {
			return timeFormat_HHmmss.format(new Date(t));
		}
		return timeFormat_mmss.format(new Date(t));
	}

	/**
	 * Returns date and time of the given time in millis in the format
	 * MM/dd/yyyy HH:mm:ss
	 * 
	 * @param t
	 * @return
	 */
	public static String dateTimeStringOfTimeInMillis(long t) {
		return dateTimeFormat_MMddyyyy_HHmmss.format(new Date(t));
	}

	/**
	 * Returns date and time of the given time in millis in the 'shorter' format
	 * MM/dd/yy HH:mm:ss
	 * 
	 * @param t
	 * @return
	 */
	public static String dateTimeShortStringOfTimeInMillis(long t) {
		return dateTimeFormat_MMddyy_HHmmss.format(new Date(t));
	}
	
	/**
	 * Returns date and time of the given time in millis in the format
	 * MM/dd/yy HH:mm
	 * 
	 * @param t
	 * @return
	 */
	public static String dateTime_MMddyy_HHmm_OfTimeInMillis(long timeInMilliSec) {
		return dateTimeFormat_MMddyy_HHmm.format(new Date(timeInMilliSec));
	}

	/**
	 * Returns date and time of the given time in millis in the xml schema
	 * dateTime format: yyyy-MM-ddTHH:mm:ssZ See
	 * http://www.w3.org/TR/xmlschema-2/#dateTime
	 * 
	 * @param t
	 * @return
	 */
	public static String xmlSchemaDateTimeStringOfTimeInMillis(long t) {
		TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
		xmlSchemaDateTimeFormat.setTimeZone(utcTimeZone);
		return xmlSchemaDateTimeFormat.format(new Date(t));
	}

	/**
	 * Returns the time interval numDecimalPlacesForSeconds=3: mm:ss.sss
	 * numDecimalPlacesForSeconds=2: mm:ss.ss numDecimalPlacesForSeconds=1:
	 * mm:ss.s numDecimalPlacesForSeconds=0: mm:ss default: mm:ss.sss
	 * 
	 * @param t
	 * @param numDecimalPlacesForSeconds
	 * @return
	 */
	public static String timeStringOfTimeIntervalInMinSec(long t,
			int numDecimalPlacesForSeconds) {
		long minutes = t / millisInMin;
		t -= millisInMin * minutes;
		double seconds = Math.ceil((double) t / millisInSec);

		String secondsString = null;
		switch (numDecimalPlacesForSeconds) {
		case 0:
			secondsString = secondsFormat0.format(Math.floor(seconds));
			break;
		case 1:
			secondsString = secondsFormat1
					.format(Math.floor(seconds * 10) / 10);
			break;
		case 2:
			secondsString = secondsFormat2
					.format(Math.floor(seconds * 100) / 100);
			break;
		case 3:
			secondsString = secondsFormat3
					.format(Math.floor(seconds * 1000) / 1000);
			break;
		default:
			secondsString = secondsFormat3
					.format(Math.floor(seconds * 1000) / 1000);
			break;
		}

		return twoDigitsFormat.format(minutes) + ":" + secondsString;
	}

	/**
	 * Returns the time interval in 1d 2h 3m 4.567s
	 * 
	 * @param t
	 * @return
	 */
	public static String timeStringOfTimeIntervalInDayHrMinSec(long t) {
		return timeStringOfTimeIntervalInDayHrMinSec(t, false);
	}

	/**
	 * Returns the time interval in 1d 2h 3m 4.567s or 1d 2h 3m 5s if
	 * roundsSeconds is true
	 * 
	 * @param t
	 * @return
	 */
	public static String timeStringOfTimeIntervalInDayHrMinSec(long t,
			boolean roundSeconds) {
		long days = t / millisInDay;
		t -= millisInDay * days;
		long hours = t / millisInHour;
		t -= millisInHour * hours;
		long minutes = t / millisInMin;
		t -= millisInMin * minutes;
		double seconds = (double) t / millisInSec;

		StringBuffer sb = new StringBuffer();
		if (days > 0) {
			sb.append(days + "d ");
		}
		if (hours > 0) {
			sb.append(hours + "h ");
		}
		if (minutes > 0) {
			sb.append(minutes + "m ");
		}
		if (roundSeconds) {
			sb.append(Math.round(seconds) + "s");
		} else {
			sb.append(secondsFormat3.format(seconds) + "s");
		}
		return sb.toString();
	}

	/**
	 * Returns the time interval in 1d 2h 3m
	 * 
	 * @param t
	 * @return
	 */
	public static String timeStringOfTimeIntervalInDayHrMin(long t) {
		long days = t / millisInDay;
		t -= millisInDay * days;
		long hours = t / millisInHour;
		t -= millisInHour * hours;
		long minutes = t / millisInMin;
		t -= millisInMin * minutes;

		StringBuffer sb = new StringBuffer();
		if (days > 0) {
			sb.append(days + "d ");
		}
		if (hours > 0) {
			sb.append(hours + "h ");
		}
		if (minutes > 0) {
			sb.append(minutes + "m ");
		}
		return sb.toString();
	}

	/**
	 * Returns the time interval in the following various formats (useful to
	 * display in a limited space, note that there are no spaces between each
	 * segment)
	 * 
	 * 123d, if more than 30 days 2d10h, if more than 1 day 20h15m, if more than
	 * 1 hour 03m05s, if less than 1 hour
	 * 
	 * @param t
	 * @return
	 */
	public static String timeStringOfTimeIntervalInFlexibleFormat(long t) {

		// need to round here, in order to avoid 1m60s
		int millisecRemainder = (int) (t % 1000);
		long roundedMiilisec = 0;

		if (millisecRemainder >= 500)
			roundedMiilisec = t + (1000 - millisecRemainder);
		else
			roundedMiilisec = t - millisecRemainder;

//		System.out.println("t:" + t);
//		System.out.println("roundedMiilisec:" + roundedMiilisec);

		long days = roundedMiilisec / millisInDay;
		roundedMiilisec -= millisInDay * days;
		long hours = roundedMiilisec / millisInHour;
		roundedMiilisec -= millisInHour * hours;
		long minutes = roundedMiilisec / millisInMin;
		roundedMiilisec -= millisInMin * minutes;
		double seconds = (double) roundedMiilisec / millisInSec;

		StringBuffer sb = new StringBuffer();

		// more than 30 days
		if (days >= 30) {
			sb.append(days + "d");
		} else // otherwise
		{
			// more than 1 day
			if (days >= 1) {
				sb.append(days + "d");
				sb.append(twoDigitsFormat.format(hours) + "h");
			} else // otherwise
			{
				// more than 1 hour
				if (hours >= 1) {
					sb.append(hours + "h");
					sb.append(twoDigitsFormat.format(minutes) + "m");
				} else // otherwise
				{
					if (minutes >= 1)
						sb.append(minutes + "m");

					sb.append(twoDigitsFormat.format(Math.round(seconds)) + "s");
				}
			}
		}

		return sb.toString();
	}
}