package com.lazyoft.legami.binding;

public class BindingExpression {
    private BindingKind kind;
    private IProperty source;
    private IProperty target;
    private String name;

    public BindingExpression(String name, BindingKind kind, IProperty source, IProperty target) {
        this.name = name;
        this.kind = kind;
        this.source = source;
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public BindingKind getKind() {
        return kind;
    }

    public IProperty getSource() {
        return source;
    }

    public IProperty getTarget() {
        return target;
    }
}
