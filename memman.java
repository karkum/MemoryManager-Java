
/**
 * This is the main class of the project, which processes the parameters sent to it. It is a memory 
 * manager that stores a city record. It responds to commands from a given file and performs the
 * appropriate actions. It uses three main data structures: a byte array for the memory pool, a
 * DoublyLinkedList for the free blocks and a record array that holds the handles to each record.
 * 
 * This project takes in three command-line arguments -the pool size (int), number of records (int), 
 * and the file name containing the commands (String).
 * 
 * Compiler Used: Java 1.6
 * Operating System Used: Windows 7 64-bit
 * Date Completed: 9/8/2011
 * 
 * @author Karthik Kumar (kkumar91)
 * @author Germond Oatneal (goatn07)
 * @version 2011.08.28
 * 
 */
public class memman {
	 // On my honor:
	 //
	 // - I have not used source code obtained from another student,
	 // or any other unauthorized source, either modified or
	 // unmodified.
	 //
	 // - All source code and documentation used in my program is
	 // either my original work, or was derived by me from the
	 // source code published in the textbook for this course.
	 //
	 // - I have not discussed coding details about this project with
	 // anyone other than my partner (in the case of a joint
	 // submission), instructor, ACM/UPE tutors or the TAs assigned
	 // to this course. I understand that I may discuss the concepts
	 // of this program with other students, and that another student
	 // may help me debug my program so long as neither of us writes
	 // anything during the discussion or modifies any computer file
	 // during the discussion. I have violated neither the spirit nor
	 // letter of this restriction.
	
	//~ Instance/static variables .........................................

	// ----------------------------------------------------------
	/* The size of our memory pool, or the number of bytes we have available */ 
	public static int poolSize;
	
	/* The total number of records we will have*/
	public static int numOfRecords;
	
	/* The name of the file which contains the commands we need to parse*/
	public static String commandFileName;
	
	//~ Public methods ....................................................

	// ----------------------------------------------------------
	/**
	 * Main method of this project, simply gets the arguments from the command line, and
	 * creates an instance of MemManager, which handles almost all of the functionality
	 * @param args the command line arguments given to us - poolSize, numOfRecords, commandFileName
	 */
	public static void main (String [] args) {
		
		// Get the argument from the command line
		poolSize = Integer.parseInt(args[0]);
		numOfRecords = Integer.parseInt(args[1]);
		commandFileName = args[2];
		
		MemManager manager = new MemManager (poolSize);
		@SuppressWarnings("unused")
		Client client = new Client(manager, numOfRecords, commandFileName);
	}
}
