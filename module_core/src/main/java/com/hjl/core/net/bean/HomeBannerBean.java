package com.hjl.core.net.bean;

/**
 * @author: long
 * @description HomeBannerBean
 * @Date: 2020/6/4
 */
public class HomeBannerBean {


    /**
     * desc : 享学~
     * id : 29
     * imagePath : https://wanandroid.com/blogimgs/6723ca73-bbc2-4b2a-9538-4c36df6edf56.png
     * isVisible : 1
     * order : 0
     * title : 可能是目前最全的《Android面试题及解析》（379页）
     * type : 0
     * url : https://mp.weixin.qq.com/s/XHdGR4ESrO3u84QLuoh4-w
     */

    private String desc;
    private int id;
    private String imagePath;
    private int isVisible;
    private int order;
    private String title;
    private int type;
    private String url;

    public HomeBannerBean(String url,String imagePath){
        this.url = url;
        this.imagePath = imagePath;
    }

    public HomeBannerBean() {

    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
