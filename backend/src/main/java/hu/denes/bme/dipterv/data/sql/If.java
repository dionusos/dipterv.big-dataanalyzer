package hu.denes.bme.dipterv.data.sql;

public class If extends Expression {
    private Expression condition;
    private Expression _true;
    private Expression _false;

    public If condition(Expression condition) {
        this.condition = condition;
        return this;
    }

    public If _true(Expression expression) {
        this._true = expression;
        return this;
    }

    public If _false(Expression expression) {
        this._false = expression;
        return this;
    }

    public Expression get_false() {
        return _false;
    }

    public Expression get_true() {
        return _true;
    }

    @Override
    public String toString() {
        return " IF(" + condition + ", " + _true + ", " + _false + ")";
    }
}
