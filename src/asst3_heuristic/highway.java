package asst3_heuristic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

//– Use ’0’ to indicate a blocked cell
//– Use ’1’ to indicate a regular unblocked cell
//– Use ’2’ to indicate a hard to traverse cell
//– Use ’a’ to indicate a regular unblocked cell with a highway
//– Use ’b’ to indicate a hard to traverse cell with a highway

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class highway {
	static char[][] map = new char[120][160];
	static boolean inter = false;
	static Node s = new Node();
	static Node g = new Node();

	static double w;

	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		System.out.print("Select # maps: ");
		Scanner mode = new Scanner(System.in);
		int sel = Integer.parseInt(mode.nextLine());
		
		System.out.print("Select # paths: ");
		int mpat = Integer.parseInt(mode.nextLine());
		
		ArrayList<Node> starts = new ArrayList<Node>();
		ArrayList<Node> goals = new ArrayList<Node>();

		Scanner wt = new Scanner(System.in);
		System.out.print("Enter weight: ");
		w = Double.parseDouble(wt.nextLine());

		for (int b = 0; b < sel; b++) {

			for (char[] r : map) {
				Arrays.fill(r, '1');
				// System.out.println(Arrays.toString(r));
			}

			// hard traversals
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

			// highways
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
				// System.out.println("start" + r[0] + " " + r[1]);
			}

			// blocked cells
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

			// start and goal
			int xg, yg;

			for (int p = 0; p < mpat; p++) {
				do {
					if (rand.nextInt(2) == 1) {
						xg = rand.nextInt(20) + rand.nextInt(2) * 100;
						yg = rand.nextInt(160);
						s.x = xg;
						s.y = yg;
					} else {
						xg = rand.nextInt(120);
						yg = rand.nextInt(20) + rand.nextInt(2) * 140;
						s.x = xg;
						s.y = yg;
					}

					if (rand.nextInt(2) == 1) {
						xg = rand.nextInt(20) + rand.nextInt(2) * 100;
						yg = rand.nextInt(160);
						g.x = xg;
						g.y = yg;
					} else {
						xg = rand.nextInt(120);
						yg = rand.nextInt(20) + rand.nextInt(2) * 140;
						g.x = xg;
						g.y = yg;
					}
				} while (dist(s, g) < 100 || map[s.x][s.y] == '0' || map[g.x][g.y] == '0');
				
				//for many paths
				
				starts.add(new Node(s.x,s.y));
				goals.add(new Node(g.x,g.y));
				
				//System.out.println(g.toString());
			}//comment
			
			
			// to file
			try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("map.txt"), "utf-8"))) {
				writer.write(s.toString() + System.lineSeparator());
				writer.write(g.toString() + System.lineSeparator());
				for (int[] r : hardtravs) {
					writer.write(Arrays.toString(r) + System.lineSeparator());
				}
				for (char[] r : map) {
					writer.write(Arrays.toString(r) + System.lineSeparator());
					// System.out.println(Arrays.toString(r));
				}
			}

			for (int u = 0; u < mpat; u++) {//comment
				long startTime = System.currentTimeMillis();
				//for many paths
				
				Node stemp = starts.get(u);
				s.x = stemp.x;
				s.y = stemp.y;
				
				Node gtemp = goals.get(u);
				g.x = gtemp.x;
				g.y = gtemp.y;
				
				if (!aStar(s, g)) {
					System.out.println("no path");
					return;
				}

				// draw spath
				// for(Node q: spath){
				// map[q.x][q.y] = 'p';
				// }

				Node next = g.parent;
				int spct = 0;
				while (next != s) {
					map[next.x][next.y] = 'z';
					next = next.parent;
					spct++;
				}

				System.out.println(spath.size() + " expansions");
				System.out.println("solution=" + spct + " moves");
				
				long endTime = System.currentTimeMillis();
				
				System.out.println("Time: " + (endTime-startTime));

				map[s.x][s.y] = 's';
				map[g.x][g.y] = 'g';

				draw();
			}//comment
			// System.out.println(starts.size());

		}

		Scanner scan = new Scanner(System.in);

		while (true) {
			System.out.print("row: ");
			int r = Integer.parseInt(scan.nextLine());
			System.out.print("column: ");
			int c = Integer.parseInt(scan.nextLine());
			Node temp = new Node(r, c);
			if (spath.contains(temp)) {
				temp = spath.get(spath.indexOf(temp));
				System.out.println("h: " + temp.h + " g: " + temp.g + " f: " + temp.f);
			}
		}
	}

	public static ArrayList<Node> spath = new ArrayList<Node>();

	public static double cost(int[] s, int[] g) {
		// regular unblocked
		if (map[s[0]][s[1]] == '1' && map[g[0]][g[1]] == '1') {
			if (s[0] == g[0] || s[1] == g[1]) {
				return 1;
			} else {
				return Math.sqrt(2);
			}
		}
		// hard to traverse
		if (map[s[0]][s[1]] == '2' && map[g[0]][g[1]] == '2') {
			if (s[0] == g[0] || s[1] == g[1]) {
				return 2;
			} else {
				return Math.sqrt(8);
			}
		}
		// unblocked to hard
		if ((map[s[0]][s[1]] == '1' && map[g[0]][g[1]] == '2') || (map[s[0]][s[1]] == '2' && map[g[0]][g[1]] == '1')) {
			if (s[0] == g[0] || s[1] == g[1]) {
				return 1.5;
			} else {
				return (Math.sqrt(2) + Math.sqrt(8)) / 2;
			}
		}

		// highways
		// regular unblocked
		if (map[s[0]][s[1]] == 'a' && map[g[0]][g[1]] == 'a') {
			return .25;
		}
		// hard to traverse
		if (map[s[0]][s[1]] == 'b' && map[g[0]][g[1]] == 'b') {
			return .5;
		}
		// unblocked to hard
		if ((map[s[0]][s[1]] == 'a' && map[g[0]][g[1]] == 'b') || (map[s[0]][s[1]] == 'b' && map[g[0]][g[1]] == 'a')) {
			return .375;
		}

		return 0;
	}

	public static ArrayList<int[]> createHighway(int x, int y, int b) {
		ArrayList<int[]> hway = new ArrayList<int[]>();
		Random rand = new Random();
		// int[] dirs = { 0, 1, -1 };
		int dir = 0;
		int i, j, ct = 0, to = 0;

		while (ct < 100 || inter) {
			inter = false;
			i = x;
			j = y;
			ct = 0;
			if (to > 20000)
				break;
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
					} else if (b == 3) {
						i += dir;
						if (dir == 0)
							j++;
					}
					if (i == -1 || i == 120 || j == -1 || j == 160)
						break;
					int[] pt = { i, j };
					if (map[i][j] == 'a' || map[i][j] == 'b')
						inter = true;
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
					// System.out.println("new direction " + dir);
				}
				to++;
				if (to > 20000)
					break;
			} while (i >= 0 && i < 120 && j >= 0 && j < 160);
		}

		return hway;
	}

	public static double g(Node g) {
		return dist(s, g);
	}

	public static double h(Node h) {
		return w * dist(h, g);
	}

	public static Node succ(Node su) {
		int i = su.x;
		int j = su.y;
		Node n;
		if (map[i][j] != '0') {
			for (int k = i - 1; k <= i + 1; k++) {
				for (int m = j - 1; m <= j + 1; m++) {
					if (k >= 0 && k < 120 && m >= 0 && m < 160) {
						if (!(i == k && j == m) && map[k][m] != '0') {
							// System.out.println(i + " " + j);
							n = new Node(k, m);
							su.neighbors.add(n);
						}
					}
				}
			}
		}
		return su;
	}

	static PriorityQueue<Node> open = new PriorityQueue<Node>(20, (a, b) -> Double.compare(a.f, b.f));

	public static boolean aStar(Node start, Node goal) {
		spath.clear();
		ArrayList<Node> closed = new ArrayList<Node>();
		start.g = 0;
		start.parent = start;

		// System.out.println(dist(start,goal));
		start.f = start.g + dist(start, goal);
		open.add(start);

		Node curr = new Node();
		while (!open.isEmpty()) {
			curr = open.poll();
			// spath.add(curr);
			// System.out.println(curr.toString());
			if (curr.equals(goal)) {
				goal.parent = curr;
				return true;
			}
			closed.add(curr);
			curr = succ(curr);

			for (Node s : curr.neighbors) {
				// System.out.println(s.toString());
				if (!closed.contains(s)) {
					if (!open.contains(s)) {
						s.g = Double.POSITIVE_INFINITY;
						s.parent = null;
					}
					UpdateVertex(curr, s);
				}
			}
		}

		return false;
	}

	public static void UpdateVertex(Node curr, Node s) {
		int[] to = { curr.x, curr.y };
		int[] from = { s.x, s.y };
		if (g(curr) + cost(to, from) < s.g) {
			s.g = g(curr) + cost(to, from);
			s.parent = curr;
			spath.add(curr);
			if (open.contains(s)) {
				open.remove(s);
			}
			s.h = h(s);
			s.f = g(s) + h(s);
			open.add(s);
			// System.out.println(s+" added to fringe");
		}
	}

	public static double dist(Node s, Node g) {
		return Math.sqrt(Math.pow(g.x - s.x, 2) + Math.pow(g.y - s.y, 2));
	}

	public static void draw() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(120, 160));
		JLabel[][] grid = new JLabel[120][160];
		for (int i = 0; i < 120; i++) {
			for (int j = 0; j < 160; j++) {
				grid[i][j] = new JLabel();
				grid[i][j].setBorder(new LineBorder(Color.BLACK));
				switch (map[i][j]) {
				case '1':
					grid[i][j].setBackground(Color.lightGray);
					break;
				case '0':
					grid[i][j].setBackground(Color.black);
					break;
				case 'a':
					grid[i][j].setBackground(Color.blue);
					break;
				case 'b':
					grid[i][j].setBackground(Color.cyan);
					break;
				case '2':
					grid[i][j].setBackground(Color.orange);
					break;
				case 's':
					grid[i][j].setBackground(Color.red);
					break;
				case 'g':
					grid[i][j].setBackground(Color.green);
					break;
				case 'p':
					grid[i][j].setBackground(Color.pink);
					break;
				case 'z':
					grid[i][j].setBackground(Color.magenta);
					break;
				}
				grid[i][j].setOpaque(true);
				panel.add(grid[i][j]);
			}
		}
		JFrame frame = new JFrame("Map");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(900, 700));
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
}
