import org.jgrapht.*;
import org.jgrapht.alg.clique.BronKerboschCliqueFinder;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

public class CliqueTester {
	public static void main(String[] args) {
		int v = 225;
		int f = 20;
		long[] jG = new long[(v*v - v)/2];
		long[] muj = new long[(v*v - v)/2];
		int[][] u;
		Graph<Integer, DefaultEdge> g;
		BronKerboschCliqueFinder<Integer, DefaultEdge> BK;
		CliqueFinder cf;
		for(int i = 0; i < (v*v - v)/2; i++) {
			GenerateTest test = new GenerateTest(v, i, f);
			for(int j = 0; j < f; j++) {
				u = convertToMatrix((test.getTest()[j]), v);
				g = setGraph(u);
				//zacne merit
				long time = System.currentTimeMillis();
				BK = new BronKerboschCliqueFinder<>(g);
				BK.maximumIterator();
				jG[i] += System.currentTimeMillis() - time;
			}
			for(int j = 0; j < f; j++) {
				u = convertToMatrix((test.getTest()[j]), v);
				//zacne merit
				long time = System.currentTimeMillis();
				cf = new CliqueFinder(u);
				cf.getLargestClique();
				muj[i] += System.currentTimeMillis() - time;
			}
			
		}
		System.out.println("JGrapht : ");
		for(long l : jG) System.out.println(l);
		System.out.println("muj : ");
		for(long l : muj) System.out.println(l);
		
	}
	
	public static int[][] convertToMatrix(int[] m, int v){
		int[][] u = new int[v][v];
        int k = 0; //index

        for (int i = 0; i < v; i++) {
            for (int j = i + 1; j < v; j++) {
                u[i][j] = m[k];
                u[j][i] = m[k]; 
                k++;
            }
        }

        return u;
	}
	public static Graph<Integer, DefaultEdge> setGraph(int[][] m){
		Graph<Integer, DefaultEdge> g = new DefaultUndirectedGraph<>(DefaultEdge.class);
		for(int i = 0; i < m.length; i++) {
			g.addVertex(i + 1);
		}
		for(int i = 0; i < m.length; i++) {
			for(int j = i + 1; j < m.length; j++) {
				if(m[i][j] == 1) g.addEdge(i + 1, j + 1);
			}
		}
		return g;
	}
	
}
