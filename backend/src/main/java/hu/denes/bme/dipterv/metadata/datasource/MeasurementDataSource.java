package hu.denes.bme.dipterv.metadata.datasource;

import hu.denes.bme.dipterv.metadata.Datasource;
import hu.denes.bme.dipterv.metadata.Dimension;
import hu.denes.bme.dipterv.metadata.DimensionDef;
import hu.denes.bme.dipterv.metadata.Kpi;
import hu.denes.bme.dipterv.metadata.KpiDef;
import hu.denes.bme.dipterv.metadata.Metadata;
import hu.denes.bme.dipterv.metadata.OfferedMetric;
import hu.denes.bme.dipterv.metadata.Schema;
import hu.denes.bme.dipterv.metadata.Schemas;
import io.swagger.model.DimensionRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MeasurementDataSource {
    private Datasource datasource;
    private Metadata metadata;
    private Schemas schemas;

    Map<Schema, Set<String>> schemaToStoredKpis = new HashMap<>();
    Map<Schema, Set<String>> schemaToStoredDimensions = new HashMap<>();

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

    public List<Schema> getShemasContaining(Collection<KpiDef> kpis, Collection<DimensionDef> dimensions) {
        Set<String> requestedKpiNames = new HashSet<>();
        if(kpis != null) {
            for (KpiDef kd : kpis) {
                requestedKpiNames.add(kd.getName());
            }
        }
        Set<String> requestedDimensionNames = new HashSet<>();
        if(dimensions != null) {
            for (DimensionDef dd : dimensions) {
                requestedDimensionNames.add(dd.getName());
            }
        }
        List<Schema> matchingSchemas = new ArrayList<>();
        for(Schema s : schemas.getSchema()) {
            if(!schemaToStoredKpis.get(s).containsAll(requestedKpiNames)){
                continue;
            }
            if(!schemaToStoredDimensions.get(s).containsAll(requestedDimensionNames)){
                continue;
            }
            matchingSchemas.add(s);
        }
        return matchingSchemas;
    }

    public void optimize() {
        for(Schema s : getSchemas().getSchema()) {
            Set<String> storeKpiSet = new HashSet<>();
            Set<String> storeDimensionSet = new HashSet<>();
            for(Kpi k : s.getKpi()){
                storeKpiSet.add(k.getName());
            }
            schemaToStoredKpis.put(s, storeKpiSet);
            for(Dimension d : s.getDimension()) {
                storeDimensionSet.add(d.getName());
            }
            schemaToStoredDimensions.put(s, storeDimensionSet);
        }
    }
}
