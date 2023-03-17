/*  Assignment 3, problem 2, Part a, Version 1
 * Attempt to implement non-reuseable barrier with busy wait and without locks and synchronization.
 * Wastes resources due the repeated checking of the 'inC==N' condition.
 * Appears to work, because the update to the shared data ('inC') is so simple that it ends up being uninteruptable.
 * DON'T DO THIS! DOES NOT WORK!
*/

class WorkerProb2Partav1 extends Thread {
	private int id;	
	private BarrierProb2Partav1 b;

	public WorkerProb2Partav1 (int id, BarrierProb2Partav1 b) {
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
		
class BarrierProb2Partav1 {
	private int inC=0;
	private final int N;
	
	public BarrierProb2Partav1(int n) {
		this.N=n;
	}	

	public void collect() {
		inC++;
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

public class A3Prob2Partav1 {
	public static final int N = 10;	
	public static void main(String[] args) throws InterruptedException {
		WorkerProb2Partav1[] Workers = new WorkerProb2Partav1[N];
		BarrierProb2Partav1 b = new BarrierProb2Partav1(N);
		System.out.println("Start of execution: "+N+" workers");
		for (int i=0; i < N; ++i) {
			Workers[i] = new WorkerProb2Partav1(i,b);
		}
		for (int i=0; i < N; ++i) {
			Workers[i].join();
		}
		System.out.println("End of execution");
	}
}
