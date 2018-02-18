package hu.denes.bme.dipterv.data.sql;

public class LessThan extends TwoOperandExpression {

    @Override
    protected String operator() {
        return " < ";
    }
}
