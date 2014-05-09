import java.io.*;
import java.util.*;

import org.abego.treelayout.internal.util.java.lang.string.StringUtil;
import org.antlr.v4.parse.GrammarTreeVisitor.tokenSpec_return;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;

public class Java7VarProfiler extends Java7BaseListener {
	private final CommonTokenStream ts;

	public final Map<String, String> varType = new HashMap<String, String>();
	private final boolean useTimeoutChecker;
	public Map<String, String> generics = new HashMap<String, String>();
	public Map<String, String> varOfMethod = new HashMap<String, String>();

	private String curMethodName;

	public Java7VarProfiler(CommonTokenStream tokens, boolean useTimeoutChecker)
			throws FileNotFoundException {
		this.ts = tokens;
		this.useTimeoutChecker = useTimeoutChecker;
	}

	private String nameOfVariable(
			Java7Parser.LocalVariableDeclarationContext ctx) {
		return ctx.getChild(1).getChild(0).getChild(0).getText();
	}

	private String nameOfType(Java7Parser.LocalVariableDeclarationContext ctx) {
		// System.out.println(ctx.getText());
		if (ctx.getChild(1).getChild(0).getChild(2) == null)
			return "-1";
		// System.out.println(ctx.getChild(1).getChild(0).getChild(2).getChild(0).getChild(0).getText());
		String tmp = ctx.getChild(1).getText();
		if (ctx.getChild(1).getChild(0).getChild(2).getChild(0).getChild(0)
				.getText().equals("new")) {
			String s = ctx.getChild(1).getChild(0).getChild(2).getChild(0)
					.getChild(1).getText();
			
			String ans = s.split("[<,\\[]")[0];
			for (int i = 0; i < s.length(); i++)
				if (s.charAt(i) == '[')
					ans += "[";
			return ans;
		} else
			return "-1";

	}

	private String objectOfMethod(
			Java7Parser.LocalVariableDeclarationContext ctx) {
		return ctx.getChild(0).getChild(1).getText();
	}

	private String nameOfMethod(Java7Parser.StatementExpressionContext ctx) {
		return ctx.getChild(0).getChild(0).getChild(0).getText();
	}

	@Override
	public void enterLocalVariableDeclaration(
			Java7Parser.LocalVariableDeclarationContext ctx) {
		varOfMethod.put(ctx.getChild(1).getChild(0).getChild(0).getText(),
				curMethodName);
		if (ctx.getChild(0).getChild(0).getChildCount() > 1) {
			// System.out.println(ctx.getChild(1).getChild(0)
			// .getText());
			String s = ctx.getChild(1).getChild(0).getText();
			//System.out.println(ctx.getChild(0).getText());
			String name = s.split("=")[0];
			for (int i = 0; i < ctx.getChild(0).getText().length(); i++) {
				if (ctx.getChild(0).getText().charAt(i) == '[')
					name += '[';
			}
			generics.put(name, ctx.getChild(0)
					.getChild(0).getChild(1).getText());

		}
		// System.out.println(nameOfVariable(ctx) + " " + nameOfType(ctx));
	}

	@Override
	public void enterStatementExpression(
			Java7Parser.StatementExpressionContext ctx) {
		// System.out.println(ctx.getChild(0).getChild(2).getChild(0).getText());
		if (2 == 2)
			return;
		
		String ans = "-1";
		System.out.println(ctx.getText());
		if (ctx.getText().equals("s=newTreeSet<>()")) {
			//System.out.println();
		}
		if (ctx.getChild(0).getChild(2).getChildCount() > 0
				&& ctx.getChild(0).getChild(2).getChild(0).getText()
						.equals("new")) {
			String s = ctx.getChild(0).getChild(2).getChild(1).getText();
			ans = s.split("[<,\\[,\\(]")[0];

			if (generics.get(ctx.getChild(0).getChild(0).getText()) != null)
				ans = ans + generics.get(ctx.getChild(0).getChild(0).getText());
			for (int i = 0; i < s.length(); i++)
				if (s.charAt(i) == '[')
					ans += "[";
		}
		String name = ctx.getChild(0).getChild(0).getText();
		if (name.contains("."))
			return;
		varType.put(
				(varOfMethod.get(name) == null ? "" : varOfMethod.get(name))
						+ '#' + name, ans);
		// System.out.println(objectOfMethod(ctx) + " " + nameOfMethod(ctx));
		// System.out.println("SE:" + ctx.getText());

	}

	@Override
	public void enterMethodDeclaration(Java7Parser.MethodDeclarationContext ctx) {
		curMethodName = ctx.getChild(1).getText();
	}

	@Override
	public void enterNewCreatorExpr(Java7Parser.NewCreatorExprContext ctx) {
		ParserRuleContext c = ctx;
		int o = 0;
		while (!c.getText().contains("=") && o < 3) {
			o++;
			c = c.getParent();
		}
		if (o == 3)
			return;
		String ans = "-1";
		ParseTree t = c.getChild(2);
		while (!t.getText().equals("new"))
			t = t.getChild(0);
		t = t.getParent();

		String s = t.getChild(1).getText();
		ans = s.split("[<,\\[,\\(]")[0];
		String l = c.getChild(0).getText();
		for (int i = 0; i<c.getText().length(); i++)
			if (c.getText().charAt(i) == '[')
				l = l + '[';
		if (generics.get(l) != null)
			ans = ans + generics.get(l);
		for (int i = 0; i < s.length(); i++)
			if (s.charAt(i) == '[')
				ans += "[";

		String name = c.getChild(0).getText();
		if (name.contains("."))
			return;
		varType.put(
				(varOfMethod.get(name) == null ? "" : varOfMethod.get(name))
						+ '#' + name, ans);
	}
}
