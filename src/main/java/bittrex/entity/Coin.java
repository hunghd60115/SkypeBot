package bittrex.entity;

import java.math.BigDecimal;

/**
 * Created by Onsiter on 2017/12/20.
 */
public class Coin {

    private BigDecimal base;
    private BigDecimal current;
    private Integer curChange;
    private Integer lastChange;

    public BigDecimal getBase() {
        return base;
    }

    public void setBase(BigDecimal base) {
        this.base = base;
    }

    public BigDecimal getCurrent() {
        return current;
    }

    public void setCurrent(BigDecimal current) {
        this.current = current;
    }

    public Integer getCurChange() {
        return curChange;
    }

    public void setcurChange(Integer curChange) {
        this.curChange = curChange;
    }

    public Integer getLastChange() {
        return lastChange;
    }

    public void setLastChange(Integer lastChange) {
        this.lastChange = lastChange;
    }

    public Coin(BigDecimal base, BigDecimal current, Integer curChange) {
        this.base = base;
        this.current = current;
        this.curChange = curChange;
    }
}
