package lexer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class KeyWordTable {

    private static final Map<String, TOKEN> keyWords = new HashMap<>() {{
        put("nil", TOKEN.NIL);
        put("true", TOKEN.TRUE);
        put("false", TOKEN.FALSE);
        put("let", TOKEN.LET);
        put("in", TOKEN.IN);
        put("if", TOKEN.IF);
        put("then", TOKEN.THEN);
        put("else", TOKEN.ELSE);
        put("lambda", TOKEN.LAMBDA);
        put("or", TOKEN.OR);
        put("and", TOKEN.AND);
        put("null", TOKEN.NULL);
        put("hd", TOKEN.HD);
        put("tl", TOKEN.TL);
        put("rec", TOKEN.REC);
    }};
    private static final Set<String> keyWordNames = keyWords.keySet();


    public static TOKEN symbol(String keyWord) {
        return keyWords.getOrDefault(keyWord, TOKEN.EOF);
    }

    public static int keywordMatchCount(String inputKeyword) {
        int count = 0;
        for (var keyword : keyWordNames) {
            if (!keyword.equals("") && keyword.startsWith(inputKeyword))
                count++;
        }
        return count;
    }


}