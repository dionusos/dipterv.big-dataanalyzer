package hu.denes.bme.dipterv.data.sql;

public class OneOperandExpression extends Expression{
    protected Expression expression;

    public OneOperandExpression(Expression expression) {
        this.expression = expression;
    }

    public OneOperandExpression() {
        this.expression = null;
    }

    @Override
    public String toString() {
        return operator() + " (" + expression.toString() + ") ";
    }

    @Override
    protected String operator() {
        return "";
    }

    public OneOperandExpression expression(Expression e) {
        this.expression = e;
        return this;
    }
}
