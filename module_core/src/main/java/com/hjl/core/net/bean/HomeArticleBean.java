package com.hjl.core.net.bean;

import java.util.List;

/**
 * Author : long
 * Description : HomeFragment recommend Article Bean
 * Date : 2020/6/10
 */
public class HomeArticleBean {


    /**
     * curPage : 2
     * datas : [{"apkLink":"","audit":1,"author":"鸿洋","canEdit":false,"chapterId":408,"chapterName":"鸿洋","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":13813,"link":"https://mp.weixin.qq.com/s/jVRTFTiwTtr7P7vyAj8G7A","niceDate":"2天前","niceShareDate":"1天前","origin":"","prefix":"","projectLink":"","publishTime":1591545600000,"selfVisible":0,"shareDate":1591627043000,"shareUser":"","superChapterId":408,"superChapterName":"公众号","tags":[{"name":"公众号","url":"/wxarticle/list/408/1"}],"title":"Android避坑指南，发现了一个极度不安全的操作","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"郭霖","canEdit":false,"chapterId":409,"chapterName":"郭霖","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":13814,"link":"https://mp.weixin.qq.com/s/BJs6GYx6-CeeMKi0OkdCgg","niceDate":"2天前","niceShareDate":"1天前","origin":"","prefix":"","projectLink":"","publishTime":1591545600000,"selfVisible":0,"shareDate":1591627057000,"shareUser":"","superChapterId":408,"superChapterName":"公众号","tags":[{"name":"公众号","url":"/wxarticle/list/409/1"}],"title":"一个Android程序至少包含几个线程？","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"","canEdit":false,"chapterId":178,"chapterName":"apk安装","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":13799,"link":"https://juejin.im/post/5edb7196f265da76dc1bc12d","niceDate":"2020-06-07 20:56","niceShareDate":"2020-06-06 22:15","origin":"","prefix":"","projectLink":"","publishTime":1591534605000,"selfVisible":0,"shareDate":1591452922000,"shareUser":"Zaylour","superChapterId":173,"superChapterName":"framework","tags":[],"title":"PKMS的作用--《源码系列》","type":0,"userId":9778,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"xiaoyang","canEdit":false,"chapterId":440,"chapterName":"官方","collect":false,"courseId":13,"desc":"<p>在之前的问答中：<\/p>\r\n<p><a href=\"https://wanandroid.com/wenda/show/12424\">每日一问 ViewPager 这个流传广泛的写法，其实是有问题的！<\/a><\/p>\r\n<p>我们指出了一个ViewPager 的错误写法，提到了根本原因是 Fragment存在恢复机制。<\/p>\r\n<p>那么我们继续深入讨论一下：<\/p>\r\n<ol>\r\n<li>一般情况下，我们讨论 Activity重建 Fragment恢复，都是以 Activity 旋转距离，其实还有个 case，就是进程由于内存不足被杀死，返回这个 app，Activity 也会被重建，这种情况下Fragment 也会被恢复吗（这个可以通过 app 授权一个相机权限，然后打开某个 activity，再去设置页关闭相机权限，切回 app ，就能模拟进程杀死activity 重建）？<\/li>\r\n<li>Fragment 恢复是真的和重建前使用的是同一个对象吗？<\/li>\r\n<li>是如何做到恢复的？<\/li>\r\n<\/ol>","descMd":"","envelopePic":"","fresh":false,"id":12574,"link":"https://www.wanandroid.com/wenda/show/12574","niceDate":"2020-06-07 09:01","niceShareDate":"2020-03-25 00:56","origin":"","prefix":"","projectLink":"","publishTime":1591491714000,"selfVisible":0,"shareDate":1585068987000,"shareUser":"","superChapterId":440,"superChapterName":"问答","tags":[{"name":"本站发布","url":"/article/list/0?cid=440"},{"name":"问答","url":"/wenda"}],"title":"每日一问 | Fragment 是如何被存储与恢复的？  有更新","type":0,"userId":2,"visible":1,"zan":12},{"apkLink":"","audit":1,"author":"鸿洋","canEdit":false,"chapterId":408,"chapterName":"鸿洋","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":13812,"link":"https://mp.weixin.qq.com/s/zwQOc0fC7KI5RlRjFBpxvA","niceDate":"2020-06-06 00:00","niceShareDate":"1天前","origin":"","prefix":"","projectLink":"","publishTime":1591372800000,"selfVisible":0,"shareDate":1591626969000,"shareUser":"","superChapterId":408,"superChapterName":"公众号","tags":[{"name":"公众号","url":"/wxarticle/list/408/1"}],"title":"ViewBinding 实战，递进优雅的写波代码","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"","canEdit":false,"chapterId":502,"chapterName":"自助","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":13778,"link":"https://juejin.im/post/5ed75d6d6fb9a047ff1ab407","niceDate":"2020-06-05 17:27","niceShareDate":"2020-06-05 17:27","origin":"","prefix":"","projectLink":"","publishTime":1591349275000,"selfVisible":0,"shareDate":1591349275000,"shareUser":"winlee28","superChapterId":494,"superChapterName":"广场Tab","tags":[],"title":"Navigation源码解析及自定义FragmentNavigator详解","type":0,"userId":25211,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"","canEdit":false,"chapterId":76,"chapterName":"项目架构","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":13772,"link":"https://juejin.im/post/5ecb4174f265da7708476f12","niceDate":"2020-06-05 09:06","niceShareDate":"2020-06-05 08:43","origin":"","prefix":"","projectLink":"","publishTime":1591319174000,"selfVisible":0,"shareDate":1591317820000,"shareUser":"鸿洋","superChapterId":81,"superChapterName":"热门专题","tags":[],"title":"我是怎么把业务代码越写越复杂的 | MVP - MVVM - Clean Architecture","type":0,"userId":2,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"","canEdit":false,"chapterId":169,"chapterName":"gradle","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":13773,"link":"https://www.jianshu.com/p/8093e9f64d8b","niceDate":"2020-06-05 09:05","niceShareDate":"2020-06-05 08:48","origin":"","prefix":"","projectLink":"","publishTime":1591319158000,"selfVisible":0,"shareDate":1591318132000,"shareUser":"鸿洋","superChapterId":60,"superChapterName":"开发环境","tags":[],"title":"WMRouter源码解析之Transform","type":0,"userId":2,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"","canEdit":false,"chapterId":232,"chapterName":"入门及知识点","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":13771,"link":"https://juejin.im/post/5ed72ba5f265da76d66c2a2f","niceDate":"2020-06-05 08:41","niceShareDate":"2020-06-05 08:40","origin":"","prefix":"","projectLink":"","publishTime":1591317700000,"selfVisible":0,"shareDate":1591317624000,"shareUser":"goweii","superChapterId":232,"superChapterName":"Kotlin","tags":[],"title":"重学 Kotlin &mdash;&mdash; object，史上更 &ldquo;快&rdquo; 单例 ？","type":0,"userId":20382,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"code小生","canEdit":false,"chapterId":414,"chapterName":"code小生","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":13786,"link":"https://mp.weixin.qq.com/s/jFr7nvkSkKkmcxKx6xBieQ","niceDate":"2020-06-05 00:00","niceShareDate":"2020-06-05 23:29","origin":"","prefix":"","projectLink":"","publishTime":1591286400000,"selfVisible":0,"shareDate":1591370976000,"shareUser":"","superChapterId":408,"superChapterName":"公众号","tags":[{"name":"公众号","url":"/wxarticle/list/414/1"}],"title":"Android 项目中 Loading 对话框的优化","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"郭霖","canEdit":false,"chapterId":409,"chapterName":"郭霖","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":13787,"link":"https://mp.weixin.qq.com/s/jd_4yC2lIm6_cWnNE7GpPA","niceDate":"2020-06-05 00:00","niceShareDate":"2020-06-05 23:29","origin":"","prefix":"","projectLink":"","publishTime":1591286400000,"selfVisible":0,"shareDate":1591370992000,"shareUser":"","superChapterId":408,"superChapterName":"公众号","tags":[{"name":"公众号","url":"/wxarticle/list/409/1"}],"title":"高仿马蜂窝旅游头像泡泡动画","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"","canEdit":false,"chapterId":252,"chapterName":"奇怪的Bug","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":13749,"link":"https://juejin.im/post/5ed65846f265da771c77f1bc","niceDate":"2020-06-04 00:17","niceShareDate":"2020-06-03 15:26","origin":"","prefix":"","projectLink":"","publishTime":1591201028000,"selfVisible":0,"shareDate":1591169166000,"shareUser":"逮虾户","superChapterId":135,"superChapterName":"项目必备","tags":[],"title":"一个一年没解决的ClassNotFoundException","type":0,"userId":63284,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"","canEdit":false,"chapterId":182,"chapterName":"JNI编程","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":13757,"link":"https://juejin.im/post/5ed73d1e6fb9a047dd275995","niceDate":"2020-06-04 00:16","niceShareDate":"2020-06-03 23:45","origin":"","prefix":"","projectLink":"","publishTime":1591200994000,"selfVisible":0,"shareDate":1591199150000,"shareUser":"鸿洋","superChapterId":182,"superChapterName":"JNI","tags":[],"title":"Android JNI和NDK学习（三）：动态注册","type":0,"userId":2,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"code小生","canEdit":false,"chapterId":414,"chapterName":"code小生","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":13768,"link":"https://mp.weixin.qq.com/s/Gj8fxXwQVAy7J9sOIp10xw","niceDate":"2020-06-04 00:00","niceShareDate":"2020-06-04 22:52","origin":"","prefix":"","projectLink":"","publishTime":1591200000000,"selfVisible":0,"shareDate":1591282366000,"shareUser":"","superChapterId":408,"superChapterName":"公众号","tags":[{"name":"公众号","url":"/wxarticle/list/414/1"}],"title":"Android MVP &amp;&amp; MVVM深度解析","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"鸿洋","canEdit":false,"chapterId":408,"chapterName":"鸿洋","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":13769,"link":"https://mp.weixin.qq.com/s/wWB5ENo3eQJH03OXvoup8w","niceDate":"2020-06-04 00:00","niceShareDate":"2020-06-04 22:53","origin":"","prefix":"","projectLink":"","publishTime":1591200000000,"selfVisible":0,"shareDate":1591282394000,"shareUser":"","superChapterId":408,"superChapterName":"公众号","tags":[{"name":"公众号","url":"/wxarticle/list/408/1"}],"title":"&ldquo;新技术&rdquo; 又又又又来了？","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"郭霖","canEdit":false,"chapterId":409,"chapterName":"郭霖","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":13770,"link":"https://mp.weixin.qq.com/s/kmvn637E56t1bae2jXgqfw","niceDate":"2020-06-04 00:00","niceShareDate":"2020-06-04 22:53","origin":"","prefix":"","projectLink":"","publishTime":1591200000000,"selfVisible":0,"shareDate":1591282409000,"shareUser":"","superChapterId":408,"superChapterName":"公众号","tags":[{"name":"公众号","url":"/wxarticle/list/409/1"}],"title":"Android秀翻天的操作&mdash;&mdash;使用协程进行网络请求","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"code小生","canEdit":false,"chapterId":414,"chapterName":"code小生","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":13752,"link":"https://mp.weixin.qq.com/s/YW1rGpH4vT5zmLMAS1T-mg","niceDate":"2020-06-03 00:00","niceShareDate":"2020-06-03 22:48","origin":"","prefix":"","projectLink":"","publishTime":1591113600000,"selfVisible":0,"shareDate":1591195706000,"shareUser":"","superChapterId":408,"superChapterName":"公众号","tags":[{"name":"公众号","url":"/wxarticle/list/414/1"}],"title":"如何解决 if&hellip;else 过多的问题","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"鸿洋","canEdit":false,"chapterId":408,"chapterName":"鸿洋","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":13753,"link":"https://mp.weixin.qq.com/s/eU25oon1I3MC-bCoiF9r8Q","niceDate":"2020-06-03 00:00","niceShareDate":"2020-06-03 22:48","origin":"","prefix":"","projectLink":"","publishTime":1591113600000,"selfVisible":0,"shareDate":1591195734000,"shareUser":"","superChapterId":408,"superChapterName":"公众号","tags":[{"name":"公众号","url":"/wxarticle/list/408/1"}],"title":"插件化技术的演进之路","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"郭霖","canEdit":false,"chapterId":409,"chapterName":"郭霖","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":13755,"link":"https://mp.weixin.qq.com/s/5U-nTNJp_7Z5J11Xyj17HA","niceDate":"2020-06-03 00:00","niceShareDate":"2020-06-03 22:49","origin":"","prefix":"","projectLink":"","publishTime":1591113600000,"selfVisible":0,"shareDate":1591195778000,"shareUser":"","superChapterId":408,"superChapterName":"公众号","tags":[{"name":"公众号","url":"/wxarticle/list/409/1"}],"title":"巧用MVVM搭建GitHub客户端","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"","canEdit":false,"chapterId":171,"chapterName":"binder","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":13736,"link":"https://juejin.im/post/5ed64bbde51d4578732e75a1","niceDate":"2020-06-02 21:56","niceShareDate":"2020-06-02 21:03","origin":"","prefix":"","projectLink":"","publishTime":1591106210000,"selfVisible":0,"shareDate":1591103036000,"shareUser":"Zaylour","superChapterId":173,"superChapterName":"framework","tags":[],"title":"浅析Binder","type":0,"userId":9778,"visible":1,"zan":0}]
     * offset : 20
     * over : false
     * pageCount : 433
     * size : 20
     * total : 8641
     */

