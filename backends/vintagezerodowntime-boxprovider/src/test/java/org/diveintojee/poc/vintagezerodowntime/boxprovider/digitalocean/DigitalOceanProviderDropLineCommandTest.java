package org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean;

import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(MockitoJUnitRunner.class)
public class DigitalOceanProviderDropLineCommandTest {

    @Mock
    private DigitalOceanClient digitalOceanClient;
    @InjectMocks
    private DigitalOceanProviderDropLineCommand underTest;

    @Test
    public void run() throws Exception {
        // When
        underTest.run();

        // Then
        Mockito.verify(digitalOceanClient).deleteDropletByTagName("vintagezerodowntime");
    }

}
