package hu.denes.bme.dipterv.data.sql;

public class GreaterOrEquals extends TwoOperandExpression {
    @Override
    protected String operator() {
        return " >= ";
    }
}
