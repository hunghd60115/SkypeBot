package bittrex;

import bittrex.entity.SettingInfo;
import bittrex.entity.TrackingCoin;
import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.table.*;
import com.microsoft.azure.storage.table.TableQuery.*;

import java.util.ArrayList;
import java.util.List;

/**\
 * Created by Onsiter on 2017/12/22.
 */
public class StorageHandler {

    public static final String storageConnectionString = "ConnectionString";
    static final String TABLE_COIN = "TrackingCoin";
    static final String TABLE_SETTING = "SettingInfo";

    // Define constants for filters.
    final static String PARTITION_KEY = "PartitionKey";
    final static String ROW_KEY = "RowKey";

    static CloudTable cloudTable;
    static CloudTableClient tableClient;

    public static void init() {
        try
        {
            // Retrieve storage account from connection-string.
            CloudStorageAccount storageAccount =
                    CloudStorageAccount.parse(storageConnectionString);

            // Create the table client.
            tableClient = storageAccount.createCloudTableClient();
            cloudTable = tableClient.getTableReference(TABLE_COIN);
            cloudTable.createIfNotExists();
            cloudTable = tableClient.getTableReference(TABLE_SETTING);
            cloudTable.createIfNotExists();
        }
        catch (Exception e)
        {
            // Output the stack trace.
            System.out.println("[CreateTable] Error");
            e.printStackTrace();
        }
    }

    public static void upsertCoin(TrackingCoin coin) {
        try
        {
            cloudTable = tableClient.getTableReference(TABLE_COIN);

            // Create an operation to add the new customer to the people table.
            TableOperation upsertCoin = TableOperation.insertOrReplace(coin);

            // Submit the operation to the table service.
            cloudTable.execute(upsertCoin);
        }
        catch (Exception e)
        {
            System.out.println("[upsertCoin] Error");
            e.printStackTrace();
        }
    }

    public static void removeCoin(TrackingCoin coin) {
        try
        {
            cloudTable = tableClient.getTableReference(TABLE_COIN);

            // Create an operation to retrieve the entity with partition key of "Smith" and row key of "Jeff".
            TableOperation retrieveCoin = TableOperation.retrieve(coin.getPartitionKey(), coin.getRowKey(), TrackingCoin.class);

            // Retrieve the entity with partition key of "Smith" and row key of "Jeff".
            TrackingCoin trackingCoin =
                    cloudTable.execute(retrieveCoin).getResultAsType();

            // Create an operation to delete the entity.
            TableOperation deleteCoin = TableOperation.delete(trackingCoin);

            // Submit the delete operation to the table service.
            cloudTable.execute(deleteCoin);
        }
        catch (Exception e)
        {
            System.out.println("[removeCoin] Error");
            e.printStackTrace();
        }
    }

    public static List<TrackingCoin> getByRowKey(String rowKey) {
        List<TrackingCoin> result = new ArrayList<>();
        try
        {
            cloudTable = tableClient.getTableReference(TABLE_COIN);
            // Create a filter condition where the row key is less than the letter "E".
            String rowFilter = TableQuery.generateFilterCondition(
                    ROW_KEY,
                    QueryComparisons.EQUAL,
                    rowKey);


            // Specify a range query, using "Smith" as the partition key,
            // with the row key being up to the letter "E".
            TableQuery<TrackingCoin> rangeQuery =
                    TableQuery.from(TrackingCoin.class)
                            .where(rowFilter);

            // Loop through the results, displaying information about the entity
            for (TrackingCoin entity : cloudTable.execute(rangeQuery)) {
                result.add(entity);
            }
        }
        catch (Exception e)
        {
            System.out.println("[GetByRowKey] Error");
            e.printStackTrace();
        }
        return result;
    }

