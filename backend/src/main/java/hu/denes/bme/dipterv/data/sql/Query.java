package hu.denes.bme.dipterv.data.sql;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import hu.denes.bme.dipterv.metadata.DimensionDef;
import hu.denes.bme.dipterv.metadata.KpiDef;
import hu.denes.bme.dipterv.metadata.MetadataProvider;
import hu.denes.bme.dipterv.metadata.OfferedMetric;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Query {
    private static final AtomicInteger ID = new AtomicInteger(0);
    private String queryName;
    private String url;
    private String user;
    private String password;
    protected List<Select> selects = new ArrayList<>();
    private List<OrderBy> orderBys = new ArrayList<>();
    private List<String> groupBys = new ArrayList<>();
    private String schema;
    private String table;
    protected Integer limit;
    private List<String> dimensions = new ArrayList<>();
    private boolean useTable = true;
    private Query joined = null;
    private List<String> joinedDimensions = null;

    private MetadataProvider metadataProvider;
    protected Expression where;
    private Expression having;

    public Query() {
        ID.set(ID.incrementAndGet() % 1000);
        queryName = "q" + ID.get();
    }

    public void addKpi(KpiDef kDef, String offeredMetric){
        String calculation = getCalculationFor(kDef, offeredMetric);
        Select s = new Select(calculation.replace("${kpi}", "${table}" + kDef.getName()), kDef.getName() + "_" + offeredMetric);
        selects.add(s);
    }

    public String getCalculationFor(KpiDef kDef, String offeredMetric) {
        String calculation = "";
        for(OfferedMetric om : kDef.getOfferedMetric()){
            if(om.getName().equals(offeredMetric)){
                calculation = om.getCalculation().replace("${kpi}", "${table}" + kDef.getName());
                break;
            }
        }
        return calculation;
    }

    public void addDimension(DimensionDef dim) {
        selects.add(new Select("${table}" + dim.getName(), dim.getName()));
        groupBys.add("${table}" + dim.getName());
        dimensions.add(dim.getName());
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
        {
            sb.append("SELECT ");
            Iterator<Select> it = selects.iterator();
            while (it.hasNext()) {
                Select s = it.next();
                sb.append(s);
                if (it.hasNext()) {
                    sb.append(", ");
                }
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
        sb.append(" " + queryName + " ");

        if(where != null && !where.toString().isEmpty()) {
            sb.append(" WHERE ");
            sb.append(where.toString());
        }

        if(joined != null && joinedDimensions != null) {
            sb.append(" JOIN ").append("(").append(joined.toString()).append(") ").append(joined.queryName);

            if(joinedDimensions.size() > 0) {
                sb.append(" ON ");
                Iterator<String> dIt = joinedDimensions.iterator();
                while (dIt.hasNext()) {
                    String d = dIt.next();
                    sb.append("(").append(queryName).append(".").append(d).append(" = ").append(joined.queryName + ".").append(d);
                    sb.append(" OR (");
                    sb.append(queryName).append(".").append(d).append(" IS NULL AND ").append(joined.queryName + ".").append(d).append(" IS NULL)");
                    sb.append(")");
                    if (dIt.hasNext()) {
                        sb.append(", ");
                    }
                }
            }
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

        if(!orderBys.isEmpty()) {
            sb.append(" ORDER BY ");
            Iterator<OrderBy> it = orderBys.iterator();
            while (it.hasNext()){
                OrderBy s = it.next();
                sb.append(s.getCalculation()).append(" ").append(s.getDirection());
                if(it.hasNext()){
                    sb.append(", ");
                }
            }
        }

        return useTable ? sb.toString().replace("${table}", queryName + ".") : sb.toString().replace("${table}", "");
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

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public void addOrder(String dimension, String direction) {
        orderBys.add(new OrderBy(dimension, direction));
    }

    public void addOrder(KpiDef kDef, String offeredMetric, String direction) {
        String calculation = getCalculationFor(kDef, offeredMetric);
        orderBys.add(new OrderBy(calculation, direction));
    }

    public List<String> getDimensions() {
        return dimensions;
    }

    public void join(Query inner, List<String> dimensions) {
        this.joined = inner;
        joinedDimensions = dimensions;
    }

    private String enrichQName(String value){
        return queryName + "." + value;
    }

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }
}
