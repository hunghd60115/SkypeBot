package bittrex.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by Onsiter on 2017/12/22.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Balance extends Result {
    @JsonProperty("Currency")
    public String currency;

    @JsonProperty("Balance")
    public String balance;

    @JsonProperty("Available")
    public String available;

    @JsonProperty("Pending")
    public String pending;

    @JsonProperty("Uuid")
    public String uuid;

    public String toString() {
        return "{ \n" +
                " Currency : " + this.currency + ",\n" +
                " Balance : " + this.balance + ",\n" +
                " Available : " + this.available + ",\n" +
                " Pending : " + this.pending + ",\n" +
                " Uuid : " + this.uuid + "\n" +
                "}";
    }
}
