package bittrex.entity.deserializer;

import bittrex.entity.enums.Exchange;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Created by Onsiter on 2017/12/19.
 */
public class ExchangeDeserializer extends JsonDeserializer<Object> {
    @Override
    public Exchange deserialize(JsonParser jp, DeserializationContext dc) throws IOException {
        final String jsonValue = jp.getText();
        for (final Exchange enumValue : Exchange.values()) {
            if (enumValue.getCode().equals(jsonValue)) {
                return enumValue;
            }
        }
        return null;
    }
}