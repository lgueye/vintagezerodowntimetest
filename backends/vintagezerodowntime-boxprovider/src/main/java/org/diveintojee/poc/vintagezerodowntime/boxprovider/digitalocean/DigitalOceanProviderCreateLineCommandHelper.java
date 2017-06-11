package org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.myjeeva.digitalocean.exception.DigitalOceanException;
import com.myjeeva.digitalocean.exception.RequestUnsuccessfulException;
import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import com.myjeeva.digitalocean.pojo.*;
import org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean.domain.Box;
import org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean.domain.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.awaitility.Awaitility.await;

/**
 * @author louis.gueye@gmail.com
 */
@Component
public class DigitalOceanProviderCreateLineCommandHelper {

//    private static final Logger LOGGER = LoggerFactory.getLogger(DigitalOceanProviderCreateLineCommand.class);

    private DigitalOceanClient digitalOceanClient;

    @Autowired
    public void setDigitalOceanClient(DigitalOceanClient digitalOceanClient) {
        this.digitalOceanClient = digitalOceanClient;
    }

    Map<String, List<String>> boxNamesBySize(Specification specification) {
        Map<String, List<String>> boxesBySize = Maps.newHashMap();
        final String env = specification.getEnv();
        for (Box box : specification.getBoxes()) {
            String key = String.format("%s", box.getSize());
            if (!boxesBySize.containsKey(key)) {
                boxesBySize.put(key, Lists.newArrayList());
            }
            boxesBySize.get(key).add(String.format("%s-%s", env, box.getName()));
        }
        return boxesBySize;
    }

    List<Droplet> digitalOceanRequests(Region region, Image image, String env, Keys keys, Map<String, List<String>> boxNamesBySize) throws RequestUnsuccessfulException, DigitalOceanException {
        List<Droplet> result = Lists.newArrayList();
        for (Map.Entry<String, List<String>> entry : boxNamesBySize.entrySet()) {
            Droplet droplet = new Droplet();
            droplet.setKeys(keys.getKeys());
            droplet.setImage(image);
            droplet.setRegion(region);
            droplet.setEnableBackup(false);
            droplet.setEnableIpv6(true);
            droplet.setNames(entry.getValue());
            droplet.setSize(entry.getKey());
            droplet.setTags(Lists.newArrayList("vintagezerodowntime", env));
            result.add(droplet);
        }
        return result;
    }

    List<Box> boxDetails(List<Droplet> digitalOceanRequests) throws RequestUnsuccessfulException, DigitalOceanException {
        List<Box> details = Lists.newArrayList();
        for (Droplet droplet : digitalOceanRequests) {
            final Droplets responses = digitalOceanClient.createDroplets(droplet);
            for (Droplet response : responses.getDroplets()) {
                await().atMost(10000, TimeUnit.MILLISECONDS)
                        .pollDelay(1000, TimeUnit.MILLISECONDS)
                        .pollInterval(200, TimeUnit.MILLISECONDS)
                        .until(() -> {
                            final Droplet dropletInfo = digitalOceanClient.getDropletInfo(response.getId());
                            final List<Network> version4Networks = dropletInfo.getNetworks().getVersion4Networks();
                            return version4Networks != null && version4Networks.size() > 0;
                        });
                final Droplet dropletInfo = digitalOceanClient.getDropletInfo(response.getId());
                final List<Network> version4Networks = dropletInfo.getNetworks().getVersion4Networks();
                final Box box = new Box();
                box.setName(dropletInfo.getName());
                box.setIps(version4Networks.stream().map(Network::getIpAddress).collect(Collectors.toList()));
                details.add(box);
            }
        }
        return details;
    }

    Keys getAvailableKeys() throws RequestUnsuccessfulException, DigitalOceanException {
        return digitalOceanClient.getAvailableKeys(1);
    }
}
