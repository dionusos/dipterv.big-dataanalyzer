package hu.denes.bme.dipterv.data.sql;

public class OrderBy {
    private String calculation;
    private String direction;

    public OrderBy(String calculation, String direction) {
        this.calculation = calculation;
        this.direction = direction;
    }

    public String getCalculation() {
        return calculation;
    }

    public String getDirection() {
        return direction;
    }
}
