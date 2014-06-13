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

import java.util.List;

public class TokenVisitor implements ITokenVisitor<BindingDescriptors> {
    private IAccessor accessorChain;
    private final IValueConverterProvider valueConverterProvider;
    private final IResourceProvider resourceProvider;

    public TokenVisitor(IAccessor accessorChain, IValueConverterProvider valueConverterProvider, IResourceProvider resourceProvider) {
        this.accessorChain = accessorChain;
        this.valueConverterProvider = valueConverterProvider;
        this.resourceProvider = resourceProvider;
    }

    public BindingDescriptors visit(Token token, BindingDescriptors param) {
        if(token == Token.NotFound)
            return param;
        return token.acceptVisitor(this, null);
    }

    @Override
    public BindingDescriptors visit(IdentifierToken token, BindingDescriptors param) {
        BindingDescriptor result = param.getLast();
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
    public BindingDescriptors visit(final PathToken token, BindingDescriptors param) {
        BindingDescriptor result = param.getLast();
        if(token.getIdentifiers().size() == 1) {
            result.targetParts = new String[] { ((IdentifierToken)token.getIdentifiers().get(0)).getName() };
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
        result.targetParts = identifiers;
        return param;
    }

    @Override
    public BindingDescriptors visit(ConstantLiteralToken token, BindingDescriptors param) {
        BindingDescriptor result = param.getLast();
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
    public BindingDescriptors visit(StringLiteralToken token, BindingDescriptors param) {
        BindingDescriptor result = param.getLast();
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
    public BindingDescriptors visit(ConversionToken token, BindingDescriptors param) {
        final BindingDescriptor result = param.getLast();

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
    public BindingDescriptors visit(final BindingExpressionToken token, BindingDescriptors param) {
        BindingDescriptor result = new BindingDescriptor();
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
        result.sourceParts = new String[] { token.getSourceName() };
        return token.getExpression().acceptVisitor(this, param);
    }

    @Override
    public BindingDescriptors visit(BindingListItemToken token, BindingDescriptors param) {
        return token.getTokens().get(0).acceptVisitor(this, param);
    }

    @Override
    public BindingDescriptors visit(BindingListToken token, BindingDescriptors param) {
        for(Token currentToken: token.getTokens()) {
            currentToken.acceptVisitor(this, param);
        }
        return param;
    }

    @Override
    public BindingDescriptors visit(BindingToken token, BindingDescriptors param) {
        return token.getTokens().get(0).acceptVisitor(this, new BindingDescriptors());
    }

    @Override
    public BindingDescriptors visit(ExpressionToken token, BindingDescriptors param) {
        return token.getTokens().get(0).acceptVisitor(this, param);
    }
}