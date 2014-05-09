import java.io.*;
import java.lang.instrument.Instrumentation;
import java.util.*;

import javax.print.attribute.HashAttributeSet;

import org.antlr.v4.parse.GrammarTreeVisitor.tokenSpec_return;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;

public class Java7MemProfiler extends Java7BaseListener {
	private final CommonTokenStream ts;

	public Map<Integer, String> incOfMethod = new HashMap<Integer, String>();
	public final Map<Integer, String> change = new HashMap<Integer, String>();
	public final List<String> counters = new ArrayList<String>();
	private final Map<String, String> memoryClass = new HashMap<>();
	private final Map<String, String> primitiveWeight = new HashMap<String, String>();
	private final Map<String, String> vars;
	private String curMethodType;
	private String curMethodName;
	private String curCounterNumber = "1";
	public Map<String, String> generics;

	private final boolean useTimeoutChecker;
	private boolean f = false;

	public Java7MemProfiler(CommonTokenStream tokens, Map<String, String> vars,
			Map<String, String> generics, Map<Integer, String> incOfMethod,  boolean useTimeoutChecker)
			throws FileNotFoundException {
		this.ts = tokens;
		this.useTimeoutChecker = useTimeoutChecker;
		this.vars = vars;
		this.incOfMethod = incOfMethod;
		this.generics = generics;
		primitiveWeight.put("boolean", "1");
		primitiveWeight.put("short", "2");
		primitiveWeight.put("int", "4");
		primitiveWeight.put("long", "8");
		primitiveWeight.put("float", "4");
		primitiveWeight.put("double", "8");
		primitiveWeight.put("char", "2");
		primitiveWeight.put("byte", "1");

	}

