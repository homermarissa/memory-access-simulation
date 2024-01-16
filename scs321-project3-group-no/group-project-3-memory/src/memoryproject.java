/*	Marissa Homer
 * Matthew Lewis
 * Shane Simpson
 * */

//imports arrayList
import java.util.ArrayList;

public class memoryproject {
	//main method
	public static void main(String[] args) {
		//creates processes for use
		process procA = new process(1,1,10,15);
		process procB = new process(2,7,15,16);
		process procC = new process(3,4,20,13);
		process procD = new process(4,10,10,18);
		process procE = new process(5,12,5,9);
		process procF = new process(6,13,5,15);
		
		//arraylist to hold processes
		ArrayList<process> incomingProcesses = new ArrayList<process>();
		//adds processes to the arraylist
		incomingProcesses.add(procA);
		incomingProcesses.add(procB);
		incomingProcesses.add(procC);
		incomingProcesses.add(procD);
		incomingProcesses.add(procE);
		incomingProcesses.add(procF);

		
		//creates segment table
		segmentTable segmentTable = new segmentTable();
	
		//timer value to indicate how many cycles to complete
		int timerVal = 26;
		//timer which loops through the processes in memory and works on them
		for(int i = 0; i < timerVal; i++) {
			//outputs system time
			System.out.println("\n=====\nSYSTEM TIME: " + i);
			//checks for empty segments
			System.out.println("Checking Empty Segments...\n~");
			//outputs empty segments to console
			segmentTable.checkEmptySegments();
			//loops through incoming processes
			for(int k = 0; k < incomingProcesses.size(); k++) {
				//if the size of the incomingProcess array is not 0
				if(incomingProcesses.size() != 0) {
					//and if the arrival time of any process in incomingProcesses is less than or equal to the current time
					if(incomingProcesses.get(k).getArrivalTime() <= i) {
						//allocate that process to memory and print
						System.out.println("Adding New Process...");
						allocateMemoryAndPrint(segmentTable, incomingProcesses.get(k), incomingProcesses.get(k).getSize());
						//remove the process from the incomingProcesses array
						incomingProcesses.remove(k);
					}	
				}
			}
			
segmentTable.condenseMemory();

			//creates buffer to hold process IDs so that a process is not worked on twice in a cycle
			ArrayList<Integer> workBuffer = new ArrayList<Integer>();
			//for each slot in the memory array
			for(int j = 0; j < segmentTable.memoryArray.length; j++) {
				//if the memory array value is allocated, meaning it is holding a process
				if(segmentTable.memoryArray[j].isAllocated()) {
					int currentID = segmentTable.memoryArray[j].getProcessID();
					//and if the process id has not been found in the work buffer
					if(!(workBuffer.contains(currentID))) {
						//declares new time for that process
						int newTime = segmentTable.memoryArray[j].getProcess().getRemainingTime()-1;
						//updates remaining time with new time
						segmentTable.memoryArray[j].getProcess().setRemainingTime(newTime);
						//adds that process id to working buffer to prevent it from being worked on twice in a cycle
						workBuffer.add(segmentTable.memoryArray[j].getProcessID());
					}
					//if the remaining time of a process is 0, empty it from memory
					if(segmentTable.memoryArray[j].getProcess().getRemainingTime() == 0) {
						//empties desired process from memory
						segmentTable.emptyMemory(currentID);
					}
				}
			}
			//prints the final segment table for this process
			System.out.println("\nSystem Cycle Completed.");
			segmentTable.printSegmentTable();
		}
		
	}


	//Helper method to allocate memory, print status, and print the segment table
    private static void allocateMemoryAndPrint(segmentTable segmentTable, process inputProc, int size) {
    	//Allocating memory
        System.out.println("Allocating memory for Process ID " + inputProc.getProcID() + " with size " + size);
        memorySegment allocatedSegment = segmentTable.allocateMemory(inputProc, size);
        //Printing status
        if (allocatedSegment != null) {
            System.out.println("Memory allocated: Start Address - " +
                    allocatedSegment.getStartAddress() + ", Size - " + allocatedSegment.getSize() +
                    ", Process ID - " + allocatedSegment.getProcessID());
        } else {
            System.out.println("Memory allocation failed for Process ID " + inputProc.getProcID() +
                    ". Insufficient memory or no suitable segment found.");
        }

        //Print the updated segment table
        segmentTable.printSegmentTable();
    }
}
