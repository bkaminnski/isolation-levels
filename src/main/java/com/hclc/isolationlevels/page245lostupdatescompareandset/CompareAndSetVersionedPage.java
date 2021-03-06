package com.hclc.isolationlevels.page245lostupdatescompareandset;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class CompareAndSetVersionedPage implements CompareAndSetPage {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String content;
    @Version
    private int version;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CompareAndSetVersionedPage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", version=" + version +
                '}';
    }
}
