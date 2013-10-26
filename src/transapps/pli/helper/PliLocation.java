/**
 * @author tnguyen
 */
package transapps.pli.helper;

import android.location.Location;

public class PliLocation extends Location {

	public PliLocation(PliLocation location) {
		super(location);
		this._id = location.getId();
	}
	
	public PliLocation(String type) {
		super(type);
	}
	
	private long _id;
	
	public long getId() {
		return _id;
	}
	public void setId(long id) {
		this._id = id;
	}
}
