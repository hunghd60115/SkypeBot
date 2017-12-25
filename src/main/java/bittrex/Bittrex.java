package bittrex;

import bittrex.entity.*;
import bittrex.entity.deserializer.JsonDecorder;
import bittrex.entity.enums.Exchange;
import bittrex.entity.response.*;
import bittrex.exception.BittrexException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Onsiter on 2017/12/04.
 */
public class Bittrex {
    private String apiKey = "";
    private String apiSecret = "";
    private String endPointPrivate;

    public Bittrex setKey(String key, String secret) {
        this.apiKey = key;
        this.apiSecret = secret;
        return this;
    }

    public Bittrex() {
        this.endPointPrivate = "bittrex.com/api";
    }

    public String createSign(String message) throws BittrexException {
        try {
            String algo = "HmacSHA512";
            String secret = this.apiSecret;

            SecretKeySpec sk = new SecretKeySpec(secret.getBytes(), algo);
            Mac mac = Mac.getInstance(algo);
            mac.init(sk);
            byte[] macBytes = mac.doFinal(message.getBytes());

            StringBuilder sb = new StringBuilder(2 * macBytes.length);
            for(byte b: macBytes) {
                sb.append(String.format("%02x", b&0xff) );
            }
            return sb.toString();
        } catch (Exception e) {
            throw new BittrexException(e.getMessage());
        }
    }

    public String createSign2(String message) {
        Mac sha512_HMAC = null;
        String result = null;

        try{
            byte [] byteKey = this.apiSecret.getBytes("UTF-8");
            final String HMAC_SHA256 = "HmacSHA512";
            sha512_HMAC = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA256);
            sha512_HMAC.init(keySpec);
            byte [] mac_data = sha512_HMAC.
                    doFinal(message.getBytes("UTF-8"));
            //result = Base64.encode(mac_data);
            result = bytesToHex(mac_data);

        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            System.out.println("Done");
            return result;
        }
    }

    public static String bytesToHex(byte[] bytes) {
        final  char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    protected Order[] getOpenOrders() throws BittrexException, IOException {
        String path = "/v1.1/market/getopenorders";
        long nonce = System.currentTimeMillis();
        List<NameValuePair> nameValuePair = new ArrayList<>();
        nameValuePair.add(new BasicNameValuePair("nonce", String.valueOf(nonce)));
        nameValuePair.add(new BasicNameValuePair("apikey", apiKey));

        URIBuilder builder = getPrivateUriBuilder(path).setParameters(nameValuePair);
        OrderResponse result = doHttpGet(builder, OrderResponse.class, getPrivateRequestHeader(builder.toString()));
        return result.result;
    }

    protected  Balance getBalance(String currency) throws BittrexException, IOException {
        String path = "/v1.1/account/getbalance";
        long nonce = System.currentTimeMillis();
        List<NameValuePair> nameValuePair = new ArrayList<>();
        nameValuePair.add(new BasicNameValuePair("nonce", String.valueOf(nonce)));
        nameValuePair.add(new BasicNameValuePair("apikey", apiKey));
        nameValuePair.add(new BasicNameValuePair("currency", currency));

        URIBuilder builder = getPrivateUriBuilder(path).setParameters(nameValuePair);
        BalanceResponse result = doHttpGet(builder, BalanceResponse.class, getPrivateRequestHeader(builder.toString()));
        return result.result;
    }

    public Ticker getTicker(Exchange exchange) throws BittrexException, IOException {
        String path = "/v1.1/public/getticker";
        List<NameValuePair> nameValuePair = new ArrayList<>();
        nameValuePair.add(new BasicNameValuePair("market", exchange.getCode()));

        URIBuilder builder = getPrivateUriBuilder(path).setParameters(nameValuePair);
        TickerResponse result = doHttpGet(builder, TickerResponse.class, new ArrayList<>());
        return result.result;
    }

    public OrderBook getOrderBook(Exchange exchange) throws BittrexException, IOException {
        String path = "/v1.1/public/getorderbook";
        List<NameValuePair> nameValuePair = new ArrayList<>();
        nameValuePair.add(new BasicNameValuePair("market", exchange.getCode()));
        nameValuePair.add(new BasicNameValuePair("type", "both"));

        URIBuilder builder = getPrivateUriBuilder(path).setParameters(nameValuePair);
        OrderBookResponse result = doHttpGet(builder, OrderBookResponse.class, new ArrayList<>());
        return result.result;
    }

    public MarketSummary[] getMarketSummaries() throws BittrexException, IOException {
        String path = "/v1.1/public/getmarketsummaries";
        URIBuilder builder = getPrivateUriBuilder(path);
        MarketSummaryResponse result = doHttpGet(builder, MarketSummaryResponse.class, new ArrayList<>());
        return result.result;
    }

    private URIBuilder getPrivateUriBuilder(String path) {
        URIBuilder builder = new URIBuilder();
        return builder.setScheme("https")
                .setHost(this.endPointPrivate)
                .setPath(path);
    }

    private List<Header> getPrivateRequestHeader(String uri) throws BittrexException {
        return makePrivateRequestHeader(createSign(uri));
    }

    private List<Header> makePrivateRequestHeader(String sign) throws BittrexException {
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("apisign", sign));
        return headers;
    }


    private <T extends ResParent> T doHttpGet(URIBuilder builder, Class<T> clazz, List<Header> header) throws BittrexException, IOException {
        try {
            URI uri = builder.build();
            HttpGet httpGet = new HttpGet(uri);
            HttpClient client = HttpClientBuilder.create().setDefaultHeaders(header).build();
            return httpExecute(client, httpGet, clazz);
        } catch (URISyntaxException e) {
            throw new BittrexException(e.getMessage());
        }
    }

    private <T extends ResParent> T httpExecute(HttpClient client, HttpRequestBase http, Class<T> clazz) throws BittrexException, IOException {
        try {
            HttpResponse response = client.execute(http);
            HttpEntity entity = response.getEntity();
            String json = EntityUtils.toString(entity);
            //System.out.println(json);

            JsonDecorder decorder = new JsonDecorder();
            T result = decorder.decode(json, clazz);
            if (result == null) {
                throw new BittrexException("[httpExecute] Result null");
            } else {
                return result;
            }
        } catch (IOException e) {
            //System.out.println(e.getLocalizedMessage());
            throw e;
        }
    }
}
