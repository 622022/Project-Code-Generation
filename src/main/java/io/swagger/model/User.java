package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import java.util.Objects;

/**
 * User
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-05-21T18:10:30.703Z[GMT]")
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class  User   {

  //@SequenceGenerator(name = "guitar_seq", initialValue = 1000001, allocationSize = 1)
  //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "guitar_seq")
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonProperty("userId")
  private Integer userId;

  @JsonProperty("username")
  private String username = null;

  @JsonProperty("password")
  private String password = null;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("role")
  private Role role = null;

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

//  @OneToOne(mappedBy = "accountObject", fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
//  private AccountObject accountObject;

  public User email(String email) {
    this.email = email;
    return this;
  }

  public User() {
  }

  public User( String username, String password, String email, Role role) {

    this.username = username;
    this.password = password;
    this.email = email;
    this.role = role;
  }

  /**
   * Get email
   * @return email
   **/
  @ApiModelProperty(value = "")

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public User password(String password) {
    this.password = password;
    return this;
  }

  /**
   * Get password
   * @return password
   **/
  @ApiModelProperty(value = "")

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public User userId(Integer userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Get userId
   * @return userId
   **/
  @ApiModelProperty(value = "")

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public User username(String username) {
    this.username = username;
    return this;
  }

  /**
   * Get username
   * @return username
   **/
  @ApiModelProperty(value = "")

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(this.email, user.email) &&
            Objects.equals(this.password, user.password) &&
            Objects.equals(this.userId, user.userId) &&
            Objects.equals(this.username, user.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, password, userId, username);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class User {\n");

    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
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