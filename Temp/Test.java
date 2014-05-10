import java.io.*;
import java.util.*;

public class Test {
	private static long memCounter$1 = 0;
	private static long tempCounter1 = 0;
	private static long memCounter$2 = 0;
	private static long tempCounter2 = 0;
	private static long memCounter$3 = 0;
	private static long tempCounter3 = 0;
	private static long memCounter$4 = 0;
	private static long tempCounter4 = 0;
	private static long memCounter$5 = 0;
	private static long tempCounter5 = 0;
	private static long memCounter$6 = 0;
	private static long tempCounter6 = 0;
	private static long memCounter$7 = 0;
	private static long tempCounter7 = 0;
	private static long memCounter$8 = 0;
	private static long tempCounter8 = 0;
	private static long memCounter$9 = 0;
	private static long tempCounter9 = 0;
	private static long counter$0 = 0;
	private static long counter$1 = 0;
	private static long counter$2 = 0;
	private static long counter$3 = 0;
	private static long counter$4 = 0;
	private static long counter$5 = 0;
	private static long counter$6 = 0;
	private static long counter$7 = 0;
	private static long counter$8 = 0;
	private static long counter$9 = 0;
	private static long counter$10 = 0;
	private static long counter$11 = 0;
	private static long counter$12 = 0;
	private static long counter$13 = 0;
	private static long counter$14 = 0;
	private static long counter$15 = 0;
	private static long counter$16 = 0;
	private static long counter$17 = 0;
	private static long counter$18 = 0;
	private static long counter$19 = 0;
	private static long counter$20 = 0;
	private static long counter$21 = 0;
	private static long counter$22 = 0;
	private static long counter$23 = 0;
	private static long counter$24 = 0;
	private static long counter$25 = 0;
	private static long counter$26 = 0;
	private static long counter$27 = 0;
	private static long counter$28 = 0;
	private static long counter$29 = 0;
	private static long counter$30 = 0;
	private static long counter$31 = 0;
	private static long counter$32 = 0;
	private static long counter$33 = 0;
	private static long counter$34 = 0;
	private static long counter$35 = 0;

	public static void profilerCleanup() {
		memCounter$1 = 0;
		memCounter$2 = 0;
		memCounter$3 = 0;
		memCounter$4 = 0;
		memCounter$5 = 0;
		memCounter$6 = 0;
		memCounter$7 = 0;
		memCounter$8 = 0;
		memCounter$9 = 0;
		counter$0 = 0;
		counter$1 = 0;
		counter$2 = 0;
		counter$3 = 0;
		counter$4 = 0;
		counter$5 = 0;
		counter$6 = 0;
		counter$7 = 0;
		counter$8 = 0;
		counter$9 = 0;
		counter$10 = 0;
		counter$11 = 0;
		counter$12 = 0;
		counter$13 = 0;
		counter$14 = 0;
		counter$15 = 0;
		counter$16 = 0;
		counter$17 = 0;
		counter$18 = 0;
		counter$19 = 0;
		counter$20 = 0;
		counter$21 = 0;
		counter$22 = 0;
		counter$23 = 0;
		counter$24 = 0;
		counter$25 = 0;
		counter$26 = 0;
		counter$27 = 0;
		counter$28 = 0;
		counter$29 = 0;
		counter$30 = 0;
		counter$31 = 0;
		counter$32 = 0;
		counter$33 = 0;
		counter$34 = 0;
		counter$35 = 0;
	}

