import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
/**
 * This class is the client that keeps track of the record array and process the commands.
 * It conducts the main processes of the project. It uses the CommandParser to get the command
 * and the parameters from the the command file. It executes the command, using the memory manager 
 * to control the memory pool. It also keeps track of the records that we have inserted, with an 
 * array of Handles.
 *  
 * @author Karthik Kumar (kkumar91)
 * @author Germond Oatneal (goatn07)
 * @version 2011.08.30
 */

public class Client {

	//~ Instance/static variables .........................................

	// ----------------------------------------------------------
	/* Parses the command and gives the parameters for the command. */
	private CommandParser parser;

	/* Handles the commands and executes the requested action. */
	private MemManager manager;

	/* Keeps track of the locations of the records in the memory pool*/
	private Handle[] records;

	/* Constants for determining command*/
	public final static int INSERT_COMMAND = 0;

	public final static int REMOVE_COMMAND = 1;

	public final static int PRINT_COMMAND = 2;

	public final static int DUMP_COMMAND = 3;

	//~ Constructor .............. .........................................

	// ----------------------------------------------------------
	/**
	 * Creates a client with a Memory Manager object, a record array and the 
	 * file of commands which need to be parsed.
	 * @param MemManager the manager that executes commands
	 * @param int the number of records that we will be storing
	 * @param String the fileName of the file that we are reading the commands from
	 */
	public Client(MemManager manager, int numOfRecords, String fileName) {
		this.setManager(manager);
		records = new Handle[numOfRecords];

		try {
			BufferedReader br = new BufferedReader(new FileReader (new File (fileName)));
			String line = br.readLine();
			while (line != null ) {
				if (!line.equals("")) {
					// Sends the line off to be parsed by the CommandParser
					parser = new CommandParser(line);
					switch (parser.getCommand()) {
					case INSERT_COMMAND:
						insert(parser.getRecord().getMessage(), parser.getRecordPosition());
						break;
					case REMOVE_COMMAND:
						remove(records[parser.getRecordPosition()]);
						break;
					case DUMP_COMMAND:
						dump();
						break;
					case PRINT_COMMAND:
						print(records[parser.getRecordPosition()]);
						System.out.println();
						break;
					}
				}
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.err.println("Cannot find specified file.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.print("Cannot read the specified file.");
			e.printStackTrace();
		}
	}

	//~ Public methods ....................................................

	// ----------------------------------------------------------

	/**
	 * Insert a record into the memory pool and create a record in the record array entry.
	 * If there is already a record at the position, remove it and try to add the new record.
	 * @param space the record to be inserted
	 * @param recordPosition the position of this record in the record array
	 */
	public void insert (byte[] space, int recordPosition) {

		if (recordPosition < 0 || recordPosition > records.length - 1) {
			System.out.println("ERROR: Specified record number is outside the range of acceptable values.\n");
		}
		// If the there is already a record at the given position, remove it and try to add the new one.
		else if (records[recordPosition] != null) {
			remove(records[recordPosition]);
			records[recordPosition] = manager.insertIntoMemoryPool(space, space.length);
			if (records[recordPosition] == null) 
				System.out.println("ERROR: Record was not inserted. Not enough space in memory pool\n");
		}
		else {
			records[recordPosition] = manager.insertIntoMemoryPool(space, space.length);
			if (records[recordPosition] == null) 
				System.out.println("ERROR: Record was not inserted. Not enough space in memory pool\n");
		}
	}

	/**
	 * Free the block at theHandle. Merge adjacent blocks if appropriate. Add this block to the
	 * freeBlockList. Output error message if there is no record at the specified Handle
	 * @param theHandle the record to free
	 */
	public void remove(Handle theHandle) {
		if (parser.getRecordPosition() < 0 || parser.getRecordPosition() > records.length - 1)
			System.out.println("ERROR: Specified record number is outside the range of acceptable values.");
		else if (theHandle == null)
			System.out.println("ERROR: No record at specified position\n");	
		else {
			manager.removeFromMemoryPool(theHandle);
			// Remove pointer to the record
			records[parser.getRecordPosition()] = null;
		}
	}

	/**
	 * Prints out the records in the memory pool and the FreeBlocks in the freeBlockList
	 */
	public void dump() {
		System.out.println("The records in the memory pool ([Location in Memory Pool] (x, y) City Name):");
		// Loop through all the records and print out contents
		for (int i = 0; i < records.length; i++) {
			if (records[i] != null) {
				System.out.print("Record " + i + ": ");
				print(records[i]);
			}
			else {
				System.out.println("Record " + i + ": " + "No Record");
			}
		}
		System.out.println();
		System.out.println("The freelist ([Starting Position, Ending Position] (Size)):");
		manager.printFreeList();
		System.out.println();
	}

	/**
	 * Prints out the record at theHandle
	 * @param theHandle the record we want to print out
	 */
	public void print(Handle theHandle) {
		if (parser.getRecordPosition() < 0 || parser.getRecordPosition() > records.length - 1)
			System.out.println("ERROR: Specified record number is outside the range of acceptable values.\n");

		else if (theHandle == null)
			System.out.println("ERROR: No record at specified position\n");
		else {
			byte[] answer = new byte[manager.getSizeOfRecord(theHandle)];
			int actuallyCopied = manager.get(answer, theHandle, manager.getSizeOfRecord(theHandle));
			// Only decode the record if we were able to get the entire record
			if (actuallyCopied == manager.getSizeOfRecord(theHandle)) {
				// Decodes the answer.
				Record rec = new Record(answer);
				System.out.print("[" + theHandle.getPosition() + "]");
				System.out.println(" " + "(" + rec.getX() + ", " + rec.getY() + ") " +
						rec.getCityName());
			}
		}
	}
	/**
	 * @param manager the manager to set
	 */
	public void setManager(MemManager manager) {
		this.manager = manager;
	}

	/**
	 * @return the manager
	 */
	public MemManager getManager() {
		return manager;
	}

	/**
	 * @param records the records to set
	 */
	public void setRecords(Handle[] records) {
		this.records = records;
	}

	/**
	 * @return the records
	 */
	public Handle[] getRecords() {
		return records;
	}
}
