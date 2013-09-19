import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.util.*;

public class frozen_pk {
static Map<String, Long> variables = new HashMap<String, Long>();
static void Reload() {
variables.put("counter_void_solve$0", (long) 0);
variables.put("counter_for_counter_void_solve$0$0", (long) 0);
variables.put("counter_for_counter_void_solve$0$1", (long) 0);
variables.put("counter_void_run$1", (long) 0);
variables.put("counter_String_nextToken$2", (long) 0);
variables.put("counter_while_counter_String_nextToken$2$0", (long) 0);
variables.put("counter_int_nextInt$3", (long) 0);
variables.put("counter_double_nextDouble$4", (long) 0);
variables.put("counter_long_nextLong$5", (long) 0);
}
static void PrintVariables() throws FileNotFoundException {
PrintWriter result_out$ = new PrintWriter("result.txt");
for (String s : variables.keySet())
 result_out$.println(s + " : " + variables.get(s));
result_out$.close(); 
 

}

	void solve() throws IOException {
variables.put("counter_void_solve$0",variables.get("counter_void_solve$0")+ 1) ;
		int n = nextInt();
		int[] a = new int[n];
		for (int i = 0; i < n; i++)
{variables.put("counter_for_counter_void_solve$0$0",variables.get("counter_for_counter_void_solve$0$0")+ 1) ;
			a[i] = nextInt();
}
		int ans = n * 2;
		for (int i = 0; i < n * 2; i++) {
variables.put("counter_for_counter_void_solve$0$1",variables.get("counter_for_counter_void_solve$0$1")+ 1) ;
			int t = nextInt();
			if (t == 0 || t == a[i % n])
				ans--;
		}
		out.println(ans);
	}

	BufferedReader br;
	StringTokenizer st;
	PrintWriter out;

	void run() {
variables.put("counter_void_run$1",variables.get("counter_void_run$1")+ 1) ;
		try {
			br = new BufferedReader(new InputStreamReader(System.in));
			out = new PrintWriter(System.out);

			br = new BufferedReader(new FileReader(new File("frozen.in")));
			out = new PrintWriter("frozen.out");

			solve();

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	String nextToken() throws IOException {
variables.put("counter_String_nextToken$2",variables.get("counter_String_nextToken$2")+ 1) ;
		while ((st == null) || !st.hasMoreTokens())
{variables.put("counter_while_counter_String_nextToken$2$0",variables.get("counter_while_counter_String_nextToken$2$0")+ 1) ;
			st = new StringTokenizer(br.readLine());
}
		return st.nextToken();
	}

	int nextInt() throws NumberFormatException, IOException {
variables.put("counter_int_nextInt$3",variables.get("counter_int_nextInt$3")+ 1) ;
		return Integer.parseInt(nextToken());
	}

	double nextDouble() throws NumberFormatException, IOException {
variables.put("counter_double_nextDouble$4",variables.get("counter_double_nextDouble$4")+ 1) ;
		return Double.parseDouble(nextToken());
	}

	long nextLong() throws NumberFormatException, IOException {
variables.put("counter_long_nextLong$5",variables.get("counter_long_nextLong$5")+ 1) ;
		return Long.parseLong(nextToken());
	}
}
