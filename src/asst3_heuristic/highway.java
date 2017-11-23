package asst3_heuristic;

import java.util.ArrayList;

//– Use ’0’ to indicate a blocked cell
//– Use ’1’ to indicate a regular unblocked cell
//– Use ’2’ to indicate a hard to traverse cell
//– Use ’a’ to indicate a regular unblocked cell with a highway
//– Use ’b’ to indicate a hard to traverse cell with a highway

import java.util.Arrays;
import java.util.Random;

public class highway {
	static char[][] map = new char[120][160];
	
	public static void main(String[] args) {
		for (char[] r : map) {
			Arrays.fill(r, '1');
			// System.out.println(Arrays.toString(r));
		}
		int[][] hardtravs = new int[8][2];
		Random rand = new Random();
		for (int[] r : hardtravs) {
			r[0] = rand.nextInt(120);
			r[1] = rand.nextInt(160);
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[i].length; j++) {
					if ((i <= r[0] + 15 && i >= r[0] - 15) && (j <= r[1] + 15 && j >= r[1] - 15)) {
						if (rand.nextInt(2) == 1)
							map[i][j] = '2';
					}
				}
			}
			// System.out.println(Arrays.toString(r));
		}

		int[][] paths = new int[4][2];
		int bound;
		ArrayList<char[]> highway;
		for (int[] r : paths) {
			bound = rand.nextInt(4);
			if (bound == 0) {
				r[0] = 0;
				r[1] = rand.nextInt(160);
				highway = createHighway(r[0],r[1], bound);
			} else if (bound == 1) {
				r[0] = rand.nextInt(120);
				r[1] = 159;
			} else if (bound == 2) {
				r[0] = 119;
				r[1] = rand.nextInt(160);
			} else {
				r[0] = rand.nextInt(120);
				r[1] = 0;
			}
			
			
		}
		
		int x,y;
		int ct = 0;
		while(ct != 3840) {
			x = rand.nextInt(120);
			y = rand.nextInt(160);
			if(!(map[x][y]=='0')){
				map[x][y] = '0';
				ct++;
			}
		}

		for (char[] r : map) {
			System.out.println(Arrays.toString(r));
		}
	}
	
	public static ArrayList<char[]> createHighway(int x, int y, int b){
		ArrayList<char[]> hway = new ArrayList<char[]>();
		Random rand = new Random();
		int dir = rand.nextInt(3);
		
		return hway;
	}
}
