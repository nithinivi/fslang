import ast.Expression;
import executor.Executor;
import parser.Parser;

public class FSlang {

    public static void main(String[] args) throws Exception {
        System.out.println("Functional Slang");
        var x = new Parser("let add = lambda x.lambda y. x+y in add 2 3");
        Expression expression = x.parseProgram();
        Executor e = new Executor();
        e.execute(expression);
    }

}
