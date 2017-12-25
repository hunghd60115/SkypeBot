package bittrex.entity;

import com.microsoft.azure.storage.table.TableServiceEntity;

/**
 * Created by Onsiter on 2017/12/22.
 */
public class SettingInfo extends TableServiceEntity {

    public SettingInfo(String exchange) {
        this.partitionKey = exchange;
        this.rowKey = "hehe";
    }

    public SettingInfo() {}

    Integer diff;
    String key;
    String secrect;
    boolean isGroup;

    public Integer getDiff() {
        return diff;
    }

    public void setDiff(Integer diff) {
        this.diff = diff;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSecrect() {
        return secrect;
    }

    public void setSecrect(String secrect) {
        this.secrect = secrect;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setIsGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }
}
