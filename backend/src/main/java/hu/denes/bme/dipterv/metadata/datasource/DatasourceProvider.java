package hu.denes.bme.dipterv.metadata.datasource;

import hu.denes.bme.dipterv.metadata.Datasource;
import hu.denes.bme.dipterv.metadata.Metadata;
import hu.denes.bme.dipterv.metadata.Schemas;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class DatasourceProvider {
    private List<MeasurementDataSource> datasources = new ArrayList<>();

    private Map<String, MeasurementDataSource> nameToDatasource = new HashMap<>();

    @PostConstruct
    public void init() {
        System.err.println("initializing FDSP.....");
        String basePath = "example/src/main/resources";
        for (final File f : new File(basePath).listFiles()) {
            if (f.isDirectory()) {
                loadDatasource(f);
            }
        }
    }

    public Collection<MeasurementDataSource> getDatasources() {
        return  datasources;
    }
    private void loadDatasource(final File f) {
        MeasurementDataSource measurementDataSource = new MeasurementDataSource();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Datasource.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Datasource datasource = (Datasource) jaxbUnmarshaller.unmarshal(new File(f, "datasource.xml"));
            measurementDataSource.setDatasource(datasource);
        } catch (JAXBException e) {
            e.printStackTrace();
            return;
        }

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Schemas.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Schemas schemas = (Schemas) jaxbUnmarshaller.unmarshal(new File(f, "schema.xml"));
            measurementDataSource.setSchemas(schemas);
        } catch (JAXBException e) {
            e.printStackTrace();
            return;
        }

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Metadata.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Metadata metadata = (Metadata) jaxbUnmarshaller.unmarshal(new File(f, "metadata.xml"));
            measurementDataSource.setMetadata(metadata);
        } catch (JAXBException e) {
            e.printStackTrace();
            return;
        }
        datasources.add(measurementDataSource);
        nameToDatasource.put(measurementDataSource.getDatasource().getName(), measurementDataSource);
    }

    public MeasurementDataSource getMeasurementDataSource(final String name) {
        return nameToDatasource.get(name);
    }
}
