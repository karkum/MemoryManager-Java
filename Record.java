import java.nio.ByteBuffer;
/**
 * This class represents a record that we are storing. It has an x, y and a cityName
 * It provides us getters and setters for these variables. It also encodes a record into a
 * byte array and decodes a byte array into each variable.
 * 
 * @author Karthik Kumar (kkumar91)
 * @author Germond Oatneal (goatn07)
 * @version 2011.09.01
 */
public class Record {
	//~ Instance/static variables .........................................

	// ----------------------------------------------------------
	/* The x coordinate we are trying to store*/
	private int x;
	
	/* The Y coordinate we are trying to store*/
	private int y;
	
	/* The city name coordinate we are trying to store*/
	private String cityName;
	
	/* The byte array version of the record trying to store*/
	private byte[] message;
	
	/* The size of the record we are trying to store*/
	private int sizeOfMessage;
	
	//~ Constructors .......................................................

	// ----------------------------------------------------------
	/**
	 * Constructor. Creates a new record with the given parameters. Encodes these variables into
	 * a byte array (message) and sets the size.
	 * @param int the x variable to set
	 * @param int the y variable to set
	 * @param String the city name to set
	 */
	public Record (int x, int y, String cityName) {
		this.setX(x);
		this.setY(y);
		this.setCityName(cityName);
		message = encode();
		setSizeOfMessage(message.length);
	}
	
	/**
	 * Constructor. Creates a new record with a byte array that has been encoded. Sets all
	 * the fields of a Record. 
	 * @param byte[] the encoded message that we need to decode an set the fields.
	 */
	public Record(byte[] message) {
		decode(message);
	}

	//~ Public Methods ........................................................
	
	// ----------------------------------------------------------
	/**
	 * Encodes this record into a byte array
	 * @return byte[] the information in this record put in a byte array 
	 */
	public byte[] encode() {
		// Gets the cityName in bytes, using the ASCII character set
		byte[] city = cityName.getBytes();
		int lengthOfCityName = city.length;

		// The total length of the byte array will be 4 for x, 
		// 4 for y, and however many bytes we need to store the string.
		int sizeOfByteArray = 8 + lengthOfCityName;

		// Combines the size, coordinates and the city name into one single byte array.
		return mergeRecordIntoByteArray(ByteBuffer.allocate(4).putInt(x).array(), 
				ByteBuffer.allocate(4).putInt(y).array(), city, sizeOfByteArray);
	}
	
	/**
	 * Decodes a given byte array into a Record's x, y and cityName. 
	 * @param message the byte array to decode
	 */
	public void decode(byte[] message) {
	
		// The first four bytes are for the x, use these bytes in a BytBuffer to set the x value
		byte[] xArr = {message[0], message[1], message[2], message[3]};
		setX(ByteBuffer.wrap(xArr).asIntBuffer().get());
		
		// Repeat the above method to find y.
		byte[] yArr = {message[4], message[5], message[6], message[7]};
		setY(ByteBuffer.wrap(yArr).asIntBuffer().get());
		
		// The city will be message.length - 8 chars long (8 will be taken up by x, y)
		byte[] city = new byte[message.length - 8];
		int j = 8; 
		// Loop to copy the message (starting at pos 8 into out array of characters
		for (int i = 0; i < city.length; i++) {
			city[i] = message[j]; 
			j++;
		}
		// Set the cityName with the char[] array
		cityName = new String(city);
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param cityName the cityName to set
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	/**
	 * @return the cityName
	 */
	public String getCityName() {
		return cityName;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(byte[] message) {
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public byte[] getMessage() {
		return message;
	}

	/**
	 * @param sizeOfMessage the sizeOfMessage to set
	 */
	public void setSizeOfMessage(int sizeOfMessage) {
		this.sizeOfMessage = sizeOfMessage;
	}

	/**
	 * @return the sizeOfMessage
	 */
	public int getSizeOfMessage() {
		return sizeOfMessage;
	}
	
	//~ Private Methods ........................................................
	
	// ----------------------------------------------------------
	
	/**
	 * Merges the x, y coordinates, and the cityName into one large byte array
	 * @param x the x coordinate, taking up four bytes
	 * @param y the y coordinate, taking up four bytes
	 * @param cityName the name of the city
	 * @param sizeOfByteArray specifies what the total size of the byte array should be
	 * @return byte[] one large byte array containing all the information of a record
	 */
	private byte[] mergeRecordIntoByteArray(byte[] x, byte[] y, byte[] cityName, 
			int sizeOfByteArray) {
		
		byte[] merged = new byte[sizeOfByteArray];
		// The first byte will always be 4 (for x), 4 (for y) and the length of the cityName
		
		// Merges the x
		for (int i = 0; i < 4; i++) {
			merged[i] = x[i];
		}
		
		// Merges the y
		for (int j = 0; j < 4; j++) {
			merged[j + 4] = y[j];
		}
		
		// Merges the cityName
		for (int k = 0; k < cityName.length; k++) {
			merged[k + 8] = cityName[k];
		}
		
		return merged;
	}
}
