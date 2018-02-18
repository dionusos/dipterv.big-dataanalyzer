package hu.denes.bme.dipterv.data.sql;

public class In extends OneOperandExpression {
    @Override
    protected String operator() {
        return " IN ";
    }
}