	public static java.util.Map<String, Long> profilerTimeData() {
		java.util.Map<String, Long> rv = new java.util.HashMap<>();
		rv.put("counter$0", counter$0);
		rv.put("counter$1", counter$1);
		rv.put("counter$2", counter$2);
		rv.put("counter$3", counter$3);
		rv.put("counter$4", counter$4);
		rv.put("counter$5", counter$5);
		rv.put("counter$6", counter$6);
		rv.put("counter$7", counter$7);
		rv.put("counter$8", counter$8);
		rv.put("counter$9", counter$9);
		rv.put("counter$10", counter$10);
		rv.put("counter$11", counter$11);
		rv.put("counter$12", counter$12);
		rv.put("counter$13", counter$13);
		rv.put("counter$14", counter$14);
		rv.put("counter$15", counter$15);
		rv.put("counter$16", counter$16);
		rv.put("counter$17", counter$17);
		rv.put("counter$18", counter$18);
		rv.put("counter$19", counter$19);
		rv.put("counter$20", counter$20);
		rv.put("counter$21", counter$21);
		rv.put("counter$22", counter$22);
		rv.put("counter$23", counter$23);
		rv.put("counter$24", counter$24);
		rv.put("counter$25", counter$25);
		rv.put("counter$26", counter$26);
		rv.put("counter$27", counter$27);
		rv.put("counter$28", counter$28);
		rv.put("counter$29", counter$29);
		rv.put("counter$30", counter$30);
		rv.put("counter$31", counter$31);
		rv.put("counter$32", counter$32);
		rv.put("counter$33", counter$33);
		rv.put("counter$34", counter$34);
		rv.put("counter$35", counter$35);
		return rv;
	}

	public static java.util.Map<String, Long> profilerMemData() {
		java.util.Map<String, Long> rv = new java.util.HashMap<>();
		rv.put("memCounter$1", memCounter$1);
		rv.put("memCounter$2", memCounter$2);
		rv.put("memCounter$3", memCounter$3);
		rv.put("memCounter$4", memCounter$4);
		rv.put("memCounter$5", memCounter$5);
		rv.put("memCounter$6", memCounter$6);
		rv.put("memCounter$7", memCounter$7);
		rv.put("memCounter$8", memCounter$8);
		rv.put("memCounter$9", memCounter$9);
		return rv;
	}

	void dfs(int v, int[] was1, int[] was2) {
		++counter$0;
		long counterBefore = tempCounter1;
		if (true) {
			if (was2[v] == 2 || was1[v] == 1) {
				memCounter$1 = Math.max(tempCounter1, counterBefore);
				{
					tempCounter1 = counterBefore;
					return;
				}
			}
			was1[v] = 1;
			tempCounter1 = tempCounter1 + 4;
			for (int i = 0; i < n; i++) {
				++counter$1;
				if (a[v][i] == 1) {
					dfs(i, was1, was2);
				}
			}
		}
		memCounter$1 = Math.max(tempCounter1, counterBefore);
	}

	int n;
	int[][] a;

	void solve() throws IOException {
		++counter$2;
		long counterBefore = tempCounter2;
		if (true) {
			n = nextInt();
			int m = nextInt();
			tempCounter2 = tempCounter2 + 4;
			a = new int[n][n];
			tempCounter2 = tempCounter2 + (4) * (n) * (n);
			final int[] val = new int[n];
			tempCounter2 = tempCounter2 + 8;
			tempCounter2 = tempCounter2 + (4) * (n);
			tempCounter2 = tempCounter2 + 4;
			for (int i = 0; i < n; i++) {
				++counter$5;
				val[i] = nextInt();
			}
			tempCounter2 = tempCounter2 + 4;
			for (int i = 0; i < m; i++) {
				++counter$6;
				int st = nextInt() - 1;
				tempCounter2 = tempCounter2 + 4;
				int en = nextInt() - 1;
				tempCounter2 = tempCounter2 + 4;
				a[st][en] = a[en][st] = 1;
			}

			Integer[] que = new Integer[n];
			tempCounter2 = tempCounter2 + 8;
			tempCounter2 = tempCounter2 + (8) * (n);
			tempCounter2 = tempCounter2 + 4;
			for (int i = 0; i < n; i++) {
				++counter$8;
				que[i] = i;
			}
			Arrays.sort(que, new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					++counter$9;
					long counterBefore = tempCounter3;
					memCounter$3 = Math.max(tempCounter3, counterBefore);
					{
						tempCounter3 = counterBefore;
						return val[o1] - val[o2];
					}
				}
			});

			int[] was = new int[n];
			tempCounter2 = tempCounter2 + 8;
			tempCounter2 = tempCounter2 + (4) * (n);
			int[] stack = new int[n];
			tempCounter2 = tempCounter2 + 8;
			tempCounter2 = tempCounter2 + (4) * (n);
			int tl = 0;
			tempCounter2 = tempCounter2 + 4;
			int start = que[0];
			tempCounter2 = tempCounter2 + 4;
			stack[tl++] = start;
			was[start] = 1;

