package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * FilterRequestInterval
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-02-13T21:19:23.429Z")

public class FilterRequestInterval   {
  @JsonProperty("lower")
  private String lower = null;

  @JsonProperty("upper")
  private String upper = null;

  public FilterRequestInterval lower(String lower) {
    this.lower = lower;
    return this;
  }

   /**
   * Get lower
   * @return lower
  **/
  @ApiModelProperty(value = "")


  public String getLower() {
    return lower;
  }

  public void setLower(String lower) {
    this.lower = lower;
  }

  public FilterRequestInterval upper(String upper) {
    this.upper = upper;
    return this;
  }

   /**
   * Get upper
   * @return upper
  **/
  @ApiModelProperty(value = "")


  public String getUpper() {
    return upper;
  }

  public void setUpper(String upper) {
    this.upper = upper;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FilterRequestInterval filterRequestInterval = (FilterRequestInterval) o;
    return Objects.equals(this.lower, filterRequestInterval.lower) &&
        Objects.equals(this.upper, filterRequestInterval.upper);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lower, upper);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FilterRequestInterval {\n");
    
    sb.append("    lower: ").append(toIndentedString(lower)).append("\n");
    sb.append("    upper: ").append(toIndentedString(upper)).append("\n");
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

