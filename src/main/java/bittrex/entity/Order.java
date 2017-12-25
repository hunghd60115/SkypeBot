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

    @JsonProperty("Condition")
    public String condition;

    @JsonProperty("ConditionTarget")
    public BigDecimal conditionTarget;

    public String toString() {
        return "[Order] OrderUuid " + orderUuid + ", Exchange " + exchange.getCode() + ", OrderType " + orderType + ", Quantity " + quantity +
                ", Limit " + limit + ", Opened " + opened + ", Condition " + condition;
    }
}

