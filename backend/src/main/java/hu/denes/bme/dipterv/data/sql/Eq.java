package hu.denes.bme.dipterv.data.sql;

public class Eq extends TwoOperandExpression {
    @Override
    protected String operator() {
        return " = ";
    }
}
