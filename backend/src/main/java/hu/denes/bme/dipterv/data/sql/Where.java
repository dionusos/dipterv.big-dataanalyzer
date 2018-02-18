package hu.denes.bme.dipterv.data.sql;

public class Where {
    public Expression expression;

    public Where expression(Expression e) {
        this.expression = e;
        return this;
    }

    @Override
    public String toString() {
        return expression.toString();
    }
}
