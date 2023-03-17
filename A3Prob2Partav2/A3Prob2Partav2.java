/* Assignment 3, Problem 2, Part a, Version 2
 * Attempt to implement non-reuseable barrier with busy wait and without locks and synchronization. 
 * Wastes resources due the repeated checking of the 'inC==N' condition.
 * Likely to deadlock, because update to shared data ('inC') is complex enough to allow for race conditions.
 * DON'T DO THIS! DOES NOT WORK!
*/

class WorkerProb2Partav2 extends Thread {
	private int id;	
	private BarrierProb2Partav2 b;

	public WorkerProb2Partav2 (int id, BarrierProb2Partav2 b) {
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
		System.out.println(indent()+"worker "+id+" in w1");
		b.barrier();  // barrier 
		System.out.println(indent()+"worker "+id+" in w2");
	}
}
		
class BarrierProb2Partav2 {
	private int inC=0;
	private final int N;
	
	public BarrierProb2Partav2(int n) {
		this.N=n;
	}
	
	public void collect() {
		int tmp;
		tmp=inC;
		tmp=tmp+1;
		for (int i=0; i<1000; i++);  // simulating longer computation; if interrupted here 'inC' might end up taking on the wrong value
		inC=tmp;
		if (inC==N) {
			assert (inC==N);
			System.out.println("=== all arrived =======================");	
		}	
		else
			while (inC<N) {
				System.out.println("waiting");
			}
	}	
	
	public void barrier() {
		assert (inC<N);
		collect();
		assert (inC==N);
	}
}

public class A3Prob2Partav2 {
	public static final int N = 10;	
	public static void main(String[] args) throws InterruptedException {
		WorkerProb2Partav2[] Workers = new WorkerProb2Partav2[N];
		BarrierProb2Partav2 b = new BarrierProb2Partav2(N);
		System.out.println("Start of execution: "+N+" workers");
		for (int i=0; i < N; ++i) {
			Workers[i] = new WorkerProb2Partav2(i,b);
		}
		for (int i=0; i < N; ++i) {
			Workers[i].join();
		}
		System.out.println("End of execution");
	}
}
