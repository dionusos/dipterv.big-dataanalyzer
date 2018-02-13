package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * KpiMeta
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-02-13T21:19:23.429Z")

public class Kpi   {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("displayName")
  private String displayName = null;

  @JsonProperty("offeredMetric")
  private String offeredMetric = null;

  @JsonProperty("unit")
  private String unit = null;

  public Kpi name(String name) {
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

  public Kpi displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

   /**
   * Get displayName
   * @return displayName
  **/
  @ApiModelProperty(value = "")


  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public Kpi offeredMetric(String offeredMetric) {
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

  public Kpi unit(String unit) {
    this.unit = unit;
    return this;
  }

   /**
   * Get unit
   * @return unit
  **/
  @ApiModelProperty(value = "")


  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Kpi kpi = (Kpi) o;
    return Objects.equals(this.name, kpi.name) &&
        Objects.equals(this.displayName, kpi.displayName) &&
        Objects.equals(this.offeredMetric, kpi.offeredMetric) &&
        Objects.equals(this.unit, kpi.unit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, displayName, offeredMetric, unit);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class KpiMeta {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    offeredMetric: ").append(toIndentedString(offeredMetric)).append("\n");
    sb.append("    unit: ").append(toIndentedString(unit)).append("\n");
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

