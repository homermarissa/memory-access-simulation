//memory segment
public class memorySegment {
	//start address of the memory segment
	private int startAddress;
	//size of the memory segment
    private int size;
    //boolean to determine whether a segment is allocated
    private boolean allocated;
    //holds process
    private process holdingProcess;
    //offset value, if available
    private int offset = -1;

    //start address getter
    public int getStartAddress() {
        return startAddress;
    }

    //size getter
    public int getSize() {
        return size;
    }
    
    //process id getter, retrieves process id from the currently held process if there is one
    public int getProcessID() {
    	if (holdingProcess != null) {
            return holdingProcess.getProcID();
        } else {
        	//ensures that the memory segment is marked as not allocated if it is not holding a process
        	this.allocated = false;
            // Handle the case when holdingProcess is null (return a default value or throw an exception)
            return -1; // or throw new IllegalStateException("holdingProcess is null");
        }
    }
  //gets the process currently being held by the memory segment
    public process getProcess() {
    	return holdingProcess;
    }

    //allocation status getter
    public boolean isAllocated() {
        return allocated;
    }

    //allocates the memory cell, marking allocated as true
    public void allocate() {
        allocated = true;
    }
    
    //deallocates the memory cell, marking allocated as false
    public void deallocate() {
        allocated = false;
    }
    
    //offset getter
    public int getOffset() {
    	return offset;
    }
    
    //offset setter
    public void setOffset(int setOffset) {
    	offset = setOffset;
    }
    
    //assigns a process to the memory cell, taking the process in as a parameter    
    public void assignProcess(process newProc) {
    	//sets this memory cell's process to be the parameter process
    	this.holdingProcess = newProc;
    	//sets the memory cell as allocated
    	this.allocate();
    }
    //unassigns a process from this memory cell
    public void unassignProcess() {
    	//sets this memory cell's process to null
    	this.holdingProcess = null;
    	//sets the memory cell as deallocated
    	this.deallocate();
    	//resets cell size
    	this.setSize(10);
    	//resets offset value
    	this.setOffset(-1);
    }   
    // Setter for size
    public void setSize(int newSize) {
        this.size = newSize;
    }
    
    //setter for start address
    public void setStartAddress(int newStartAddress) {
        this.startAddress = newStartAddress;
    }
    
    //memory segment constructor
    public memorySegment(int startAddress, int size) {
        this.startAddress = startAddress;
        this.size = size;
        this.allocated = false;
    }

}
