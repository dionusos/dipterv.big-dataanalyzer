package io.swagger.api;

import hu.denes.bme.dipterv.data.DataProvider;
import io.swagger.model.DataRequest;
import io.swagger.model.DataResponse;

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
public class DataApiController implements DataApi {

    @Autowired
    private DataProvider dataProvider;

    public ResponseEntity<DataResponse> dataPost(@ApiParam(value = ""  )  @Valid @RequestBody DataRequest body) {
        try {
            DataResponse response = dataProvider.getData(body);
            return new ResponseEntity<DataResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
