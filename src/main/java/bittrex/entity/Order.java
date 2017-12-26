package bittrex.entity;

/**
 * Created by Onsiter on 2017/12/19.
 */

import bittrex.entity.enums.Exchange;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Order extends Result{
    @JsonProperty("OrderUuid")
    public String orderUuid;

    @JsonProperty("Exchange")
    public Exchange exchange;

    @JsonProperty("OrderType")
    public String orderType;

    @JsonProperty("QuantityRemaining")
    public BigDecimal quantityRemaining;

    @JsonProperty("Quantity")
    public BigDecimal quantity;

    @JsonProperty("Limit")
    public BigDecimal limit;

    @JsonProperty("Opened")
    public Date opened;

    @JsonProperty("Closed")
    public Date closed;

    @JsonProperty("Condition")
    public String condition;

    @JsonProperty("ConditionTarget")
    public BigDecimal conditionTarget;

    @JsonProperty("PricePerUnit")
    public BigDecimal pricePerUnit;

    public String toString() {
        return "{\n" +
                " \"OrderUuid\" : " + orderUuid + ",\n" +
                " \"Exchange\" : " + exchange.getCode() + ",\n" +
                " \"OrderType\" : " + orderType + ",\n" +
                " \"Quantity\" : "+ quantity + ",\n" +
                " \"QuantityRemaining\" : " + quantityRemaining + ",\n" +
                " \"Limit\" : " + limit + ",\n" +
                " \"PricePerUnit\" : " + pricePerUnit + ",\n" +
                " \"Opened\" : " + opened + ",\n" +
                " \"Closed\" : " + closed + ",\n" +
                " \"Condition\" : " + condition + ",\n" +
                " \"ConditionTarget\" : " + conditionTarget + "\n" +
                "}";
    }
}

