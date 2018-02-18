package hu.denes.bme.dipterv.data.sql;

public class And extends TwoOperandExpression{
    public And(Expression left, Expression right) {
        super(left, right);
    }

    public And() {
        super(null, null);
    }

    @Override
    protected String operator() {
        return " AND ";
    }

}
