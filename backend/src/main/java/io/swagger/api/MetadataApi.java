/**
 * NOTE: This class is auto generated by the swagger code generator program (2.2.3).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.model.Datasource;
import io.swagger.model.Datasources;
import io.swagger.model.DimensionList;
import io.swagger.model.KpiList;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-02-13T21:19:23.429Z")

@Api(value = "metadata", description = "the metadata API")
@CrossOrigin(origins = "*")
public interface MetadataApi {

    @ApiOperation(value = "", notes = "", response = DimensionList.class, tags={ "Metadata", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Ok", response = DimensionList.class),
        @ApiResponse(code = 404, message = "Datasource not found", response = Void.class) })
    
    @RequestMapping(value = "/metadata/datasource/{datasourceId}/dimension/list",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<DimensionList> metadataDatasourceDatasourceIdDimensionListGet(@ApiParam(value = "", required = true) @PathVariable("datasourceId") String datasourceId);


    @ApiOperation(value = "", notes = "", response = Datasource.class, tags={ "Metadata", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Ok", response = Datasource.class),
        @ApiResponse(code = 404, message = "Datasource not found", response = Void.class) })
    
    @RequestMapping(value = "/metadata/datasource/{datasourceId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Datasource> metadataDatasourceDatasourceIdGet(@ApiParam(value = "ID of datasource that needs to be fetched", required = true) @PathVariable("datasourceId") String datasourceId);


    @ApiOperation(value = "", notes = "", response = KpiList.class, tags={ "Metadata", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Ok", response = KpiList.class),
        @ApiResponse(code = 404, message = "Datasource not found", response = Void.class) })
    
    @RequestMapping(value = "/metadata/datasource/{datasourceId}/kpi/list",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<KpiList> metadataDatasourceDatasourceIdKpiListGet(@ApiParam(value = "", required = true) @PathVariable("datasourceId") String datasourceId);


    @ApiOperation(value = "List all datastores", notes = "", response = Datasources.class, tags={ "Metadata", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "", response = Datasources.class) })
    
    @RequestMapping(value = "/metadata/datasources",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Datasources> metadataDatasourcesGet();

}
