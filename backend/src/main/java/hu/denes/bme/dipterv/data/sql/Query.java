package hu.denes.bme.dipterv.data.sql;

import hu.denes.bme.dipterv.metadata.DimensionDef;
import hu.denes.bme.dipterv.metadata.KpiDef;
import hu.denes.bme.dipterv.metadata.MetadataProvider;
import hu.denes.bme.dipterv.metadata.OfferedMetric;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Query {
    private String url;
    private String user;
    private String password;
    private List<Select> selects = new ArrayList<>();
    private List<String> groupBys = new ArrayList<>();
    private String schema;
    private String table;

    private MetadataProvider metadataProvider;
    private Expression where;
    private Expression having;

    public void addKpi(KpiDef kDef, String offeredMetric){
        String calculation = getCalculationFor(kDef, offeredMetric);
        Select s = new Select(calculation, kDef.getName() + "_" + offeredMetric);
        selects.add(s);
    }

    public String getCalculationFor(KpiDef kDef, String offeredMetric) {
        String calculation = "";
        for(OfferedMetric om : kDef.getOfferedMetric()){
            if(om.getName().equals(offeredMetric)){
                calculation = om.getCalculation();
                break;
            }
        }
        return calculation;
    }

    public void addDimension(DimensionDef dim) {
        selects.add(new Select(dim.getName(), dim.getName()));
        groupBys.add(dim.getName());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MetadataProvider getMetadataProvider() {
        return metadataProvider;
    }

    public void setMetadataProvider(MetadataProvider metadataProvider) {
        this.metadataProvider = metadataProvider;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        Iterator<Select> it = selects.iterator();
        while (it.hasNext()){
            Select s = it.next();
            sb.append(s.getCalculation()).append(" as ").append(s.getAlias());
            if(it.hasNext()){
                sb.append(", ");
            }
        }

        sb.append(" FROM ");
        if(schema != null && table != null) {
            sb.append(schema + "." + table);
        } else if (schema != null) {
            sb.append(schema);
        } else {
            sb.append(table);
        }

        if(where != null && !where.toString().isEmpty()) {
            sb.append(" WHERE ");
            sb.append(where.toString());
        }

        if(groupBys.size() > 0) {
            sb.append(" GROUP BY ");
            Iterator<String> itgb = groupBys.iterator();
            while (itgb.hasNext()) {
                String s = itgb.next();
                sb.append(s);
                if (itgb.hasNext()) {
                    sb.append(", ");
                }
            }
        }

        if(having != null && !having.toString().isEmpty()){
            sb.append(" HAVING ");
            sb.append(having.toString());
        }

        return sb.toString();
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public void setWhere(Expression where) {
        this.where = where;
    }

    public void setHaving(And having) {
        this.having = having;
    }
}
