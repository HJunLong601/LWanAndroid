package com.hjl.core.net.bean;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author: long
 * description please add a description here
 * Date: 2021/5/14
 */
public class MavenItemBean {

    /**
     * artifactMap : {"viewpager":[{"artifact":"viewpager","content":"implementation 'androidx.viewpager:viewpager:1.0.0-alpha1'","date":null,"group":"androidx.viewpager","id":3953,"version":"1.0.0-alpha1"},{"artifact":"viewpager","content":"implementation 'androidx.viewpager:viewpager:1.0.0-alpha3'","date":null,"group":"androidx.viewpager","id":3954,"version":"1.0.0-alpha3"},{"artifact":"viewpager","content":"implementation 'androidx.viewpager:viewpager:1.0.0-beta01'","date":null,"group":"androidx.viewpager","id":3955,"version":"1.0.0-beta01"},{"artifact":"viewpager","content":"implementation 'androidx.viewpager:viewpager:1.0.0-rc01'","date":null,"group":"androidx.viewpager","id":3956,"version":"1.0.0-rc01"},{"artifact":"viewpager","content":"implementation 'androidx.viewpager:viewpager:1.0.0-rc02'","date":null,"group":"androidx.viewpager","id":3957,"version":"1.0.0-rc02"},{"artifact":"viewpager","content":"implementation 'androidx.viewpager:viewpager:1.0.0'","date":null,"group":"androidx.viewpager","id":3958,"version":"1.0.0"}]}
     * groupName : androidx.viewpager
     */

    private LinkedTreeMap<String,List<MavenVersionBean>> artifactMap;
    private String groupName;
    private ArrayList<Map.Entry<String, List<MavenVersionBean>>> artifactList;

    public LinkedTreeMap<String,List<MavenVersionBean>> getArtifactMap() {
        return artifactMap;
    }

    public void setArtifactMap(LinkedTreeMap<String,List<MavenVersionBean>> artifactMap) {
        this.artifactMap = artifactMap;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ArrayList<Map.Entry<String, List<MavenVersionBean>>> getArtifactList(){
        if (artifactList == null){
            artifactList = new ArrayList<>(artifactMap.entrySet());
        }
        return artifactList;
    }

    public static class MavenVersionBean {
        /**
         * artifact : viewpager
         * content : implementation 'androidx.viewpager:viewpager:1.0.0-alpha1'
         * date : null
         * group : androidx.viewpager
         * id : 3953
         * version : 1.0.0-alpha1
         */

        private String artifact;
        private String content;
        private Object date;
        private String group;
        private int id;
        private String version;

        public String getArtifact() {
            return artifact;
        }

        public void setArtifact(String artifact) {
            this.artifact = artifact;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Object getDate() {
            return date;
        }

        public void setDate(Object date) {
            this.date = date;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
