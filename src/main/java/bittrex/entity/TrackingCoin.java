package bittrex.entity;

import com.microsoft.azure.storage.table.TableServiceEntity;

import java.math.BigDecimal;

/**
 * Created by Onsiter on 2017/12/22.
 */
public class TrackingCoin extends TableServiceEntity {

    public TrackingCoin(String exchange, String SkypeID) {
        this.partitionKey = exchange;
        this.rowKey = SkypeID;
    }

    public TrackingCoin() {}

    boolean isGroup;
    String base;
    String current;
    Integer curChange;
    Integer lastChange;

    public boolean getType() {
        return isGroup;
    }

    public void setType(boolean isGroup) {
        this.isGroup = isGroup;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public Integer getCurChange() {
        return curChange;
    }

    public void setCurChange(Integer curChange) {
        this.curChange = curChange;
    }

    public Integer getLastChange() {
        return lastChange;
    }

    public void setLastChange(Integer lastChange) {
        this.lastChange = lastChange;
    }
}
