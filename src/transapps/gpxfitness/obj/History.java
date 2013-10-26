package transapps.gpxfitness.obj;

public class History {
	private String total_cal;
	private String date;
	
	public History(String date, String total_cal) {
		this.total_cal=total_cal;
		this.date=date;
	}

	public String getTotalCal() {
		return this.total_cal;
	}
	
	public String getDate() {
		return this.date;
	}
	
	public int getYear() {
		String[] s = this.date.split(" ");
		return Integer.parseInt(s[2]);
	}
	
	public int getMonth() {
		String[] s = this.date.split(" ");
		String mo = s[0];
		if (mo.equals("January")) return 1;
		else if (mo.equals("February")) return 2;
		else if (mo.equals("March")) return 3;
		else if (mo.equals("April")) return 4;
		else if (mo.equals("May")) return 5;
		else if (mo.equals("June")) return 6;
		else if (mo.equals("July")) return 7;
		else if (mo.equals("August")) return 8;
		else if (mo.equals("September")) return 9;
		else if (mo.equals("October")) return 10;
		else if (mo.equals("November")) return 11;
		else if (mo.equals("December")) return 12;
		else return -1;
	}
	
	public int getDay() {
		String[] s = this.date.split(" ");
		return Integer.parseInt(s[1].substring(0, 2));
	}
}
