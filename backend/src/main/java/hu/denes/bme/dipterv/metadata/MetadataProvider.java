package hu.denes.bme.dipterv.metadata;

import hu.denes.bme.dipterv.metadata.datasource.DatasourceProvider;
import hu.denes.bme.dipterv.metadata.datasource.MeasurementDataSource;
import io.swagger.model.Datasource;
import io.swagger.model.Datasources;
import io.swagger.model.Kpi;
import io.swagger.model.KpiList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetadataProvider {

    @Autowired
    private DatasourceProvider fileDatasourceProvider;

    public Datasources getDatasources() {
        Datasources result = new Datasources();
        for(MeasurementDataSource mds : fileDatasourceProvider.getDatasources()) {
            Datasource ds = new Datasource()
                    .name(mds.getDatasource().getName())
                    .displayName(mds.getDatasource().getFriendlyName()
                    );
            result.add(ds);
        }

        return result;
    }

    public KpiList getKpisFor(final String datasource) {
        KpiList kpiList = new KpiList();
        kpiList.add(new Kpi().name("temperature").displayName("Temperature"));
        return kpiList;
    }

    public MeasurementDataSource getMeasurementDataSourceByName(final String name) {
        return fileDatasourceProvider.getMeasurementDataSource(name);
    }
}
