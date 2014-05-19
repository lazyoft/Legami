package com.lazyoft.legami.parsing.tokens;

import com.lazyoft.legami.parsing.Token;
import com.lazyoft.legami.parsing.TokenUtils;

import java.util.ArrayList;
import java.util.List;

public class Conversion extends Token {
    public Conversion(Object ...tokens) {
        super(tokens);
    }

    public static Conversion produce(List<Token> tokens) {
        List<Token> result = new ArrayList<Token>();

        // conversion = identifier open-paren *ws path *ws [comma *ws [string-literal / constant-literal ] *ws close-paren
        Identifier identifier = Identifier.peek(tokens);
        if(identifier != null && tokens.get(Identifier.sizeOf(identifier)) instanceof Terminals.OpenParen) {
            result.add(identifier);
            Identifier.consume(tokens, identifier);
            result.add(tokens.get(0));
            tokens.remove(0);
            TokenUtils.consumeWhitespace(tokens);
            Path path = Path.produce(tokens);
            if(path != null) {
                result.add(path);
                TokenUtils.consumeWhitespace(tokens);
                if(tokens.get(0) instanceof Terminals.Comma) {
                    result.add(tokens.get(0));
                    tokens.remove(0);
                    TokenUtils.consumeWhitespace(tokens);
                    StringLiteral stringLiteral = StringLiteral.produce(tokens);
                    if(stringLiteral != null) {
                        result.add(stringLiteral);
                    }
                    else {
                        ConstantLiteral constantLiteral = ConstantLiteral.produce(tokens);
                        if(constantLiteral != null) {
                            result.add(constantLiteral);
                        }
                    }
                    TokenUtils.consumeWhitespace(tokens);
                    if(tokens.get(0) instanceof Terminals.CloseParen) {
                        result.add(tokens.get(0));
                        tokens.remove(0);
                        return new Conversion(result);
                    }
                }
            }
        }

        return null;
    }
}
