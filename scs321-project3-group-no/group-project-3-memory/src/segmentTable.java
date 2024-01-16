//segment table class to manage memory allocation

import java.util.Arrays;

public class segmentTable {
	//memory array to hold processes
	int memSegInd = 10;
	memorySegment[] memoryArray = new memorySegment[memSegInd];
	//arrays to hold the cells that are allocated/deallocated
	int allocatedSegments[] = {};
	int availableSegments[] = {}; 
	
	//creates 10 memory segments
	memorySegment mem0 = new memorySegment(0,10);
	memorySegment mem1 = new memorySegment(1,10);
	memorySegment mem2 = new memorySegment(2,10);
	memorySegment mem3 = new memorySegment(3,10);
	memorySegment mem4 = new memorySegment(4,10);
	memorySegment mem5 = new memorySegment(5,10);
	memorySegment mem6 = new memorySegment(6,10);
	memorySegment mem7 = new memorySegment(7,10);
	memorySegment mem8 = new memorySegment(8,10);
	memorySegment mem9 = new memorySegment(9,10);
	
	//constructor for segment table
	segmentTable(){
		this.memoryArray[0] = this.mem0;
		this.memoryArray[1] = this.mem1;
		this.memoryArray[2] = this.mem2;
		this.memoryArray[3] = this.mem3;
		this.memoryArray[4] = this.mem4;
		this.memoryArray[5] = this.mem5;
		this.memoryArray[6] = this.mem6;
		this.memoryArray[7] = this.mem7;
		this.memoryArray[8] = this.mem8;
		this.memoryArray[9] = this.mem9;
	}
	
	//allocates a process to a memory cell
	public memorySegment allocateMemory(process procInput, int size) {
    	//The remaining size of the memory to be allocated for the process
        int remainingSize = size;
        //Loops through each memory segment in the array
        for (int i = 0; i < memoryArray.length; i++) {
        	//Gets a reference to the current memory segment
            memorySegment segment = memoryArray[i];
            //Checks if the current segment is not already allocated and if its size is greater than or equal to the requested size
            if (!segment.isAllocated() && segment.getSize() >= remainingSize) {
            	//Creates a new memory segment for the allocated memory with the remaining size
                memorySegment allocatedSegment = new memorySegment(segment.getStartAddress(), remainingSize);
                allocatedSegment.assignProcess(procInput);
                //Stores the allocated segment in the memory array
                memoryArray[i] = allocatedSegment;

                // Update the original segment to reflect the reduced size
                segment.setSize(segment.getSize() - remainingSize);
                segment.setStartAddress(allocatedSegment.getStartAddress() + remainingSize);

                return allocatedSegment;
                
            } else if (!segment.isAllocated()) {
                //If the segment is not allocated but doesn't have enough space,try to find the next available segment
                int availableSize = segment.getSize();
                remainingSize -= availableSize;

                //Creates a new segment for the remaining size
                memorySegment allocatedSegment = new memorySegment(segment.getStartAddress(), availableSize);
                allocatedSegment.assignProcess(procInput);
                //Stores the allocated segment in the memory array
                memoryArray[i] = allocatedSegment;

                //Updates the original segment to reflect that it's fully allocated
                segment.setSize(0);
                segment.allocate();
                //sets the offset of the memory cell
                allocatedSegment.setOffset((this.nextEmpty()*10)-(allocatedSegment.getStartAddress()*10));
                //If the remaining size is now 0, return the last allocated segment
                if (remainingSize == 0) {
                    return allocatedSegment;
                }
            }
        }

        return null; // No suitable segment found
    }
	
