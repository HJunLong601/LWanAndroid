package com.hjl.core.net.bean;

import java.util.List;

/**
 * Author : long
 * Description :
 * Date : 2020/9/5
 */
public class CollectItemBean {


    /**
     * curPage : 1
     * datas : [{"author":"","chapterId":530,"chapterName":"11（R）","courseId":13,"desc":"","envelopePic":"","id":151494,"link":"https://blog.csdn.net/shensky711/article/details/102677056","niceDate":"2020-08-27 10:52","origin":"","originId":14953,"publishTime":1598496720000,"title":"Android Q 深色模式（Dark Mode）源码解析","userId":73864,"visible":0,"zan":0},{"author":"","chapterId":471,"chapterName":"10.0（Q）","courseId":13,"desc":"","envelopePic":"","id":150808,"link":"https://juejin.im/post/6862633674089693197","niceDate":"2020-08-24 12:10","origin":"","originId":14880,"publishTime":1598242248000,"title":"Android 10(Q)/11(R) 分区存储适配","userId":73864,"visible":0,"zan":0},{"author":"享学","chapterId":249,"chapterName":"干货资源","courseId":13,"desc":"","envelopePic":"","id":150807,"link":"https://mp.weixin.qq.com/s/e85znnF-bVabW8TXUXuwYw","niceDate":"2020-08-24 12:10","origin":"","originId":14624,"publishTime":1598242245000,"title":"终于来了！耗时268天，7大模块、2983页58万字，Android开发核心知识笔记！对标阿里P7！","userId":73864,"visible":0,"zan":0},{"author":"","chapterId":229,"chapterName":"AOP","courseId":13,"desc":"","envelopePic":"","id":150744,"link":"https://www.jianshu.com/p/61cd84e28844","niceDate":"2020-08-24 00:44","origin":"","originId":14938,"publishTime":1598201043000,"title":"基于gradle transform的asm实践库AutoRegister源码分析","userId":73864,"visible":0,"zan":0},{"author":"","chapterId":134,"chapterName":"SurfaceView","courseId":13,"desc":"","envelopePic":"","id":150743,"link":"https://www.jianshu.com/p/5e5ae2f524ce","niceDate":"2020-08-24 00:44","origin":"","originId":14930,"publishTime":1598201042000,"title":"SurfaceView原理分析","userId":73864,"visible":0,"zan":0},{"author":"xiaoyang","chapterId":440,"chapterName":"官方","courseId":13,"desc":"<p>之前我们问过：<\/p>\r\n<p><a href=\"https://wanandroid.com/wenda/show/14738\">每日一问 Java编译器背后干了多少活 之 「内部类构造」<\/a><\/p>\r\n<p>提到了isSynthetic，<strong>注意今天的问题也是个类似的问题。<\/strong><\/p>\r\n<p>首先我们编写个接口：<\/p>\r\n<pre><code>interface Animal&lt;T&gt;{\r\n    void test(T t);\r\n}\r\n<\/code><\/pre><p>这个接口有个实现类：<\/p>\r\n<pre><code>class Dog implements Animal&lt;String&gt;{\r\n\r\n    @override\r\n    public void test(String str){\r\n    }\r\n}\r\n<\/code><\/pre><p>符合我们平时的写法对吧。<\/p>\r\n<p>但是你仔细推敲一下：<\/p>\r\n<p>接口 Animal 类的泛型，在编译成 class 后，会经历泛型擦除，会变成这样：<\/p>\r\n<pre><code>interface Animal{\r\n    void test(Object obj);\r\n}\r\n<\/code><\/pre><p>而实现类Dog里面有个方法<code>test(String str)<\/code>，注意<strong>这个方法和接口类的方法参数并不一致<\/strong>。<\/p>\r\n<p>那么也就是说，<strong>并没有实现接口中的方法。<\/strong><\/p>\r\n<p>但是，接口的方法，实现类是必须实现的。<\/p>\r\n<p>问题来了：<\/p>\r\n<ul>\r\n<li>为何不报错呢？<\/li>\r\n<li>除了这个场景，编译期间还有哪里有类似的处理方式么？(可不回答)<\/li>\r\n<\/ul>","envelopePic":"","id":150742,"link":"https://wanandroid.com/wenda/show/14941","niceDate":"2020-08-24 00:43","origin":"","originId":14941,"publishTime":1598201035000,"title":"每日一问  | Java 泛型与接口碰撞出的火花！","userId":73864,"visible":0,"zan":0},{"author":"Ruheng","chapterId":26,"chapterName":"基础UI控件","courseId":13,"desc":"详解Android图文混排实现。","envelopePic":"","id":149800,"link":"http://www.jianshu.com/p/6843f332c8df","niceDate":"2020-08-18 16:13","origin":"","originId":1165,"publishTime":1597738399000,"title":"Android图文混排实现方式详解","userId":73864,"visible":0,"zan":0},{"author":"","chapterId":502,"chapterName":"自助","courseId":13,"desc":"","envelopePic":"","id":149490,"link":"https://juejin.im/post/6857457525764620302","niceDate":"2020-08-17 10:07","origin":"","originId":14755,"publishTime":1597630028000,"title":":fire:Android进阶基础系列：Window和WindowManager ，全面理解！","userId":73864,"visible":0,"zan":0},{"author":"","chapterId":510,"chapterName":"大厂分享","courseId":13,"desc":"","envelopePic":"","id":149489,"link":"https://juejin.im/post/6860014199973871624","niceDate":"2020-08-17 10:07","origin":"","originId":14747,"publishTime":1597630027000,"title":"快手客户端稳定性体系建设","userId":73864,"visible":0,"zan":0}]
     * offset : 0
     * over : true
     * pageCount : 1
     * size : 20
     * total : 9
     */

