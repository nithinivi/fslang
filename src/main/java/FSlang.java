import ast.Expression;
import executor.Executor;
import parser.Parser;

import java.nio.file.Files;
import java.nio.file.Path;

public class FSlang {

    public static void main(String[] args) throws Exception {
        var f = new FSlang();
        f.run("ex1.txt");
    }

    public void run(String path) throws Exception {
        String code = Files.readString(Path.of(path));

        var x = new Parser(code);
        Expression expression = x.parseProgram();
        Executor e = new Executor();
        e.execute(expression);
    }

}
