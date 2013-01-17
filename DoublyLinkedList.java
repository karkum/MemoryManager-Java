/**
 * This class provides us a doubly linked list to keep track of the free blocks 
 * in the memory pool. It inserts the FreeBlock in descending order of size. If two
 * block have the same size, it inserts in ascending order by position.
 * 
 * @author Karthik Kumar (kkumar91)
 * @author Germond Oatneal (goatn07)
 * @version 2011.08.24
 */
public class DoublyLinkedList {

	//~ Instance/static variables .........................................

	// ----------------------------------------------------------
	private FreeBlock head;
	private FreeBlock tail;

	//~ Constructors .....................................................

	// ----------------------------------------------------------
	/**
	 * Constructor. In the beginning we will have one block in out list that has size poolSize and 
	 * position 0.
	 */
	public DoublyLinkedList (int position, int size) {
		head = new FreeBlock (-1, -1, null, null);
		tail = new FreeBlock (-1, -1, null, null);
		FreeBlock first = new FreeBlock(position, size, head, tail);
		head.setNext(first);
		tail.setPrevious(first);
	}

	//~ Public methods ....................................................

	// ----------------------------------------------------------
	/**
	 * Adds a new FreeBlock in descending order based on record size (or by ascending position,
	 * if two records have same size), every time a record is removed from the memory pool
	 * 
	 * @param position the position of the free block
	 * @param size the size of the free block
	 * @param toggleMerge tells whether or not to merge, because this method is also called by merge
	 */
	public void add(int position, int size, boolean toggleMerge) {
		// New block that will be created
		FreeBlock newBlock = null;

		// A pointer to the block right before the new one we are adding
		FreeBlock prevSmaller = null;

//		if (head != null) {
//			// Finds the smallest previous block when we need to insert in the correct order
//			prevSmaller = orderBySize(head, position, size);
//		} 		
		newBlock = new FreeBlock (position, size, prevSmaller, prevSmaller.next);
		prevSmaller.getNext().setPrevious(newBlock);
		prevSmaller.setNext(newBlock);
		// Only merge when we are not calling add() from the merge method
		if (toggleMerge) 
			merge(head, newBlock);
	}

	/**
	 * Removes a block of size that is needed for the manager. It looks for the block
	 * with the specified size. If the size we want to remove is less than the size we have in our 
	 * list, it creates a new node with the difference and merges this new node with the 
	 * existing one. 
	 * 
	 * @param position the position of the node we want to remove
	 * @param size the size of the block to remove
	 * @return
	 */
	public void remove(int position, int size) {
		FreeBlock iter = head.next;
		// Loop through the list
		while (iter != tail) {
			// This is when we want to remove based on position
			if (iter.getPosition() == position) {
				// The difference between the size we are looking for and the one we are at now
				int diff = iter.getSize() - size;
				if (diff == 0) {
					// It they are the same size, remove it like usual
					iter.previous.next = iter.next;
					iter.next = iter.previous;
				}
				else {
					// otherwise, 
					iter.previous.next = iter.next;
					iter.next.previous = iter.previous;
					add(iter.getPosition() + size, diff, true);
				}
			}
			iter = iter.next;
		}
	}

	/**
	 * Prints out a list of the all the free blocks
	 */
	public void printList() {
		FreeBlock iterator = head.next;
		while (iterator != tail) {
			System.out.println("[" + iterator.getPosition() + ", " + (iterator.getPosition() + 
					iterator.getSize() - 1) + "]" + " (" + iterator.getSize() + " bytes)");
			iterator = iterator.next;
		}
	}

	/**
	 * Gets the size of the biggest FreeBlock in the list. Return -1, if no best fit possible
	 * @return int the size biggest FreeBlock
	 */
	public int getBestFit(int size) {
		int bestFitIndex = -1;
		int bestFit = Integer.MAX_VALUE;
		FreeBlock iter = head.next;
		// Loop through the list
		while (iter != tail) {
			// If the size we are looking for is less than the size of the block we are looking at
			// now AND if this fit is better than any we have seen thus far, set the bestFit to this
			// and the index to this block's index
			if (iter.getSize() >= size && iter.getSize() < bestFit ) {
				bestFit = iter.getSize();
				bestFitIndex = iter.getPosition();
			}
			iter = iter.next;
		}
		return bestFitIndex;
	}


	//~ Private methods ....................................................

	/**
	 * @param head the head to set
	 */
	public void setHead(FreeBlock head) {
		this.head = head;
	}

	/**
	 * @return the tail
	 */
	public FreeBlock getTail() {
		return tail;
	}

	/**
	 * @param tail the tail to set
	 */
	public void setTail(FreeBlock tail) {
		this.tail = tail;
	}


