package bittrex.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Onsiter on 2017/12/25.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Market extends Result{
    @JsonProperty("uuid")
    public String uuid;
}
