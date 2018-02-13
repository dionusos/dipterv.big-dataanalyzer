package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.FilterRequestInterval;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * FilterRequest
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-02-13T21:19:23.429Z")

public class FilterRequest   {
  @JsonProperty("dimension")
  private String dimension = null;

  @JsonProperty("kpi")
  private String kpi = null;

  @JsonProperty("value")
  private String value = null;

  @JsonProperty("interval")
  private FilterRequestInterval interval = null;

  @JsonProperty("isNegative")
  private Boolean isNegative = null;

  public FilterRequest dimension(String dimension) {
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

  public FilterRequest kpi(String kpi) {
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

  public FilterRequest value(String value) {
    this.value = value;
    return this;
  }

   /**
   * Get value
   * @return value
  **/
  @ApiModelProperty(value = "")


  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public FilterRequest interval(FilterRequestInterval interval) {
    this.interval = interval;
    return this;
  }

   /**
   * Get interval
   * @return interval
  **/
  @ApiModelProperty(value = "")

  @Valid

  public FilterRequestInterval getInterval() {
    return interval;
  }

  public void setInterval(FilterRequestInterval interval) {
    this.interval = interval;
  }

  public FilterRequest isNegative(Boolean isNegative) {
    this.isNegative = isNegative;
    return this;
  }

   /**
   * Get isNegative
   * @return isNegative
  **/
  @ApiModelProperty(value = "")


  public Boolean getIsNegative() {
    return isNegative;
  }

  public void setIsNegative(Boolean isNegative) {
    this.isNegative = isNegative;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FilterRequest filterRequest = (FilterRequest) o;
    return Objects.equals(this.dimension, filterRequest.dimension) &&
        Objects.equals(this.kpi, filterRequest.kpi) &&
        Objects.equals(this.value, filterRequest.value) &&
        Objects.equals(this.interval, filterRequest.interval) &&
        Objects.equals(this.isNegative, filterRequest.isNegative);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dimension, kpi, value, interval, isNegative);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FilterRequest {\n");
    
    sb.append("    dimension: ").append(toIndentedString(dimension)).append("\n");
    sb.append("    kpi: ").append(toIndentedString(kpi)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    interval: ").append(toIndentedString(interval)).append("\n");
    sb.append("    isNegative: ").append(toIndentedString(isNegative)).append("\n");
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