    private int curPage;
    private int offset;
    private boolean over;
    private int pageCount;
    private int size;
    private int total;
    private List<Article> datas;

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

    public List<Article> getDatas() {
        return datas;
    }

    public void setDatas(List<Article> datas) {
        this.datas = datas;
    }

    public static class Article {
        /**
         * apkLink :
         * audit : 1
         * author : 鸿洋
         * canEdit : false
         * chapterId : 408
         * chapterName : 鸿洋
         * collect : false
         * courseId : 13
         * desc :
         * descMd :
         * envelopePic :
         * fresh : false
         * id : 13813
         * link : https://mp.weixin.qq.com/s/jVRTFTiwTtr7P7vyAj8G7A
         * niceDate : 2天前
         * niceShareDate : 1天前
         * origin :
         * prefix :
         * projectLink :
         * publishTime : 1591545600000
         * selfVisible : 0
         * shareDate : 1591627043000
         * shareUser :
         * superChapterId : 408
         * superChapterName : 公众号
         * tags : [{"name":"公众号","url":"/wxarticle/list/408/1"}]
         * title : Android避坑指南，发现了一个极度不安全的操作
         * type : 0
         * userId : -1
         * visible : 1
         * zan : 0
         */

        private String apkLink;
        private int audit;
        private String author;
        private boolean canEdit;
        private int chapterId;
        private String chapterName;
        private boolean collect;
        private int courseId;
        private String desc;
        private String descMd;
        private String envelopePic;
        private boolean fresh;
        private int id;
        private String link;
        private String niceDate;
        private String niceShareDate;
        private String origin;
        private String prefix;
        private String projectLink;
        private long publishTime;
        private int selfVisible;
        private long shareDate;
        private String shareUser;
        private int superChapterId;
        private String superChapterName;
        private String title;
        private int type;
        private int userId;
        private int visible;
        private int zan;
        private boolean isTop;
        private List<TagsBean> tags;

