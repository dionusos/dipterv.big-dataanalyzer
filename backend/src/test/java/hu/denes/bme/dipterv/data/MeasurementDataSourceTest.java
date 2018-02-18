package hu.denes.bme.dipterv.data;

import hu.denes.bme.dipterv.metadata.datasource.DatasourceProvider;
import hu.denes.bme.dipterv.metadata.datasource.MeasurementDataSource;
import org.junit.Before;
import org.junit.Test;

public class MeasurementDataSourceTest {

    MeasurementDataSource ds;
    DatasourceProvider provider;

    @Before
    public void init(){
        System.setProperty("datasources.path",  "../example/src/main/resources");
        provider = new DatasourceProvider();
        provider.init();
        ds = provider.getMeasurementDataSource("example1");
    }

    @Test
    public void test() {
        ds.getShemasContaining(null, null);
    }
}
