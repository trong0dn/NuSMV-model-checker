/*  Assignment 3, Problem 2, Part c and d
 * Failed attempts to make non-reusable barrier reusable.
 * Part c  don't reset 'inC' at all
 * Part d  reset 'inC' when all threads have arrived, i.e., when 'inC==N'
*/

class WorkerProb2Partcandd extends Thread {
	private int id;	
	private BarrierProb2Partcandd b;

	public WorkerProb2Partcandd (int id, BarrierProb2Partcandd b) {
		extracted(id);
		this.b = b;
		start();
	}
	private void extracted(int id) {
		this.id = id;
	}
	public String indent() {
		String s="";
		for (int i=0; i<id; i++) 
			s = s+"                 ";
		return s;
	}
	public void run() {
		System.out.println(indent()+"Worker "+id+":w1");
		b.barrier();  // barrier 1
		System.out.println(indent()+"Worker "+id+":w2");
		b.barrier();  // barrier 2
		System.out.println(indent()+"Worker "+id+":w3");
	}
}
		
class BarrierProb2Partcandd {
	private int inC=0;
	private final int N;
	
	public BarrierProb2Partcandd(int n) {
		this.N=n;
	}
	
	public synchronized void collect() {
		try {
			inC++;
			if (inC==N) {
				assert (inC==N);
//				inC=0;  // uncomment for Problem 2 Part d
				System.out.println("=== all arrived =======================");	
				notifyAll();
			}	
			else
				while (inC<N) 
					wait();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void barrier() {
			// assert (inC<N);  
			collect();
			// assert(inC==N);   
	}
}

public class A3Prob2Partcandd {
	public static final int N = 5;
	public static void main(String[] args) throws InterruptedException {
		WorkerProb2Partcandd[] Workers = new WorkerProb2Partcandd[N];
		BarrierProb2Partcandd b = new BarrierProb2Partcandd(N);
		System.out.println("Start of execution: "+N+" workers");
		for (int i=0; i < N; ++i) {
			Workers[i] = new WorkerProb2Partcandd(i,b);
		}
		for (int i=0; i < N; ++i) {
			Workers[i].join();
		}
		System.out.println("End of execution");
	}
}
