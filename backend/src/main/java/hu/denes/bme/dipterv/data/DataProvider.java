package hu.denes.bme.dipterv.data;

import hu.denes.bme.dipterv.data.extractor.ResultSetExtractor;
import hu.denes.bme.dipterv.data.sql.And;
import hu.denes.bme.dipterv.data.sql.Eq;
import hu.denes.bme.dipterv.data.sql.Expression;
import hu.denes.bme.dipterv.data.sql.GreaterOrEquals;
import hu.denes.bme.dipterv.data.sql.GreaterThan;
import hu.denes.bme.dipterv.data.sql.If;
import hu.denes.bme.dipterv.data.sql.LessOrEquals;
import hu.denes.bme.dipterv.data.sql.LessThan;
import hu.denes.bme.dipterv.data.sql.MySqlQuery;
import hu.denes.bme.dipterv.data.sql.Not;
import hu.denes.bme.dipterv.data.sql.Or;
import hu.denes.bme.dipterv.data.sql.Query;
import hu.denes.bme.dipterv.metadata.DimensionDef;
import hu.denes.bme.dipterv.metadata.KpiDef;
import hu.denes.bme.dipterv.metadata.MetadataProvider;
import hu.denes.bme.dipterv.metadata.Schema;
import hu.denes.bme.dipterv.metadata.datasource.MeasurementDataSource;
import io.swagger.model.DataRequest;
import io.swagger.model.DataResponse;
import io.swagger.model.DataResponseHeader;
import io.swagger.model.DimensionRequest;
import io.swagger.model.FilterRequest;
import io.swagger.model.FilterRequestIntervals;
import io.swagger.model.FilterRequestKpi;
import io.swagger.model.KpiRequest;
import io.swagger.model.OrderRequest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.denes.bme.dipterv.data.extractor.ResultSetExtractor.*;

@Service
public class DataProvider {
    @Autowired
    private MetadataProvider metadataProvider;

    Map<String, ResultSetExtractor> typeToExtractor = new HashMap<>();

    private boolean isTimeSeriesQuery = false;
    private static Set<String> NEGATIVE_START_TIME = new HashSet<>(Arrays.asList("start_time"));
    private static Set<String> EMPTY_NEGATIVE = Collections.emptySet();

    List<DimensionDef> extractDimensionsIntoQuery(DataRequest request, MeasurementDataSource ds, Query q, Set<String> negative) {
        if(negative == null) {
            negative = Collections.emptySet();
        }
        List<DimensionDef> requestdDimensions = new ArrayList<>();
        if(request.getDimensions() != null) {
            for (DimensionRequest dr : request.getDimensions()) {
                if(negative.contains(dr.getName())) {
                    continue;
                }
                DimensionDef dd = ds.getDimensionFor(dr.getName());
                q.addDimension(dd);
                requestdDimensions.add(dd);
            }
        }
        return requestdDimensions;
    }

    private List<ResultSetExtractor> extractExtractors(DataRequest request, MeasurementDataSource ds) {
        List<ResultSetExtractor> extractors = new ArrayList<>();
        if(request.getDimensions() != null) {
            for (DimensionRequest dr : request.getDimensions()) {
                DimensionDef dd = ds.getDimensionFor(dr.getName());
                extractors.add(typeToExtractor.get(dd.getDatatype()));
            }
        }
        return extractors;
    }

    private List<DataResponseHeader> extractHeaderInformation(DataRequest request) {
        List<DataResponseHeader> header = new ArrayList<>();
        if(request.getDimensions() != null) {
            for (DimensionRequest dr : request.getDimensions()) {
                header.add(new DataResponseHeader().dimension(dr.getName()));
            }
        }
        return header;
    }

