/*  Assignment 3, Problem 2, Part b 
 * Implementation of non-reusable barrier using the barrier's intrinsic lock and wait/notify.
*/

class WorkerProb2Partb extends Thread {
	private int id;	
	private BarrierProb2Partb b;

	public WorkerProb2Partb (int id, BarrierProb2Partb b) {
		this.id = id;
		this.b = b;
		start();
	}
	public String indent() {
		String s="";
		for (int i=0; i<id; i++) 
			s = s+"                 ";
		return s;
	}
	public void run() {
		System.out.println(indent()+"worker "+id+" in w1");   // [NuSMV: w1]
		b.barrier();  									   	  // [NuSMV: bc1, bc2, bc3, bc4, bc5]
		System.out.println(indent()+"worker "+id+" in w2");   // [NuSMV: w2]
	}
}
		
class BarrierProb2Partb {
	private int inC=0;
	private final int N;
	
	public BarrierProb2Partb(int n) {
		this.N=n;
	}
		
	public synchronized void collect() {
		try {
			inC++;						// [NuSMV: bc1]
			if (inC==N) {               // [NuSMV: bc2]
				assert (inC==N);
				System.out.println("=== all arrived =======================");	
				notifyAll();  			// wake up 'waiting' threads
			}	
			else
				while (inC<N) wait();  	// put thread into 'waiting' state [NuSMV: bc4]
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void barrier() {
			assert (inC<N);
			collect();
			assert (inC==N);
	}
}

public class A3Prob2Partb {
    public static final int N = 5;
    public static void main(String[] args) throws InterruptedException {
	WorkerProb2Partb[] Workers = new WorkerProb2Partb[N];
	BarrierProb2Partb b = new BarrierProb2Partb(N);
	System.out.println("Start of execution: "+N+" workers");
	for (int i=0; i < N; ++i) {
	    Workers[i] = new WorkerProb2Partb(i,b);
	}
	for (int i=0; i < N; ++i) {
	    Workers[i].join();
	}
	System.out.println("End of execution");
    }
}
