package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.FilterRequestIntervals;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * FilterRequest
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-02-25T13:39:26.575Z")

public class FilterRequest   {
  @JsonProperty("dimension")
  private String dimension = null;

  @JsonProperty("kpi")
  private String kpi = null;

  @JsonProperty("values")
  private List<String> values = null;

  @JsonProperty("intervals")
  private List<FilterRequestIntervals> intervals = null;

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

  public FilterRequest values(List<String> values) {
    this.values = values;
    return this;
  }

  public FilterRequest addValuesItem(String valuesItem) {
    if (this.values == null) {
      this.values = new ArrayList<String>();
    }
    this.values.add(valuesItem);
    return this;
  }

   /**
   * Get values
   * @return values
  **/
  @ApiModelProperty(value = "")


  public List<String> getValues() {
    return values;
  }

  public void setValues(List<String> values) {
    this.values = values;
  }

  public FilterRequest intervals(List<FilterRequestIntervals> intervals) {
    this.intervals = intervals;
    return this;
  }

  public FilterRequest addIntervalsItem(FilterRequestIntervals intervalsItem) {
    if (this.intervals == null) {
      this.intervals = new ArrayList<FilterRequestIntervals>();
    }
    this.intervals.add(intervalsItem);
    return this;
  }

   /**
   * Get intervals
   * @return intervals
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<FilterRequestIntervals> getIntervals() {
    return intervals;
  }

  public void setIntervals(List<FilterRequestIntervals> intervals) {
    this.intervals = intervals;
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
        Objects.equals(this.values, filterRequest.values) &&
        Objects.equals(this.intervals, filterRequest.intervals) &&
        Objects.equals(this.isNegative, filterRequest.isNegative);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dimension, kpi, values, intervals, isNegative);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FilterRequest {\n");
    
    sb.append("    dimension: ").append(toIndentedString(dimension)).append("\n");
    sb.append("    kpi: ").append(toIndentedString(kpi)).append("\n");
    sb.append("    values: ").append(toIndentedString(values)).append("\n");
    sb.append("    intervals: ").append(toIndentedString(intervals)).append("\n");
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

