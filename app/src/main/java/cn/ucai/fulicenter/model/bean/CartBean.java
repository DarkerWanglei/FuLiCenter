package cn.ucai.fulicenter.model.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/9.
 */

public class CartBean implements Serializable{

    /**
     * id : 2015
     * userName : a123456
     * goodsId : 1
     * goods : null
     * count : 14
     * isChecked : false
     * checked : false
     */

    private int id;
    private String userName;
    private int goodsId;
    private GoodsDetailsBean goods;
    private int count;
    private boolean isChecked;
    private boolean checked;

    public CartBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public GoodsDetailsBean getGoods() {
        return goods;
    }

    public void setGoods(GoodsDetailsBean goods) {
        this.goods = goods;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "CartBean{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", goodsId=" + goodsId +
                ", goods=" + goods +
                ", count=" + count +
                ", isChecked=" + isChecked +
                ", checked=" + checked +
                '}';
    }
}
