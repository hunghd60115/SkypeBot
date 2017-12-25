package bittrex.entity.deserializer;

import bittrex.entity.response.ResParent;
import bittrex.entity.response.Responses;
import bittrex.exception.BittrexException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by Onsiter on 2017/12/19.
 */
public class JsonDecorder {

    public <T extends ResParent> T decode(String json, Class<T> clazz) throws BittrexException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            T result = mapper.readValue(json, clazz);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
