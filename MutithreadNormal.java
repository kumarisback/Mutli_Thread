
	
	import java.util.concurrent.ExecutorService;
	import java.util.concurrent.Executors;
	import java.util.concurrent.Future;
	import java.util.concurrent.Callable;
	
	public class MultiThread
	{
	   
	    public static String formatTime(long milliseconds) {
	        long seconds = milliseconds / 1000;
	        long minutes = seconds / 60;
	        long hours = minutes / 60;
	
	        return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60);
	    }
	    
	    
		public static void main(String[] args) {
		    var limit= 100000000;
		    var noOfTread=10;
		    var perLimit= limit/noOfTread;
		    long startTime = System.currentTimeMillis();
		     
		     
		     ExecutorService execute= Executors.newFixedThreadPool(noOfTread);
		     Future<Integer>[] f = new Future[noOfTread];
		     PrimeCounter[] primeCounters = new PrimeCounter[noOfTread];
		     
		     
		     for(int i=0;i<noOfTread;i++){
//		         var start=i*noOfTread+1;
//		         var end=(i == noOfTread - 1) ? limit : (i + 1) * perLimit;
//		          primeCounters[i] = new PrimeCounter(start, end);
//		         f[i]= execute.submit(primeCounters[i]);
		         
		         
		         var start = i * perLimit + 1;
		         var end = (i == noOfTread - 1) ? limit : (i + 1) * perLimit;

		         primeCounters[i] = new PrimeCounter(start, end);
		         f[i] = execute.submit(primeCounters[i]);
		     }
		     var totalPrimes=0;
		     
		      try {
	           for (int i = 0; i < noOfTread; i++) {
	        totalPrimes += f[i].get();
	        System.out.println("Thread " + i + " execution time: " + formatTime( primeCounters[i].getTime()));
	    }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		     long endTime = System.currentTimeMillis();
		     System.out.println(formatTime(endTime-startTime));
		     execute.shutdown();
		}
	}
	
	class PrimeCounter implements Callable<Integer>  {
	    private int start;
	    private int end;
	    private int treadTime;
	
	    public PrimeCounter(int start, int end) {
	        this.start = start;
	        this.end = end;
	    }
	
	    @Override
	    public Integer call() {
	        
	          long startTime = System.currentTimeMillis();
	
	        int count = countPrimes(start, end);
	
	        long endTime = System.currentTimeMillis();
	        this.treadTime=(int)(endTime-startTime);
	        
	        return count;
	    }
	
	    public int getTime(){ return treadTime;}
	    private static int countPrimes(int start, int end) {
	        int count = 0;
	
	        for (int i = start; i <= end; i++) {
	            if (isPrime(i)) {
	                count++;
	            }
	        }
	
	        return count;
	    }
	
	    private static boolean isPrime(int number) {
	        if (number <= 1) {
	            return false;
	        }
	
	        for (int i = 2; i <= Math.sqrt(number); i++) {
	            if (number % i == 0) {
	                return false;
	            }
	        }
	
	        return true;
	    }
	}
