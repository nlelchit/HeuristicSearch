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
		ArrayList<int[]> highway;
		for (int[] r : paths) {
			bound = rand.nextInt(4);
			if (bound == 0) {
				r[0] = 0;
				r[1] = rand.nextInt(160);
				highway = createHighway(r[0], r[1], bound);
				for (int[] c : highway) {
					if (map[c[0]][c[1]] == '2') {
						map[c[0]][c[1]] = 'b';
					} else {
						map[c[0]][c[1]] = 'a';
					}
				}
			} else if (bound == 1) {
				r[0] = rand.nextInt(120);
				r[1] = 159;
				highway = createHighway(r[0], r[1], bound);
				for (int[] c : highway) {
					if (map[c[0]][c[1]] == '2') {
						map[c[0]][c[1]] = 'b';
					} else {
						map[c[0]][c[1]] = 'a';
					}
				}
			} else if (bound == 2) {
				r[0] = 119;
				r[1] = rand.nextInt(160);
				highway = createHighway(r[0], r[1], bound);
				for (int[] c : highway) {
					if (map[c[0]][c[1]] == '2') {
						map[c[0]][c[1]] = 'b';
					} else {
						map[c[0]][c[1]] = 'a';
					}
				}
			} else {
				r[0] = rand.nextInt(120);
				r[1] = 0;
				highway = createHighway(r[0], r[1], bound);
				for (int[] c : highway) {
					if (map[c[0]][c[1]] == '2') {
						map[c[0]][c[1]] = 'b';
					} else {
						map[c[0]][c[1]] = 'a';
					}
				}
			}

		}

		int x, y;
		int ct = 0;
		while (ct != 3840) {
			x = rand.nextInt(120);
			y = rand.nextInt(160);
			if (map[x][y] == 'b' || map[x][y] == 'a')
				continue;
			if (!(map[x][y] == '0')) {
				map[x][y] = '0';
				ct++;
			}
		}

		for (char[] r : map) {
			System.out.println(Arrays.toString(r));
		}
	}

	public static ArrayList<int[]> createHighway(int x, int y, int b) {
		ArrayList<int[]> hway = new ArrayList<int[]>();
		Random rand = new Random();
		int[] dirs = { 0, 1, -1 };
		int dir = dirs[rand.nextInt(3)];
		int i, j, ct = 0;
		boolean inter = false;
		while (ct < 100 && !inter) {
			i = x;
			j = y;
			ct = 0;
			hway = new ArrayList<int[]>();
			do {
				for (int p = 0; p < 20; p++) {
					if (b == 0) {
						j += dir;
						if (dir == 0)
							i++;
					} else if (b == 1) {
						i += dir;
						if (dir == 0)
							j--;
					} else if (b == 2) {
						j += dir;
						if (dir == 0)
							i--;
					} else {
						i += dir;
						if (dir == 0)
							j++;
					}
					if (i == -1 || i == 120 || j == -1 || j == 160)
						break;
					int[] pt = { i, j };
					if(map[i][j]=='a' || map[i][j]=='b') inter = true;
					hway.add(pt);
					ct++;
					// System.out.println(i + " " + j);
				}

				if (rand.nextInt(10) < 4) {
					ArrayList<Integer> newdir = new ArrayList<Integer>();
					newdir.add(0);
					newdir.add(1);
					newdir.add(-1);
					newdir.remove(Integer.valueOf(dir));
					dir = newdir.get(rand.nextInt(2));
				}
			} while (i >= 0 && i < 120 && j >= 0 && j < 160);
		}

		return hway;
	}
}
