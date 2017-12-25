package bittrex.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Onsiter on 2017/12/19.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Orders extends Result {
    public Order[] orders;

    public Orders() {
    }

    public Orders(Order[] orders) {
        this.orders = orders;
    }
}