        public String getApkLink() {
            return apkLink;
        }

        public void setApkLink(String apkLink) {
            this.apkLink = apkLink;
        }

        public int getAudit() {
            return audit;
        }

        public void setAudit(int audit) {
            this.audit = audit;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public boolean isCanEdit() {
            return canEdit;
        }

        public void setCanEdit(boolean canEdit) {
            this.canEdit = canEdit;
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

        public boolean isCollect() {
            return collect;
        }

        public void setCollect(boolean collect) {
            this.collect = collect;
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

        public String getDescMd() {
            return descMd;
        }

        public void setDescMd(String descMd) {
            this.descMd = descMd;
        }

        public String getEnvelopePic() {
            return envelopePic;
        }

        public void setEnvelopePic(String envelopePic) {
            this.envelopePic = envelopePic;
        }

        public boolean isFresh() {
            return fresh;
        }

        public void setFresh(boolean fresh) {
            this.fresh = fresh;
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

        public String getNiceShareDate() {
            return niceShareDate;
        }

        public void setNiceShareDate(String niceShareDate) {
            this.niceShareDate = niceShareDate;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getProjectLink() {
            return projectLink;
        }

        public void setProjectLink(String projectLink) {
            this.projectLink = projectLink;
        }

        public long getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(long publishTime) {
            this.publishTime = publishTime;
        }

        public int getSelfVisible() {
            return selfVisible;
        }

        public void setSelfVisible(int selfVisible) {
            this.selfVisible = selfVisible;
        }

        public long getShareDate() {
            return shareDate;
        }

        public void setShareDate(long shareDate) {
            this.shareDate = shareDate;
        }

        public String getShareUser() {
            return shareUser;
        }

        public void setShareUser(String shareUser) {
            this.shareUser = shareUser;
        }

        public int getSuperChapterId() {
            return superChapterId;
        }

        public void setSuperChapterId(int superChapterId) {
            this.superChapterId = superChapterId;
        }

        public String getSuperChapterName() {
            return superChapterName;
        }

        public void setSuperChapterName(String superChapterName) {
            this.superChapterName = superChapterName;
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

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public boolean isTop() {
            return isTop;
        }

        public void setTop(boolean top) {
            isTop = top;
        }

        public static class TagsBean {
            /**
             * name : 公众号
             * url : /wxarticle/list/408/1
             */

            private String name;
            private String url;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
