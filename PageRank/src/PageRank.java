import java.util.List;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class PageRank {

	public static void main(String[] args) {
		double tau = 0.01;
		double lamb = 0.15;
		
		 if(args.length!=0){
	            tau = Double.parseDouble(args[0]);
	            lamb = Double.parseDouble(args[1]);
	        }

		HashMap<String,List> linksTo = new HashMap<String,List>();
		HashMap<String,List> linksOut = new HashMap<String,List>();
		HashSet<String> pages = new HashSet<String>();
		File file = new File ("links.srt");

		try {

			Scanner in = new Scanner(file);
			//in.useDelimiter("\\t");
			while (in.hasNextLine()){
				String line = in.nextLine();
				StringTokenizer st = new StringTokenizer(line); 
				while(st.hasMoreTokens()) { 

					String keySourcePage = st.nextToken();
					String targetPage = st.nextToken();

					pages.add(keySourcePage);
					pages.add(targetPage);

					if(linksTo.containsKey(keySourcePage)){
						List tmp = linksTo.get(keySourcePage);
						tmp.add(targetPage);
					}else{
						List tmp = new ArrayList();
						tmp.add(targetPage);
						linksTo.put(keySourcePage, tmp);
					}

					if(linksOut.containsKey(targetPage)){
						List tmp = linksOut.get(targetPage);
						tmp.add(keySourcePage);
					}else{
						List tmp = new ArrayList();
						tmp.add(keySourcePage);
						linksOut.put(targetPage, tmp);
					}
				}
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		
		inLinksTextFile(inLinksRank(linksOut));
		rankTextFile(rank(linksOut, pages, tau, lamb));


	}//end main

	static TreeMap<Integer,List> inLinksRank(HashMap<String,List> data){

		TreeMap<Integer, List> ret = new TreeMap<Integer,List>(Collections.reverseOrder());

		for (Entry<String, List> entry : data.entrySet()) {

			int numLinksIn = entry.getValue().size();
			String title = entry.getKey();

			if(ret.containsKey(numLinksIn)){
				List tmp = ret.get(numLinksIn);
				tmp.add(title);
			}else{
				List tmp = new ArrayList();
				tmp.add(title);
				ret.put(numLinksIn, tmp);
			}

		}

		return ret;

	}// end method inLinksRank

	static void inLinksTextFile(TreeMap<Integer,List> inLinksRank){
		int topN = 50;
		String ret ="";
		int n = 0;

		for (Entry<Integer, List> entry : inLinksRank.entrySet()) {
			List titles = entry.getValue();
			for( Object title : titles){
				n++;
				if(n==51){break;}
				ret+="title: "+title+ " rank: "+ n + " inlink count: " + entry.getKey();
				ret+="\r\n";
			}
			if(n==51){break;}
		}


		try {
			File report = new File("inlinks.txt");
			FileWriter writer = new FileWriter(report,true);
			writer.write(ret);
			writer.flush();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}


	static TreeMap<String,Double> rank(HashMap<String,List> linksTo, HashSet<String> pages, double tau, double lamb){

		TreeMap<String, Double> i = new TreeMap<String,Double>();
		TreeMap<String, Double> r = new TreeMap<String,Double>();

		TreeMap<String, Double> old = new TreeMap<String,Double>();

		for(String s : pages){
			double initialR = 1/(pages.size());
			i.put(s, initialR);
			r.put(s,0.0);
		}
           old.putAll(i);
		while(notConverged(tau,pages.size(),i,old)){

			for(String s : pages){
				double rd = lamb/(pages.size());
				r.put(s, rd);
			}
			
			for(String s : pages){

				List Q = linksTo.get(s);

				if ( Q != null && Q.size()>0 ){
					for(Object p : Q){
						double iq = i.get(p);
						double rq = r.get(p);

						double updateRQ =  (1-lamb)*(iq/Q.size());

						r.put((String) p, r.get(p)+updateRQ);

					}//end for
				}//end if
				else{

					for(String x : pages){
						double iq = i.get(x);
						double rq = r.get(x);

						double updateRQ =  (1-lamb)*(iq/pages.size());

						r.put((String) x, r.get(x)+updateRQ);
					}//end for
					
				}//end else

				
			}//end for
			
			old.putAll(i);
			i.putAll(r);
			
		}//end while


		return r;

	}// end method rank


	static boolean notConverged(double tau,int size,TreeMap<String, Double> r,TreeMap<String, Double> i){
		double n = 0;
		for(Entry<String, Double> entry : r.entrySet()) {
			  String key = entry.getKey();
			  Double value = entry.getValue();
			  Double value2 = i.get(key);

			 n+= (value2-value)*(value2-value);
			}
			n = Math.sqrt(n);
		
		return (n/size)<tau;
	}
	
	static void rankTextFile(TreeMap<String,Double> r){
		int topN = 50;
		String ret ="";
		int n = 0;

		for (Entry<String, Double> entry : r.entrySet()) {
				n++;
				if(n==51){break;}
				ret+="title: "+entry.getKey()+ " rank: "+ n + " score: "+ entry.getValue() ;
				ret+="\r\n";
			
		}


		try {
			File report = new File("pagerank.txt");
			FileWriter writer = new FileWriter(report,true);
			writer.write(ret);
			writer.flush();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	
}//end class