	//prints segment table
	public void printSegmentTable() {
		System.out.println("");
        System.out.println("Segment Table:");
        for (int i = 0; i < 10; i++) {
        	//Gets a reference to the current memory segment
            memorySegment segment = memoryArray[i];
            System.out.println("Physical Start Address: " + segment.getStartAddress() +
                    "0, Remaining Size: " + segment.getSize() +
                    ", Allocated: " + segment.isAllocated() +
                    ((segment.getProcessID() != -1) ? ", Process ID: " + segment.getProcessID()  : "") +
                    ((segment.getOffset() != -1) ? ", Offset: " + segment.getOffset() + " bytes away from current Start Address." : ""));
        }
    }

	//checks for empty segments in the table
	public void checkEmptySegments() {
		for(int i = 0; i <= 9; i++) {
			if(!(memoryArray[i].isAllocated())) {
				System.out.println("Memory Segment " + memoryArray[i].getStartAddress() + " is AVAILABLE.");
			} else {
				System.out.println("MEMORY SEGMENT " + memoryArray[i].getStartAddress() + " is ALLOCATED.");
			}
		}
		System.out.println("~");
	}
	
	//finds next empty cell
	public int nextEmpty() {
		for(int i = 0; i <= 9; i++) {
			if(!(memoryArray[i].isAllocated())) {
				availableSegments = Arrays.copyOf(availableSegments, availableSegments.length + 1);
				//returns next empty cell
				return memoryArray[i].getStartAddress();
			}
		}
		//if no cells are empty, then return -1
		return -1;
	}
	
	
	//empties a memory cell of its contents and toggles allocation flag to false
	public void emptyMemory(int procID) {
		//loops through all memory cells in array
		for(int i = 0; i < memoryArray.length; i++) {
			//if the memory cell is allocated
			if(memoryArray[i].isAllocated()) {
				//and if the memory cell contains a process with a matching process ID
				if(memoryArray[i].getProcessID() == procID) {
					//remove the process, deallocating the memory cell
					memoryArray[i].unassignProcess();
				}
			}
		}
	}
	
	//condenses memory
	public void condenseMemory() {
		// Checks segments and creates(populates) the allocated and available arrays.
		availableSegments = new int[] {};
		allocatedSegments = new int[] {};
		for(int i = 0; i <= 9; i++) {
			if(!(memoryArray[i].isAllocated())) {
				availableSegments = Arrays.copyOf(availableSegments, availableSegments.length + 1);
				availableSegments[availableSegments.length-1] = i;
			} else {
				allocatedSegments = Arrays.copyOf(allocatedSegments, allocatedSegments.length + 1);
				allocatedSegments[allocatedSegments.length-1] = i;
			}
		}
		
		memorySegment[] tempMemorySegment = new memorySegment[memSegInd];
		int outOfPlace = 0; //ammount of incorrectly positioned segments
		
		for(int i = 0; i<memSegInd; i++) {
			// If there are still allocated segments, put them at the beginning of the new temporary array
			if(i<allocatedSegments.length) {
				tempMemorySegment[i] = memoryArray[allocatedSegments[i]];

//				System.out.println(allocatedSegments[i]+ " is allocated ");
			} else { 
				// All the available segments are moved to the end of the array.
				tempMemorySegment[i] = memoryArray[availableSegments[i-allocatedSegments.length]];
				
				// If the segment is out of place, print where the correct place would be and add to the out of place counter.
				if(availableSegments[i-allocatedSegments.length] != i) {
//					System.out.println(availableSegments[i-allocatedSegments.length]+ " is available when " + i + " should be. \nCONDENSING!");
					outOfPlace++;
				} //else System.out.println(availableSegments[i-allocatedSegments.length]+ " is available.");
			}
		}
		
		// Reset the addresses to match their new location
		for(int i = 0; i<memSegInd; i++) {
			tempMemorySegment[i].setStartAddress(i);
		}
		
		// Print if there were any changes to the memory array.
		if(outOfPlace>0) System.out.println(outOfPlace + " segments condensed!\n\n");
		
		// replace memory array with the new condensed array
		memoryArray = new memorySegment[] {};
		memoryArray = tempMemorySegment;
	}
}
