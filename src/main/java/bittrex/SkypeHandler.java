package bittrex;

import bittrex.entity.*;
import bittrex.entity.enums.Exchange;
import fr.delthas.skype.Group;
import fr.delthas.skype.Skype;
import fr.delthas.skype.User;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by HungHD1 on 2017/12/20.
 * Handle Skype action
 */
public class SkypeHandler {
    // Group Bitification
    private final String GROUP_ID = "a8668b3db92941a198546ed73c33ca51";
    private final String TOPIC = "Bitification - ";
    private final String HELP_MESSAGE = "1) Update, add coin - set: (coin), (baseValue)\n" +
            " Ex  set: xvg, 0.0003\n" +
            "\n" +
            "2) Remove coin from list - rm: (coin)\n" +
            " Ex  rm: xvg\n" +
            " \n" +
            "3) Get list current tracking coin@- gcl\n" +
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
            " Ex   gov: pot, 40";
    private Skype skype;
    private final BigDecimal WHALE = new BigDecimal(0.5);

    public SkypeHandler() {
        skype = new Skype("xxxxx@gmail.com", "xxxxx");
    }

    public void init() {
        // Listen message from group
        System.out.println("[init] Add group listener");
        skype.addGroupMessageListener((group, user, message) -> {
            handleGroupMessage(group, user, message);
        });

        // Listen message from user
        System.out.println("[init] Add user listener");
        skype.addUserMessageListener(((user, message) -> {
            handleUserMessage(user, message);
        }));

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
            groups.stream().filter(group -> group.getId().equals(id)).forEach(group -> {
                group.sendMessage(message);
            });
        } else {
            List<User> users = skype.getContacts();
            users.stream().filter(user -> user.getUsername().equals(id)).forEach(user -> {
                user.sendMessage(message);
            });
        }
    }

    public void changeGroupTopic(String topicName) {
        List<Group> groups =  skype.getGroups();
        groups.stream().filter(group -> group.getId().equals(GROUP_ID)).forEach(group -> {
            group.changeTopic(TOPIC + topicName);
        });
    }

    private void handleGroupMessage(Group group, User user, String message) {
        System.out.println("[Group] GroupID: " + group.getId() + ", message: " + message);
        if (message.toLowerCase().contains("hello")) {
            group.sendMessage("Hello " + user.getUsername());
        } else if (message.toLowerCase().contains("set:")) {
            updateTrackingCoin(group, user, message, group.getId(), true);
        } else if (message.toLowerCase().contains("get:")) {
            getCoin(group, user, message, true);
        } else if (message.toLowerCase().contains("gcl")) {
            String listCoint = getCurrentTrackingCoin(group.getId());
            group.sendMessage(listCoint);
        } else if (message.toLowerCase().contains("rm:")) {
            removeTrackingCoin(message, group.getId());
            group.sendMessage("Remove success");
        } else if (message.toLowerCase().contains("setdif:")) {
            String result = setDiff(group.getId(), message);
            group.sendMessage(result);
        } else if (message.toLowerCase().contains("getdif")) {
            group.sendMessage(getDiff(group.getId()));
        } else if (message.toLowerCase().contains("gob:")) {
            group.sendMessage(getOrderBook(message));
        } else if (message.toLowerCase().contains("gov:")) {
            group.sendMessage(getVol(message));
        } else if (message.toLowerCase().contains("-help")) {
            group.sendMessage(HELP_MESSAGE);
        }
    }

    private void handleUserMessage(User user, String message) {
        System.out.println("[User] Id: " + user.getUsername() + ", message:" + message);
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
            updateTrackingCoin(null, user, message, user.getUsername(), false);
        } else if (message.toLowerCase().contains("get:")) {
            getCoin(null, user, message, false);
        } else if (message.toLowerCase().contains("gcl")) {
            String listCoint = getCurrentTrackingCoin(user.getUsername());
            user.sendMessage(listCoint);
        } else if (message.toLowerCase().contains("rm:")) {
            removeTrackingCoin(message, user.getUsername());
            user.sendMessage("Remove success");

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

        } else if (message.toLowerCase().contains("-help")) {
            user.sendMessage(HELP_MESSAGE);
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

    private void updateTrackingCoin(Group group, User user, String message, String id, boolean isGroup) {
        String info = message.split(":")[1].trim();
        Exchange market = null;
        BigDecimal baseValue = null;
        try {
            market = Exchange.valueOf("BTC_" + info.split(",")[0].trim().toUpperCase());
            baseValue = new BigDecimal(info.split(",")[1].trim());
        } catch (Exception e) {
            System.out.println("[updateTrackingCoin] Error: " + e.getMessage());
        }

        // If wrong market
        if (market == null || market.getCode().equals("")) {
            if (isGroup) {
                group.sendMessage(user.getUsername() + " input wrong market: " + info.split(",")[0]);
            } else {
                user.sendMessage(user.getUsername() + " input wrong market: " + info.split(",")[0]);
            }
        } else if (baseValue == null) {
            if (isGroup) {
                group.sendMessage(user.getUsername() + " input wrong base value: " + info.split(",")[1]);
            } else {
                user.sendMessage(user.getUsername() + " input wrong base value: " + info.split(",")[1]);
            }
        } else {

            //update coin
            TrackingCoin coin = new TrackingCoin(market.getCode(), id);
            coin.setBase(baseValue.toString());
            coin.setType(isGroup);
            StorageHandler.upsertCoin(coin);

            if (isGroup) {
                group.sendMessage("Hi " + user.getUsername() + ", update success! market: " + market + ", base: " + baseValue);
            } else {
                user.sendMessage("Hi " + user.getUsername() + ", update success! market: " + market + ", base: " + baseValue);
            }
        }
    }

    private void removeTrackingCoin(String message, String id) {
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
            }
    }

    // Get coin user
    private void getCoin(Group group, User user, String message, boolean isGroup) {
        String info = message.split(":")[1].trim();
        Exchange market = null;
        try {
            market = Exchange.valueOf("BTC_" + info.toUpperCase());
        } catch (Exception e) {
            System.out.println("[updateTrackingCoin] Error: " + e.getMessage());
        }

        if (market == null || market.getCode().equals("")) {
            if (isGroup) {
                group.sendMessage("Hi " + user.getUsername() + "! " + "Market " + info + " not exist");
            } else {
                user.sendMessage("Hi " + user.getUsername() + "! " + "Market " + info + " not exist");
            }
            return;
        }

        MarketSummary marketSummary = MainProcess.listBittrexCoin.get(market);
        if (isGroup) {
            group.sendMessage("Hi " + user.getUsername() + "! " + marketSummary.toString());
        } else {
            user.sendMessage("Hi " + user.getUsername() + "! " + marketSummary.toString());
        }
    }

    private String getDiff(String id) {
        SettingInfo settingInfo = StorageHandler.getSettingInfo(id);
        return settingInfo == null ? "Not set, default is " + MainProcess.DIFF : "Diff: " + settingInfo.getDiff();
    }

    private String setDiff(String id, String message) {
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
        String info = message.split(":")[1].trim();
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
}
