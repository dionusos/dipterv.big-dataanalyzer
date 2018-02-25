package hu.denes.bme.dipterv.data.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class NOperandExpression  extends Expression {
    private List<Expression> expressions = new ArrayList<>();

    public NOperandExpression(){

    }

    public NOperandExpression(Expression e) {
        expressions.add(e);
    }

    public NOperandExpression(Expression... e) {
        expressions.addAll(Arrays.asList(e));
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        Iterator<Expression> it = expressions.iterator();
        while (it.hasNext()) {
            Expression e = it.next();
            res.append(" (").append(e).append(") ");
            if(it.hasNext()) {
                res.append(operator());
            }
        }
        return res.toString();
    }

    public void add(Expression e) {
        expressions.add(e);
    }
}