	// ----------------------------------------------------------
	/**
	 * Merges the newBlock with any existing, adjacent FreeBlock(s) already in the List.
	 * It does so by first finding the positions that indicate adjacency, then it searches through
	 * the list look for any matches. It is capable of merging both backwards and forwards. 
	 * Once it finds a match, it removes the newBlock, the found match(s) and adds in a new 
	 * FreeBlock that contains the size of all the merged FreeBlocks.
	 */
	private void merge(FreeBlock head, FreeBlock newBlock) {
		int newCombinedPosition = 0;
		int newCombinedSize = 0;
		FreeBlock mergeForward = null;
		FreeBlock mergeBackward = null;
		int forwardKey = newBlock.getPosition() + newBlock.size;
		int backwardKey = newBlock.getPosition();
		FreeBlock iter = head.next;
		while (iter != tail) {
			if (iter.getPosition() + iter.size == backwardKey) {
				mergeBackward = iter;
			}
			if (iter.getPosition() == forwardKey) {
				mergeForward = iter;
			}

			iter = iter.next;
		}
		// Check where we merge
		if (mergeForward != null && mergeBackward != null) {
			// Remove all three that need to merge and insert one big block.
			newCombinedPosition = mergeBackward.getPosition();
			newCombinedSize = newBlock.size + mergeForward.size + mergeBackward.size;
			removeBasedOnPosition(mergeForward.position);
			removeBasedOnPosition(mergeBackward.position);
			removeBasedOnPosition(newBlock.position);
			add(newCombinedPosition, newCombinedSize, false);
		}
		else if (mergeForward != null && mergeBackward == null) {
			// Remove all two that need to merge and insert one big block.
			newCombinedPosition = newBlock.getPosition();
			newCombinedSize = newBlock.size + mergeForward.size;
			removeBasedOnPosition(mergeForward.position);
			removeBasedOnPosition(newBlock.position);
			add(newCombinedPosition, newCombinedSize, false);
		}
		else if (mergeBackward != null && mergeForward == null) {
			// Remove all two that need to merge and insert one big block.
			newCombinedPosition = mergeBackward.getPosition();
			newCombinedSize = newBlock.size + mergeBackward.size;
			removeBasedOnPosition(mergeBackward.position);
			removeBasedOnPosition(newBlock.position);
			add(newCombinedPosition, newCombinedSize, false);
		}
	}
	/**
	 * Removes a free block based on the position given. Used in merge method
	 * @param position
	 */
	private void removeBasedOnPosition(int position) {
		FreeBlock iter = head.next;
		while (iter != tail) {
			if (iter.getPosition() == position) {
				iter.previous.next = iter.next;
				iter.next.previous = iter.previous;
			}
			iter = iter.next;
		}
	}

	/**
	 * Returns the free block that is just smaller than size of the block we are adding.
	 * If the requested block is bigger than any blocks currently in the list, return head.
	 * If the requested block is smaller than any blcok currently in the list, return tail.
	 * Otherwise, loop through the free list to find the block whose size is just below the
	 * requested block's size
	 * 
	 * @param head the head of the list
	 * @param size the size of the free block that we are trying to add
	 * @return block the block that is smaller than the size
	 */
	private FreeBlock orderBySize(FreeBlock head, int position, int size) {
		// An iterator we will use to loop through the list
		FreeBlock iterator = tail.previous;

		// If the size we are requesting to add is the bigger than the biggest we have now
		if (iterator.getSize() > size)
			return iterator;

		// Loop through to find the freeBlock that goes before the one we are trying to insert.
		while (iterator != head) {
			if (iterator.getSize() < size) {
				iterator = iterator.getPrevious();
			}
			else if (iterator.getSize() == size) {
				if (iterator.getPosition() < position) {
					return iterator;
				}
				else 
					iterator = iterator.getPrevious();
			}
			else
				return iterator;
		}
		return head;
	}

	//~ Private classes.......................................................
	// ----------------------------------------------------------

	/**
	 * This class provides us a container that represents a Freed block of memory
	 * 
	 * @author Karthik Kumar (kkumar91)
	 * @author Germond Oatneal (goatn07)
	 * @version 2011.08.24
	 */
	public class FreeBlock {
		//~ Instance/static variables .........................................

		// ----------------------------------------------------------
		private int position;
		private int size;
		private FreeBlock next;
		private FreeBlock previous;

		//~ Constructors .....................................................

		// ----------------------------------------------------------
		/**
		 * Creates a free block with the position of the free space in the pool
		 * @param pos the position of the free space
		 * @param size the size of the free block
		 * @param next the free block that follows this one in the list.
		 * @param prev the free block that precedes this one in the list.
		 */
		public FreeBlock(int pos, int size, FreeBlock prev, FreeBlock next) {
			setPosition(pos);
			setSize(size);
			setPrevious(prev);
			setNext(next);
		}

		//~ Public methods ....................................................

		// ----------------------------------------------------------
		/**
		 * Get the position of the free block.
		 * @return the element
		 */
		public int getPosition() {
			return position;
		}

		// ----------------------------------------------------------
		/**
		 * Set the position stored in this block.
		 * @param value the new data value to set
		 */
		public void setPosition(int value) {
			position = value;
		}

		// ----------------------------------------------------------
		/**
		 * Get the next block in this chain.
		 * @return a reference to the next block in the chain.
		 */
		public FreeBlock getNext() {
			return next;
		}

		// ----------------------------------------------------------
		/**
		 * Set the value of this block's next pointer.
		 * @param value the block to point to as the next one in the chain.
		 */
		public void setNext(FreeBlock value) {
			next = value;
		}

		// ----------------------------------------------------------
		/**
		 * Get the previous block in this chain.
		 * @return a reference to the previous block in the chain.
		 */
		public FreeBlock getPrevious() {
			return previous;
		}

		// ----------------------------------------------------------
		/**
		 * Set the value of this block's previous pointer.
		 * @param value the block to point to as the previous one in the chain.
		 */
		public void setPrevious(FreeBlock value) {
			previous = value;
		}

		// ----------------------------------------------------------
		/**
		 * Sets the size of the free block
		 * 
		 * @param size the size to set the freeblock's size to
		 */
		public void setSize(int size) {
			this.size = size;
		}

		// ----------------------------------------------------------
		/**
		 * Gets the size of the free block
		 * @return size the size of the free block
		 */
		public int getSize() {
			return size;
		}
	}
}