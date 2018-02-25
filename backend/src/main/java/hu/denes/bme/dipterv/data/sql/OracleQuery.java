package hu.denes.bme.dipterv.data.sql;

public class OracleQuery extends Query {
    private boolean limitSet = false;
    @Override
    public String toString() {
        if(!limitSet && limit != null && limit > 0) {
            limitSet = true;
            And where = (And)this.where;
            if(where == null) {
                where = new And();

            }
            where.add(new LessOrEquals(new Expression("ROWNUM"), new Expression(limit)));
        }
        return super.toString();
    }
}
