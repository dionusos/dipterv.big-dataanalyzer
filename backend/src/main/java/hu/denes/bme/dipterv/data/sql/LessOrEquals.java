package hu.denes.bme.dipterv.data.sql;

public class LessOrEquals extends TwoOperandExpression {
    @Override
    protected String operator() {
        return " <= ";
    }
}
