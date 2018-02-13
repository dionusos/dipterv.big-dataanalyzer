package hu.denes.bme.dipterv.metadata;

import io.swagger.model.Kpi;
import io.swagger.model.KpiList;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class MetadataProvider {
    private Map<String, FileDatasource> datasources;

    public KpiList getKpisFor(final String datasource) {
        KpiList kpiList = new KpiList();
        kpiList.add(new Kpi().name("temperature").displayName("Temperature"));
        return kpiList;
    }

}
