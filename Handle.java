/**
 * This class contains an integer that gives us a handle to the record we want.
 * 
 * @author Karthik Kumar (kkumar91)
 * @author Germond Oatneal (goatn07)
 * @version 2011.08.28
 */

public class Handle {
	//~ Instance/static variables.........................................

	// ----------------------------------------------------------
	private int positionOfRecord;
	
	//~ Constructors .....................................................

	// ----------------------------------------------------------
	/**
	 * Constructor. Creates a handle with the specified position.
	 * @param pos the position to create a handle to
	 */
	public Handle(int pos) {
		positionOfRecord = pos;
	}

	/**
	 * @param positionOfRecord the positionOfRecord to set
	 */
	public void setHandle(int positionOfRecord) {
		this.positionOfRecord = positionOfRecord;
	}

	/**
	 * @return the positionOfRecord
	 */
	public int getPosition() {
		return positionOfRecord;
	}
}