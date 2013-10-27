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
 
    BufferedReader br;
    StringTokenizer in;
    PrintWriter out;
 
    private static final int UNVISITED = -1;
    private static final int PROCESING = -2;
    private static final int READY = -3;
 
    public String nextToken() throws IOException {
        while (in == null || !in.hasMoreTokens()) {
            in = new StringTokenizer(br.readLine());
        }
        return in.nextToken();
    }
 
    public int nextInt() throws IOException {
        return Integer.parseInt(nextToken());
    }
 
    public static void main(String[] args) throws IOException {
        new Main().run();
    }
 
    public void solve() throws IOException {
        int n = nextInt();
        int m = nextInt();
        ArrayList<ArrayList<Integer>> graph = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < n + 1; i++) {
            graph.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < m; i++) {
            int from = nextInt();
            int to = nextInt();
            graph.get(from).add(to);
        }
        int[] vertexes = new int[n + 1];
        for (int i = 0; i < n + 1; i++) {
            vertexes[i] = UNVISITED;
        }
        ArrayList<Integer> answer = new ArrayList<Integer>();
        for (int i = 1; i < n + 1; i++) {
            if (vertexes[i] == UNVISITED) {
                topSort(graph, i, vertexes, answer);
            }
        }
        for (int i = 0; i < n + 1; i++) {
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
        if (topsortIdx == 0) {
            return;
        }
        int from = topSort.get(topsortIdx);
        for (Integer v : graph.get(from)) {
            if (d[v] < d[from] + 1) {
                d[v] = d[from] + 1;
            }
        }
        countPathsFrom(graph, d, topsortIdx - 1, topSort);
    }
 
    public void topSort(ArrayList<ArrayList<Integer>> graph, int start, int[] vertexes, ArrayList<Integer> answer) {
        vertexes[start] = PROCESING;
        for (Integer v : graph.get(start)) {
            if (vertexes[v] == UNVISITED) {
                topSort(graph, v, vertexes, answer);
            }
        }
        vertexes[start] = READY;
        answer.add(start);
    }
 
    public void run() {
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