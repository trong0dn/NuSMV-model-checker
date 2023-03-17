/* Assignment 3, Problem 3
 * Reusable (cyclic) barrier with double turnstile using the barrier's intrinsic lock.
 * Works.
*/ 

class WorkerProb3 extends Thread {
	private int id;	
	private BarrierProb3 b;
	public WorkerProb3 (int id, BarrierProb3 b) {
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
		while (true) {
			System.out.println(indent()+"worker "+id+" in w1");
			b.barrier();  // barrier 1
			System.out.println(indent()+"worker "+id+" in w2");
			b.barrier();  // barrier 2
			System.out.println(indent()+"worker "+id+" in w3");
			b.barrier();  // barrier 3
		}
	}
}
		
class BarrierProb3 {
	private int inC=0;
	private int outC=0;
	private final int N;
	
	public BarrierProb3(int n) {
		this.N=n;
	}
	
	public synchronized void collect() {
		try {
			inC++;
			if (inC==N) {
				assert (inC==N & outC==0);
				outC=N;
				assert (inC==N & outC==N);
				System.out.println("=== all arrived =======================");	
				notifyAll();
			}	
			else
				while (inC<N) 
					wait();
			assert (inC==N);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void release() {
		try {
			outC--;
			if (outC==0) {
				assert (inC==N & outC==0);
				inC=0;
				assert (inC==0 & outC==0);
				System.out.println("=== all departed ======================");	
				notifyAll();
			}
			else 
				while (outC>0) wait();
			assert (outC==0);  
		}
		catch (InterruptedException e){
				e.printStackTrace();
		}
	}
	
	public void barrier() {
		assert (inC<N & outC==0);
		collect();	// first turnstile
		assert (inC==N & outC>0);
		release();	// second turnstile
		assert (inC<N & outC==0);
	}
}

public class A3Prob3 {	
    public static final int N = 5;	
    public static void main(String[] args) throws InterruptedException {
	WorkerProb3[] Workers = new WorkerProb3[N];
	BarrierProb3 b = new BarrierProb3(N);
	System.out.println("Start of execution: "+N+" workers");
	for (int i=0; i < N; ++i) {
	    Workers[i] = new WorkerProb3(i,b);
	}
	for (int i=0; i < N; ++i) {
	    Workers[i].join();
	}
	System.out.println("End of execution");
    }
}
