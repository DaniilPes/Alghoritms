import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GenerateTest {
	//celkove hran
	int e = 0;
	//vygenerovane testy
	int[][] t;
	
	public GenerateTest(int v, int e, int f) {
		this.e = e;
		t = new int[f][(v*v - v)/2];
		if(e <= (v*v - v)/2) {
			for(int i = 0; i < f; i++) {
				System.arraycopy(setTest(v, e), 0, t[i], 0, (v*v - v)/2);
			}
		}
	}
	
	public int[] setTest(int v, int e) {
		int[] result = new int[(v*v - v)/2];
        Random random = new Random();
        Set<Integer> chosenIndices = new HashSet<>();

        // Generate unique random indices and set elements to 1
        while (chosenIndices.size() < e) {
            int randomIndex = random.nextInt((v*v - v)/2);
            chosenIndices.add(randomIndex);
        }

        for (int index : chosenIndices) {
            result[index] = 1;
        }

        return result;
	}
	
	public int[][] getTest(){
        return t;
	}
}
