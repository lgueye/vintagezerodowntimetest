package org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.diveintojee.poc.vintagezerodowntime.boxprovider.SupportedProvider;

import java.util.List;

/**
 * @author louis.gueye@gmail.com
 */
public class Request {
    private SupportedProvider provider;
    private Intent intent;
    private String image;
    private String region;
    private List<Specification> specifications;

    public Request() {
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public List<Specification> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<Specification> specifications) {
        this.specifications = specifications;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public SupportedProvider getProvider() {
        return provider;
    }

    public void setProvider(SupportedProvider provider) {
        this.provider = provider;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return provider == request.provider &&
                intent == request.intent &&
                Objects.equal(image, request.image) &&
                Objects.equal(region, request.region) &&
                Objects.equal(specifications, request.specifications);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(provider, intent, image, region, specifications);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("provider", provider)
                .add("intent", intent)
                .add("image", image)
                .add("region", region)
                .add("specifications", specifications)
                .toString();
    }
}
