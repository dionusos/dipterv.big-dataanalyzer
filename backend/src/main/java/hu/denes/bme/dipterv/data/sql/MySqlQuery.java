package hu.denes.bme.dipterv.data.sql;

public class MySqlQuery extends Query {
    @Override
    public String toString() {
        if (this.limit == null) {
            return super.toString();
        }
        return super.toString() + " LIMIT " + this.limit;
    }
}
