package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.DimensionRequest;
import io.swagger.model.FilterRequest;
import io.swagger.model.KpiRequest;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * DataRequest
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-02-16T19:53:36.064Z")

public class DataRequest   {
  @JsonProperty("datasource")
  private String datasource = null;

  @JsonProperty("kpis")
  private List<KpiRequest> kpis = null;

  @JsonProperty("dimensions")
  private List<DimensionRequest> dimensions = null;

  @JsonProperty("filters")
  private List<FilterRequest> filters = null;

  public DataRequest datasource(String datasource) {
    this.datasource = datasource;
    return this;
  }

   /**
   * Get datasource
   * @return datasource
  **/
  @ApiModelProperty(value = "")


  public String getDatasource() {
    return datasource;
  }

  public void setDatasource(String datasource) {
    this.datasource = datasource;
  }

  public DataRequest kpis(List<KpiRequest> kpis) {
    this.kpis = kpis;
    return this;
  }

  public DataRequest addKpisItem(KpiRequest kpisItem) {
    if (this.kpis == null) {
      this.kpis = new ArrayList<KpiRequest>();
    }
    this.kpis.add(kpisItem);
    return this;
  }

   /**
   * Get kpis
   * @return kpis
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<KpiRequest> getKpis() {
    return kpis;
  }

  public void setKpis(List<KpiRequest> kpis) {
    this.kpis = kpis;
  }

  public DataRequest dimensions(List<DimensionRequest> dimensions) {
    this.dimensions = dimensions;
    return this;
  }

  public DataRequest addDimensionsItem(DimensionRequest dimensionsItem) {
    if (this.dimensions == null) {
      this.dimensions = new ArrayList<DimensionRequest>();
    }
    this.dimensions.add(dimensionsItem);
    return this;
  }

   /**
   * Get dimensions
   * @return dimensions
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<DimensionRequest> getDimensions() {
    return dimensions;
  }

  public void setDimensions(List<DimensionRequest> dimensions) {
    this.dimensions = dimensions;
  }

  public DataRequest filters(List<FilterRequest> filters) {
    this.filters = filters;
    return this;
  }

  public DataRequest addFiltersItem(FilterRequest filtersItem) {
    if (this.filters == null) {
      this.filters = new ArrayList<FilterRequest>();
    }
    this.filters.add(filtersItem);
    return this;
  }

   /**
   * Get filters
   * @return filters
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<FilterRequest> getFilters() {
    return filters;
  }

  public void setFilters(List<FilterRequest> filters) {
    this.filters = filters;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataRequest dataRequest = (DataRequest) o;
    return Objects.equals(this.datasource, dataRequest.datasource) &&
        Objects.equals(this.kpis, dataRequest.kpis) &&
        Objects.equals(this.dimensions, dataRequest.dimensions) &&
        Objects.equals(this.filters, dataRequest.filters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(datasource, kpis, dimensions, filters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataRequest {\n");
    
    sb.append("    datasource: ").append(toIndentedString(datasource)).append("\n");
    sb.append("    kpis: ").append(toIndentedString(kpis)).append("\n");
    sb.append("    dimensions: ").append(toIndentedString(dimensions)).append("\n");
    sb.append("    filters: ").append(toIndentedString(filters)).append("\n");
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

