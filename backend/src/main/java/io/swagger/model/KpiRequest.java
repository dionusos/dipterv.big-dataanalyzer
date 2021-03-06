package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * KpiRequest
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-02-13T21:19:23.429Z")

public class KpiRequest   {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("offeredMetric")
  private String offeredMetric = null;

  public KpiRequest name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(value = "")


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public KpiRequest offeredMetric(String offeredMetric) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KpiRequest kpiRequest = (KpiRequest) o;
    return Objects.equals(this.name, kpiRequest.name) &&
        Objects.equals(this.offeredMetric, kpiRequest.offeredMetric);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, offeredMetric);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class KpiRequest {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    offeredMetric: ").append(toIndentedString(offeredMetric)).append("\n");
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

