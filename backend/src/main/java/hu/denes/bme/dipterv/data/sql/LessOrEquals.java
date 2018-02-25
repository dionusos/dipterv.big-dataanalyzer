package hu.denes.bme.dipterv.data.sql;

public class LessOrEquals extends TwoOperandExpression {

    public LessOrEquals(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected String operator() {
        return " <= ";
    }
}
