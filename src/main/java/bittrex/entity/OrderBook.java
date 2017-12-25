package bittrex.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Onsiter on 2017/12/22.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderBook extends Result {
    @JsonProperty("buy")
    public Buy[] buys;

    @JsonProperty("sell")
    public Sell[] sells;

}
