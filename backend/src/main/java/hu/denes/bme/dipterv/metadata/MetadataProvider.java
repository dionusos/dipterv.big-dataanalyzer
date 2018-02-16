package hu.denes.bme.dipterv.metadata;

import hu.denes.bme.dipterv.metadata.datasource.DatasourceProvider;
import hu.denes.bme.dipterv.metadata.datasource.MeasurementDataSource;
import io.swagger.model.Datasource;
import io.swagger.model.Datasources;
import io.swagger.model.Dimension;
import io.swagger.model.DimensionList;
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
        MeasurementDataSource ds = fileDatasourceProvider.getMeasurementDataSource(datasource);
        KpiList kpiList = new KpiList();
        for(KpiDef kpiDef : ds.getMetadata().getKpi()) {
            for(OfferedMetric om : kpiDef.getOfferedMetric()) {
                kpiList.add(new Kpi().name(kpiDef.name).displayName(kpiDef.friendlyName + " " + om.friendlyName).offeredMetric(om.name));
            }
        }
        return kpiList;
    }

    public DimensionList getDimensionsFor(final String datasource) {
        MeasurementDataSource ds = fileDatasourceProvider.getMeasurementDataSource(datasource);
        DimensionList dimensionList = new DimensionList();
        for(DimensionDef dimensionDef : ds.getMetadata().getDimension()) {
            dimensionList.add(new Dimension().name(dimensionDef.name).displayName(dimensionDef.friendlyName));
        }
        return dimensionList;
    }

    public MeasurementDataSource getMeasurementDataSourceByName(final String name) {
        return fileDatasourceProvider.getMeasurementDataSource(name);
    }
}
