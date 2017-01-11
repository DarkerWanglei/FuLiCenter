package cn.ucai.fulicenter.model.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/9.
 */

public class AlbumsBean implements Serializable{

    /**
     *
     * pid : 7672
     * imgId : 28283
     * imgUrl : 201509/goods_img/7672_P_1442389445199.jpg
     * thumbUrl : no_picture.gif
     */

    private int pid;
    private int imgId;
    private String imgUrl;
    private String thumbUrl;

    public AlbumsBean() {
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    @Override
    public String toString() {
        return "AlbumsBean{" +
                "pid=" + pid +
                ", imgId=" + imgId +
                ", imgUrl='" + imgUrl + '\'' +
                ", thumbUrl='" + thumbUrl + '\'' +
                '}';
    }
}
