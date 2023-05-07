package parser;

import ast.*;
import lexer.Lexer;
import lexer.TOKEN;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static lexer.TOKEN.*;

public class Parser extends Lexer {

    private final int applicationPriority = 7;

    private List<TOKEN> symbols = Arrays.asList(TOKEN.values());

    private List<TOKEN> unaryOperators = List.of(MINUS, HD, TL, NULL, MINUS);
    private List<TOKEN> binaryOperators = List.of(CONS, AND, OR, EQ, NE, LT, LE, GT, GE, PLUS, MINUS, MUL, DIV);
    private List<TOKEN> rightAssoc = List.of(CONS);
    private List<TOKEN> startExp = Stream.concat(unaryOperators.stream(), Stream.of(WORD, NUMERAL, EMPTY, NIL, CHAR_LITERAL, TRUE, FALSE, OPREN, LET, IF, LAMBDA)).toList();
    private Map<TOKEN, Integer> oprPriority = new HashMap<>();
    private TOKEN token;


    public Parser(String program) {
        super(program);
        this.initOperatorPriority();
    }

    private void initOperatorPriority() {
        for (var symbol : symbols) {
            oprPriority.put(symbol, 0);
        }

        oprPriority.put(CONS, 1);
        oprPriority.put(OR, 2);
        oprPriority.put(AND, 3);

        for (var symbol : List.of(EQ, NE, LT, GT, LE, GE))
            oprPriority.put(symbol, 4);

        oprPriority.put(PLUS, 5);
        oprPriority.put(MINUS, 5);
        oprPriority.put(MUL, 6);
        oprPriority.put(DIV, 3);

    }

    private void parseToken() throws Exception {
        this.token = getSymbol();
    }


    public Expression parseProgram() throws Exception {
        return parseExpression();
    }

    private Expression parseExpression() throws Exception {
        return parseExp(1);
    }

    private Expression parseExp(Integer priority) throws Exception {
        Expression a, e = null;
        if (priority < applicationPriority) {
            e = parseExp(priority + 1);

            if (binaryOperators.contains(token) || rightAssoc.contains(token) && oprPriority.get(this.token).equals(priority)) {
                a = e;
                TOKEN binaryOperator = token;
                parseToken();
                e = new BinaryExpression(binaryOperator, a, parseExp(priority));
            } else {
                while (binaryOperators.contains(token) && !rightAssoc.contains(token) && oprPriority.get(token).equals(priority)) {
                    a = e;
                    TOKEN binaryOperator = token;
                    parseToken();
                    e = new BinaryExpression(binaryOperator, a, this.parseExp(priority + 1));
                }
            }

        } else if (priority == applicationPriority) {
            e = parseExp(priority + 1);
            while (startExp.contains(token) && !binaryOperators.contains(token)) {
                a = e;
                e = new Application(a, parseExp(priority + 1));
            }
        } else if (unaryOperators.contains(token)) {
            TOKEN unaryOperator = token;
            parseToken();
            e = new UnaryExpression(unaryOperator, parseExp(priority));
        } else if (startExp.contains(token)) {
            switch (token) {
                case WORD -> {
                    e = new Ident(getVariableName());
                    parseToken();
                }

                case NUMERAL -> {
                    e = new NumCon(getNumber());
                    parseToken();
                }

                case CHAR_LITERAL -> {
                    e = new CharCon(getQuotedString().charAt(0));
                    parseToken();
                }

                case EMPTY -> {
                    e = new EmptyCon();
                    parseToken();
                }

                case NIL -> {
                    e = new NilCon();
                    parseToken();
                }

                case TRUE -> {
                    e = new BoolCon(true);
                    parseToken();
                }

                case FALSE -> {
                    e = new BoolCon(false);
                    parseToken();
                }

                case OPREN -> {
                    parseToken();
                    e = this.parseExpression();
                    checkTokenAndThrowError(CPREN, ") expected");
                    parseToken();
                }
                case LET -> {
                    parseToken();
                    e = parseParameterDeclarations();
                }
                case IF -> {
                    parseToken();
                    Expression e1 = parseExpression();
                    this.checkTokenAndThrowError(THEN, "no then");

                    parseToken();
                    Expression e2 = parseExpression();
                    this.checkTokenAndThrowError(ELSE, "no else");

                    parseToken();
                    Expression e3 = parseExpression();
                    e = new IfExp(e1, e2, e3);
                }
                case LAMBDA -> {
                    parseToken();
                    Expression fparm = parseFunctionParameter();
                    parseToken();
                    checkTokenAndThrowError(DOT, "Excepted");
                    parseToken();
                    e = new LambdaExp(fparm, this.parseExpression());
                }
            }

        } else
            error("bad operand");


        return e;
    }

    private Expression parseFunctionParameter() {
    }

    private Expression parseParameterDeclarations() {
    }

    private void checkTokenAndThrowError(TOKEN token, String s) {
    }

    private void error(String message) {
    }
}
