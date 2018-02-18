package hu.denes.bme.dipterv.data.sql;

import org.junit.Before;
import org.junit.Test;

public class TestWhere {
    Where where;

    @Before
    public void init() {
        where = new Where();
    }

    @Test
    public void test() {
        System.out.println(
                where.expression(
                        new And(new Expression("cica"), new Or(new Expression("kutya"), new Not(new Expression("macska"))))));
        System.out.println(
                where.expression(
                        new Between("cica").left(new Expression(100)).right(new Expression(200))));
    }

}
