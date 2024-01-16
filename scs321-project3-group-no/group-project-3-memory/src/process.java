//process
public class process {
	//process variables
	//id for process
	private int procID;
	//arrival time for process
	private int arrivalTime;
	//remaining time for process
	private int remainingTime;
	//desired runtime for process
	private int burstTime;
	//size of process
	private int size;
	
	//gets process id
	public int getProcID() {
		return procID;
	}
	//sets process id
	public void setProcID(int setID) {
		procID = setID;
	}
	//gets arrival time
	public int getArrivalTime() {
		return arrivalTime;
	}
	//sets arrival time
	public void setArrivalTime(int setArrival) {
		arrivalTime = setArrival;
	}
	//gets remaining time
	public int getRemainingTime() {
		return remainingTime;
	}
	//sets remaining time
	public void setRemainingTime(int setRemaining) {
		remainingTime = setRemaining;
	}
	//gets burst time
	public int getBurstTime() {
		return burstTime;
	}
	//sets burst time
	public void setBurstTime(int setBurst) {
		burstTime = setBurst;
	}
	//gets priority
	public int getSize() {
		return size;
	}
	//sets priority
	public void setSize(int setSize) {
		size = setSize;
	}
	//prints process
	public void printProcess() {
		System.out.println("Process ID: " + procID + "\nArrivalTime: " + arrivalTime + "\nBurst Time: " + burstTime + "\nRemaining Time: " + "\nSize: " + size + "\n-----");
	}
	//process constructor
	public process(int iprocID, int iarrivalTime, int iburstTime, int isize) {
		procID = iprocID;
		arrivalTime = iarrivalTime;
		burstTime = iburstTime;
		remainingTime = burstTime;
		size = isize;
		
		
		
	}
}
