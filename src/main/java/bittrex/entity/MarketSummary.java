package bittrex.entity;

import bittrex.entity.enums.Exchange;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Onsiter on 2017/12/20.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketSummary extends Result {

    @JsonProperty("MarketName")
    public Exchange marketName;

    @JsonProperty("High")
    public BigDecimal high;

    @JsonProperty("Low")
    public BigDecimal low;

    @JsonProperty("Volume")
    public BigDecimal volume;

    @JsonProperty("Last")
    public BigDecimal last;

    @JsonProperty("BaseVolume")
    public BigDecimal baseVolume;

    @JsonProperty("TimeStamp")
    public Date timeStamp;

    @JsonProperty("PrevDay")
    public BigDecimal prevDay;

    public String toString() {
        return "[MarketSummary] MarketName " + marketName.getCode() + "\nHigh " + high + "\nLow " + low + "\nLast " + last +
                "\nTimeStamp " + timeStamp + "\nBaseVolume " + baseVolume +"\nPrevDay " + prevDay;
    }
}
