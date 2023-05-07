package lexer;

public enum TOKEN {
    EOF,
    NIL,
    IN, LET, FALSE, TRUE,
    IF, THEN, ELSE, LAMBDA,
    OR, AND, NULL, HD, TL, REC,
    COMMA, MUL, DIV, PLUS, MINUS,
    OPREN, CPREN, SQ_OPEN, SQ_CLOSE, EMPTY,
    DOT, QUOTE, EQ,
    LT, NE, LE, GT, GE,
    COLON, CONS,
    CHAR_LITERAL, WORD, NUMERAL;
}