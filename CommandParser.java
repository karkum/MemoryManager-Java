
/**
 * This class allows us to parse a line of command and set appropriate fields.
 * 
 * @author Karthik Kumar (kkumar91)
 * @author Germond Oatneal (goatn07)
 * @version 2011.09.20
 */

public class CommandParser {

	//~ Instance/static variables .........................................

	// ----------------------------------------------------------
	/* The record that will be created if we are inserting*/
	private Record record;
	
	/* The command that we are executing*/
	private int command;
	
	private int recordPosition;

	//~ Constructors .....................................................

	// ----------------------------------------------------------
	/**
	 * Constructor. Gets the line that we need to parse, remove spaces and processes the command.
	 * @param line the String we need to parse
	 */
	public CommandParser(String line) {
		String lineOfCommand = removeSpaces (line);
		processCommand(lineOfCommand);
	}

	//~ Private methods ....................................................

	// ----------------------------------------------------------
	/**
	 * Uses Regular Expressions to remove unnecessary spaces. We replace one or more occurrences of 
	 * a space with just one space.
	 * @param lineOfCommand the line to remove the spaces from
	 */
	private String removeSpaces(String lineOfCommand) {
		return lineOfCommand.replaceAll("  +", " ").trim();
	}

	/**
	 * Processes the command. 
	 * If the command is insert, sets "command" variable to 0, sets recordPosition, x, y, cityName
	 * to their appropriate values, creates a byte array containing the coordinates and city name
	 * to be stored.
	 * 
	 * If the command is remove, sets "command" variable to 1, sets recordPosition to its
	 * appropriate value.
	 * 
	 * If the command is print, sets "command" variable to 2, signifying a dump of information
	 * 
	 * If the command is to print a specific record number, sets "command" variable to 3, sets 
	 * recordPosition to its appropriate value.
	 * 
	 * @param lineOfCommand
	 */
	private void processCommand(String lineOfCommand) {
		if (lineOfCommand.contains("insert")) {
			command = Client.INSERT_COMMAND;

			// The following finds the record position, x, and y using the spaces as a guide
			int indexOfFirstSpace = lineOfCommand.indexOf(" ", 7);
			int x = Integer.parseInt(String.valueOf((lineOfCommand.substring(7, indexOfFirstSpace))));

			int indexOfSecondSpace = lineOfCommand.indexOf(" ", indexOfFirstSpace + 1);
			int y = Integer.parseInt(lineOfCommand.substring(indexOfFirstSpace + 1, indexOfSecondSpace));

			// The cityName is the remaining string after we look at the other parameters
			String cityName = lineOfCommand.substring(indexOfSecondSpace + 1);

			record = new Record(x, y, cityName);

		}
		else if (lineOfCommand.contains("remove")) {
			command = Client.REMOVE_COMMAND;
			recordPosition = Integer.parseInt(lineOfCommand.substring(7));
		}
		else if (lineOfCommand.equals("print")) {
			command = Client.DUMP_COMMAND;
		}
		else {
			command = Client.PRINT_COMMAND;
			recordPosition = Integer.parseInt(lineOfCommand.substring(6));
		}
	}
	
	//~ Public methods ....................................................

	// ----------------------------------------------------------
	/**
	 * @param recordPosition the recordPosition to set
	 */
	public void setRecordPosition(int recordPosition) {
		this.recordPosition = recordPosition;
	}

	/**
	 * @return the recordPosition
	 */
	public int getRecordPosition() {
		return recordPosition;
	}

	/**
	 * @return the command
	 */
	public int getCommand() {
		return command;
	}

	/**
	 * @param command the command to set
	 */
	public void setCommand(int command) {
		this.command = command;
	}

	/**
	 * @param record the record to set
	 */
	public void setRecord(Record record) {
		this.record = record;
	}

	/**
	 * @return the record
	 */
	public Record getRecord() {
		return record;
	}
}
