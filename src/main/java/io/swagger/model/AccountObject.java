package io.swagger.model;

import java.util.Objects;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * AccountObject
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-05-21T18:10:30.703Z[GMT]")
@Entity
public class AccountObject   {

  @Id
  @JsonProperty("iban")
  private String IBAN = null;


  @JsonProperty("amount")
  private Integer amount = null;

  @JsonProperty("ownerId")
  private Integer ownerId = null;

  @ManyToOne(cascade = {CascadeType.ALL})
  private User user;

  public AccountObject() {
  }

  public AccountObject(Integer ownerId, TypeEnum type) { // most fields are ints
    this.IBAN = generateIban();
    this.ownerId = ownerId;
    this.type = type;
    this.amount = 0;
    this.status = StatusEnum.ACTIVE;
    this.transactionLimit = 0.0d;
    this.dayLimit = 0;
    this.absolutelimit = 0;

  }

  public AccountObject(Integer amount, Integer ownerId, TypeEnum type, StatusEnum status, Double transactionLimit, Integer dayLimit, Integer absolutelimit) {
    this.IBAN = generateIban();
    this.amount = amount;
    this.ownerId = ownerId;
    this.type = type;
    this.status = status;
    this.transactionLimit = transactionLimit;
    this.dayLimit = dayLimit;
    this.absolutelimit = absolutelimit;
  }

  public AccountObject(String s, int i, Integer userId, TypeEnum checking, StatusEnum active, double v, int i1, int i2) {
  }

  /**
   * Generate IBAN
   */
  public String generateIban() {
    Random rand = new Random();
    String iBan = "NL16ABNA";

    for (int i = 0; i < 10; i++)
    {
      int accountNumber = rand.nextInt(10) + 0;
      iBan += Integer.toString(accountNumber);
    }
    return  iBan;
  }


  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    CHECKING("Checking"),

    SAVING("Saving");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String text) {
      for (TypeEnum b : TypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("type")
  private TypeEnum type = null;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("Active"),

    CLOSED("Closed");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("status")
  private StatusEnum status = null;

  @JsonProperty("transactionLimit")
  private Double transactionLimit = null;

  @JsonProperty("dayLimit")
  private Integer dayLimit = null;

  @JsonProperty("absolutelimit")
  private Integer absolutelimit = null;

  public AccountObject IBAN(String IBAN) {
    this.IBAN = IBAN;
    return this;
  }

  /**
   * Get IBAN
   * @return IBAN
   **/
  @ApiModelProperty(value = "")

  public String getIBAN() {
    return IBAN;
  }

  public void setIBAN(String IBAN) {
    this.IBAN = IBAN;
  }

  public AccountObject amount(Integer amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
   **/
  @ApiModelProperty(value = "")

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public AccountObject ownerId(Integer ownerId) {
    this.ownerId = ownerId;
    return this;
  }

  /**
   * Get ownerId
   * @return ownerId
   **/
  @ApiModelProperty(value = "")

  public Integer getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(Integer ownerId) {
    this.ownerId = ownerId;
  }

  public AccountObject type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   **/
  @ApiModelProperty(value = "")

  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public AccountObject status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   **/
  @ApiModelProperty(value = "")

  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public AccountObject transactionLimit(Double transactionLimit) {
    this.transactionLimit = transactionLimit;
    return this;
  }

  /**
   * Get transactionLimit
   * @return transactionLimit
   **/
  @ApiModelProperty(value = "")

  public Double getTransactionLimit() {
    return transactionLimit;
  }

  public void setTransactionLimit(Double transactionLimit) {
    this.transactionLimit = transactionLimit;
  }

  public AccountObject dayLimit(Integer dayLimit) {
    this.dayLimit = dayLimit;
    return this;
  }

  /**
   * Get dayLimit
   * @return dayLimit
   **/
  @ApiModelProperty(value = "")

  public Integer getDayLimit() {
    return dayLimit;
  }

  public void setDayLimit(Integer dayLimit) {
    this.dayLimit = dayLimit;
  }

  public AccountObject absolutelimit(Integer absolutelimit) {
    this.absolutelimit = absolutelimit;
    return this;
  }

  /**
   * Get absolutelimit
   * @return absolutelimit
   **/
  @ApiModelProperty(value = "")

  public Integer getAbsolutelimit() {
    return absolutelimit;
  }

  public void setAbsolutelimit(Integer absolutelimit) {
    this.absolutelimit = absolutelimit;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccountObject accountObject = (AccountObject) o;
    return Objects.equals(this.IBAN, accountObject.IBAN) &&
            Objects.equals(this.amount, accountObject.amount) &&
            Objects.equals(this.ownerId, accountObject.ownerId) &&
            Objects.equals(this.type, accountObject.type) &&
            Objects.equals(this.status, accountObject.status) &&
            Objects.equals(this.transactionLimit, accountObject.transactionLimit) &&
            Objects.equals(this.dayLimit, accountObject.dayLimit) &&
            Objects.equals(this.absolutelimit, accountObject.absolutelimit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(IBAN, amount, ownerId, type, status, transactionLimit, dayLimit, absolutelimit);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccountObject {\n");

    sb.append("    IBAN: ").append(toIndentedString(IBAN)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    transactionLimit: ").append(toIndentedString(transactionLimit)).append("\n");
    sb.append("    dayLimit: ").append(toIndentedString(dayLimit)).append("\n");
    sb.append("    absolutelimit: ").append(toIndentedString(absolutelimit)).append("\n");
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