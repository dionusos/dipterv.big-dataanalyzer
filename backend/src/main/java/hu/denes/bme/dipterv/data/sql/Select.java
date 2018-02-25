package hu.denes.bme.dipterv.data.sql;

public class Select {
    private String calculation;
    private String alias;

    public Select(String calculation, String alias) {
        this.calculation = calculation;
        this.alias = alias;
    }

    public String getCalculation() {
        return calculation;
    }

    public String getAlias() {
        return alias;
    }

    @Override
    public String toString() {
        if(alias == null) {
            return calculation;
        } else {
            return calculation + " as " + alias;
        }
    }
}
