package bittrex.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by Onsiter on 2017/12/19.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ticker extends Result {

    @JsonProperty("Bid")
    public BigDecimal bid;

    @JsonProperty("Ask")
    public BigDecimal ask;

    @JsonProperty("Last")
    public BigDecimal last;

    public String toString() {
        return "[Ticker] Bid " + bid + ", Ask " + ask + ", Last " + last;
    }
}
