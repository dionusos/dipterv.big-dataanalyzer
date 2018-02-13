package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * DataResponseHeader
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-02-13T21:19:23.429Z")

public class DataResponseHeader   {
  @JsonProperty("kpi")
  private String kpi = null;

  @JsonProperty("offeredMetric")
  private String offeredMetric = null;

  @JsonProperty("dimension")
  private String dimension = null;

  public DataResponseHeader kpi(String kpi) {
    this.kpi = kpi;
    return this;
  }

   /**
   * Get kpi
   * @return kpi
  **/
  @ApiModelProperty(value = "")


  public String getKpi() {
    return kpi;
  }

  public void setKpi(String kpi) {
    this.kpi = kpi;
  }

  public DataResponseHeader offeredMetric(String offeredMetric) {
    this.offeredMetric = offeredMetric;
    return this;
  }

   /**
   * Get offeredMetric
   * @return offeredMetric
  **/
  @ApiModelProperty(value = "")


  public String getOfferedMetric() {
    return offeredMetric;
  }

  public void setOfferedMetric(String offeredMetric) {
    this.offeredMetric = offeredMetric;
  }

  public DataResponseHeader dimension(String dimension) {
    this.dimension = dimension;
    return this;
  }

   /**
   * Get dimension
   * @return dimension
  **/
  @ApiModelProperty(value = "")


  public String getDimension() {
    return dimension;
  }

  public void setDimension(String dimension) {
    this.dimension = dimension;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataResponseHeader dataResponseHeader = (DataResponseHeader) o;
    return Objects.equals(this.kpi, dataResponseHeader.kpi) &&
        Objects.equals(this.offeredMetric, dataResponseHeader.offeredMetric) &&
        Objects.equals(this.dimension, dataResponseHeader.dimension);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kpi, offeredMetric, dimension);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataResponseHeader {\n");
    
    sb.append("    kpi: ").append(toIndentedString(kpi)).append("\n");
    sb.append("    offeredMetric: ").append(toIndentedString(offeredMetric)).append("\n");
    sb.append("    dimension: ").append(toIndentedString(dimension)).append("\n");
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

