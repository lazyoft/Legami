package com.lazyoft.legami.binding;

import com.lazyoft.legami.accessors.Accessor;
import com.lazyoft.legami.accessors.IAccessor;
import com.lazyoft.legami.accessors.IValueConverter;
import com.lazyoft.legami.parsing.BindingExpressionToken;
import com.lazyoft.legami.parsing.BindingListItemToken;
import com.lazyoft.legami.parsing.BindingListToken;
import com.lazyoft.legami.parsing.BindingToken;
import com.lazyoft.legami.parsing.ConstantLiteralToken;
import com.lazyoft.legami.parsing.ConversionToken;
import com.lazyoft.legami.parsing.ExpressionToken;
import com.lazyoft.legami.parsing.ITokenVisitor;
import com.lazyoft.legami.parsing.IdentifierToken;
import com.lazyoft.legami.parsing.PathToken;
import com.lazyoft.legami.parsing.StringLiteralToken;
import com.lazyoft.legami.parsing.Token;

public class TokenVisitor implements ITokenVisitor<VisitResultList> {
    private IAccessor accessorChain;
    private final IValueConverterProvider valueConverterProvider;
    private final IResourceProvider resourceProvider;

    public TokenVisitor(IAccessor accessorChain, IValueConverterProvider valueConverterProvider, IResourceProvider resourceProvider) {
        this.accessorChain = accessorChain;
        this.valueConverterProvider = valueConverterProvider;
        this.resourceProvider = resourceProvider;
    }

    public VisitResultList visit(Token token, VisitResultList param) {
        if(token == Token.NotFound)
            return param;
        return token.acceptVisitor(this, null);
    }

    @Override
    public VisitResultList visit(IdentifierToken token, VisitResultList param) {
        VisitResult result = param.getCurrentResult();
        final String path = token.getName();
        result.target = new IProperty() {
            @Override
            public Object get(Object source) {
                return accessorChain.get(path, source);
            }

            @Override
            public void set(Object source, Object value) {
                accessorChain.set(path, source, value);
            }
        };
        return param;
    }

    @Override
    public VisitResultList visit(final PathToken token, VisitResultList param) {
        VisitResult result = param.getCurrentResult();
        if(token.getIdentifiers().size() == 1) {
            return token.getIdentifiers().get(0).acceptVisitor(this, param);
        }

        final String[] identifiers = new String[token.getIdentifiers().size()];
        int index = 0;
        for(Token identifier: token.getIdentifiers())
            identifiers[index++] = ((IdentifierToken)identifier).getName();

        result.target = new IProperty() {
            @Override
            public Object get(Object source) {
                Object result = source;
                for(String identifier: identifiers) {
                    result = accessorChain.get(identifier, result);
                    if(result == null || result == Accessor.noValue)
                        break;
                }
                return result;
            }

            @Override
            public void set(Object source, Object value) {
                Object result = source;
                for(int i = 0; i < identifiers.length - 1; i++) {
                    result = accessorChain.get(identifiers[i], result);
                    if(result == null || result == Accessor.noValue)
                        break;
                }

                if(result != null && result != Accessor.noValue)
                    accessorChain.set(identifiers[identifiers.length - 1], result, value);
            }
        };
        return param;
    }

    @Override
    public VisitResultList visit(ConstantLiteralToken token, VisitResultList param) {
        VisitResult result = param.getCurrentResult();
        final Object constant = resourceProvider.get(token.getName());
        if(result.converted)
            result.parameter = constant;
        else {
            result.target = new IProperty() {
                @Override
                public Object get(Object source) {
                    return constant;
                }

                @Override
                public void set(Object source, Object value) {
                }
            };
        }
        return param;
    }

    @Override
    public VisitResultList visit(StringLiteralToken token, VisitResultList param) {
        VisitResult result = param.getCurrentResult();
        final String literal = token.getText();
        if(result.converted)
            result.parameter = literal;
        else {
            result.target = new IProperty() {
                @Override
                public Object get(Object source) {
                    return literal;
                }

                @Override
                public void set(Object source, Object value) {
                }
            };
        }
        return param;
    }

    @Override
    public VisitResultList visit(ConversionToken token, VisitResultList param) {
        final VisitResult result = param.getCurrentResult();

        result.converted = true;
        param = token.getParameter().acceptVisitor(this, param);
        param = token.getPath().acceptVisitor(this, param);

        final IProperty base = result.target;
        final String converterName = token.getName();

        result.target = new IProperty() {
            @Override
            public Object get(Object source) {
                IValueConverter converter = valueConverterProvider.get(converterName);
                return converter.convert(base.get(source), result.parameter);
            }

            @Override
            public void set(Object source, Object value) {
                IValueConverter converter = valueConverterProvider.get(converterName);
                base.set(source, converter.convertBack(value, result.parameter));
            }
        };

        return param;
    }

    @Override
    public VisitResultList visit(final BindingExpressionToken token, VisitResultList param) {
        VisitResult result = new VisitResult();
        param.add(result);
        result.source = new IProperty() {
            @Override
            public Object get(Object source) {
                return accessorChain.get(token.getSourceName(), source);
            }

            @Override
            public void set(Object source, Object value) {
                accessorChain.set(token.getSourceName(), source, value);
            }
        };
        return token.getExpression().acceptVisitor(this, param);
    }

    @Override
    public VisitResultList visit(BindingListItemToken token, VisitResultList param) {
        return token.getTokens().get(0).acceptVisitor(this, param);
    }

    @Override
    public VisitResultList visit(BindingListToken token, VisitResultList param) {
        for(Token currentToken: token.getTokens()) {
            currentToken.acceptVisitor(this, param);
        }
        return param;
    }

    @Override
    public VisitResultList visit(BindingToken token, VisitResultList param) {
        return token.getTokens().get(0).acceptVisitor(this, new VisitResultList());
    }

    @Override
    public VisitResultList visit(ExpressionToken token, VisitResultList param) {
        return token.getTokens().get(0).acceptVisitor(this, param);
    }
}