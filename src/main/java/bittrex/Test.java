package bittrex;

import bittrex.entity.Buy;
import bittrex.entity.Order;
import bittrex.entity.OrderBook;
import bittrex.entity.Ticker;
import bittrex.entity.enums.Exchange;
import bittrex.exception.BittrexException;
import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;
import fr.delthas.skype.Skype;

import java.io.IOException;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Onsiter on 2017/12/04.
 */
public class Test {

    static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    public static void main(String[] args) throws Exception {

        Bittrex btx = new Bittrex();

        System.out.println(btx.getBalance("POT").toString());

    }

    public static void memc() throws IOException, BittrexException {
        //initialize the SockIOPool that maintains the Memcached Server Connection Pool
        String[] servers = {"localhost:11211"};
        SockIOPool pool = SockIOPool.getInstance("Test1");
        pool.setServers( servers );
        pool.setFailover( true );
        pool.setInitConn( 10 );
        pool.setMinConn( 5 );
        pool.setMaxConn( 250 );
        pool.setMaintSleep( 30 );
        pool.setNagle( false );
        pool.setSocketTO( 3000 );
        pool.setAliveCheck( true );
        pool.initialize();
        //Get the Memcached Client from SockIOPool named Test1
        MemCachedClient mcc = new MemCachedClient("Test1");
        //Get value from cache
        System.out.println("Get from Cache:"+mcc.get(Exchange.BTC_ADA.getCode()));
        //add some value in cache
        System.out.println("add status:"+mcc.add(Exchange.BTC_ADA.getCode(), "test"));
        //Get value from cache
        System.out.println("Get from Cache:"+mcc.get(Exchange.BTC_ADA.getCode()));
    }

    public static void bitt() throws IOException, BittrexException {

        Bittrex btx = new Bittrex();

        OrderBook orderBook = btx.getOrderBook(Exchange.BTC_POWR);
        StringBuilder buyString = new StringBuilder("Bid: price, btc\n");
        for (int i = 0; i < 10; i++) {
            buyString.append(orderBook.buys[i].rate).append(", ").append(orderBook.buys[i].rate.multiply(orderBook.buys[i].quantity))
                    .append("\n");
        }

        StringBuilder sellString = new StringBuilder("Ask: price, btc\n");
        for (int i = 0; i < 10; i++) {
            sellString.append(orderBook.sells[i].rate).append(", ").append(orderBook.sells[i].rate.multiply(orderBook.sells[i].quantity))
                    .append("\n");
        }
        System.out.println(buyString);
        System.out.println(sellString);
    }


}
