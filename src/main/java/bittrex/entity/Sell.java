package bittrex.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by Onsiter on 2017/12/22.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sell extends Result {
    @JsonProperty("Quantity")
    public BigDecimal quantity;

    @JsonProperty("Rate")
    public BigDecimal rate;

}
