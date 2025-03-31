import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CliqueFinder {
	int[][] m;
	int[] score;
	int[] vOrded;
	int max;
	int largestFound = 0;
	boolean foundLargest = false;
	int[] largestClique;
	
	public CliqueFinder(int[][] m) {
		this.m = m;
		this.vOrded = new int[m.length];
		for(int i = 0; i < m.length; i++) vOrded[i] = i + 1;
	}
	
	public boolean isNull() {
		if(m.length == 0) return true;
		return false;
	}
	
	public boolean isEmpty() {
		for(int i = 0; i < m.length; i++) if(score[i] != 0) return false;
		return true;
	}
	
	public void setScore() {
		score = Arrays.stream(m).mapToInt(row -> Arrays.stream(row).sum()).toArray();
	}
	
	public int[] filterTrivial(){
		int[] tmp = new int[2];
		boolean fix = false;
		for(int i = 0; i < score.length; i++) {
			if(score[i] == 1) {
				fix = true;
				tmp[0] = i + 1;
				for(int j = 0; j < score.length; j++) {
					if(m[i][j] == 1) {
						tmp[1] = j + 1;
						break;
					}
				}
				break;
			}
		}
		
		while(fix) {
			fix = false;
			for(int i = 0; i < score.length; i++) {
				if(score[i] == 1) {
					fix = true;
					for(int j = 0; j < score.length; j++) {
						if(m[i][j] == 1) {
							m[i][j] = 0;
							m[j][i] = 0;
							score[i]--;
							score[j]--;
						}
					}
				}
			}
		}
		return tmp;
	}
	
	public void sortVerticesByScore() {
		int[] tempScore = new int[score.length];
		System.arraycopy(score, 0, tempScore, 0, score.length);
		for(int i = 0; i < score.length - 1; i++) {
			for(int j = 0; j < score.length - i - 1; j++) {
				if(tempScore[j] < tempScore[j + 1]) {
					int temp1 = tempScore[j + 1];
					int temp2 = vOrded[j + 1];
					tempScore[j + 1] = tempScore[j];
					vOrded[j + 1] = vOrded[j];
					tempScore[j] = temp1;
					vOrded[j] = temp2;
				}
			}
		}
	}
	
	public void setMax() {
		for(int i = 0; i < score.length; i++) {
			int temp = score[i];
			if(score[temp]  >= temp) {
				max = temp + 1;
				return;
			}
		}
		max = -1;
		return;
	}
	
	public boolean isEmpty(boolean[] x) {
		for(boolean a : x) if(a) return false;
		return true;
	}
	
	public void BK(int[] r, int[] p, boolean[] x) {
		if(foundLargest) return;
		if(r.length >= max) {
			largestClique = r;
			foundLargest = true;
			return;
		}
		if(r.length > largestFound) {
			largestClique = r;
			largestFound = r.length;
		}
		if(p.length == 0 && isEmpty(x)) return;
		for(int i  = 0; i < p.length; i++) {
			if(largestFound > score[p[i] - 1]) {
				foundLargest = true;
				return;
			}
			int[] tmp = Arrays.copyOf(r, r.length + 1);
			tmp[r.length] = p[i];
			r = tmp;
			BK(r, getNewP(p, p[i]), getNewX(x, p[i]));
			x[p[i] - 1] = true;
			int[] p2 = new int[p.length - 1];
			System.arraycopy(p, 0, p2, 0, i);
			System.arraycopy(p, i + 1, p2, i, p.length - i - 1);
			p = p2;			
		}
		
	}
	
	public int[] getNewP(int[] a, int b) {
		List<Integer> p = new ArrayList<Integer>(0);
		for(int i : a) if(m[i - 1][b - 1] == 1) p.add(i);
		return p.stream().mapToInt(Integer::intValue).toArray();
	}
	
	public boolean[] getNewX(boolean[] a, int b) {
		boolean[] c = new boolean[a.length];
		for(int i = 0; i < c.length; i ++) c[i] = false;
		for(int i = 0; i < c.length; i++) if(m[b - 1][i] == 1 && a[i]) c[i] = true;
		return c;
	}
	
	public int[] getLargestClique() {
		if(isNull()) return new int[0];
		setScore();
		if(isEmpty()) {
			int[] c = {1};
			return c;
		}
		int[] c = filterTrivial();
		if(isEmpty()) return c;
		sortVerticesByScore();
		setMax();
		BK(new int[0], vOrded, new boolean[vOrded.length]);
		return largestClique;
		
	}
}
