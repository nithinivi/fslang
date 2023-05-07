package lexer;

import java.util.function.Supplier;

import static lexer.TOKEN.*;


public class Lexer {

    private final int length;
    String expr;
    int index;
    double number;
    Supplier<Character> ch = () -> this.expr.charAt(this.index);
    String quotedString;
    String variableName;
    private Integer lineNumber;


    public Lexer(String expr) {
        this.expr = expr;
        length = expr.length();
        index = 0;
        lineNumber = 1;

    }

    public TOKEN getSymbol() throws Exception {
        TOKEN tok;

        while (index < length && ch.get() == ' ' || index < length && ch.get() == '\t')
            index++;

        // if end of string
        if (index >= length) {
            return EOF;
        }
        switch (ch.get()) {

            case '{' -> {

                while (ch.get() != '}') // if not closed
                    index++;
                index++;
                tok = getSymbol();
            }
            case '\n' -> {
                lineNumber++;
                index++;
                tok = getSymbol();
            }

            case '\r', '\t', ' ' -> {
                index++;
                tok = getSymbol();
            }

            case ',' -> {
                index++;
                tok = COMMA;
            }
            case '+' -> {
                index++;
                tok = PLUS;
            }
            case '-' -> {
                tok = MINUS;
                index++;
            }
            case '*' -> {
                tok = MUL;
                index++;
            }
            case '/' -> {
                tok = DIV;
                index++;
            }
            case '(' -> {
                tok = OPREN;
                if (ch.get() == ')') {
                    index++;
                    tok = EMPTY;
                }
            }
            case ')' -> {
                index++;
                tok = CPREN;
            }
            case '[' -> {
                index++;
                tok = SQ_OPEN;
            }
            case ']' -> {
                index++;
                tok = SQ_CLOSE;
            }

            case '.' -> {
                index++;
                tok = DOT;
            }
            case '"' -> {
                index++;
                tok = QUOTE;
            }
            case '=' -> {
                index++;
                tok = EQ;
            }

            case '<' -> {
                tok = LT;
                if (ch.get() == '>') {
                    index++;
                    tok = NE;
                } else if (ch.get() == '=') {
                    index++;
                    tok = LE;
                }
            }

            case '>' -> {
                tok = GT;
                index++;
                if (ch.get() == '=') {
                    index++;
                    tok = GE;
                }

            }

            case ':' -> {
                tok = COLON;
                if (expr.charAt(index++) == ':') {
                    index += 2;
                    tok = CONS;
                }
            }

            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> tok = getDoubleFromString();

            case '\'' -> {
                parseCharacterLiteral();
                tok = CHAR_LITERAL;
            }

            default -> {
                tok = getKeyWordSymbol();
                if (tok != EOF)
                    break;

                tok = getVariableSymbol();
                if (tok != EOF)
                    break;

                throw new IllegalStateException("Unexpected char found : " + ch.get());
            }
        }

        return tok;
    }


    private TOKEN getDoubleFromString() {

        var bld = new StringBuilder();
        while (index < length && Character.isDigit(ch.get())) {
            index++;
            bld.append(ch.get());
        }

        this.number = Double.parseDouble(bld.toString());
        return NUMERAL;
    }

    private TOKEN getKeyWordSymbol() {
        String possibleKeyWord = "";
        int tempIndex = index;
        TOKEN tokenFromKeyWord = KeyWordTable.symbol(possibleKeyWord);

        while (KeyWordTable.keywordMatchCount(possibleKeyWord) > 0) {
            if (KeyWordTable.symbol(possibleKeyWord) != EOF) {
                tokenFromKeyWord = KeyWordTable.symbol(possibleKeyWord);
                this.index = tempIndex;
            }
            possibleKeyWord += expr.charAt(tempIndex);
            tempIndex++;
        }

        return tokenFromKeyWord;
    }


    private TOKEN getVariableSymbol() {

        if (Character.isLetter(ch.get())) {
            var bld = new StringBuilder();
            while (index < length &&
                    Character.isAlphabetic(ch.get()) ||
                    Character.isDigit(ch.get()) ||
                    ch.get() == '_') {
                bld.append(ch.get());
                index++;
            }
            variableName = bld.toString();
            return WORD;

        } else
            return EOF;
    }


    private void parseCharacterLiteral() throws Exception {
        var bld = new StringBuilder();
        int quoteCount = 0;

        if (ch.get() == '\'') {
            while (index < length && quoteCount < 2) {
                if (ch.get() == '\'')
                    quoteCount++;
                else
                    bld.append(ch.get());
            }
        }

        quotedString = bld.toString();


        if (quoteCount != 2)
            throw new Exception("Quoted string ended");


        if (quotedString.length() != 1)
            throw new Exception("Quoted string should be 1, got \"+ t" + quotedString.length());


    }


    String getVariableName() {
        return variableName;
    }

    String getQuotedString() {
        return quotedString;
    }

    double getNumber() {
        return number;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }
}
