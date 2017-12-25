package bittrex;

import bittrex.entity.MarketSummary;
import bittrex.entity.SettingInfo;
import bittrex.entity.TrackingCoin;
import bittrex.entity.enums.Exchange;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Onsiter on 2017/12/20.
 */
public class MainProcess {
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
    private static SkypeHandler skypeHandler;
    private static Bittrex bittrex;
    public static Map<Exchange, MarketSummary> listBittrexCoin;
    public static int DIFF = 5;
    public static int timeInterval = 60000;
    public static boolean runable = true;

    public static void main(String[] args) {
        // Init (Skype, mem, bittrex)
        init();

        // get current price each 30s


        Runnable runnable = () -> {
            while (runable) {
//                System.out.println("[Main] Update Bittrex coin");
                updateBittrexCoin();

                List<TrackingCoin> trackingCoinList = StorageHandler.getAllRecords();
//                System.out.println("[Main] Sync list tracking coin");
                Map<Exchange, Map<String, TrackingCoin>> listTrackingCoin = getListTrackingCoin(trackingCoinList);

                // Update change for list tracking coin
//                System.out.println("[Main] Update change");
                updateChange(listTrackingCoin);
                try {
                    Thread.sleep(timeInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

    }

    private static void init () {

        // Init Bittrex
        bittrex = new Bittrex();
        listBittrexCoin = new HashMap<>();

        StorageHandler.init();

        // Init Skype
        skypeHandler = new SkypeHandler();
        skypeHandler.init();
    }

    private static void updateBittrexCoin() {
        MarketSummary[] listMarket = null;
        try {
            listMarket = bittrex.getMarketSummaries();
        } catch (Exception e) {
            System.out.println("[updateBittrexCoin] Exception: " + e.getMessage());
            e.printStackTrace();
        }

        // parse to map If list market have record
        if (listMarket != null && listMarket.length != 0) {
            for (MarketSummary market:listMarket) {
                if (market.marketName != null && !market.marketName.getCode().equals("")){
                    listBittrexCoin.put(market.marketName, market);
                }
            }
        }
    }

    public static Map<Exchange, Map<String, TrackingCoin>> getListTrackingCoin(List<TrackingCoin> trackingCoinList) {

        Map<Exchange, Map<String, TrackingCoin>> result = new HashMap<>();
        for(TrackingCoin coin : trackingCoinList) {

            Map<String, TrackingCoin> map = result.get(Exchange.valueOf(coin.getPartitionKey().replace("-", "_")));
            if (map == null || map.size() == 0) {
                map = new HashMap<>();
            }
            map.put(coin.getRowKey(), coin);
            result.put(Exchange.valueOf(coin.getPartitionKey().replace("-", "_")), map);
        }
        return result;
    }

    public static void updateChange(Map<Exchange, Map<String, TrackingCoin>> listTrackingCoin) {
        if (listTrackingCoin.size() == 0 || listBittrexCoin.size() == 0) {
            System.out.println("[updateChange] List tracking coin empty!");
            return;
        }
//        System.out.println("Update list coin");
        for (Map.Entry<Exchange, Map<String, TrackingCoin>> map : listTrackingCoin.entrySet()) {
            // Get exchange (market), coin from iist tracking coin
            Exchange exchange = map.getKey();
            Map<String, TrackingCoin> coinMap = map.getValue();
            for (Map.Entry<String, TrackingCoin> coinEntry : coinMap.entrySet()) {
                String id = coinEntry.getKey();
//                System.out.println("Update: " + exchange);
                TrackingCoin coin = coinEntry.getValue();

                // Get current marketSUmmary from bittrex
                MarketSummary marketSummary = listBittrexCoin.get(exchange);

                // Get base, current and calculate change, round up 2
                BigDecimal baseValue = new BigDecimal(coin.getBase());
                BigDecimal currentValue = marketSummary.last;
                Integer curChange = (((currentValue.subtract(baseValue)).divide(baseValue, 2, RoundingMode.HALF_UP)).multiply(ONE_HUNDRED)).intValue();
                Integer lastChange = coin.getLastChange();
                coin.setCurrent(currentValue.toString());
                coin.setCurChange(curChange);


                // If last change != null and = current change => same change, don't need to notify
                if (lastChange != null && curChange.equals(lastChange)) {
//                    System.out.println("Change the same");
                } else {
//                    System.out.println("Change dif: " + curChange);
                    // else notify if greater than DIFF
                    SettingInfo settingInfo = StorageHandler.getSettingInfo(id);
                    Integer diff = settingInfo == null ? DIFF:settingInfo.getDiff() ;
                    if (curChange % diff == 0) {
                        // Set last change = current Change if have change.
                        coin.setLastChange(curChange);
                        StringBuilder msg = new StringBuilder("Coin ");
                        msg.append(exchange).append(" has change ").append(curChange).append("% ")
                                .append("\nBase: ").append(baseValue)
                                .append("\nCurrent: ").append(currentValue)
                                .append("\nUpdated: ").append(marketSummary.timeStamp);

                        skypeHandler.sendMsg(msg.toString(), id, coin.getType());
                    }
                }
                StorageHandler.upsertCoin(coin);
            }
        }

    }

}
