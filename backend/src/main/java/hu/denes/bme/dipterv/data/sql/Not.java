package hu.denes.bme.dipterv.data.sql;

public class Not extends OneOperandExpression {
    public Not(Expression expression) {
        super(expression);
    }

    @Override
    protected String operator() {
        return " NOT ";
    }
}
