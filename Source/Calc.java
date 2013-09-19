import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Calc {

	/**
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		// TODO Auto-generated method stub
		ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(
				args[0]));

		Java7Lexer lexer = new Java7Lexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		Java7Parser parser = new Java7Parser(tokens);
		ParseTree tree = parser.compilationUnit();
		Java7ConditionListener listener = new Java7ConditionListener(tokens);
		
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(listener, tree);
		new Reader().run(listener.incOfMethod, listener.counters, args[0]);
		
	}
}
