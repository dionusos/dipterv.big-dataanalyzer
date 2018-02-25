package hu.denes.bme.dipterv.data.sql;

public class Or extends NOperandExpression{

    public Or() {

    }

    public Or(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected String operator() {
        return " OR ";
    }

}
