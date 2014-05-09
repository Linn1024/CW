import java.io.*;
import java.util.*;

import org.antlr.v4.parse.GrammarTreeVisitor.tokenSpec_return;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;

public class Java7Profiler extends Java7BaseListener {
	private final CommonTokenStream ts;

	public final Map<Integer, String> incOfMethod = new HashMap<Integer, String>();
	public final Map<Integer, String> change = new HashMap<Integer, String>();
	public final List<String> counters = new ArrayList<String>();
	private final Map<String, String> timeClass = new HashMap<>();
	private final Map vars;
	private final boolean useTimeoutChecker;
	private boolean f = false;
	private String curMethodName = "";

	private Map generics;

	public Java7Profiler(CommonTokenStream tokens, Map vars,
			boolean useTimeoutChecker, Map generics) throws FileNotFoundException {
		this.ts = tokens;
		this.useTimeoutChecker = useTimeoutChecker;
		this.vars = vars;
		this.generics = generics;
	}

	private String incrementFor(String counter) {
		if (useTimeoutChecker) {
			return "if ((++" + counter
					+ " & 262143) == 0) TimeoutChecker.check();";
		} else {
			return "++" + counter + ";";
		}
	}

	private String incrementFor(String counter, String add) {
		if (useTimeoutChecker) {
			return "if ((++" + counter
					+ " & 262143) == 0) TimeoutChecker.check();";
		} else {
			return counter + " = " + counter + "+" + add + ";";
		}
	}

