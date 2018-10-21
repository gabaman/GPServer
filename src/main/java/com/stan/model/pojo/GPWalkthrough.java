package com.stan.model.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;


@Table(name = "gp_walkthrough")
public class GPWalkthrough {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private Long locid;
    private Long typeid;
    private Long istext;
    private Long isfirst;

    private Timestamp datecreated;
    private Timestamp dateupdated;

    public Map<String,Object> toMap(String image){
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("title", title);
        map.put("image", image);
        map.put("locId", locid);
        map.put("typeId",typeid);
        map.put("isItem",1);

        return map;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getLocid() {
        return locid;
    }

    public void setLocid(Long locid) {
        this.locid = locid;
    }

    public Long getTypeid() {
        return typeid;
    }

    public void setTypeid(Long typeid) {
        this.typeid = typeid;
    }

    public Long getIstext() {
        return istext;
    }

    public void setIstext(Long istext) {
        this.istext = istext;
    }

    public Timestamp getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(Timestamp datecreated) {
        this.datecreated = datecreated;
    }

    public Timestamp getDateupdated() {
        return dateupdated;
    }

    public void setDateupdated(Timestamp dateupdated) {
        this.dateupdated = dateupdated;
    }

    public Long getIsfirst() {
        return isfirst;
    }

    public void setIsfirst(Long isfirst) {
        this.isfirst = isfirst;
    }
}
