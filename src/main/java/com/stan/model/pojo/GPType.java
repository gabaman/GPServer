package com.stan.model.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;


@Table(name = "gp_type")
public class GPType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String typename;
    private Long typeprefix;
    private String image;
    private Long gameid;
    private Long isitem;
    private String searchimage;


    private Timestamp datecreated;
    private Timestamp dateupdated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public Long getTypeprefix() {
        return typeprefix;
    }

    public void setTypeprefix(Long typeprefix) {
        this.typeprefix = typeprefix;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getGameid() {
        return gameid;
    }

    public void setGameid(Long gameid) {
        this.gameid = gameid;
    }

    public Long getIsitem() {
        return isitem;
    }

    public void setIsitem(Long isitem) {
        this.isitem = isitem;
    }

    public String getSearchimage() {
        return searchimage;
    }

    public void setSearchimage(String searchimage) {
        this.searchimage = searchimage;
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
}
