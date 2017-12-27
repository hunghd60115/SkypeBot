package bittrex;

import bittrex.entity.*;
import bittrex.entity.enums.Exchange;
import bittrex.entity.response.MarketResponse;
import fr.delthas.skype.Group;
import fr.delthas.skype.Skype;
import fr.delthas.skype.User;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by HungHD1 on 2017/12/20.
 * Handle Skype action
 */
public class SkypeHandler {
    // Group Bitification
    //private final String GROUP_ID = "a8668b3db92941a198546ed73c33ca51";
    private final String TOPIC = "Bitification - ";
    private final String HELP_MESSAGE = "1) Update, add coin - set: (coin), (baseValue)\n" +
            " Ex  set: xvg, 0.0003\n" +
            "\n" +
            "2) Remove coin from list - rm: (coin)\n" +
            " Ex  rm: xvg\n" +
            " \n" +
            "3) Get list current tracking coin�@- gcl\n" +
            " Ex  gcl\n" +
            " \n" +
            "4) Set dif (percent) - using for notification. - setdif:\n" +
            " Ex  setdif: 5\n" +
            " \n" +
            "5) Get dif (percent) - using for notification. - getdif\n" +
            " Ex  getdif\n" +
            " \n" +
            "6) Get information about coin from bittrex - get: (coin)\n" +
            " Ex   get: xvg\n" +
            "\n" +
            "7) Get list order book buy and sell - gob: (coin), (number)\n" +
            " Ex   gob: pot, 20\n" +
            "\n" +
            "8) Get sum each 10 order vol - gov: (coin), (number must chia het cho 10)\n" +
            " Ex   gov: pot, 40\n" +
            "\n" +
            "9) Get current USDT of BTC\n" +
            "  Ex:   getbtc"+
            "\n" +
            "10) Get other currency.\n" +
            "  Ex:   getother: usdt-btc";

    private final String ORDER_MESSAGE = "1) Add, edit, remove key. \n" +
            " setkey: ( key), (secrect)   Ex: setkey: xxxxx, xxxxx\n" +
            " getkey: \n" +
            " rmkey:\n" +
            "\n" +
            "2) Get Balance, Available of inputed coin. \n" +
            " gb: (coin)       Ex: gb: BTC   or gb: pot ...\n" +
            "\n" +
            "  \n" +
            "3) Buy coin. Input coin's name, price want to buy, and percent of available btc want to buy\n" +
            "  buy: (coin), (price), (percent)\n" +
            "  Ex:  buy: pot, 0.0002300, 20 \n" +
            "  \n" +
            "4) Buy coin immediately, buy with the ask price. Input coin's name and percent of available btc want to buy.\n" +
            "  buynow: (coin), (percent)\n" +
            "  Ex:   buynow: POT, 30\n" +
            "  \n" +
            "5) Sell coin. Input coin's name, price want to sell, and percent of available coin want to sell\n" +
            "  sell: (coin), (price), (percent)\n" +
            "  Ex:  sell: pot, 0.0002500, 70\n" +
            "  \n" +
            "6) sell coin immediately. Input coin's name and percent of available coin want to sell.\n" +
            "  sellnow: (coin), (percent)\n" +
            "  Ex:   sellnow: POT, 100\n" +
            "\n" +
            "7) Get order by orderID: \n" +
            "  go: (uuid)     Ex: go: 90bdf6fa-b99b-4353-abb6-34ad1fb001c8\n" +
            " \n" +
            "8) Cancel order by orderID:\n" +
            "  co: (uuid)    Ex: co: 90bdf6fa-b99b-4353-abb6-34ad1fb001c8\n" +
            " \n" +
            "9) Get all open orders. \n" +
            "  goo \n" +
            "\n" +
            "10) cancel all order (buy + sell) of one coin. \n" +
            "  cao: (coin)    Ex: cao: POT ";

    // https://www.reddit.com/r/CryptoCurrency/comments/7ecga1/how_small_an_amount_can_you_trade_on_bittrex/
    private final BigDecimal MIN_TRADE = new BigDecimal(0.0005);

    private Skype skype;
    private final BigDecimal WHALE = new BigDecimal(0.5);

    public SkypeHandler() {
        skype = new Skype("username", "password");
    }

