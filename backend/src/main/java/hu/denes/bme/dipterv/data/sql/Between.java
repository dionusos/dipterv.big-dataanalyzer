package hu.denes.bme.dipterv.data.sql;

public class Between extends TwoOperandExpression {
    private String value;

    public Between(String value) {
        this.value = value;
    }

    @Override
    protected String operator() {
        return " BETWEEN ";
    }

    @Override
    public String toString() {
        return value + " " + operator() + " (" + left.toString() + " AND " +  right.toString() + ") ";
    }
}