	public void timeClasses() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("timeClasses"));
		while (true) {
			String s = br.readLine();
			if (s == null)
				break;
			String[] tmp = s.split("\\^");
			// TimeClass tc = new TimeClass(tmp[0], tmp[1]);
			timeClass.put(tmp[0] + "$" + tmp[1], tmp[2]);
		}
		br.close();
	}

	private String nameOfVariable(
			Java7Parser.LocalVariableDeclarationContext ctx) {
		return ctx.getChild(1).getChild(0).getChild(0).getText();
	}

	private String nameOfType(Java7Parser.LocalVariableDeclarationContext ctx) {
		return ctx.getChild(0).getText();
	}

	private String objectOfMethod(ParserRuleContext ctx) {
		// System.out.println(ctx.getChild(0).getChild(0).getText());
		String s = ctx.getChild(0).getChild(0).getText();
		String ans = s.split("\\[")[0];
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '[')
				ans += '[';
		}
		return ans;
	}

	private String fullObjectOfMethod(ParserRuleContext ctx) {
		return ctx.getChild(0).getChild(0).getText();
	}

	private String argOfMethod(ParserRuleContext ctx, int num) {
		// System.out.println(ctx.getChild(2).getText());
		return ctx.getChild(2).getChild(2 * num - 2).getText();
	}

	private String nameOfMethod(ParserRuleContext ctx) {
		// System.out.println(ctx.getChild(0).getText());
		if (ctx.getChild(0).getText().contains("."))
			return ctx.getChild(0).getText().split("\\.")[1];
		else
			return "-1";
	}

	@Override
	public void enterLocalVariableDeclaration(
			Java7Parser.LocalVariableDeclarationContext ctx) {
		// System.out.println(nameOfType(ctx));
	}

	@Override
	public void enterForInit(Java7Parser.ForInitContext ctx) {
		f = true;
	}

	@Override
	public void exitForInit(Java7Parser.ForInitContext ctx) {
		f = false;
	}

	@Override
	public void enterFieldExpr(Java7Parser.FieldExprContext ctx1) {
		ParserRuleContext ctx = ctx1.getParent();
		//System.out.println("FE:" + ctx.getText());
		if (nameOfMethod(ctx).equals("-1"))
			return;
		String oom = objectOfMethod(ctx);

		String name = oom.split("\\[")[0];
		String type = (String) vars.get(curMethodName + "#" + name);
		if (type == null) {
			type = (String) vars.get("#" + name);
		}
		if (type == null)
			return;
		int tsz = type.length() - 1;
		int nsz = oom.length() - 1;
		while (type.charAt(tsz) == '[' && type.charAt(tsz) == oom.charAt(nsz)) {
			tsz--;
			nsz--;
		}
		type = type.substring(0, tsz + 1);
		String find = type + "$" + nameOfMethod(ctx);
		// System.out.println(objectOfMethod(ctx));
		String counter = "counter$" + counters.size();
		counters.add(counter);

		if (timeClass.containsKey(find)) {
			String add = timeClass.get(find);
			add = add.replaceAll("\\$0", fullObjectOfMethod(ctx));
			while (true) {
				int beg = add.indexOf('$') + 1;
				if (beg == 0)
					break;
				String num = "";
				while (beg < add.length() && add.charAt(beg) >= '0'
						&& add.charAt(beg) <= '9') {
					num = num + add.charAt(beg);
					beg++;

				}
				add = add.replaceAll("\\$" + num,
						argOfMethod(ctx, Integer.parseInt(num)));
			}
			// System.out.println(argOfMethod(ctx, 2));
			int incr = (!f ? 1 : 0);

			if (incOfMethod.get(ctx.getStart().getLine() + incr) != null) {
				String s = incOfMethod.get(ctx.getStart().getLine() + incr);
				if (s.charAt(s.length() - 1) == '}') {
					incOfMethod.put(ctx.getStart().getLine() + incr,
							s.substring(0, s.length() - 2) + ";" + "\n"
									+ incrementFor(counter, add) + '}');
					return;
				}
				incOfMethod.put(ctx.getStart().getLine() + incr,
						incOfMethod.get(ctx.getStart().getLine() + incr)
								+ ";\n" + incrementFor(counter, add));
			} else
				incOfMethod.put(ctx.getStart().getLine() + incr,
						incrementFor(counter, add));
		}

	}
	
	@Override
	public void enterNewCreatorExpr(Java7Parser.NewCreatorExprContext ctx) {
		// System.out.println(ctx.getParent().getParent().getText());
		// System.out.println(ctx.getChild(0).getChild(2).getChild(0).getText());
		ParserRuleContext c = ctx;
		int o = 0;
		while (!c.getText().contains("=") && o < 3) {
			o++;
			c = c.getParent();
		}
		if (o == 3)
			return;
		// ParserRuleContext c = ctx.getParent().getParent();
		String ans = "-1";
		// System.out.println(ctx.getText());
		ParseTree t = c.getChild(2);
		while (!t.getText().equals("new"))
			t = t.getChild(0);
		t = t.getParent();
		//System.out.println(t.getText());

		String s = t.getChild(1).getText();
		ans = s.split("[<,\\[,\\(]")[0];

		if (generics.get(c.getChild(0).getText()) != null)
			ans = ans + generics.get(c.getChild(0).getText());
		for (int i = 0; i < s.length(); i++)
			if (s.charAt(i) == '[')
				ans += "[";

		String name = c.getChild(0).getText();
		if (name.contains("."))
			return;
		String type = (String) vars.get(curMethodName + "#" + name);
		if (type == null) {
			type = (String) vars.get("#" + name);
		}
		String counter = "counter$" + counters.size();
		counters.add(counter);

		if (type == null)
			return; 
		if (timeClass.get(type + "$" + type)!=null){
			add(incOfMethod, ctx.getStart().getLine() + 1, incrementFor(counter, timeClass.get(type + "$" + type)));
		}
	}


	@Override
	public void enterStatementExpression(
			Java7Parser.StatementExpressionContext ctx) {
		// System.out.println(objectOfMethod(ctx) + " " + nameOfMethod(ctx));
		// System.out.println("SE:" + ctx.getText());
	}

	@Override
	public void enterMethodDeclaration(Java7Parser.MethodDeclarationContext ctx) {
		curMethodName = ctx.getChild(1).getText();
		String counter = "counter$" + counters.size();
		counters.add(counter);
		if (!ctx.getChild(1).getText().equals("main")) {
			incOfMethod.put(ctx.methodBody().getStart().getLine() + 1,
					incrementFor(counter));
		}
	}

	@Override
	public void enterForState(Java7Parser.ForStateContext ctx) {
		String counter = "counter$" + counters.size();
		counters.add(counter);
		if (ctx.statement().getText().charAt(0) == ';') {
			incOfMethod.put(ctx.statement().getStart().getLine() + 1, "{"
					+ incrementFor(counter) + "}");
			change.put(ctx.statement().getStart().getLine(), "s");
			return;
		}
		if (ctx.statement().getText().charAt(0) != '{') {
			incOfMethod.put(ctx.statement().getStart().getLine(), "{ "
					+ incrementFor(counter));
			incOfMethod.put(ctx.statement().getStop().getLine() + 1, "}");
		} else {
			incOfMethod.put(ctx.statement().getStart().getLine() + 1,
					incrementFor(counter));
		}
	}

	@Override
	public void enterWhileState(Java7Parser.WhileStateContext ctx) {
		String counter = "counter$" + counters.size();
		counters.add(counter);
		if (ctx.statement().getText().charAt(0) != '{') {
			incOfMethod.put(ctx.statement().getStart().getLine(), "{ "
					+ incrementFor(counter));
			incOfMethod.put(ctx.statement().getStop().getLine() + 1, "}");
		} else {
			incOfMethod.put(ctx.statement().getStart().getLine() + 1,
					incrementFor(counter));
		}
	}

	@Override
	public void enterDoWhileState(Java7Parser.DoWhileStateContext ctx) {
		String counter = "counter$" + counters.size();
		counters.add(counter);
		if (ctx.statement().getText().charAt(0) != '{') {
			incOfMethod.put(ctx.statement().getStart().getLine(), "{ "
					+ incrementFor(counter));
			incOfMethod.put(ctx.statement().getStop().getLine() + 1, "}");
		} else {
			incOfMethod.put(ctx.statement().getStart().getLine() + 1,
					incrementFor(counter));
		}
	}
	private void add(Map<Integer, String> M, int I, String S){
		if (M.get(I) == null){
			M.put(I, S);
			return;
		}
		
		M.put(I, M.get(I) + ";\n" + S);
	}

}
