import java.io.*;
import java.util.*;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.TokenStream;
import org.antlr.v4.parse.GrammarTreeVisitor.tokenSpec_return;
import org.antlr.v4.runtime.tree.ParseTree;

public class Java7ConditionListener extends Java7BaseListener {
	private static final String FUNCTION = "startsWith";
	private int num = 0;
	private int numFor = 0;
	private int curMethod = 0;
	private Stack<ArrayList<String>> argsStack;
	private PrintWriter out;
	private org.antlr.v4.runtime.CommonTokenStream ts;
	public Map<Integer, String> incOfMethod = new HashMap<Integer, String>();
	public ArrayList<String> counters = new ArrayList<String>();

	public Java7ConditionListener(org.antlr.v4.runtime.CommonTokenStream tokens)
			throws FileNotFoundException {
		this.ts = tokens;
		// argsStack = new Stack<ArrayList<String>>();
		// out = new PrintWriter(new File(Core.filename + ".gen"));
	}

	@Override
	public void enterIfStatement(Java7Parser.IfStatementContext ctx) {
		System.out.println(ctx.getStart().getLine());
	}

	@Override
	public void enterMethodDeclaration(Java7Parser.MethodDeclarationContext ctx) {
		//System.out.println(ctx.getStart().getLine());
		counters.add("counter_"
				+ ts.getText(ctx).substring(0, ts.getText(ctx).indexOf("("))
						.replaceAll("[\\[\\]<> ]", "_") + "$" + (num++));
		incOfMethod.put(ctx.methodBody().getStart().getLine() + 1,
				"variables.put(\"" + counters.get(counters.size() - 1) + "\",variables.get(\"" + counters.get(counters.size() - 1) + "\")+ 1) ;");
		curMethod = counters.size() - 1;
		numFor = 0;
		if (ctx.getChild(1).getText().equals("main")) {
			incOfMethod.put(ctx.methodBody().getStart().getLine() + 1, "try {");
			incOfMethod.put(ctx.methodBody().getStop().getLine(), "} finally {");
		}
		// System.out.print("counter_" + ts.getText(ctx).substring(0,
		// ts.getText(ctx).indexOf(")") + 1 ).replace(" ", "_"));
	}

	@Override
	public void enterForState(Java7Parser.ForStateContext ctx) {
		counters.add("counter_for_" + counters.get(curMethod) + "$" + (numFor++));
		if (ctx.statement().getText().charAt(0) != '{'){
			incOfMethod.put(ctx.statement().getStart().getLine(), "{" + "variables.put(\"" + counters.get(counters.size() - 1) + "\",variables.get(\"" + counters.get(counters.size() - 1) + "\")+ 1) ;");
			incOfMethod.put(ctx.statement().getStop().getLine() + 1, "}");
		}
		else
			incOfMethod.put(ctx.statement().getStart().getLine() + 1, "variables.put(\"" + counters.get(counters.size() - 1) + "\",variables.get(\"" + counters.get(counters.size() - 1) + "\")+ 1) ;");
	}

	@Override
	public void enterWhileState(Java7Parser.WhileStateContext ctx) {
		counters.add("counter_while_" + counters.get(curMethod) + "$" + (numFor++));
		if (ctx.statement().getText().charAt(0) != '{'){
			incOfMethod.put(ctx.statement().getStart().getLine(), "{" + "variables.put(\"" + counters.get(counters.size() - 1) + "\",variables.get(\"" + counters.get(counters.size() - 1) + "\")+ 1) ;");
			incOfMethod.put(ctx.statement().getStop().getLine() + 1, "}");
		}
		else
		incOfMethod.put(ctx.statement().getStart().getLine() + 1, "variables.put(\"" + counters.get(counters.size() - 1) + "\",variables.get(\"" + counters.get(counters.size() - 1) + "\")+ 1) ;");
	}
	
	@Override
	public void enterDoWhileState(Java7Parser.DoWhileStateContext ctx) {
		counters.add("counter_doWhile_" + counters.get(curMethod) + "$" + (numFor++));
		if (ctx.statement().getText().charAt(0) != '{'){
			incOfMethod.put(ctx.statement().getStart().getLine(), "{ variables.put(\"" + counters.get(counters.size() - 1) + "\",variables.get(\"" + counters.get(counters.size() - 1) + "\")+ 1) ;");
			incOfMethod.put(ctx.statement().getStop().getLine() + 1, "}");
		}
		else
		incOfMethod.put(ctx.statement().getStart().getLine() + 1, "variables.put(\"" + counters.get(counters.size() - 1) + "\",variables.get(\"" + counters.get(counters.size() - 1) + "\")+ 1) ;");
	}
	
	

	
	@Override
	public void exitCompilationUnit(Java7Parser.CompilationUnitContext ctx) {

		int counter_void_main_String_args_;
		// if (ctx.getChildCount() == 0)
		// System.out.println(ts.getText(ctx));

		// out.close();
	}

}