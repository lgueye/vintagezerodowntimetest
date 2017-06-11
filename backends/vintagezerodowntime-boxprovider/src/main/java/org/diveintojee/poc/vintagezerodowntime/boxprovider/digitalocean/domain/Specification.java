package org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.List;

/**
 * @author louis.gueye@gmail.com
 */
public class Specification {

    private String env;
    private List<Box> boxes;

    protected Specification() {
    }

    public Specification(String env, List<Box> boxes) {
        this.env = env;
        this.boxes = boxes;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public List<Box> getBoxes() {
        return boxes;
    }

    public void setBoxes(List<Box> boxes) {
        this.boxes = boxes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Specification that = (Specification) o;
        return Objects.equal(env, that.env) &&
                Objects.equal(boxes, that.boxes);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(env, boxes);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("env", env)
                .add("boxes", boxes)
                .toString();
    }
}
