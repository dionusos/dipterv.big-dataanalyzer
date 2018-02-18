package hu.denes.bme.dipterv.data;

import hu.denes.bme.dipterv.data.sql.Query;
import hu.denes.bme.dipterv.metadata.DimensionDef;
import hu.denes.bme.dipterv.metadata.KpiDef;
import hu.denes.bme.dipterv.metadata.MetadataProvider;
import hu.denes.bme.dipterv.metadata.datasource.MeasurementDataSource;
import io.swagger.model.DataRequest;
import io.swagger.model.DataResponse;
import io.swagger.model.DataResponseHeader;
import io.swagger.model.Datasource;
import io.swagger.model.DimensionRequest;
import io.swagger.model.Kpi;
import io.swagger.model.KpiRequest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataProvider {
    @Autowired
    private MetadataProvider metadataProvider;

    public DataResponse getData(final DataRequest request) {
        MeasurementDataSource ds = metadataProvider.getMeasurementDataSourceByName(request.getDatasource());
        Query q = createQuery(ds);
        List<KpiDef> requestdKpis = new ArrayList<>();
        List<DimensionDef> requestdDimensions = new ArrayList<>();

        DataResponse response = new DataResponse();
        List<DataResponseHeader> header = new ArrayList<>();

        for(KpiRequest kpi : request.getKpis()) {
            KpiDef kDef = ds.getKpiFor(kpi.getName(), kpi.getOfferedMetric());
            q.addKpi(kDef, kpi.getOfferedMetric());
            header.add(new DataResponseHeader().kpi(kpi.getName()).offeredMetric(kpi.getOfferedMetric()));
            requestdKpis.add(kDef);
        }

        for(DimensionRequest dr : request.getDimensions()) {
            DimensionDef dd = ds.getDimensionFor(dr.getName());
            q.addDimension(dd);
            header.add(new DataResponseHeader().dimension(dr.getName()));
            requestdDimensions.add(dd);
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
            List<List<String>> mx = new ArrayList<>();
            while (rs.next()) {
                List<String> row = new ArrayList<>();
                for(int i = 1; i <= response.getHeader().size(); ++i) {
                    row.add("" + rs.getFloat(i));
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
}