    public DataResponse getData(final DataRequest request) throws Exception {
        deterMineIfTimeSeries(request);
        List<ResultSetExtractor> extractors = new ArrayList<>();
        extractors.add(new StringResultSetExtractor());

        MeasurementDataSource ds = metadataProvider.getMeasurementDataSourceByName(request.getDatasource());
        Query q = createQuery(ds);
        Query inner = createQuery(ds);
        List<KpiDef> requestdKpis = new ArrayList<>();
        List<DimensionDef> requestdDimensions = new ArrayList<>();

        DataResponse response = new DataResponse();
        List<DataResponseHeader> header = extractHeaderInformation(request);
        extractors.addAll(extractExtractors(request, ds));
        if(isTimeSeriesQuery) {
            extractDimensionsIntoQuery(request, ds, inner, NEGATIVE_START_TIME);
        }
        extractDimensionsIntoQuery(request, ds, q, EMPTY_NEGATIVE);

        if(request.getKpis() != null) {
            for (KpiRequest kpi : request.getKpis()) {
                KpiDef kDef = ds.getKpiFor(kpi.getName(), kpi.getOfferedMetric());
                q.addKpi(kDef, kpi.getOfferedMetric());
                if(isTimeSeriesQuery) {
                    inner.addKpi(kDef, kpi.getOfferedMetric());
                }
                header.add(new DataResponseHeader().kpi(kpi.getName()).offeredMetric(kpi.getOfferedMetric()));
                requestdKpis.add(kDef);
                extractors.add(typeToExtractor.get("DOUBLE"));
            }
        }

        extractFilters(request, ds, q, null);

        if(request.getLimit() != null && request.getLimit() > 0) {
            q.setLimit(request.getLimit());
        }

        if(request.getOrders() != null) {
            for(OrderRequest o : request.getOrders()) {
                String direction;
                if(o.getDirection() != null && o.getDirection().equals("DESC")) {
                    direction = "DESC";
                } else {
                    direction = "ASC";
                }
                if(o.getDimension() != null) {
                    q.addOrder(o.getDimension(), direction);
                } else {
                    KpiDef kDef = ds.getKpiFor(o.getKpi().getName(), o.getKpi().getOfferedMetric());
                    q.addOrder(kDef, o.getKpi().getOfferedMetric(), direction);
                }

            }
        }

        response.setHeader(header);

        Query helperQuery = createQuery(ds);
        helperQuery.addDimension("`schema`");
        helperQuery.addDimension("`table`");
        helperQuery.setSchema("aggregator");
        helperQuery.setTable("statistics");

        If a = new If();
        Iterator<String> it = ds.getTablesWithResolution().keySet().iterator();
        If last = a;
        while (it.hasNext()) {
            String name = it.next();
            Integer res = ds.getTablesWithResolution().get(name);
            last.condition(new Eq().left(new Expression("`table`")).right(new Expression(name, true)));
            last._true(new Expression(res));
            if(it.hasNext()) {
                last._false(new If());
                last = (If)last.get_false();
            } else {
                last._false(new Expression(0));
            }
        }
        helperQuery.addKpi("count(*) * " + a.toString(), "coverage");
        helperQuery.addOrder("count(*) * " + a.toString(), "DESC");
        helperQuery.addOrder("count(*)", "ASC");
        helperQuery.setLimit(1);
        extractFilters(request, ds, helperQuery, Arrays.asList("start_time"));
        Or or = new Or();
        for(Schema s : ds.getShemasContaining(requestdKpis, requestdDimensions)) {
            or.add(new Eq().left(new Expression("`schema`")).right(new Expression(s.getName(), true)));
        }
        if(null != helperQuery.getWhere() && !"".equals(helperQuery.getWhere().toString())) {
            helperQuery.setWhere(new And(helperQuery.getWhere(), or));
        } else {
            helperQuery.setWhere(or);
        }

        if(request.getLimit() != null && request.getLimit() > 0) {
            if(isTimeSeriesQuery) {
                inner.setLimit(request.getLimit());
                q.setLimit(null);
            } else {
                q.setLimit(request.getLimit());
            }
        }

        Query queryToSetOrder = q;
        if(isTimeSeriesQuery) {
            queryToSetOrder = inner;
        }
        if(isTimeSeriesQuery) {
            if (request.getOrders() != null) {
                for (OrderRequest o : request.getOrders()) {
                    String direction;
                    if (o.getDirection() != null && o.getDirection().equals("DESC")) {
                        direction = "DESC";
                    } else {
                        direction = "ASC";
                    }
                    if (o.getDimension() != null) {
                        if("start_time".equals(o.getDimension())) {
                            continue;
                        }
                        queryToSetOrder.addOrder(o.getDimension(), direction);
                    } else {
                        KpiDef kDef = ds.getKpiFor(o.getKpi().getName(), o.getKpi().getOfferedMetric());
                        queryToSetOrder.addOrder(kDef, o.getKpi().getOfferedMetric(), direction);
                    }

                }
            }
        } else {
            if (request.getOrders() != null) {
                for (OrderRequest o : request.getOrders()) {
                    String direction;
                    if (o.getDirection() != null && o.getDirection().equals("DESC")) {
                        direction = "DESC";
                    } else {
                        direction = "ASC";
                    }
                    if (o.getDimension() != null) {
                        queryToSetOrder.addOrder(o.getDimension(), direction);
                    } else {
                        KpiDef kDef = ds.getKpiFor(o.getKpi().getName(), o.getKpi().getOfferedMetric());
                        queryToSetOrder.addOrder(kDef, o.getKpi().getOfferedMetric(), direction);
                    }

                }
            }
        }
        if(isTimeSeriesQuery){
            q.join(inner, inner.getDimensions());
        }

        response.setHeader(header);

        Connection conn = null;
        try {
            ResultSet rs;
            rs = runQuery(conn, helperQuery);
            if (rs.next()){
                q.setSchema(rs.getString("schema"));
                q.setTable(rs.getString("table"));
                if(isTimeSeriesQuery) {
                    inner.setSchema(q.getSchema());
                    inner.setTable(q.getTable());
                }
            } else {
                throw new Exception("No Schema found");
            }
            rs.close();
            rs = runQuery(conn, q);
            List<List<Object>> mx = new ArrayList<>();
            while (rs.next()) {
                List<Object> row = new ArrayList<>();
                for(int i = 1; i <= response.getHeader().size(); ++i) {
                    row.add(extractors.get(i).extract(rs, i));
                }
                mx.add(row);
            }
            response.setDataMatrix(mx);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    private void extractFilters(DataRequest request, MeasurementDataSource ds, Query q, Collection<String> filterNameFilter) {
        if(request.getFilters() != null && !request.getFilters().isEmpty()) {
            And whereExpression = new And();
            And havingExpression = new And();
            for (FilterRequest filterRequest : request.getFilters()) {
                if(filterNameFilter != null && filterRequest.getDimension() != null && !filterNameFilter.contains(filterRequest.getDimension())) {
                    continue;
                }
                if (filterRequest.getDimension() != null) {
                    boolean shouldQuote = ds.getDimensionFor(filterRequest.getDimension()).getDatatype().equals("STRING");
                    if (filterRequest.getIsNegative()) {
                        if (filterRequest.getValues() != null && !filterRequest.getValues().isEmpty()) {
                            Or ors = new Or();
                            for(String v : filterRequest.getValues()) {
                                ors.add(new Not(new Eq().left(new Expression(filterRequest.getDimension())).right(new Expression(v, shouldQuote))));
                            }
                            whereExpression.add(ors);
                        }
                        if(filterRequest.getIntervals() != null && !filterRequest.getIntervals().isEmpty()) {
                            Or ors = new Or();
                            for(FilterRequestIntervals interval : filterRequest.getIntervals()) {
                                And a = new And();
                                if(interval.getLower() != null) {
                                    a.add(new GreaterThan().left(new Expression(interval.getLower())).right(new Expression(filterRequest.getDimension())));
                                }
                                if(interval.getUpper() != null) {
                                    a.add(new GreaterOrEquals().left(new Expression(filterRequest.getDimension())).right(new Expression(interval.getUpper())));
                                }
                                ors.add(a);
                            }
                            whereExpression.add(ors);
                        }
                    } else {
                        if (filterRequest.getValues() != null) {
                            Or ors = new Or();
                            for (String v : filterRequest.getValues()) {
                                ors.add(new Eq().left(new Expression(filterRequest.getDimension())).right(new Expression(v, shouldQuote)));
                            }
                            whereExpression.add(ors);
                        }
                        if(filterRequest.getIntervals() != null && !filterRequest.getIntervals().isEmpty()) {
                            Or ors = new Or();
                            for(FilterRequestIntervals interval : filterRequest.getIntervals()) {
                                And a = new And();
                                if(interval.getLower() != null) {
                                    a.add(new LessOrEquals(new Expression(interval.getLower()), new Expression(filterRequest.getDimension())));
                                }
                                if(interval.getUpper() != null) {
                                    a.add(new LessThan().left(new Expression(filterRequest.getDimension())).right(new Expression(interval.getUpper())));
                                }
                                ors.add(a);
                            }
                            whereExpression.add(ors);
                        }
                    }
                } else {
                    if (filterRequest.getIsNegative()) {
                        if (filterRequest.getValues() != null && !filterRequest.getValues().isEmpty()) {
                            Or ors = new Or();
                            for(String v : filterRequest.getValues()) {
                                FilterRequestKpi k = filterRequest.getKpi();
                                ors.add(new Not(new Eq().left(new Expression(q.getCalculationFor(ds.getKpiFor(k.getName(), k.getOfferedMetric()), k.getOfferedMetric()))).right(new Expression(v))));
                            }
                            havingExpression.add(ors);
                        }
                        if(filterRequest.getIntervals() != null && !filterRequest.getIntervals().isEmpty()) {
                            Or ors = new Or();
                            FilterRequestKpi k = filterRequest.getKpi();
                            for(FilterRequestIntervals interval : filterRequest.getIntervals()) {
                                And a = new And();
                                if(interval.getLower() != null) {
                                    a.add(new GreaterThan().left(new Expression(interval.getLower())).right(new Expression(q.getCalculationFor(ds.getKpiFor(k.getName(), k.getOfferedMetric()), k.getOfferedMetric()))));
                                }
                                if(interval.getUpper() != null) {
                                    a.add(new GreaterOrEquals().left(new Expression(q.getCalculationFor(ds.getKpiFor(k.getName(), k.getOfferedMetric()), k.getOfferedMetric()))).right(new Expression(interval.getUpper())));
                                }
                                ors.add(a);
                            }
                            havingExpression.add(ors);
                        }
                    } else {
                        if (filterRequest.getValues() != null) {
                            Or ors = new Or();
                            for (String v : filterRequest.getValues()) {
                                FilterRequestKpi k = filterRequest.getKpi();
                                ors.add(new Eq().left(new Expression(q.getCalculationFor(ds.getKpiFor(k.getName(), k.getOfferedMetric()), k.getOfferedMetric()))).right(new Expression(v)));
                            }
                            havingExpression.add(ors);
                        }
                        if(filterRequest.getIntervals() != null && !filterRequest.getIntervals().isEmpty()) {
                            Or ors = new Or();
                            FilterRequestKpi k = filterRequest.getKpi();
                            for(FilterRequestIntervals interval : filterRequest.getIntervals()) {
                                And a = new And();
                                if(interval.getLower() != null) {
                                    a.add(new LessOrEquals(new Expression(interval.getLower()), new Expression(q.getCalculationFor(ds.getKpiFor(k.getName(), k.getOfferedMetric()), k.getOfferedMetric()))));
                                }
                                if(interval.getUpper() != null) {
                                    a.add(new LessThan().left(new Expression(q.getCalculationFor(ds.getKpiFor(k.getName(), k.getOfferedMetric()), k.getOfferedMetric()))).right(new Expression(interval.getUpper())));
                                }
                                ors.add(a);
                            }
                            havingExpression.add(ors);
                        }
                    }
                }


            }
            q.setWhere(whereExpression);
            q.setHaving(havingExpression);
        }
    }

    private Connection createConnection(Query q) throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", q.getUser());
        props.setProperty("password", q.getPassword());
        props.setProperty("useSSL", "false");
        props.setProperty("serverTimezone", "UTC");
        return DriverManager.getConnection(q.getUrl(),  props);
    }

    private ResultSet runQuery(Connection conn, Query q) throws SQLException {
        conn = createConnection(q);
        Statement statement = conn.createStatement();
        String queryString = q.toString();
        System.out.println("Running query: [" + queryString + "]");
        return statement.executeQuery(queryString);
    }

    private void deterMineIfTimeSeries(DataRequest request) {
        if(request.getDimensions() != null){
            for(DimensionRequest dr : request.getDimensions()) {
                if("start_time".equals(dr.getName())) {
                    this.isTimeSeriesQuery = true;
                    break;
                }
            }
            if(isTimeSeriesQuery && request.getDimensions().size() < 2) {
                isTimeSeriesQuery = false;
                request.limit(null);
                return;
            }

        }
    }

    private Query createQuery(MeasurementDataSource mds) {
        Query q = new MySqlQuery();
        hu.denes.bme.dipterv.metadata.Datasource ds = mds.getDatasource();
        q.setUrl(ds.getUrl());
        q.setUser(ds.getUser());
        q.setPassword(ds.getPassword());
        q.setMetadataProvider(metadataProvider);
        return q;
    }

    @PostConstruct
    public void init() {
        typeToExtractor.put("BOOLEAN", new BooleanResultSetExtractor());
        typeToExtractor.put("FLOAT", new FloatResultSetExtractor());
        typeToExtractor.put("DOUBLE", new DoubleResultSetExtractor());
        typeToExtractor.put("STRING", new StringResultSetExtractor());
        typeToExtractor.put("INT", new IntegerResultSetExtractor());
        typeToExtractor.put("INT8", new IntegerResultSetExtractor());
        typeToExtractor.put("INT16", new IntegerResultSetExtractor());
        typeToExtractor.put("INT32", new IntegerResultSetExtractor());
        typeToExtractor.put("LONG", new LongResultSetExtractor());
        typeToExtractor.put("INT64", new LongResultSetExtractor());
    }
}
