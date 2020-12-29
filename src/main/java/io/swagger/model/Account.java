package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;
import java.util.Random;

/**
 * AccountObject
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-05-21T18:10:30.703Z[GMT]")
@Entity
public class Account {

  @Id
  @JsonProperty("iban")
  private String IBAN = null;

  @JsonProperty("amount")
  private Double amount = null;

  @JsonProperty("ownerId")
  private Integer ownerId = null;

  @ManyToOne(cascade = {CascadeType.ALL})
  private User user;

public Account(){

}
  public Account(int i, int ownerId, TypeEnum saving, StatusEnum active, double transactionLimit, int dayLimit, int i1) {
  }

  public Account(Integer ownerId, TypeEnum type) { // most fields are ints
    this.IBAN = generateIban();
    this.ownerId = ownerId;
    this.type = type;
    this.amount = 700d;
    this.status = StatusEnum.ACTIVE;
    this.transactionLimit = 900.0d;
    this.dayLimit = 10;
    this.absolutelimit = 20d;

  }

  public Account(Double amount, Integer ownerId, TypeEnum type, StatusEnum status, Double transactionLimit, Integer dayLimit, Double absolutelimit) {
    this.IBAN = generateIban();
    this.amount = amount;
    this.ownerId = ownerId;
    this.type = type;
    this.status = status;
    this.transactionLimit = transactionLimit;
    this.dayLimit = dayLimit;
    this.absolutelimit = absolutelimit;
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
    CHECKING("CHECKING"),

    SAVING("SAVING");

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
  private Double absolutelimit = null;

  public Account IBAN(String IBAN) {
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

  public Account amount(Double amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
   **/
  @ApiModelProperty(value = "")

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public Account ownerId(Integer ownerId) {
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

  public Account type(TypeEnum type) {
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

  public Account status(StatusEnum status) {
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

  public Account transactionLimit(Double transactionLimit) {
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

  public Account dayLimit(Integer dayLimit) {
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

  public Account absolutelimit(Double absolutelimit) {
    this.absolutelimit = absolutelimit;
    return this;
  }

  /**
   * Get absolutelimit
   * @return absolutelimit
   **/
  @ApiModelProperty(value = "")

  public Double getAbsolutelimit() {
    return absolutelimit;
  }

  public void setAbsolutelimit(Double absolutelimit) {
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
    Account account = (Account) o;
    return Objects.equals(this.IBAN, account.IBAN) &&
            Objects.equals(this.amount, account.amount) &&
            Objects.equals(this.ownerId, account.ownerId) &&
            Objects.equals(this.type, account.type) &&
            Objects.equals(this.status, account.status) &&
            Objects.equals(this.transactionLimit, account.transactionLimit) &&
            Objects.equals(this.dayLimit, account.dayLimit) &&
            Objects.equals(this.absolutelimit, account.absolutelimit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(IBAN, amount, ownerId, type, status, transactionLimit, dayLimit, absolutelimit);
  }

  @Override
  public String toString() {
    return "Account{" +
            "IBAN='" + IBAN + '\'' +
            ", amount=" + amount +
            ", ownerId=" + ownerId +
            ", user=" + user +
            ", type=" + type +
            ", status=" + status +
            ", transactionLimit=" + transactionLimit +
            ", dayLimit=" + dayLimit +
            ", absolutelimit=" + absolutelimit +
            '}';
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