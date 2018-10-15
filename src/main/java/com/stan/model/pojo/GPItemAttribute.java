package com.stan.model.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table(name = "gp_item_mapper")
public class GPItemAttribute {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Long attributeIndex;
    private Long typeid;
    private Long searchable;

    private Timestamp dateCreated;
    private Timestamp dateUpdated;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAttributeIndex() {
        return attributeIndex;
    }

    public void setAttributeIndex(Long attributeIndex) {
        this.attributeIndex = attributeIndex;
    }

    public Long getTypeid() {
        return typeid;
    }

    public void setTypeid(Long typeid) {
        this.typeid = typeid;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Timestamp dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Long getSearchable() {
        return searchable;
    }

    public void setSearchable(Long searchable) {
        this.searchable = searchable;
    }
}
