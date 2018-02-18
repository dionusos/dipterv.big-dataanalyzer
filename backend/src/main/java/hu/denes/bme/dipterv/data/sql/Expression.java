package hu.denes.bme.dipterv.data.sql;

public class Expression {
    private String expression;

    public Expression(String expression){
        this.expression = expression;
    }

    public Expression(Integer expression){
        this.expression = "" + expression;
    }

    public Expression(){
        this.expression = "";
    }

    protected String operator() { return ""; }

    @Override
    public String toString() {
        return expression;
    }
}