	private void add(Map<Integer, String> M, int I, String S) {
		if (M.get(I) == null) {
			M.put(I, S);
			return;
		}

		M.put(I, M.get(I) + "\n" + S);
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
			return counter + " = " + counter + "+" + add + ";\n" + "if (("
					+ counter + " & 262143) == 0) TimeoutChecker.check();";
		} else {

			return counter + " = " + counter + "+" + add + ";";
		}

	}

	private String nameOfVariable(
			Java7Parser.LocalVariableDeclarationContext ctx) {
		return ctx.getChild(1).getChild(0).getChild(0).getText();
	}

	private String nameOfType(Java7Parser.LocalVariableDeclarationContext ctx) {
		return ctx.getChild(0).getText();
	}

	/*
	 * private String objectOfMethod(ParserRuleContext ctx) {
	 * System.out.println(ctx.getChild(0).getChild(0).getText()); String s =
	 * ctx.getChild(0).getChild(0).getText(); String ans = s.split("\\[")[0];
	 * for (int i = 0; i < s.length(); i++) { if (s.charAt(i) == '[') ans +=
	 * '['; } return ans; }
	 */

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
		String counter = "tempCounter" + curCounterNumber;
		int incr = (!f ? 1 : 0);
		if (primitiveWeight.get(nameOfType(ctx)) != null) {

			add(incOfMethod, ctx.getStart().getLine() + incr,
					incrementFor(counter, primitiveWeight.get(nameOfType(ctx))));
			return;
		}
		add(incOfMethod, ctx.getStart().getLine() + incr,
				incrementFor(counter, "8"));
		// incOfMethod.put(ctx.getStart().getLine(), );
	}

	@Override
	public void enterForInit(Java7Parser.ForInitContext ctx) {
		f = true;
	}

	@Override
	public void exitForInit(Java7Parser.ForInitContext ctx) {
		f = false;
	}

	public void memoryClasses() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("memoryClasses"));
		while (true) {
			String s = br.readLine();
			if (s == null)
				break;
			String[] tmp = s.split("\\^");
			// TimeClass tc = new TimeClass(tmp[0], tmp[1]);
			memoryClass.put(tmp[0] + "$" + tmp[1], tmp[2]);
		}
		br.close();

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
		// String ans = "-1";
		// System.out.println(ctx.getText());
		ParseTree t = c.getChild(2);
		while (!t.getText().equals("new"))
			t = t.getChild(0);
		t = t.getParent();
		// System.out.println(t.getText());

		// System.out.println(t.getText());
		String s = t.getChild(1).getText();
		// ans = s.split("[<,\\[,\\(]")[0];

		// if (generics.get(c.getChild(0).getText()) != null)
		// ans = ans + generics.get(c.getChild(0).getText());
		// System.out.println(ans.toString());
		ArrayList<String> arraySizes = new ArrayList<>();
		String curSize = "";
		String ans = "";
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '[') {
				ans += "[";
				curSize = "";
			} else if (s.charAt(i) == ']') {
				// ans += "[";
				arraySizes.add(curSize);
			} else
				curSize += s.charAt(i);
		}

		String name = c.getChild(0).getText();

		if (name.contains("."))
			return;
		String type = (String) vars.get(curMethodName + "#" + name);
		if (type == null) {
			type = (String) vars.get("#" + name);
		}
		String counter = "tempCounter" + curCounterNumber;
		// counters.add(counter);
		// TreeSet<Integer>[][] k = new TreeSet[3][4];
		if (type == null)
			return;
		String typeWithoutArray = type.split("\\[")[0]
				+ (generics.get(name + ans) == null ? "" : generics.get(name
						+ ans));
		String add = "("
				+ (primitiveWeight.get(typeWithoutArray) == null ? "8"
						: primitiveWeight.get(typeWithoutArray)) + ")";
		for (int i = 0; i < arraySizes.size(); i++)
			add = add + " * (" + (arraySizes.get(i)) + ")";
		// if (memoryClass.get(typeWithoutArray + "$" + typeWithoutArray) !=
		// null
		// || primitiveWeight.get(typeWithoutArray) != null) {
		add(incOfMethod, ctx.getStart().getLine() + 1,
				incrementFor(counter, add));
		// }
	}

	@Override
	public void enterFieldExpr(Java7Parser.FieldExprContext ctx1) {
		ParserRuleContext ctx = ctx1.getParent();
		// System.out.println("FE:" + ctx.getText());
		if (nameOfMethod(ctx).equals("-1"))
			return;
		String oom = objectOfMethod(ctx);

		String name = oom.split("\\[")[0];
		if (name.equals("b"))
			System.out.println();
		// System.out.println(name);
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

		String counter = "tempCounter" + curCounterNumber;
		// counters.add(counter);

		if (memoryClass.containsKey(find)) {
			String add = memoryClass.get(find);
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
			// System.out.println(incr);
			if (incOfMethod.get(ctx.getStart().getLine() + incr) != null) {
				String s = incOfMethod.get(ctx.getStart().getLine() + incr);
				// System.out.println(s);
				if (s.equals("{}"))
					System.out.println();
				if (s.contains("}")) {
					int end = s.indexOf('}');
					incOfMethod.put(
							ctx.getStart().getLine() + incr,
							s.substring(0, end) + "\n"
									+ incrementFor(counter, add)
									+ s.substring(end, s.length()));
					return;
				}
				incOfMethod.put(ctx.getStart().getLine() + incr,
						incOfMethod.get(ctx.getStart().getLine() + incr) + "\n"
								+ incrementFor(counter, add));
			} else
				incOfMethod.put(ctx.getStart().getLine() + incr,
						incrementFor(counter, add));
		}

	}

	/*
	 * @Override public void enterFieldExpr(Java7Parser.FieldExprContext ctx1) {
	 * ParserRuleContext ctx = ctx1.getParent(); // System.out.println("FE:" +
	 * objectOfMethod(ctx)); if (nameOfMethod(ctx).equals("-1")) return; String
	 * oom = objectOfMethod(ctx); if (objectOfMethod(ctx).equals("v[[")) {
	 * System.out.println(); } String name = oom.split("\\[")[0]; String type =
	 * (String) vars.get(name); if (type == null) return; int tsz =
	 * type.length() - 1; int nsz = oom.length() - 1; while (type.charAt(tsz) ==
	 * '[' && type.charAt(tsz) == oom.charAt(nsz)) { tsz--; nsz--; } type =
	 * type.substring(0, tsz + 1); String find = type + "$" + nameOfMethod(ctx);
	 * // System.out.println(objectOfMethod(ctx)); String counter = "counter$" +
	 * counters.size(); counters.add(counter);
	 * 
	 * if (timeClass.containsKey(find)) { String add = timeClass.get(find); add
	 * = add.replaceAll("\\$0", fullObjectOfMethod(ctx)); while (true) { int beg
	 * = add.indexOf('$') + 1; if (beg == 0) break; String num = ""; while (beg
	 * < add.length() && add.charAt(beg) >= '0' && addcharAt(beg) <= '9') { num
	 * = num + add.charAt(beg); beg++;
	 * 
	 * } add = add.replaceAll("\\$" + num, argOfMethod(ctx,
	 * Integer.parseInt(num))); } // System.out.println(argOfMethod(ctx, 2));
	 * int incr = (!f ? 1 : 0);
	 * 
	 * if (incOfMethod.get(ctx.getStart().getLine() + incr) != null) { String s
	 * = incOfMethod.get(ctx.getStart().getLine() + incr); if
	 * (s.charAt(s.length() - 1) == '}') {
	 * incOfMethod.put(ctx.getStart().getLine() + incr, s.substring(0,
	 * s.length() - 2) + ";" + "\n" + incrementFor(counter, add) + '}'); return;
	 * } incOfMethod.put(ctx.getStart().getLine() + incr,
	 * incOfMethod.get(ctx.getStart().getLine() + incr) + ";\n" +
	 * incrementFor(counter, add)); } else
	 * incOfMethod.put(ctx.getStart().getLine() + incr, incrementFor(counter,
	 * add)); }
	 * 
	 * }
	 * 
	 * @Override public void enterStatementExpression(
	 * Java7Parser.StatementExpressionContext ctx) { //
	 * System.out.println(objectOfMethod(ctx) + " " + nameOfMethod(ctx)); //
	 * System.out.println("SE:" + ctx.getText()); }
	 */

	@Override
	public void enterBlockStatement(Java7Parser.BlockStatementContext ctx) {
		String counter = "tempCounter" + curCounterNumber;
		String memCounter = "memCounter$" + curCounterNumber;
		// System.out.println(ctx.getChild(0).getChild(0).getText());
		if (ctx.getChild(0).getChild(0).getText().equals("return")) {
			add(incOfMethod, ctx.getStop().getLine(),  memCounter
					+ " = Math.max(tempCounter" + curCounterNumber + " , counterBefore);");
			add(incOfMethod, ctx.getStart().getLine(), "{" + counter 
					+ " = counterBefore;");
			add(incOfMethod, ctx.getStart().getLine() + 1, "}");
		}
	}

	@Override
	public void enterForState(Java7Parser.ForStateContext ctx) {
		// String counter = "counter$" + counters.size();
		// counters.add(counter);
		if (ctx.statement().getText().charAt(0) == ';') {
			incOfMethod
					.put(ctx.statement().getStart().getLine() + 1, "{" + "}");
			change.put(ctx.statement().getStart().getLine(), "s");
			return;
		}
		if (ctx.statement().getText().charAt(0) != '{') {
			incOfMethod.put(ctx.statement().getStart().getLine(), "{ ");
			incOfMethod.put(ctx.statement().getStop().getLine() + 1, "}");
		}// else {
			// incOfMethod.put(ctx.statement().getStart().getLine() + 1,
			// incrementFor(counter));
			// }
	}

	@Override
	public void enterMethodDeclaration(Java7Parser.MethodDeclarationContext ctx) {
		curMethodName = ctx.getChild(1).getText();
		curCounterNumber = ((Integer) (counters.size() + 1)).toString();
		//add(incOfMethod, ctx.getStart().getLine() - 1, "long tempCounter"
			//	+ curCounterNumber + " = 0;");
		
		
		curMethodType = ctx.getChild(0).getText();
		String counter = "memCounter$" + curCounterNumber;
		add(incOfMethod, ctx.getStart().getLine() + 1,
				"long counterBefore = tempCounter" + curCounterNumber + ';');
		if (curMethodType.equals("void"))
			add(incOfMethod, ctx.getStart().getLine() + 1, "if (true) {");

		counters.add(counter);
		if (curMethodType.equals("void")) {
			add(incOfMethod, ctx.getStop().getLine(), "}\n" + counter
					+ " = Math.max(tempCounter" + curCounterNumber + " , counterBefore);");
		}
	}

	@Override
	public void exitMethodDeclaration(Java7Parser.MethodDeclarationContext ctx) {
		curCounterNumber = ((Integer) (Integer.parseInt(curCounterNumber) - 1))
				.toString();
	}
}
