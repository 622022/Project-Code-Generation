package io.swagger.model.api;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

/**
 * UserCredentials
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-05-21T18:10:30.703Z[GMT]")
public class UserCredentials {
  @JsonProperty("userId")
  private String userId = null;

  @JsonProperty("tokenType")
  private String tokenType = null;

  @JsonProperty("tokenValue")
  private String tokenValue = null;

  public UserCredentials(String userId, String tokenType, String tokenValue) {
    this.userId = userId;
    this.tokenType = tokenType;
    this.tokenValue = tokenValue;
  }

  public UserCredentials userId(String userId) {
    this.userId = userId;
    return this;
  }

  public UserCredentials() {
  }

  /**
   * Get userId
   * @return userId
  **/
  @ApiModelProperty(value = "")
  
    public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public UserCredentials tokenType(String tokenType) {
    this.tokenType = tokenType;
    return this;
  }

  /**
   * Get tokenType
   * @return tokenType
  **/
  @ApiModelProperty(value = "")
  
    public String getTokenType() {
    return tokenType;
  }

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  public UserCredentials tokenValue(String tokenValue) {
    this.tokenValue = tokenValue;
    return this;
  }

  /**
   * Get tokenValue
   * @return tokenValue
  **/
  @ApiModelProperty(value = "")
  
    public String getTokenValue() {
    return tokenValue;
  }

  public void setTokenValue(String tokenValue) {
    this.tokenValue = tokenValue;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserCredentials userCredentials = (UserCredentials) o;
    return Objects.equals(this.userId, userCredentials.userId) &&
        Objects.equals(this.tokenType, userCredentials.tokenType) &&
        Objects.equals(this.tokenValue, userCredentials.tokenValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, tokenType, tokenValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserCredentials {\n");
    
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    tokenType: ").append(toIndentedString(tokenType)).append("\n");
    sb.append("    tokenValue: ").append(toIndentedString(tokenValue)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
