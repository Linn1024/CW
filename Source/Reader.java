import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Reader {
	StringTokenizer st;
	PrintWriter out;
	BufferedReader br;

	// BufferedReader tmp;

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

	// public static void main(String[] args) throws FileNotFoundException {
	// new Reader().run();

	// }

	void run(Map<Integer, String> adds, ArrayList<String> counters,
			String InpFile) throws IOException {
		br = new BufferedReader(new FileReader(InpFile));
		out = new PrintWriter("Temp/" + InpFile);
		int numberOfString = 0;
		out.println("import java.util.HashMap;");
		out.println("import java.util.Map;");
		boolean init = false;
		while (true) {
			numberOfString++;
			String tmp = br.readLine();
			if (tmp == null) {
				out.close();
				return;

			}

			if (adds.get(numberOfString) != null) {
				out.println(adds.get(numberOfString));
				if (adds.get(numberOfString).equals("} finally {")) {
					//out.println(" try{ \n PrintWriter result_out$ = new PrintWriter(\"result.txt\");");
					//for (String s : counters) {
						//out.println("result_out$.println(\"" + s + ":\" + " + s
						//		+ ");");
					//}
					//out.println("result_out$.close(); \n } catch (FileNotFoundException e) { \n e.printStackTrace(); \n }");
					out.println("}");
				}
				out.println(tmp);
				adds.remove(numberOfString);
			} else {
				out.println(tmp);
			}
			if (tmp.contains(" class ") & !init) {
				out.println("static Map<String, Long> variables = new HashMap<String, Long>();");
				out.println("static void Reload() {");
				//out.println("Map<String, Integer> variables = new HashMap<String, Integer>();");
				for (String s : counters) {
					out.println("variables.put(\"" + s + "\", (long) 0);");
				}
				out.println("}");
				out.println("static void PrintVariables() throws FileNotFoundException {");
				out.println("PrintWriter result_out$ = new PrintWriter(\"result.txt\");");
				out.println("for (String s : variables.keySet())\n result_out$.println(s + \" : \" + variables.get(s));");
				out.println("result_out$.close(); \n ");
				out.println("\n}");
				init = true;
			}
		}

	}
}
