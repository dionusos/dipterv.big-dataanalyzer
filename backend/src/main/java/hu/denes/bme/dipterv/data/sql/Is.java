package hu.denes.bme.dipterv.data.sql;

public class Is extends OneOperandExpression {
    public Is(Expression expression) {
        super(expression);
    }

    @Override
    protected String operator() {
        return " IS ";
    }
}
