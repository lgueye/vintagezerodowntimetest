package org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean;

import com.google.common.collect.Maps;
import com.myjeeva.digitalocean.exception.DigitalOceanException;
import com.myjeeva.digitalocean.exception.RequestUnsuccessfulException;
import com.myjeeva.digitalocean.pojo.Droplet;
import com.myjeeva.digitalocean.pojo.Image;
import com.myjeeva.digitalocean.pojo.Keys;
import com.myjeeva.digitalocean.pojo.Region;
import org.assertj.core.util.Lists;
import org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean.domain.Box;
import org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean.domain.Request;
import org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean.domain.Specification;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(MockitoJUnitRunner.class)
public class DigitalOceanProviderCreateLineCommandTest {

    @Mock
    private DigitalOceanProviderCreateLineCommandHelper helper;
    @Mock
    private AnsibleInventoryWriter ansibleInventoryWriter;
    @Mock
    private AnsibleInventoryBuilder ansibleInventoryBuilder;
    @InjectMocks
    private DigitalOceanProviderCreateLineCommand underTest;

    @Test
    public void runShouldSucceed() throws RequestUnsuccessfulException, IOException, DigitalOceanException {
        // Given
        Request request = new Request();
        request.setRegion("ams2");
        request.setImage("ubuntu-16.04");
        final String env = "production";
        final Specification specification0 = new Specification(env, Lists.newArrayList());
        request.setSpecifications(Lists.newArrayList(specification0));
        underTest.setRequest(request);
        Map<String, List<String>> boxNamesBySize = Maps.newHashMap();
        Keys keys = mock(Keys.class);
        when(helper.getAvailableKeys()).thenReturn(keys);
        when(helper.boxNamesBySize(specification0)).thenReturn(boxNamesBySize);
        ArgumentCaptor<Region> regionArgumentCaptor = ArgumentCaptor.forClass(Region.class);
        ArgumentCaptor<Image> imageArgumentCaptor = ArgumentCaptor.forClass(Image.class);
        List<Droplet> digitalOceanRequests = Lists.newArrayList(mock(Droplet.class));
        when(helper.digitalOceanRequests(regionArgumentCaptor.capture(), imageArgumentCaptor.capture(), eq(env), eq(keys), eq(boxNamesBySize))).thenReturn(digitalOceanRequests);
        final List<Box> inventory = Lists.newArrayList(mock(Box.class));
        when(helper.boxDetails(digitalOceanRequests)).thenReturn(inventory);
        final String inventoryAsString = "inventory as string";
        when(ansibleInventoryBuilder.build(inventory)).thenReturn(inventoryAsString);
        // When
        underTest.run();

        // Then
        Region region = regionArgumentCaptor.getValue();
        Assert.assertEquals("ams2", region.getSlug());
        Image image = imageArgumentCaptor.getValue();
        Assert.assertEquals("ubuntu-16.04", image.getSlug());
        verify(ansibleInventoryWriter).write(inventoryAsString);
    }
}
