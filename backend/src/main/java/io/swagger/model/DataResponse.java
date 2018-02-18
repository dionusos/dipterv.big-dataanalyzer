package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.DataResponseHeader;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * DataResponse
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-02-13T21:19:23.429Z")

public class DataResponse   {
  @JsonProperty("header")
  private List<DataResponseHeader> header = null;

  @JsonProperty("dataMatrix")
  private List<List<Object>> dataMatrix = null;

  public DataResponse header(List<DataResponseHeader> header) {
    this.header = header;
    return this;
  }

  public DataResponse addHeaderItem(DataResponseHeader headerItem) {
    if (this.header == null) {
      this.header = new ArrayList<DataResponseHeader>();
    }
    this.header.add(headerItem);
    return this;
  }

   /**
   * Get header
   * @return header
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<DataResponseHeader> getHeader() {
    return header;
  }

  public void setHeader(List<DataResponseHeader> header) {
    this.header = header;
  }

  public DataResponse dataMatrix(List<List<Object>> dataMatrix) {
    this.dataMatrix = dataMatrix;
    return this;
  }

  public DataResponse addDataMatrixItem(List<Object> dataMatrixItem) {
    if (this.dataMatrix == null) {
      this.dataMatrix = new ArrayList<List<Object>>();
    }
    this.dataMatrix.add(dataMatrixItem);
    return this;
  }

   /**
   * Get dataMatrix
   * @return dataMatrix
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<List<Object>> getDataMatrix() {
    return dataMatrix;
  }

  public void setDataMatrix(List<List<Object>> dataMatrix) {
    this.dataMatrix = dataMatrix;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataResponse dataResponse = (DataResponse) o;
    return Objects.equals(this.header, dataResponse.header) &&
        Objects.equals(this.dataMatrix, dataResponse.dataMatrix);
  }

  @Override
  public int hashCode() {
    return Objects.hash(header, dataMatrix);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataResponse {\n");
    
    sb.append("    header: ").append(toIndentedString(header)).append("\n");
    sb.append("    dataMatrix: ").append(toIndentedString(dataMatrix)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