			tempCounter2 = tempCounter2 + 4;
			for (int i = 0; i < n; i++) {
				++counter$12;
				out.print(val[stack[tl - 1]]);
				for (int j : que) {
					++counter$14;
					if (was[j] == 0) {
						int[] oldwas = was.clone();
						counter$15 = counter$15 + was.length;
						tempCounter2 = tempCounter2 + 8;
						tempCounter2 = tempCounter2 + was.length;
						int[] oldstack = stack.clone();
						counter$16 = counter$16 + stack.length;
						tempCounter2 = tempCounter2 + 8;
						tempCounter2 = tempCounter2 + stack.length;
						int oldtl = tl;
						tempCounter2 = tempCounter2 + 4;
						while (tl > 0 && a[stack[tl - 1]][j] == 0) {
							++counter$17;
							was[stack[tl - 1]] = 2;
							tl--;
						}
						if (tl == 0) {
							was = oldwas;
							stack = oldstack;
							tl = oldtl;
							continue;
						}
						was[j] = 1;
						stack[tl++] = j;
						int[] dfswas = new int[n];
						tempCounter2 = tempCounter2 + 8;
						tempCounter2 = tempCounter2 + (4) * (n);
						tempCounter2 = tempCounter2 + 4;
						for (int k = 0; k < n; k++) {
							++counter$19;
							if (was[k] == 1) {
								dfs(k, dfswas, was);
							}
						}
						boolean fl = true;
						tempCounter2 = tempCounter2 + 1;
						tempCounter2 = tempCounter2 + 4;
						for (int k = 0; k < n; k++) {
							++counter$20;
							if (dfswas[k] == 0 && was[k] == 0) {
								fl = false;
							}
						}
						if (fl) {
							break;
						} else {
							was = oldwas;
							stack = oldstack;
							tl = oldtl;
						}
					}
				}
			}
			out.println();
		}
		memCounter$2 = Math.max(tempCounter2, counterBefore);
	}

	void run() throws IOException {
		++counter$22;
		long counterBefore = tempCounter4;
		if (true) {
			in = new BufferedReader(new FileReader("C.in"));
			tempCounter4 = tempCounter4 + (8);
			out = new PrintWriter("C.out");
			tempCounter4 = tempCounter4 + (8);
			int t = nextInt();
			tempCounter4 = tempCounter4 + 4;
			tempCounter4 = tempCounter4 + 4;
			for (int i = 1; i <= t; i++) {
				++counter$25;
				System.err.println(i);
				out.print("Case #" + i + ": ");
				solve();
			}
			out.close();
		}
		memCounter$4 = Math.max(tempCounter4, counterBefore);
	}

	public static void main(String[] args) throws IOException {
		long counterBefore = tempCounter5;
		if (true) {
			new Test().run();
		}
		memCounter$5 = Math.max(tempCounter5, counterBefore);
	}

	BufferedReader in;
	PrintWriter out;
	StringTokenizer st;

	String next() throws IOException {
		++counter$29;
		long counterBefore = tempCounter6;
		while (st == null || !st.hasMoreTokens()) {
			++counter$30;
			String temp = in.readLine();
			tempCounter6 = tempCounter6 + 8;
			if (temp == null) {
				memCounter$6 = Math.max(tempCounter6, counterBefore);
				{
					tempCounter6 = counterBefore;
					return null;
				}
			}
			st = new StringTokenizer(temp);
		}
		memCounter$6 = Math.max(tempCounter6, counterBefore);
		{
			tempCounter6 = counterBefore;
			return st.nextToken();
		}
	}

	int nextInt() throws IOException {
		++counter$33;
		long counterBefore = tempCounter7;
		memCounter$7 = Math.max(tempCounter7, counterBefore);
		{
			tempCounter7 = counterBefore;
			return Integer.parseInt(next());
		}
	}

	double nextDouble() throws IOException {
		++counter$34;
		long counterBefore = tempCounter8;
		memCounter$8 = Math.max(tempCounter8, counterBefore);
		{
			tempCounter8 = counterBefore;
			return Double.parseDouble(next());
		}
	}

	long nextLong() throws IOException {
		++counter$35;
		long counterBefore = tempCounter9;
		memCounter$9 = Math.max(tempCounter9, counterBefore);
		{
			tempCounter9 = counterBefore;
			return Long.parseLong(next());
		}
	}

}
