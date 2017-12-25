package bittrex.entity.response;

import bittrex.entity.Result;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Onsiter on 2017/12/19.
 */
public class Responses<T extends Result> extends ResParent {
    public boolean success;
    public String message;
    @JsonProperty("result")
    public T[] result;

    @Override
    public String toString() {
        return "[Response] success " + success + ", message " + message;
    }
}
