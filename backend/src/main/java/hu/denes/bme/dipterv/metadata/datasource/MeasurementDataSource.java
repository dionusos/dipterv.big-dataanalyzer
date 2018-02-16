package hu.denes.bme.dipterv.metadata.datasource;

import hu.denes.bme.dipterv.metadata.Datasource;
import hu.denes.bme.dipterv.metadata.DimensionDef;
import hu.denes.bme.dipterv.metadata.Kpi;
import hu.denes.bme.dipterv.metadata.KpiDef;
import hu.denes.bme.dipterv.metadata.Metadata;
import hu.denes.bme.dipterv.metadata.OfferedMetric;
import hu.denes.bme.dipterv.metadata.Schemas;

public class MeasurementDataSource {
    private Datasource datasource;
    private Metadata metadata;
    private Schemas schemas;

    public MeasurementDataSource(){

    }

    public MeasurementDataSource(Datasource datasource, Metadata metadata, Schemas schemas) {
        this.datasource = datasource;
        this.metadata = metadata;
        this.schemas = schemas;
    }

    public Datasource getDatasource() {
        return datasource;
    }

    public void setDatasource(Datasource datasource) {
        this.datasource = datasource;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Schemas getSchemas() {
        return schemas;
    }

    public void setSchemas(Schemas schemas) {
        this.schemas = schemas;
    }

    public KpiDef getKpiFor(String name, String offeredMetric) {
        KpiDef kpi = null;
        for(KpiDef k : metadata.getKpi()) {
            if(!k.getName().equals(name)){
                continue;
            }
            for(OfferedMetric om : k.getOfferedMetric()){
                if(!om.getName().equals(offeredMetric)){
                    continue;
                }
                kpi = k;
            }
        }
        return kpi;
    }

    public DimensionDef getDimensionFor(String name) {
        for(DimensionDef dd : metadata.getDimension()) {
            if(dd.getName().equals(name)){
                return dd;
            }
        }
        return null;
    }
}