    public void init() {
        // Listen message from group
        System.out.println("[init] Add group listener");
        skype.addGroupMessageListener(this::handleGroupMessage);

        // Listen message from user
        System.out.println("[init] Add user listener");
        skype.addUserMessageListener(this::handleUserMessage);

        try {
            System.out.println("[init] connect");
            skype.connect(); // Will block until we're connected
        } catch (IOException e) {
            System.err.println("An error occured while connecting...");
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Use for send message, send notification about coin to group.
    public void sendMsg(String message, String id, boolean isGroup) {

        if (isGroup) {
            List<Group> groups =  skype.getGroups();
            groups.stream().filter(group -> group.getId().equals(id)).forEach(group -> group.sendMessage(message));
        } else {
            List<User> users = skype.getContacts();
            users.stream().filter(user -> user.getUsername().equals(id)).forEach(user -> user.sendMessage(message));
        }
    }

    private void handleGroupMessage(Group group, User user, String message) {
//        System.out.println("[Group] GroupID: " + group.getId() + ", message: " + message);
        if (message.toLowerCase().contains("hello")) {
            group.sendMessage("Hello " + user.getUsername());
        } else if (message.toLowerCase().contains("set:")) {
            group.sendMessage(updateTrackingCoin(message, group.getId(), true));
        } else if (message.toLowerCase().contains("get:")) {
            group.sendMessage(getCoin(message));
        } else if (message.toLowerCase().contains("gcl")) {
            String listCoint = getCurrentTrackingCoin(group.getId());
            group.sendMessage(listCoint);
        } else if (message.toLowerCase().contains("rm:")) {
            group.sendMessage(removeTrackingCoin(message, group.getId()));
        } else if (message.toLowerCase().contains("setdif:")) {
            String result = setDiff(group.getId(), message);
            group.sendMessage(result);
        } else if (message.toLowerCase().contains("getdif")) {
            group.sendMessage(getDiff(group.getId()));
        } else if (message.toLowerCase().contains("gob:")) {
            group.sendMessage(getOrderBook(message));
        } else if (message.toLowerCase().contains("gov:")) {
            group.sendMessage(getVol(message));
        } else if (message.toLowerCase().contains("getother:")) {
            group.sendMessage(getOther(message));
        } else if (message.toLowerCase().contains("-help")) {
            group.sendMessage(HELP_MESSAGE);
        }
    }

    private void handleUserMessage(User user, String message) {
        if (user.getUsername().equals("hunghd60115")) {
            if (message.toLowerCase().contains("setinterval:")) {
                MainProcess.timeInterval = Integer.parseInt(message.split(":")[1].trim());
            } else if (message.toLowerCase().contains("getinterval")) {
                user.sendMessage("Interval: " + MainProcess.timeInterval);
            }
        }
        if (message.toLowerCase().contains("quit")) {
            System.out.println("[User] quit");
            if (user.getUsername().equals("hunghd60115")) {
                skype.disconnect();
            } else {
                user.sendMessage("Only Hung can do this");
            }
            MainProcess.runable = false;
        } if (message.toLowerCase().contains("hello")) {
            user.sendMessage("Hello " + user.getUsername());
        } else if (message.toLowerCase().contains("set:")) {
            user.sendMessage(updateTrackingCoin(message, user.getUsername(), false));
        } else if (message.toLowerCase().contains("get:")) {
            user.sendMessage(getCoin(message));
        } else if (message.toLowerCase().contains("gcl")) {
            String listCoint = getCurrentTrackingCoin(user.getUsername());
            user.sendMessage(listCoint);
        } else if (message.toLowerCase().contains("rm:")) {
            user.sendMessage(removeTrackingCoin(message, user.getUsername()));
        } else if (message.toLowerCase().contains("setdif:")) {
            String result = setDiff(user.getUsername(), message);
            user.sendMessage(result);
        } else if (message.toLowerCase().contains("getdif")) {
            user.sendMessage(getDiff(user.getUsername()));

        } else if (message.toLowerCase().contains("gob:")) {
            user.sendMessage(getOrderBook(message));
        } else if (message.toLowerCase().contains("gov:")) {
            user.sendMessage(getVol(message));

        } else if (message.toLowerCase().contains("key:")) {
            if (message.toLowerCase().contains("set")) {
                user.sendMessage(setKey(user.getUsername(), message));
            } else if(message.toLowerCase().contains("get")) {
                user.sendMessage(getKey(user.getUsername()));
            } else if (message.toLowerCase().contains("rm")) {
                user.sendMessage(removeKey(user.getUsername()));
            }

        } else if (message.toLowerCase().contains("gb:")) {
            user.sendMessage(getBalance(message, user.getUsername()));
        } else if (message.toLowerCase().contains("goo")) {
            user.sendMessage(getOpenOrders(user.getUsername()));
        } else if (message.toLowerCase().contains("buy:")) {
            user.sendMessage(buy(user.getUsername(), message));
        } else if (message.toLowerCase().contains("sell:")) {
            user.sendMessage(sell(user.getUsername(), message));
        } else if (message.toLowerCase().contains("sellnow:")) {
            user.sendMessage(sellNow(user.getUsername(), message));
        } else if (message.toLowerCase().contains("buynow:")) {
            user.sendMessage(buyNow(user.getUsername(), message));
        } else if (message.toLowerCase().contains("go:")) {
            user.sendMessage(getOrder(user.getUsername(), message));
        } else if (message.toLowerCase().contains("co:")) {
            user.sendMessage(cancelOrder(user.getUsername(), message));
        } else if (message.toLowerCase().contains("cao:")) {
            user.sendMessage(cancelAllOrder(user.getUsername(), message));
        } else if (message.toLowerCase().contains("getother:")) {
            user.sendMessage(getOther(message));
        } else if (message.toLowerCase().contains("-help")) {
            user.sendMessage(HELP_MESSAGE);
        } else if (message.toLowerCase().contains("-xxx")) {
            user.sendMessage(ORDER_MESSAGE);
        }
    }

    private String getCurrentTrackingCoin(String id){
        List<TrackingCoin> trackingCoinList = StorageHandler.getByRowKey(id);
        StringBuilder result = new StringBuilder("");
        Map<Exchange, Map<String, TrackingCoin>> listTrackingCoin = MainProcess.getListTrackingCoin(trackingCoinList);
        for (Map.Entry<Exchange, Map<String, TrackingCoin>> map : listTrackingCoin.entrySet()) {
            Map<String, TrackingCoin> coinMap = map.getValue();

            result.append(map.getKey()).append(": ").append(coinMap.get(id).getBase()).append(", ").append(coinMap.get(id).getCurrent())
            .append(", ").append(coinMap.get(id).getCurChange()).append("\n");
        }
        if (result.toString().equals("")) {
            return "List empty, please update by using \"set: (coin), (basevalue)\" ";
        }
        return result.toString();
    }

    private String updateTrackingCoin(String message, String id, boolean isGroup) {
        if (!isValidInput(message)) {
            return "Missing value, please input value.";
        }
        String[] info = message.split(":")[1].trim().split(",");
        if (info.length != 2) {
            return "Missing input something... " + message;
        }
        Exchange market;
        BigDecimal baseValue;
        try {
            market = Exchange.valueOf("BTC_" + info[0].trim().toUpperCase());
            baseValue = new BigDecimal(info[1].trim());
        } catch (Exception e) {
            System.out.println("[updateTrackingCoin] Error: " + e.getMessage());
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }

        // If wrong market
        if (market == null || market.getCode().equals("")) {
            return "Input wrong market: " + info[0];
        }

        //update coin
        TrackingCoin coin = new TrackingCoin(market.getCode(), id);
        coin.setBase(baseValue.toString());
        coin.setType(isGroup);
        StorageHandler.upsertCoin(coin);

        return "update success! market: " + market + ", base: " + baseValue;
    }

    private String removeTrackingCoin(String message, String id) {
        if (!isValidInput(message)) {
            return "Missing value, please input value.";
        }
        String info = message.split(":")[1].trim();
        Exchange market = null;
        try {
            market = Exchange.valueOf("BTC_" + info.toUpperCase());
        } catch (Exception e) {
            System.out.println("[updateTrackingCoin] Error: " + e.getMessage());
        }

            if (market != null) {
                TrackingCoin coin = new TrackingCoin(market.getCode(), id);
                StorageHandler.removeCoin(coin);
                return "Remove success!";
            }
        return "Remove fail! Please check again.";
    }

    // Get coin user
    private String getCoin(String message) {
        if (!isValidInput(message)) {
            return "Missing value, please input value.";
        }
        String info = message.split(":")[1].trim();
        Exchange market;
        try {
            market = Exchange.valueOf("BTC_" + info.toUpperCase());
        } catch (Exception e) {
            System.out.println("[updateTrackingCoin] Error: " + e.getMessage());
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }

        if (market == null || market.getCode().equals("")) {
            return "Market " + info + " not exist";
        }

        MarketSummary marketSummary = MainProcess.listBittrexCoin.get(market);
        return marketSummary.toString();
    }

    private String getDiff(String id) {
        SettingInfo settingInfo = StorageHandler.getSettingInfo(id);
        return settingInfo == null ? "Not set, default is " + MainProcess.DIFF : "Diff: " + settingInfo.getDiff();
    }

    private String setDiff(String id, String message) {
        if (!isValidInput(message)) {
            return "Missing value, please input value.";
        }
        String info = message.split(":")[1].trim();
        Integer diff;
        try {
            diff = Integer.parseInt(info);
        } catch (Exception e) {
            System.out.println("[setDiff] Error");
            e.printStackTrace();
            return "Input wrong: " + message ;
        }

        SettingInfo settingInfo = StorageHandler.getSettingInfo(id);
        settingInfo.setDiff(diff);
        StorageHandler.upsertSetting(settingInfo);
        return "Update diff OK";
    }

    private String getOrderBook(String message) {
        if (!isValidInput(message)) {
            return "Missing value, please input value.";
        }
        StringBuilder result = new StringBuilder("");
        String info = message.split(":")[1].trim();
        Exchange market;
        Integer number;
        try {
            market = Exchange.valueOf("BTC_" + info.split(",")[0].toUpperCase());
            number = Integer.parseInt(info.split(",")[1].trim());
        } catch (Exception e) {
            System.out.println("[getOrderBook] Error: " + e.getMessage());
            return "Exception " + e.getMessage();
        }

        if (market == null || market.getCode().equals("")) {
            return "Market " + info + " not exist";
        }

        if (number <= 0 || number >= 99) {
            return "Number invalid " + info.split(",")[1].trim();
        }

        OrderBook orderBook;
        try {
            orderBook = new Bittrex().getOrderBook(market);

            if(orderBook == null) {
                return "OrderBook null, please contact HungHD";
            }

            result.append("Bid: price, btc\n");
            for (int i = 0; i < number; i++) {
                BigDecimal price = orderBook.buys[i].rate.multiply(orderBook.buys[i].quantity);
                result.append(orderBook.buys[i].rate).append(", ").append(price);
                if (price.compareTo(WHALE) >= 0) {
                    result.append(", *WHALE*");
                }
                result.append("\n");
            }

            result.append("\nAsk: price, btc\n");
            for (int i = 0; i < number; i++) {
                BigDecimal price = orderBook.sells[i].rate.multiply(orderBook.sells[i].quantity);
                result.append(orderBook.sells[i].rate).append(", ").append(price);
                if (price.compareTo(WHALE) >= 0) {
                    result.append(", *WHALE*");
                }
                result.append("\n");
            }
        } catch (Exception e) {
            System.out.println("[getOrderBook] Error");
            e.printStackTrace();
            return "Exception " + e.getMessage();
        }
        return result.toString();
    }

    private String getVol(String message) {
        if (!isValidInput(message)) {
            return "Missing value, please input value.";
        }
        StringBuilder result = new StringBuilder("");
        String info = message.split(":")[1].trim();
        Exchange market;
        Integer number;
        try {
            market = Exchange.valueOf("BTC_" + info.split(",")[0].toUpperCase());
            number = Integer.parseInt(info.split(",")[1].trim());
        } catch (Exception e) {
            System.out.println("[getOrderBook] Error: " + e.getMessage());
            return "Exception " + e.getMessage();
        }

        if (market == null || market.getCode().equals("")) {
            return "Market " + info + " not exist";
        }
        if (number <= 0 || number >= 99) {
            return "Number invalid " + info.split(",")[1].trim();
        }

        OrderBook orderBook;
        try {
            orderBook = new Bittrex().getOrderBook(market);
        } catch (Exception e) {
            System.out.println("[getOrderBook] Error");
            e.printStackTrace();
            return "Exception " + e.getMessage();
        }

        if(orderBook == null) {
            return "OrderBook null, please contact HungHD";
        }
        BigDecimal volBid = new BigDecimal(0.0);
        BigDecimal volAsk = new BigDecimal(0.0);
        result.append("Bid: \n");
        for (int i = 0; i < number; i++) {
            volBid = volBid.add(orderBook.buys[i].rate.multiply(orderBook.buys[i].quantity));
            if ((i - 9)%10 == 0) {
                result.append("Vol rank ").append(i + 1).append(" ").append(volBid).append("\n");
            }
        }

        result.append("\nAsk: \n");
        for (int i = 0; i < number; i++) {
            volAsk = volAsk.add(orderBook.sells[i].rate.multiply(orderBook.sells[i].quantity));
            if ((i - 9)%10 == 0) {
                result.append("Vol rank ").append(i + 1).append(" ").append(volAsk).append("\n");
            }
        }

        return result.toString();
    }

    private String getKey(String id) {
        SettingInfo settingInfo = StorageHandler.getSettingInfo(id);
        if (settingInfo == null || settingInfo.getKey() == null || settingInfo.getKey().equals("")) {
            return "Key not set!";
        }
        return "Key: " + settingInfo.getKey() + ", Secrect: " + settingInfo.getSecrect();
    }

    private String setKey(String id, String message) {
        if (!isValidInput(message)) {
            return "Missing value, please input value.";
        }
        String info = message.split(":")[1].trim();
        String[] keys = info.split(",");
        if (keys.length != 2) {
            return "missing Key, secrect. Please set by ( setkey: key, secrect )";
        }
        String key = info.split(",")[0].trim();
        String secret = info.split(",")[1].trim();
        if ("".equals(key) || "".equals(secret)) {
            return "Key and secrect invalid! " + info;
        }
        SettingInfo settingInfo = StorageHandler.getSettingInfo(id);
        settingInfo.setKey(key);
        settingInfo.setSecrect(secret);
        StorageHandler.upsertSetting(settingInfo);
        return "Update Key OK";
    }

    private String removeKey(String id) {
        SettingInfo settingInfo = StorageHandler.getSettingInfo(id);
        settingInfo.setKey(null);
        settingInfo.setSecrect(null);
        StorageHandler.upsertSetting(settingInfo);
        return "Remove Key OK";
    }

    private String getBalance(String message, String id) {
        if (!isValidInput(message)) {
            return "Missing value, please input value.";
        }
        String currency = message.split(":")[1].trim();

        if (currency.equals("")) {
            return "Market " + currency + " invalid";
        }

        SettingInfo settingInfo = StorageHandler.getSettingInfo(id);
        if (!isInputKey(settingInfo)) {
            return "Please input your key/secrect";
        }

        Balance balance;
        try {
            Bittrex btx = new Bittrex();
            btx.setKey(settingInfo.getKey(), settingInfo.getSecrect());
            balance = btx.getBalance(currency);

        } catch (Exception e) {
            System.out.println("[getBalance] Error");
            e.printStackTrace();
            return "Exception " + e.getMessage();
        }

        if(balance == null) {
            return "Balance null, please contact HungHD";
        }

        return balance.toString();
    }


    private boolean isInputKey(SettingInfo settingInfo) {
        return settingInfo != null
                && !(settingInfo.getKey() == null || settingInfo.getKey().equals(""))
                && !(settingInfo.getSecrect() == null || settingInfo.getSecrect().equals(""));

    }

    private String getOpenOrders(String id) {

        SettingInfo settingInfo = StorageHandler.getSettingInfo(id);
        if (!isInputKey(settingInfo)) {
            return "Please input your key/secrect";
        }

        Order[] orders;
        try {
            Bittrex btx = new Bittrex();
            btx.setKey(settingInfo.getKey(), settingInfo.getSecrect());
            orders = btx.getOpenOrders();

        } catch (Exception e) {
            System.out.println("[getOpenOrders] Error");
            e.printStackTrace();
            return "Exception " + e.getMessage();
        }

        if(orders == null || orders.length == 0) {
            return "Orders empty";
        }

        StringBuilder result = new StringBuilder("");
        for (Order order : orders) {
            result.append(order.toString());
        }

        return result.toString();
    }

    private String buy(String id, String message) {
        if (!isValidInput(message)) {
            return "Missing value, please input value.";
        }
        StringBuilder result = new StringBuilder("");
        String info = message.split(":")[1].trim();
        Exchange market;
        BigDecimal base;
        Integer percent;
        try {
            market = Exchange.valueOf("BTC_" + info.split(",")[0].toUpperCase());
            base = new BigDecimal(info.split(",")[1].trim());
            percent = Integer.parseInt(info.split(",")[2].trim());
        } catch (Exception e) {
            System.out.println("[getOrderBook] Error: " + e.getMessage());
            return "Exception " + e.getMessage();
        }

        if (market == null || market.getCode().equals("")) {
            return "Market " + info + " not exist";
        }
        if (percent <= 0 || percent > 100) {
            return "Percent invalid " + info.split(",")[2].trim();
        }

        SettingInfo settingInfo = StorageHandler.getSettingInfo(id);
        if (!isInputKey(settingInfo)) {
            return "Please input your key/secrect";
        }

        Balance balance;
        Bittrex btx;
        try {
            btx = new Bittrex();
            btx.setKey(settingInfo.getKey(), settingInfo.getSecrect());
            balance = btx.getBalance("BTC");
        } catch (Exception e) {
            System.out.println("[Buy] get balance  Error");
            e.printStackTrace();
            return "Exception " + e.getMessage();
        }

        if (balance == null || balance.available == null || balance.available.equals("")) {
            return "Don't have available btc";
        }

        BigDecimal available;
        if (percent == 100) {
            available = new BigDecimal(balance.available);
        } else {
            available = new BigDecimal(balance.available).multiply(new BigDecimal(percent)).divide(BigDecimal.TEN.multiply(BigDecimal.TEN), 5, RoundingMode.HALF_UP);
        }

        if (available.compareTo(MIN_TRADE) <= 0) {
            return "Minimum trade is 0.0005 btc. Your available is " + balance.available + ", " + percent + "% is " + available;
        }

        BigDecimal quantity = available.divide(base, 3, RoundingMode.HALF_UP);

        MarketResponse buyResut;
        try {
            buyResut = btx.buyLimit(market, quantity, base);
        } catch (Exception e) {
            System.out.println("[Buy] Buy  Error");
            e.printStackTrace();
            return "Exception " + e.getMessage();
        }

        if (buyResut == null || !buyResut.success) {
            return "Buy fail: " + (buyResut == null ? "" : buyResut.message);
        }

        result.append("Buy ").append(market.getCode()).append(" OK!\n Uuid: ")
                .append(buyResut.result.uuid);

        return result.toString();
    }

    private String sell(String id, String message) {
        if (!isValidInput(message)) {
            return "Missing value, please input value.";
        }
        StringBuilder result = new StringBuilder("");
        String info = message.split(":")[1].trim();
        Exchange market;
        BigDecimal sellPrice;
        Integer percent;
        try {
            market = Exchange.valueOf("BTC_" + info.split(",")[0].toUpperCase());
            sellPrice = new BigDecimal(info.split(",")[1].trim());
            percent = Integer.parseInt(info.split(",")[2].trim());
        } catch (Exception e) {
            System.out.println("[sell] Error: " + e.getMessage());
            return "Exception " + e.getMessage();
        }

        if (market == null || market.getCode().equals("")) {
            return "Market " + info + " not exist";
        }
        if (percent <= 0 || percent > 100) {
            return "Percent invalid " + info.split(",")[2].trim();
        }

        SettingInfo settingInfo = StorageHandler.getSettingInfo(id);
        if (!isInputKey(settingInfo)) {
            return "Please input your key/secrect";
        }

        Balance balance;
        Bittrex btx;
        try {
            btx = new Bittrex();
            btx.setKey(settingInfo.getKey(), settingInfo.getSecrect());
            balance = btx.getBalance(info.split(",")[0].toUpperCase());
        } catch (Exception e) {
            System.out.println("[Sell] get balance  Error");
            e.printStackTrace();
            return "Exception " + e.getMessage();
        }

        if (balance == null || balance.available == null || balance.available.equals("")) {
            return "Don't have available of " + market.getCode();
        }
        BigDecimal quantity;
        if (percent == 100) {
            quantity = new BigDecimal(balance.available);
        } else {
            quantity = new BigDecimal(balance.available).multiply(new BigDecimal(percent)).divide(BigDecimal.TEN.multiply(BigDecimal.TEN), 5, RoundingMode.HALF_UP);
        }
        MarketResponse sellResut;
        try {
            sellResut = btx.sellLimit(market, quantity, sellPrice);
        } catch (Exception e) {
            System.out.println("[Sell] Sell  Error");
            e.printStackTrace();
            return "Exception " + e.getMessage();
        }

        if (sellResut == null || !sellResut.success) {
            return "Sell fail: " + (sellResut == null ? "" : sellResut.message);
        }

        result.append("Sell ").append(market.getCode()).append(" OK!\n Uuid: ")
                .append(sellResut.result.uuid);

        return result.toString();
    }

    private String sellNow(String id, String message) {
        if (!isValidInput(message)) {
            return "Missing value, please input value.";
        }
        StringBuilder result = new StringBuilder("");
        String info = message.split(":")[1].trim();
        Exchange market;
        Integer percent;
        try {
            market = Exchange.valueOf("BTC_" + info.split(",")[0].toUpperCase());
            percent = Integer.parseInt(info.split(",")[1].trim());
        } catch (Exception e) {
            System.out.println("[sellNow] Error: " + e.getMessage());
            return "Exception " + e.getMessage();
        }

        if (market == null || market.getCode().equals("")) {
            return "Market " + info + " not exist";
        }
        if (percent <= 0 || percent > 100) {
            return "Percent invalid " + info.split(",")[1].trim();
        }

        SettingInfo settingInfo = StorageHandler.getSettingInfo(id);
        if (!isInputKey(settingInfo)) {
            return "Please input your key/secrect";
        }

        Balance balance;
        Bittrex btx;
        try {
            btx = new Bittrex();
            btx.setKey(settingInfo.getKey(), settingInfo.getSecrect());
            balance = btx.getBalance(info.split(",")[0].toUpperCase());
        } catch (Exception e) {
            System.out.println("[Sellnow] get balance  Error");
            e.printStackTrace();
            return "Exception " + e.getMessage();
        }

        if (balance == null || balance.available == null || balance.available.equals("")) {
            return "Don't have available of " + market.getCode();
        }

        BigDecimal quantity;
        if (percent == 100) {
            quantity = new BigDecimal(balance.available);
        } else {
            quantity = new BigDecimal(balance.available).multiply(new BigDecimal(percent)).divide(BigDecimal.TEN.multiply(BigDecimal.TEN), 5, RoundingMode.HALF_UP);
        }
        MarketResponse sellResut;
        try {
            sellResut = btx.sellLimit(market, quantity, MainProcess.listBittrexCoin.get(market).low);
        } catch (Exception e) {
            System.out.println("[Sellnow] Sell  Error");
            e.printStackTrace();
            return "Exception " + e.getMessage();
        }

        if (sellResut == null || !sellResut.success) {
            return "Sellnow fail: " + (sellResut == null ? "" : sellResut.message);
        }

        Order order;
        try {
            order = btx.getOrder(sellResut.result.uuid);
        } catch (Exception e) {
            System.out.println("[Sellnow] Get order error, uuid " + sellResut.result.uuid);
            e.printStackTrace();
            return "Get order error, uuid " + sellResut.result.uuid + e.getMessage();
        }

        result.append("Sell ").append(market.getCode()).append(" OK!\n Uuid: ")
                .append(sellResut.result.uuid).append("\n Actual sell price: ").append(order.pricePerUnit.toString());

        return result.toString();
    }

    private String buyNow(String id, String message) {
        if (!isValidInput(message)) {
            return "Missing value, please input value.";
        }
        StringBuilder result = new StringBuilder("");
        String info = message.split(":")[1].trim();
        Exchange market;
        Integer percent;
        try {
            market = Exchange.valueOf("BTC_" + info.split(",")[0].toUpperCase());
            percent = Integer.parseInt(info.split(",")[1].trim());
        } catch (Exception e) {
            System.out.println("[buyNow] Error: " + e.getMessage());
            return "Exception " + e.getMessage();
        }

        if (market == null || market.getCode().equals("")) {
            return "Market " + info + " not exist";
        }
        if (percent <= 0 || percent > 100) {
            return "Percent invalid " + info.split(",")[1].trim();
        }

        SettingInfo settingInfo = StorageHandler.getSettingInfo(id);
        if (!isInputKey(settingInfo)) {
            return "Please input your key/secrect";
        }

        Balance balance;
        Bittrex btx;
        try {
            btx = new Bittrex();
            btx.setKey(settingInfo.getKey(), settingInfo.getSecrect());
            balance = btx.getBalance("BTC");
        } catch (Exception e) {
            System.out.println("[buyNow] get balance  Error");
            e.printStackTrace();
            return "Exception " + e.getMessage();
        }

        if (balance == null || balance.available == null || balance.available.equals("")) {
            return "Don't have available of " + market.getCode();
        }

        BigDecimal available;
        if (percent == 100) {
            available = new BigDecimal(balance.available);
        } else {
            available = new BigDecimal(balance.available).multiply(new BigDecimal(percent)).divide(BigDecimal.TEN.multiply(BigDecimal.TEN), 5, RoundingMode.HALF_UP);
        }
        BigDecimal quantity = available.divide(MainProcess.listBittrexCoin.get(market).high, 3, RoundingMode.HALF_UP);

        MarketResponse buyResut;
        try {
            buyResut = btx.buyLimit(market, quantity, MainProcess.listBittrexCoin.get(market).high);
        } catch (Exception e) {
            System.out.println("[buyNow] Buy  Error");
            e.printStackTrace();
            return "Exception " + e.getMessage();
        }

        if (buyResut == null || !buyResut.success) {
            return "buyNow fail: " + (buyResut == null ? "" : buyResut.message);
        }

        Order order;
        try {
            order = btx.getOrder(buyResut.result.uuid);
        } catch (Exception e) {
            System.out.println("[BuyNow] Get order error, uuid " + buyResut.result.uuid);
            e.printStackTrace();
            return "Get order error, uuid " + buyResut.result.uuid + e.getMessage();
        }

        result.append("Buy ").append(market.getCode()).append(" OK!\n Uuid: ")
                .append(buyResut.result.uuid).append("\n Actual buy price: ").append(order.pricePerUnit.toString());

        return result.toString();
    }

    private String cancelOrder(String id, String message) {
        if (!isValidInput(message)) {
            return "Missing value, please input value.";
        }
        String uuid = message.split(":")[1].trim();


        if (uuid.equals("")) {
            return "uuid " + uuid + " not exist";
        }

        SettingInfo settingInfo = StorageHandler.getSettingInfo(id);
        if (!isInputKey(settingInfo)) {
            return "Please input your key/secrect";
        }

        MarketResponse response;
        Bittrex btx;
        try {
            btx = new Bittrex();
            btx.setKey(settingInfo.getKey(), settingInfo.getSecrect());
            response = btx.cancelOrder(uuid);
        } catch (Exception e) {
            System.out.println("[CancelOrder] Error");
            e.printStackTrace();
            return "Exception " + e.getMessage();
        }

        if(!response.success) {
            return "Cancel order fail, uuid: " + uuid + ", message: " + response.message;
        }

        return "Cancel order " + uuid + " success!";
    }

    private String cancelAllOrder(String id, String message) {
        if (!isValidInput(message)) {
            return "Missing value, please input value.";
        }
        String info = message.split(":")[1].trim();
        Exchange market;
        try {
            market = Exchange.valueOf("BTC_" + info.toUpperCase());
        } catch (Exception e) {
            System.out.println("[cancelAllOrder] Error: " + e.getMessage());
            return "Exception " + e.getMessage();
        }

        if (market == null || market.getCode().equals("")) {
            return "Market " + info + " not exist";
        }

        SettingInfo settingInfo = StorageHandler.getSettingInfo(id);
        if (!isInputKey(settingInfo)) {
            return "Please input your key/secrect";
        }

        List<MarketResponse> response = new ArrayList<>();
        Bittrex btx;
        try {
            btx = new Bittrex();
            btx.setKey(settingInfo.getKey(), settingInfo.getSecrect());
            Order[] orders = btx.getOpenOrders();
            if (orders == null || orders.length == 0) {
                return "Market " + market.getCode() + " don't have any order";
            }
            for (Order order : orders) {
                if (order.exchange.equals(market)) {
                    response.add(btx.cancelOrder(order.orderUuid));
                }
            }

        } catch (Exception e) {
            System.out.println("[cancelAllOrder] Error");
            e.printStackTrace();
            return "Exception " + e.getMessage();
        }

        List<String> fail = response.stream().filter(balance -> !balance.success).map(balance -> balance.message).collect(Collectors.toList());

        if (fail.size() != 0) {
            return "Cancel " + fail.size() + " order fail! Message: " + fail.get(0);
        }

        return "Cancel order success!";
    }

    private String getOrder(String id, String message) {
        if (!isValidInput(message)) {
            return "Missing value, please input value.";
        }
        String uuid = message.split(":")[1].trim();

        if (uuid.equals("")) {
            return "uuid " + uuid + " not exist";
        }

        SettingInfo settingInfo = StorageHandler.getSettingInfo(id);
        if (!isInputKey(settingInfo)) {
            return "Please input your key/secrect";
        }

        Order response;
        Bittrex btx;
        try {
            btx = new Bittrex();
            btx.setKey(settingInfo.getKey(), settingInfo.getSecrect());
            response = btx.getOrder(uuid);
        } catch (Exception e) {
            System.out.println("[getOrder] Error");
            e.printStackTrace();
            return "Exception " + e.getMessage();
        }


        return response.toString();
    }

    private boolean isValidInput(String message) {
        String[] info = message.split(":");
        return info.length == 2;
    }

    private String getOther(String message) {
        if (!isValidInput(message)) {
            return "Missing value, please input value.";
        }
        String[] info = message.split("-");
        if (info.length != 2) {
            return "Missing - between market";
        }
        MarketSummary[] marketSummary;
        try {
            marketSummary = new Bittrex().getMarketSummary(message.split(":")[1].trim().toUpperCase());
        } catch (Exception e) {
            System.out.println("[getOther] Error");
            e.printStackTrace();
            return "[getOther] Exception " + e.getMessage();

        }

        if (marketSummary == null || marketSummary.length == 0) {
            return "Market empty";
        }
        StringBuilder result = new StringBuilder("");
        for (MarketSummary mrk : marketSummary) {
            result.append(mrk.toString()).append("\n");
        }
        return result.toString();
    }
}
