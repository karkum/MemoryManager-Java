
/**
 * This class acts as the Memory Manager and handles most of the functionality required by 
 * this project. It inserts a given byte array into our memory pool, removes records from
 * memory pool and gets a certain record in the memory.
 * 
 * @author Karthik Kumar (kkumar91)
 * @author Germond Oatneal (goatn07)
 * @version 2011.08.28
 */

public class MemManager {

	//~ Instance/static variables .........................................

	// ----------------------------------------------------------
	/* This DoublyLinkedList will contain all of our free blocks */
	private DoublyLinkedList freeBlockList;

	/* Memory pool contains an array of bytes that stores records*/ 
	private byte [] memoryPool;

	//~ Constructors .....................................................

	// ----------------------------------------------------------
	/**
	 * Constructor. Creates a memory pool of specified size.
	 * 
	 * Creates a DoublyLinkedList that has one FreeBlock that is of size poolSize and has 
	 * position 0.
	 * 
	 * @param poolSize the size of pool
	 */
	public MemManager(int poolSize) {
		setMemoryPool(new byte [poolSize]);
		setFreeBlockList(new DoublyLinkedList(0, poolSize));
	}

	//~ Public methods ....................................................

	// ----------------------------------------------------------
	/**
	 * Insert a record and return its position handle. Parameter space contains the record 
	 * to be inserted, of length size. Return null if the record could not be inserted.
	 * @param space the record to be inserted
	 * @param size the size of the record to be inserted
	 * @return Handle pointing to the location where record was inserted, null if not inserted
	 */
	public Handle insertIntoMemoryPool (byte[] space, int size) {
		int bestFitPosition = freeBlockList.getBestFit(size + 1);
		if (bestFitPosition == -1) {
			// No space available
			return null;
		}
		// Find big enough space in Linked List to insert into memory pool	
		Handle pos = new Handle(bestFitPosition);

		// Take away the free space in the freeBlockList
		freeBlockList.remove(bestFitPosition, size + 1);

		/* The first byte will store the position. This may be negative if the size of the record
		* is larger than 127 (since bytes in java are signed). This problem is overcome in our 
		* get method which ands the size with 255 to convert it to a positive value.
		*/
		memoryPool[pos.getPosition()] = (byte) (size);
		int j = pos.getPosition() + 1;
		
		// Loop through and copy the data from space into our memory pool
		for (int i = 0; i < size; i ++) {
			memoryPool[j] = space[i];
			j++;
		}
		return pos;
	}
	
	/**
	 * Removes the record at theHandle. Merges adjacent blocks if appropriate. 
	 * Adds this freed block to the freeBlockList
	 * @param theHandle the record to free
	 */
	public void removeFromMemoryPool(Handle theHandle) {
		int size = getSizeOfRecord(theHandle);
		freeBlockList.add(theHandle.getPosition(), size + 1, true);
	}

	/**
	 * Return the record with handle theHandle, up to size bytes. Place the record into space.
	 * @param space the destination for the record to get
	 * @param theHandle points to the record in to memory pool to get
	 * @param size the size of record we are trying to get
	 */
	public int get(byte[] space, Handle theHandle, int size) {
		int numOfBytesActuallyCopiedIntoSpace = 0;
		int j = 0;
		// Loop through from the starting position of the record (theHandle) all the way till
		// we get all the bytes we need (size) and copy over the info from out memPool to space
		for (int i = theHandle.getPosition() + 1; i <= theHandle.getPosition() + size; i++) {
			space[j] = memoryPool[i];
			numOfBytesActuallyCopiedIntoSpace++;
			j++;
		}
		return numOfBytesActuallyCopiedIntoSpace;
	}

	/**
	 * Return the size of a record, without including the size byte.
	 * @param theHandle the position of the record to find the size of
	 * @return the size of the record
	 */
	public int getSizeOfRecord(Handle theHandle) {
		// And by 255 to convert a size > 127
		return memoryPool[theHandle.getPosition()] & 255; 
	}

	/**
	 * @return the freeBlockList
	 */
	public DoublyLinkedList getFreeBlockList() {
		return freeBlockList;
	}

	/**
	 * @param freeBlockList the freeBlockList to set
	 */
	public void setFreeBlockList(DoublyLinkedList freeBlockList) {
		this.freeBlockList = freeBlockList;
	}

	/**
	 * @return the memoryPool
	 */
	public byte[] getMemoryPool() {
		return memoryPool;
	}

	/**
	 * @param memoryPool the memoryPool to set
	 */
	public void setMemoryPool(byte[] memoryPool) {
		this.memoryPool = memoryPool;
	}

	/**
	 * Simply calls the freeBlockList's printList method.
	 */
	public void printFreeList() {
		freeBlockList.printList();
	}
}