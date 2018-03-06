package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.FilterRequestKpi;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * OrderRequest
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-03-06T19:13:26.963Z")

public class OrderRequest   {
  @JsonProperty("dimension")
  private String dimension = null;

  @JsonProperty("kpi")
  private FilterRequestKpi kpi = null;

  @JsonProperty("direction")
  private String direction = null;

  public OrderRequest dimension(String dimension) {
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

  public OrderRequest kpi(FilterRequestKpi kpi) {
    this.kpi = kpi;
    return this;
  }

   /**
   * Get kpi
   * @return kpi
  **/
  @ApiModelProperty(value = "")

  @Valid

  public FilterRequestKpi getKpi() {
    return kpi;
  }

  public void setKpi(FilterRequestKpi kpi) {
    this.kpi = kpi;
  }

  public OrderRequest direction(String direction) {
    this.direction = direction;
    return this;
  }

   /**
   * Get direction
   * @return direction
  **/
  @ApiModelProperty(value = "")


  public String getDirection() {
    return direction;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderRequest orderRequest = (OrderRequest) o;
    return Objects.equals(this.dimension, orderRequest.dimension) &&
        Objects.equals(this.kpi, orderRequest.kpi) &&
        Objects.equals(this.direction, orderRequest.direction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dimension, kpi, direction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderRequest {\n");
    
    sb.append("    dimension: ").append(toIndentedString(dimension)).append("\n");
    sb.append("    kpi: ").append(toIndentedString(kpi)).append("\n");
    sb.append("    direction: ").append(toIndentedString(direction)).append("\n");
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

