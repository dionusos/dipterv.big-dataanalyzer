package hu.denes.bme.dipterv.data;

import hu.denes.bme.dipterv.metadata.MetadataProvider;
import hu.denes.bme.dipterv.metadata.datasource.MeasurementDataSource;
import io.swagger.model.DataRequest;
import io.swagger.model.DataResponse;
import io.swagger.model.DataResponseHeader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataProvider {
    @Autowired
    private MetadataProvider metadataProvider;

    public DataResponse getData(final DataRequest request) {
        MeasurementDataSource ds = metadataProvider.getMeasurementDataSourceByName(request.getDatasource());
        DataResponse response = new DataResponse();
        DataResponseHeader drh = new DataResponseHeader();
        drh.setKpi("temperature");
        DataResponseHeader drh2 = new DataResponseHeader();
        drh2.setDimension("city");
        response.setHeader(Arrays.asList( drh2, drh));

        List<List<String>> mx = new ArrayList<>();
        mx.add(new ArrayList<>());
        mx.get(0).add("Budapest");
        mx.get(0).add("10");
        response.setDataMatrix(mx);

        return response;
    }
}
