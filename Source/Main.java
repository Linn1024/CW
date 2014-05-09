import java.io.*;
import java.util.*;

import org.antlr.v4.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {
    private static void usage() {
        System.err.println("Usage: Main <input-file> <output-file> [options...]");
        System.err.println("Options:");
        System.err.println("    -timeout - issue calls to TimeoutChecker (default: false");
        System.exit(1);
        throw new IllegalArgumentException();
    }
    private static String classNameFrom(String a) {
        if (a.endsWith(".java")) {
            a = a.substring(0, a.length() - 5);
        }
        int lastSlash = a.lastIndexOf('/');
        if (lastSlash >= 0) {
            a = a.substring(lastSlash + 1);
        }
        lastSlash = a.lastIndexOf('\\');
        if (lastSlash >= 0) {
            a = a.substring(lastSlash + 1);
        }
        return a;
    }
	public static void main(String[] args) throws IOException {
	    if (args.length < 2) {
	        usage();
	    }
	    boolean useTimeoutChecker = false;
	    for (int i = 2; i < args.length; ++i) {
	        String o = args[i];
	        if (o.equals("-timeout")) {
	            useTimeoutChecker = true;
	        } else {
	            usage();
	        }
	    }
	    final Map<Integer, String> incOfMethod;
	    final List<String> counters;
	    final Map<Integer, String> change;
	    try (FileInputStream fis = new FileInputStream(args[0])) {
	        ANTLRInputStream input = new ANTLRInputStream(fis);
    		Java7Lexer lexer = new Java7Lexer(input);
	    	CommonTokenStream tokens = new CommonTokenStream(lexer);
		    Java7Parser parser = new Java7Parser(tokens);
    		ParseTree tree = parser.compilationUnit();
	    	Java7VarProfiler varListener = new Java7VarProfiler(tokens, useTimeoutChecker);
	    	
	    	ParseTreeWalker varWalker = new ParseTreeWalker();
		    varWalker.walk(varListener, tree);
	    	Map vars = varListener.varType;
	    	//System.out.println(vars);
	    	//System.out.println(varListener.generics);
    		Java7Profiler listener = new Java7Profiler(tokens, vars, useTimeoutChecker, varListener.generics);
    		listener.timeClasses();
    		ParseTreeWalker walker = new ParseTreeWalker();
		    walker.walk(listener, tree);
    		Java7MemProfiler memListener = new Java7MemProfiler(tokens, vars, varListener.generics, listener.incOfMethod, useTimeoutChecker);
	    	memListener.memoryClasses();
		    walker.walk(memListener, tree);
		    incOfMethod = memListener.incOfMethod;
		    
		    //TreeSet<Integer>[][] v = new TreeSet[4][4];
		    change = memListener.change;
		    counters = memListener.counters;
		    counters.addAll(listener.counters);
		}
		try (BufferedReader input = new BufferedReader(new FileReader(args[0]));
		     PrintWriter output = new PrintWriter(args[1])) {
		    new Reader(useTimeoutChecker).run(incOfMethod, change, counters, input, output, classNameFrom(args[0]), classNameFrom(args[1]));
		}
	}
}
