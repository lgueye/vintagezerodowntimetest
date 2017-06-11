package org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean;

import com.google.common.collect.Maps;
import com.myjeeva.digitalocean.exception.DigitalOceanException;
import com.myjeeva.digitalocean.exception.RequestUnsuccessfulException;
import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import com.myjeeva.digitalocean.pojo.*;
import org.assertj.core.util.Lists;
import org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean.domain.Box;
import org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean.domain.Specification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(MockitoJUnitRunner.class)
public class DigitalOceanProviderCreateLineCommandHelperTest {
    @Mock
    private DigitalOceanClient digitalOceanClient;

    @InjectMocks
    private DigitalOceanProviderCreateLineCommandHelper underTest;

    @Test
    public void boxNamesBySizeShouldSucceed() {
        // Given
        Box b0 = new Box("b0", "1go", Lists.newArrayList());
        Box b1 = new Box("b1", "2go", Lists.newArrayList());
        Box b2 = new Box("b2", "1go", Lists.newArrayList());
        Specification specification = new Specification("staging", Lists.newArrayList(b0, b1, b2));
        final Map<String, List<String>> expected = Maps.newHashMap();
        expected.putIfAbsent("1go", Lists.newArrayList("staging-b0","staging-b2"));
        expected.putIfAbsent("2go", Lists.newArrayList("staging-b1"));
        // When
        final Map<String, List<String>> actual = underTest.boxNamesBySize(specification);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void digitalOceanRequestsShouldSucceed() throws RequestUnsuccessfulException, DigitalOceanException {
        // Given
        Region region = mock(Region.class);
        Image image = mock(Image.class);
        String env = "local";
        Keys keys = mock(Keys.class);
        final List<Key> keyList = Lists.newArrayList(mock(Key.class));
        when(keys.getKeys()).thenReturn(keyList);
        final Map<String, List<String>> boxNamesBySize = Maps.newHashMap();
        final ArrayList<String> names0 = Lists.newArrayList("local-b0", "local-b2");
        final String size0 = "1go";
        boxNamesBySize.putIfAbsent(size0, names0);
        final ArrayList<String> names1 = Lists.newArrayList("local-b1");
        final String size1 = "2go";
        boxNamesBySize.putIfAbsent(size1, names1);
        final List<String> tags = Lists.newArrayList("vintagezerodowntime", env);

        // When
        final List<Droplet> actual = underTest.digitalOceanRequests(region, image, env, keys, boxNamesBySize);

        // Then
        // All this ugliness because equals was not implemented in the client library
        for (int i = 0; i < actual.size(); i++) {
            Droplet a = actual.get(i);
            assertEquals(keyList, a.getKeys());
            assertEquals(image, a.getImage());
            assertEquals(region, a.getRegion());
            assertFalse(a.getEnableBackup());
            assertTrue(a.getEnableIpv6());
            if (i == 0) {
                assertEquals(names0, a.getNames());
                assertEquals(size0, a.getSize());
            } else {
                assertEquals(names1, a.getNames());
                assertEquals(size1, a.getSize());
            }
            assertEquals(tags, a.getTags());
        }
    }

    @Test
    public void boxDetailsShouldSucceed() throws RequestUnsuccessfulException, DigitalOceanException {
        // Given
        Droplet req0 = mock(Droplet.class);
        Droplet req1 = mock(Droplet.class);
        Droplets res0 = mock(Droplets.class);
        final Droplet created0 = new Droplet();
        created0.setId(0);
        created0.setNetworks(new Networks());
        created0.getNetworks().setVersion4Networks(Lists.newArrayList(new Network()));
        final Droplet created1 = new Droplet();
        created1.setId(1);
        created1.setNetworks(new Networks());
        created1.getNetworks().setVersion4Networks(Lists.newArrayList(new Network()));
        when(res0.getDroplets()).thenReturn(Lists.newArrayList(created0, created1));
        Droplets res1 = mock(Droplets.class);
        final Droplet created2 = new Droplet();
        created2.setId(2);
        created2.setNetworks(new Networks());
        created2.getNetworks().setVersion4Networks(Lists.newArrayList(new Network()));
        when(res1.getDroplets()).thenReturn(Lists.newArrayList(created2));
        List<Droplet> requests = Lists.newArrayList(req0, req1);
        when(digitalOceanClient.createDroplets(req0)).thenReturn(res0);
        when(digitalOceanClient.createDroplets(req1)).thenReturn(res1);

        final Droplet info0 = new Droplet();
        info0.setName("name0");
        info0.setNetworks(new Networks());
        final Network network0 = new Network();
        network0.setIpAddress("0.0.0.0");
        info0.getNetworks().setVersion4Networks(Lists.newArrayList(network0));
        when(digitalOceanClient.getDropletInfo(0)).thenReturn(info0);

        final Droplet info1 = new Droplet();
        info1.setName("name1");
        info1.setNetworks(new Networks());
        final Network network1 = new Network();
        network1.setIpAddress("1.1.1.1");
        info1.getNetworks().setVersion4Networks(Lists.newArrayList(network1));
        when(digitalOceanClient.getDropletInfo(1)).thenReturn(info1);

        final Droplet info2 = new Droplet();
        info2.setName("name2");
        info2.setNetworks(new Networks());
        final Network network2 = new Network();
        network2.setIpAddress("2.2.2.2");
        info2.getNetworks().setVersion4Networks(Lists.newArrayList(network2));
        when(digitalOceanClient.getDropletInfo(2)).thenReturn(info2);

        Box box0 = new Box();
        box0.setName("name0");
        box0.setIps(Lists.newArrayList("0.0.0.0"));
        Box box1 = new Box();
        box1.setName("name1");
        box1.setIps(Lists.newArrayList("1.1.1.1"));
        Box box2 = new Box();
        box2.setName("name2");
        box2.setIps(Lists.newArrayList("2.2.2.2"));
        List<Box> expected = Lists.newArrayList(box0, box1, box2);

        // When
        final List<Box> actual = underTest.boxDetails(requests);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void getAvailableKeysShouldSucceed() throws RequestUnsuccessfulException, DigitalOceanException {
        // Given
        Keys expected = mock(Keys.class);
        when(digitalOceanClient.getAvailableKeys(1)).thenReturn(expected);

        // When
        final Keys actual = underTest.getAvailableKeys();

        // Then
        assertSame(expected, actual);
    }
}
