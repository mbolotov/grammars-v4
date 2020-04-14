import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.junit.Assert;
import org.junit.Test;

public class PerformanceTest {
	static int functionCount = 256;

	static String functions;
	static {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < functionCount; i++) {
			b.append(String.format("\tfunc test%s() {\n\t}\n", i));
		}
		functions = b.toString();
	}

	static String  source = "class MyTest : XCTestCase {\n" + functions + "\n}";

	@Test
	public void test1kLinesOfTestMethods() {
		long start = System.currentTimeMillis();
		Swift3Lexer lexer = new Swift3Lexer(CharStreams.fromString(source));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		Swift3Parser parser = new Swift3Parser(tokens);
        parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
        parser.top_level();
        long time = System.currentTimeMillis() - start;
		Assert.assertTrue("Swift3 parser is too slow: " + time + " ms total, " + (time / functionCount) + " ms per empty function", time < 500);
	}
}
