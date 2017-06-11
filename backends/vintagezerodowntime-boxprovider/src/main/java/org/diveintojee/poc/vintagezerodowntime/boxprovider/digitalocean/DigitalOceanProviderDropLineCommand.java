package org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean;

import com.myjeeva.digitalocean.exception.DigitalOceanException;
import com.myjeeva.digitalocean.exception.RequestUnsuccessfulException;
import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author louis.gueye@gmail.com
 */
@Component
public class DigitalOceanProviderDropLineCommand {

    private DigitalOceanClient digitalOceanClient;

    @Autowired
    public void setDigitalOceanClient(DigitalOceanClient digitalOceanClient) {
        this.digitalOceanClient = digitalOceanClient;
    }

    public void run() throws RequestUnsuccessfulException, DigitalOceanException {
        digitalOceanClient.deleteDropletByTagName("vintagezerodowntime");
    }
}