    public static List<TrackingCoin> getById(String partiionKey, String rowKey) {
        List<TrackingCoin> result = new ArrayList<>();
        try
        {
            cloudTable = tableClient.getTableReference(TABLE_COIN);
            // Create a filter condition where the row key is less than the letter "E".
            String partitionFilter = TableQuery.generateFilterCondition(
                    PARTITION_KEY,
                    QueryComparisons.EQUAL,
                    partiionKey);

            // Create a filter condition where the row key is less than the letter "E".
            String rowFilter = TableQuery.generateFilterCondition(
                    ROW_KEY,
                    QueryComparisons.EQUAL,
                    rowKey);

            // Combine the two conditions into a filter expression.
            String combinedFilter = TableQuery.combineFilters(partitionFilter,
                    Operators.AND, rowFilter);

            // Specify a range query, using "Smith" as the partition key,
            // with the row key being up to the letter "E".
            TableQuery<TrackingCoin> rangeQuery =
                    TableQuery.from(TrackingCoin.class)
                            .where(combinedFilter);

            // Loop through the results, displaying information about the entity
            for (TrackingCoin entity : cloudTable.execute(rangeQuery)) {
                result.add(entity);
            }
        }
        catch (Exception e)
        {
            System.out.println("[getById] Error");
            e.printStackTrace();
        }
        return result;
    }

    public static List<TrackingCoin> getAllRecords() {
        List<TrackingCoin> result = new ArrayList<>();
        try
        {
            cloudTable = tableClient.getTableReference(TABLE_COIN);

            // Specify a range query, using "Smith" as the partition key,
            // with the row key being up to the letter "E".
            TableQuery<TrackingCoin> rangeQuery =
                    TableQuery.from(TrackingCoin.class);

            // Loop through the results, displaying information about the entity
            for (TrackingCoin entity : cloudTable.execute(rangeQuery)) {
                result.add(entity);
            }
        }
        catch (Exception e)
        {
            System.out.println("[GetByRowKey] Error");
            e.printStackTrace();
        }
        return result;
    }

    public static void upsertSetting(SettingInfo settingInfo) {
        try
        {
            cloudTable = tableClient.getTableReference(TABLE_SETTING);

            // Create an operation to add the new customer to the people table.
            TableOperation insertSetting = TableOperation.insertOrReplace(settingInfo);

            // Submit the operation to the table service.
            cloudTable.execute(insertSetting);
        }
        catch (Exception e)
        {
            System.out.println("[upsertSetting] Error");
            e.printStackTrace();
        }
    }

    public static void removeSetting(SettingInfo settingInfo) {
        try
        {
            cloudTable = tableClient.getTableReference(TABLE_SETTING);

            // Create an operation to retrieve the entity with partition key of "Smith" and row key of "Jeff".
            TableOperation retrieveCoin = TableOperation.retrieve(settingInfo.getPartitionKey(), settingInfo.getRowKey(), SettingInfo.class);

            // Retrieve the entity with partition key of "Smith" and row key of "Jeff".
            SettingInfo retrieveSetting =
                    cloudTable.execute(retrieveCoin).getResultAsType();

            // Create an operation to delete the entity.
            TableOperation deleteCoin = TableOperation.delete(retrieveSetting);

            // Submit the delete operation to the table service.
            cloudTable.execute(deleteCoin);
        }
        catch (Exception e)
        {
            System.out.println("[removeSetting] Error");
            e.printStackTrace();
        }
    }

    public static SettingInfo getSettingInfo(String id) {
        SettingInfo result = null;
        try
        {
            cloudTable = tableClient.getTableReference(TABLE_SETTING);

            // Create an operation to retrieve the entity with partition key of "Smith" and row key of "Jeff".
            TableOperation retrieveCoin = TableOperation.retrieve(id, "hehe", SettingInfo.class);

            // Retrieve the entity with partition key of "Smith" and row key of "Jeff".
            result =
                    cloudTable.execute(retrieveCoin).getResultAsType();
        }
        catch (Exception e)
        {
            System.out.println("[removeSetting] Error");
            e.printStackTrace();
        }
        return result;
    }
}