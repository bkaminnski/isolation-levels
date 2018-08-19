package com.hclc.isolationlevels.page247writeskew;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class WriteSkewDoctor {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public WriteSkewDoctor() {
    }

    public WriteSkewDoctor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WriteSkewDoctor that = (WriteSkewDoctor) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "WriteSkewDoctor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
