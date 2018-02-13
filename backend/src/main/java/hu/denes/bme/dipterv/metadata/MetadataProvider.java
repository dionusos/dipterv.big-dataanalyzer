package hu.denes.bme.dipterv.metadata;

import io.swagger.model.Kpi;
import io.swagger.model.KpiList;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class MetadataProvider {
    private Map<String, Datasource> datasources;

    public KpiList getKpisFor(final String datasource) {
        KpiList kpiList = new KpiList();
        Datasource ds = datasources.get(datasource);
        for(KpiMeta kpi : ds.getKpis()) {
            kpiList.add(new Kpi().name(kpi.getName()).displayName(kpi.getFriendlyName()));
        }
        return kpiList;
    }

}
