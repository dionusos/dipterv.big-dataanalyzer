package hu.denes.bme.dipterv.data.sql;

public class GreaterThan extends TwoOperandExpression{

    @Override
    protected String operator() {
        return " > ";
    }
}
