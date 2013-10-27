import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
 
/**
 * Created with IntelliJ IDEA.
 * User: Egor
 * Date: 10/22/13
 * Time: 19:15
 * To change this template use File | Settings | File Templates.
 */
public class Main {
static Map<String, Long> variables = new HashMap<String, Long>();
static void Reload() {
variables.put("counter_String_nextToken$0", (long) 0);
variables.put("counter_while_counter_String_nextToken$0$0", (long) 0);
variables.put("counter_int_nextInt$1", (long) 0);
variables.put("counter_void_main$2", (long) 0);
variables.put("counter_void_solve$3", (long) 0);
variables.put("counter_for_counter_void_solve$3$0", (long) 0);
variables.put("counter_for_counter_void_solve$3$1", (long) 0);
variables.put("counter_for_counter_void_solve$3$2", (long) 0);
variables.put("counter_for_counter_void_solve$3$3", (long) 0);
variables.put("counter_for_counter_void_solve$3$4", (long) 0);
variables.put("counter_void_countPathsFrom$4", (long) 0);
variables.put("counter_for_counter_void_countPathsFrom$4$0", (long) 0);
variables.put("counter_void_topSort$5", (long) 0);
variables.put("counter_for_counter_void_topSort$5$0", (long) 0);
variables.put("counter_void_run$6", (long) 0);
}
static void PrintVariables() throws FileNotFoundException {
PrintWriter result_out$ = new PrintWriter("result.txt");
for (String s : variables.keySet())
 result_out$.println(s + " : " + variables.get(s));
result_out$.close(); 
 

}
 
    BufferedReader br;
    StringTokenizer in;
    PrintWriter out;
 
    private static final int UNVISITED = -1;
    private static final int PROCESING = -2;
    private static final int READY = -3;
 
    public String nextToken() throws IOException {
variables.put("counter_String_nextToken$0",variables.get("counter_String_nextToken$0")+ 1) ;
        while (in == null || !in.hasMoreTokens()) {
variables.put("counter_while_counter_String_nextToken$0$0",variables.get("counter_while_counter_String_nextToken$0$0")+ 1) ;
            in = new StringTokenizer(br.readLine());
        }
        return in.nextToken();
    }
 
    public int nextInt() throws IOException {
variables.put("counter_int_nextInt$1",variables.get("counter_int_nextInt$1")+ 1) ;
        return Integer.parseInt(nextToken());
    }
 
    public static void main(String[] args) throws IOException {
try {
        new Main().run();
} finally {
}
    }
 
    public void solve() throws IOException {
variables.put("counter_void_solve$3",variables.get("counter_void_solve$3")+ 1) ;
        int n = nextInt();
        int m = nextInt();
        ArrayList<ArrayList<Integer>> graph = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < n + 1; i++) {
variables.put("counter_for_counter_void_solve$3$0",variables.get("counter_for_counter_void_solve$3$0")+ 1) ;
            graph.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < m; i++) {
variables.put("counter_for_counter_void_solve$3$1",variables.get("counter_for_counter_void_solve$3$1")+ 1) ;
            int from = nextInt();
            int to = nextInt();
            graph.get(from).add(to);
        }
        int[] vertexes = new int[n + 1];
        for (int i = 0; i < n + 1; i++) {
variables.put("counter_for_counter_void_solve$3$2",variables.get("counter_for_counter_void_solve$3$2")+ 1) ;
            vertexes[i] = UNVISITED;
        }
        ArrayList<Integer> answer = new ArrayList<Integer>();
        for (int i = 1; i < n + 1; i++) {
variables.put("counter_for_counter_void_solve$3$3",variables.get("counter_for_counter_void_solve$3$3")+ 1) ;
            if (vertexes[i] == UNVISITED) {
                topSort(graph, i, vertexes, answer);
            }
        }
        for (int i = 0; i < n + 1; i++) {
variables.put("counter_for_counter_void_solve$3$4",variables.get("counter_for_counter_void_solve$3$4")+ 1) ;
            vertexes[i] = UNVISITED;
        }
        int[] longestPath = new int[n + 1];
        //int infinity = 1000000000;
        //for (int i = 0; i < n + 1; i++) {
        //    longestPath[i] = infinity;
        //}
        //longestPath[1] = 0;
        countPathsFrom(graph, longestPath, answer.size() - 1, answer);
        if (longestPath[answer.get(0)] == n - 1) {
            out.write("YES");
        } else {
            out.write("NO");
        }
    }
 
 
    public void countPathsFrom(ArrayList<ArrayList<Integer>> graph, int[] d, int topsortIdx, ArrayList<Integer> topSort) {
variables.put("counter_void_countPathsFrom$4",variables.get("counter_void_countPathsFrom$4")+ 1) ;
        if (topsortIdx == 0) {
            return;
        }
        int from = topSort.get(topsortIdx);
        for (Integer v : graph.get(from)) {
variables.put("counter_for_counter_void_countPathsFrom$4$0",variables.get("counter_for_counter_void_countPathsFrom$4$0")+ 1) ;
            if (d[v] < d[from] + 1) {
                d[v] = d[from] + 1;
            }
        }
        countPathsFrom(graph, d, topsortIdx - 1, topSort);
    }
 
    public void topSort(ArrayList<ArrayList<Integer>> graph, int start, int[] vertexes, ArrayList<Integer> answer) {
variables.put("counter_void_topSort$5",variables.get("counter_void_topSort$5")+ 1) ;
        vertexes[start] = PROCESING;
        for (Integer v : graph.get(start)) {
variables.put("counter_for_counter_void_topSort$5$0",variables.get("counter_for_counter_void_topSort$5$0")+ 1) ;
            if (vertexes[v] == UNVISITED) {
                topSort(graph, v, vertexes, answer);
            }
        }
        vertexes[start] = READY;
        answer.add(start);
    }
 
    public void run() {
variables.put("counter_void_run$6",variables.get("counter_void_run$6")+ 1) ;
        try {
            br = new BufferedReader(new FileReader("hamiltonian.in"));
            out = new PrintWriter("hamiltonian.out");
 
            solve();
 
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
 
}
