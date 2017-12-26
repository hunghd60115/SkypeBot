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
                " Currency : " + currency + ",\n" +
                " Balance : " + balance + ",\n" +
                " Available : " + available + ",\n" +
                " Pending : " + pending + ",\n" +
                " Uuid : " + uuid + "\n" +
                "}";
    }
}
