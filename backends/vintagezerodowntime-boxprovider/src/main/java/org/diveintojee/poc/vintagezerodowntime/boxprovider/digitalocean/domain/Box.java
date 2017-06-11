package org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.List;

/**
 * @author louis.gueye@gmail.com
 */
public class Box {
    private String name;
    private String size;
    private List<String> ips;

    public Box() {
    }

    private Box(String name, String size) {
        this.name = name;
        this.size = size;
    }
    public Box(String name, String size, List<String> ips) {
        this(name, size);
        this.ips = ips;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List<String> getIps() {
        return ips;
    }

    public void setIps(List<String> ips) {
        this.ips = ips;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Box box = (Box) o;
        return Objects.equal(name, box.name) &&
                Objects.equal(size, box.size) &&
                Objects.equal(ips, box.ips);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, size, ips);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("size", size)
                .add("ips", ips)
                .toString();
    }
}
