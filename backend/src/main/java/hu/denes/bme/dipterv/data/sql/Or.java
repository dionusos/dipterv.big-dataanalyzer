package hu.denes.bme.dipterv.data.sql;

public class Or extends TwoOperandExpression{
    public Or(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected String operator() {
        return " OR ";
    }

}
