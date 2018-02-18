package hu.denes.bme.dipterv.data.sql;

public abstract class TwoOperandExpression extends Expression {
    protected Expression left;
    protected Expression right;

    public TwoOperandExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public TwoOperandExpression() {
        this.left = null;
        this.right = null;
    }

    @Override
    public String toString() {
        return " (" + left.toString() + ") " + operator() + " (" +  right.toString() + ") ";
    }

    public TwoOperandExpression left(Expression e){
        this.left = e;
        return this;
    }
    public TwoOperandExpression right(Expression e) {
        this.right = e;
        return this;
    }

}
