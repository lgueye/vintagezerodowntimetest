package org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean;

import com.google.common.collect.Lists;
import com.myjeeva.digitalocean.exception.DigitalOceanException;
import com.myjeeva.digitalocean.exception.RequestUnsuccessfulException;
import com.myjeeva.digitalocean.pojo.Droplet;
import com.myjeeva.digitalocean.pojo.Image;
import com.myjeeva.digitalocean.pojo.Keys;
import com.myjeeva.digitalocean.pojo.Region;
import org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean.domain.Box;
import org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean.domain.Request;
import org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean.domain.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author louis.gueye@gmail.com
 */
@Component
public class DigitalOceanProviderCreateLineCommand {

//    private static final Logger LOGGER = LoggerFactory.getLogger(DigitalOceanProviderCreateLineCommand.class);

    private Request request;
    private DigitalOceanProviderCreateLineCommandHelper helper;
    private AnsibleInventoryBuilder ansibleInventoryBuilder;
    private AnsibleInventoryWriter ansibleInventoryWriter;

    public void setRequest(Request request) {
        this.request = request;
    }

    @Autowired
    public void setHelper(DigitalOceanProviderCreateLineCommandHelper helper) {
        this.helper = helper;
    }

    @Autowired
    public void setAnsibleInventoryBuilder(AnsibleInventoryBuilder ansibleInventoryBuilder) {
        this.ansibleInventoryBuilder = ansibleInventoryBuilder;
    }

    @Autowired
    public void setAnsibleInventoryWriter(AnsibleInventoryWriter ansibleInventoryWriter) {
        this.ansibleInventoryWriter = ansibleInventoryWriter;
    }

    /**
     * Implement curl -XPOST -H "Content-Type: application/json" -H "Authorization: Bearer $DO_API_TOKEN" -d '{"names":["production-lb"],"region":"fra1","size":"1gb","image":"ubuntu-16-04-x64","ssh_keys":["id1","id2"],"backups":false,"ipv6":true,"user_data":null,"private_networking":null,"tags":null}' "https://api.digitalocean.com/v2/droplets"
     * @throws RequestUnsuccessfulException
     * @throws DigitalOceanException
     */

    public void run() throws RequestUnsuccessfulException, DigitalOceanException, IOException {
        final Keys availableKeys = helper.getAvailableKeys();
        final Region region = new Region();
        region.setSlug(request.getRegion());
        Image image = new Image();
        image.setSlug(request.getImage());

        List<Box> inventory = Lists.newArrayList();
        for (Specification specification : request.getSpecifications()) {
            // For each env resolve boxes to create per size/plan
            Map<String, List<String>> boxesBySize = helper.boxNamesBySize(specification);
            final String env = specification.getEnv();
            List<Droplet> digitalOceanRequests  = helper.digitalOceanRequests(region, image, env, availableKeys, boxesBySize);
            List<Box> boxes = helper.boxDetails(digitalOceanRequests);
            inventory.addAll(boxes);
        }
        final String inventoryAsString = ansibleInventoryBuilder.build(inventory);
        ansibleInventoryWriter.write(inventoryAsString);
    }

}
