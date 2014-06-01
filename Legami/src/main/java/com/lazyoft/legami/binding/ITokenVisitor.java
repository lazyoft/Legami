package com.lazyoft.legami.binding;

import com.lazyoft.legami.parsing.BindingExpressionToken;
import com.lazyoft.legami.parsing.BindingListItemToken;
import com.lazyoft.legami.parsing.BindingListToken;
import com.lazyoft.legami.parsing.BindingToken;
import com.lazyoft.legami.parsing.ConstantLiteralToken;
import com.lazyoft.legami.parsing.ConversionToken;
import com.lazyoft.legami.parsing.ExpressionToken;
import com.lazyoft.legami.parsing.PathToken;
import com.lazyoft.legami.parsing.StringLiteralToken;
import com.lazyoft.legami.parsing.Token;

public interface ITokenVisitor<TResult> {
    TResult visit(Token token, TResult partial);
    TResult visit(BindingToken token, TResult partial);
    TResult visit(ExpressionToken token, TResult partial);
    TResult visit(PathToken token, TResult partial);
    TResult visit(ConstantLiteralToken token, TResult partial);
    TResult visit(StringLiteralToken token, TResult partial);
    TResult visit(ConversionToken token, TResult partial);
    TResult visit(BindingExpressionToken token, TResult partial);
    TResult visit(BindingListItemToken token, TResult partial);
    TResult visit(BindingListToken token, TResult partial);
}