    private int curPage;
    private int offset;
    private boolean over;
    private int pageCount;
    private int size;
    private int total;
    private List<CollectItem> datas;

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<CollectItem> getDatas() {
        return datas;
    }

    public void setDatas(List<CollectItem> datas) {
        this.datas = datas;
    }

    public static class CollectItem {
        /**
         * author :
         * chapterId : 530
         * chapterName : 11（R）
         * courseId : 13
         * desc :
         * envelopePic :
         * id : 151494
         * link : https://blog.csdn.net/shensky711/article/details/102677056
         * niceDate : 2020-08-27 10:52
         * origin :
         * originId : 14953
         * publishTime : 1598496720000
         * title : Android Q 深色模式（Dark Mode）源码解析
         * userId : 73864
         * visible : 0
         * zan : 0
         */

        private String author;
        private int chapterId;
        private String chapterName;
        private int courseId;
        private String desc;
        private String envelopePic;
        private int id;
        private String link;
        private String niceDate;
        private String origin;
        private int originId;
        private long publishTime;
        private String title;
        private int userId;
        private int visible;
        private int zan;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public int getChapterId() {
            return chapterId;
        }

        public void setChapterId(int chapterId) {
            this.chapterId = chapterId;
        }

        public String getChapterName() {
            return chapterName;
        }

        public void setChapterName(String chapterName) {
            this.chapterName = chapterName;
        }

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getEnvelopePic() {
            return envelopePic;
        }

        public void setEnvelopePic(String envelopePic) {
            this.envelopePic = envelopePic;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getNiceDate() {
            return niceDate;
        }

        public void setNiceDate(String niceDate) {
            this.niceDate = niceDate;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public int getOriginId() {
            return originId;
        }

        public void setOriginId(int originId) {
            this.originId = originId;
        }

        public long getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(long publishTime) {
            this.publishTime = publishTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getVisible() {
            return visible;
        }

        public void setVisible(int visible) {
            this.visible = visible;
        }

        public int getZan() {
            return zan;
        }

        public void setZan(int zan) {
            this.zan = zan;
        }
    }
}
