package hu.denes.bme.dipterv.data.sql;

public class And extends NOperandExpression{

    public And() {

    }

    public And(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected String operator() {
        return " AND ";
    }

}
