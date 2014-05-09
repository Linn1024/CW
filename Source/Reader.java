import java.io.*;
import java.util.*;

public class Reader {
	StringTokenizer st;
	PrintWriter out;
	BufferedReader br;

	private final boolean useTimeoutChecker;

	public Reader(boolean useTimeoutChecker) {
		this.useTimeoutChecker = useTimeoutChecker;
	}

	private String nextToken() throws IOException {
		while (st == null || !st.hasMoreTokens()) {
			st = new StringTokenizer(br.readLine());
		}
		return st.nextToken();
	}

	private int nextInt() throws IOException {
		return Integer.parseInt(nextToken());
	}

	private long nextLong() throws IOException {
		return Long.parseLong(nextToken());
	}

	private double nextDouble() throws IOException {
		return Double.parseDouble(nextToken());
	}

	public void run(Map<Integer, String> adds, Map<Integer, String> change,
			List<String> counters, BufferedReader br, PrintWriter out,
			String originalName, String newName) throws IOException {
		int numberOfString = 0;
		boolean init = false;
		String line;
		while ((line = br.readLine()) != null) {
			line = line.replace(originalName, newName);
			numberOfString++;
			if (line.contains(" class ") & !init && useTimeoutChecker) {
				out.println("import ru.ifmo.ctd.ngp.demo.testgen.TimeoutChecker;");
			}
			if (adds.get(numberOfString) != null) {
				out.println(adds.get(numberOfString));
				if (change.get(numberOfString) != null)
					out.print(line.substring(0, line.length() - 1));
				else
					out.println(line);
			} else {
				if (change.get(numberOfString) != null)
					out.print(line.substring(0, line.length() - 1));
				else
					out.println(line);
			}
			if (line.contains(" class ") & !init) {
				for (String s : counters) {
					out.println("    private static long " + s + " = 0;");
					if (s.startsWith("mem")) {
						out.println("    private static long tempCounter"
								+ s.split("\\$")[1] + " = 0;");
					}
				}
				out.println("    public static void profilerCleanup() {");
				for (String s : counters) {
					out.println("        " + s + " = 0;");
				}
				out.println("    }");
				out.println("    public static java.util.Map<String, Long> profilerTimeData() {");
				out.println("        java.util.Map<String, Long> rv = new java.util.HashMap<>();");
				for (String s : counters) {
					if (!s.startsWith("mem"))
						out.println("        rv.put(\"" + s + "\", " + s + ");");
				}
				out.println("        return rv;");
				out.println("    }");
				out.println("    public static java.util.Map<String, Long> profilerMemData() {");
				out.println("        java.util.Map<String, Long> rv = new java.util.HashMap<>();");
				for (String s : counters) {
					if (s.startsWith("mem"))
						out.println("        rv.put(\"" + s + "\", " + s + ");");
				}
				out.println("        return rv;");
				out.println("    }");
				init = true;
			}
		}
	}
}
