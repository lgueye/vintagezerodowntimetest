package org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean;

import org.assertj.core.util.Lists;
import org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean.domain.Box;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * Created by <a href="mailto:louis.gueye@domo-safety.com">Louis Gueye</a>.
 */
public class AnsibleInventoryBuilderTest {

    private AnsibleInventoryBuilder underTest;

    @Before
    public void before() {
        underTest = new AnsibleInventoryBuilder();
    }

    @Test
    public void buildAnsibleInventoryShouldSucceed() throws Exception {
        // Given
        Box b0 = new Box("production-db-master", null, Lists.newArrayList("217.50.50.50"));
        Box b1 = new Box("production-lb", null, Lists.newArrayList("217.50.50.51"));
        Box b2 = new Box("production-backends-01", null, Lists.newArrayList("217.50.50.52"));
        Box b3 = new Box("production-backends-02", null, Lists.newArrayList("217.50.50.53"));

        Box b4 = new Box("staging-db-master", null, Lists.newArrayList("217.50.50.54"));
        Box b5 = new Box("staging-lb", null, Lists.newArrayList("217.50.50.55"));
        Box b6 = new Box("staging-backends-01", null, Lists.newArrayList("217.50.50.56"));
        Box b7 = new Box("staging-backends-02", null, Lists.newArrayList("217.50.50.57"));

        String expected = new String(Files.readAllBytes(
                Paths.get(getClass().getClassLoader()
                        .getResource("ansible_inventory")
                        .toURI())));
        // When
        String actual = underTest.build(Lists.newArrayList(b0, b1, b2, b3, b4, b5, b6, b7));
        // Then
        assertEquals(expected, actual);
    }

}
