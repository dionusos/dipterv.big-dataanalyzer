package hu.denes.bme.dipterv.data;

import hu.denes.bme.dipterv.data.extractor.ResultSetExtractor;
import hu.denes.bme.dipterv.data.sql.And;
import hu.denes.bme.dipterv.data.sql.Eq;
import hu.denes.bme.dipterv.data.sql.Expression;
import hu.denes.bme.dipterv.data.sql.GreaterOrEquals;
import hu.denes.bme.dipterv.data.sql.GreaterThan;
import hu.denes.bme.dipterv.data.sql.LessOrEquals;
import hu.denes.bme.dipterv.data.sql.LessThan;
import hu.denes.bme.dipterv.data.sql.Not;
import hu.denes.bme.dipterv.data.sql.Or;
import hu.denes.bme.dipterv.data.sql.Query;
import hu.denes.bme.dipterv.metadata.DimensionDef;
import hu.denes.bme.dipterv.metadata.KpiDef;
import hu.denes.bme.dipterv.metadata.MetadataProvider;
import hu.denes.bme.dipterv.metadata.datasource.MeasurementDataSource;
import io.swagger.model.DataRequest;
import io.swagger.model.DataResponse;
import io.swagger.model.DataResponseHeader;
import io.swagger.model.DimensionRequest;
import io.swagger.model.FilterRequest;
import io.swagger.model.FilterRequestIntervals;
import io.swagger.model.FilterRequestKpi;
import io.swagger.model.KpiRequest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.denes.bme.dipterv.data.extractor.ResultSetExtractor.*;

@Service
public class DataProvider {
    @Autowired
    private MetadataProvider metadataProvider;

    Map<String, ResultSetExtractor> typeToExtractor = new HashMap<>();

    public DataResponse getData(final DataRequest request) {
        List<ResultSetExtractor> extractors = new ArrayList<>();
        extractors.add(new StringResultSetExtractor());

        MeasurementDataSource ds = metadataProvider.getMeasurementDataSourceByName(request.getDatasource());
        Query q = createQuery(ds);
        List<KpiDef> requestdKpis = new ArrayList<>();
        List<DimensionDef> requestdDimensions = new ArrayList<>();

        DataResponse response = new DataResponse();
        List<DataResponseHeader> header = new ArrayList<>();

        if(request.getDimensions() != null) {
            for (DimensionRequest dr : request.getDimensions()) {
                DimensionDef dd = ds.getDimensionFor(dr.getName());
                q.addDimension(dd);
                header.add(new DataResponseHeader().dimension(dr.getName()));
                requestdDimensions.add(dd);
                extractors.add(typeToExtractor.get(dd.getDatatype()));
            }
        }

        if(request.getKpis() != null) {
            for (KpiRequest kpi : request.getKpis()) {
                KpiDef kDef = ds.getKpiFor(kpi.getName(), kpi.getOfferedMetric());
                q.addKpi(kDef, kpi.getOfferedMetric());
                header.add(new DataResponseHeader().kpi(kpi.getName()).offeredMetric(kpi.getOfferedMetric()));
                requestdKpis.add(kDef);
                extractors.add(typeToExtractor.get("DOUBLE"));
            }
        }

        if(request.getFilters() != null && !request.getFilters().isEmpty()) {
            And whereExpression = new And();
            And havingExpression = new And();
            for (FilterRequest filterRequest : request.getFilters()) {


                if (filterRequest.getDimension() != null) {
                    if (filterRequest.getIsNegative()) {
                        if (filterRequest.getValues() != null && !filterRequest.getValues().isEmpty()) {
                            Or ors = new Or();
                            for(String v : filterRequest.getValues()) {
                                ors.add(new Not(new Eq().left(new Expression(filterRequest.getDimension())).right(new Expression(v))));
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
                                ors.add(new Eq().left(new Expression(filterRequest.getDimension())).right(new Expression(v)));
                            }
                            whereExpression.add(ors);
                        }
                        if(filterRequest.getIntervals() != null && !filterRequest.getIntervals().isEmpty()) {
                            Or ors = new Or();
                            for(FilterRequestIntervals interval : filterRequest.getIntervals()) {
                                And a = new And();
                                if(interval.getLower() != null) {
                                    a.add(new LessOrEquals().left(new Expression(interval.getLower())).right(new Expression(filterRequest.getDimension())));
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
                                    a.add(new LessOrEquals().left(new Expression(interval.getLower())).right(new Expression(q.getCalculationFor(ds.getKpiFor(k.getName(), k.getOfferedMetric()), k.getOfferedMetric()))));
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

        response.setHeader(header);

        q.setSchema(ds.getShemasContaining(requestdKpis, requestdDimensions).get(0).getName());
        q.setTable(ds.getSchemas().getSchema().get(0).getTable().get(0).getName());

        Connection conn = null;
        try {
            Properties props = new Properties();
            props.setProperty("user", q.getUser());
            props.setProperty("password", q.getPassword());
            props.setProperty("useSSL", "false");
            props.setProperty("serverTimezone", "UTC");
            conn = DriverManager.getConnection(q.getUrl(),  props);
            Statement statement = conn.createStatement();
            String queryString = q.toString();
            System.out.println("Running query: [" + queryString + "]");
            ResultSet rs = statement.executeQuery(queryString);
            List<List<Object>> mx = new ArrayList<>();
            while (rs.next()) {
                List<Object> row = new ArrayList<>();
                for(int i = 1; i <= response.getHeader().size(); ++i) {
                    row.add(extractors.get(i).extract(rs, i));
                }
                mx.add(row);
            }
            response.setDataMatrix(mx);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    private Query createQuery(MeasurementDataSource mds) {
        Query q = new Query();
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
