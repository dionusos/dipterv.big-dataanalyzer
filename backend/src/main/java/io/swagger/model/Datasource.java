package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Datasource
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-02-13T21:19:23.429Z")

public class Datasource   {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("displayName")
  private String displayName = null;

  @JsonProperty("beginning")
  private Integer beginning = null;

  @JsonProperty("end")
  private Integer end = null;

  public Datasource name(String name) {
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

  public Datasource displayName(String displayName) {
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

  public Datasource beginning(Integer beginning) {
    this.beginning = beginning;
    return this;
  }

   /**
   * Get beginning
   * @return beginning
  **/
  @ApiModelProperty(value = "")


  public Integer getBeginning() {
    return beginning;
  }

  public void setBeginning(Integer beginning) {
    this.beginning = beginning;
  }

  public Datasource end(Integer end) {
    this.end = end;
    return this;
  }

   /**
   * Get end
   * @return end
  **/
  @ApiModelProperty(value = "")


  public Integer getEnd() {
    return end;
  }

  public void setEnd(Integer end) {
    this.end = end;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Datasource datasource = (Datasource) o;
    return Objects.equals(this.name, datasource.name) &&
        Objects.equals(this.displayName, datasource.displayName) &&
        Objects.equals(this.beginning, datasource.beginning) &&
        Objects.equals(this.end, datasource.end);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, displayName, beginning, end);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Datasource {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    beginning: ").append(toIndentedString(beginning)).append("\n");
    sb.append("    end: ").append(toIndentedString(end)).append("\n");
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

