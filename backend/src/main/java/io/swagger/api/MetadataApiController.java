package io.swagger.api;

import hu.denes.bme.dipterv.metadata.MetadataProvider;
import io.swagger.model.Datasource;
import io.swagger.model.Datasources;
import io.swagger.model.DimensionList;
import io.swagger.model.KpiList;

import io.swagger.annotations.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.validation.constraints.*;
import javax.validation.Valid;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-02-13T21:19:23.429Z")

@Controller
public class MetadataApiController implements MetadataApi {

    @Autowired
    private MetadataProvider metadataProvider;

    public ResponseEntity<DimensionList> metadataDatasourceDatasourceIdDimensionListGet(@ApiParam(value = "",required=true ) @PathVariable("datasourceId") String datasourceId) {
        // do some magic!
        return new ResponseEntity<DimensionList>(HttpStatus.OK);
    }

    public ResponseEntity<Datasource> metadataDatasourceDatasourceIdGet(@ApiParam(value = "ID of datasource that needs to be fetched",required=true ) @PathVariable("datasourceId") String datasourceId) {
        // do some magic!
        return new ResponseEntity<Datasource>(HttpStatus.OK);
    }

    public ResponseEntity<KpiList> metadataDatasourceDatasourceIdKpiListGet(@ApiParam(value = "",required=true ) @PathVariable("datasourceId") String datasourceId) {
        // do some magic!
        return new ResponseEntity<KpiList>(HttpStatus.OK);
    }

    public ResponseEntity<Datasources> metadataDatasourcesGet() {
        // do some magic!
        return new ResponseEntity<Datasources>(HttpStatus.OK);
    }

}
