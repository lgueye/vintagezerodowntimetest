package org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * @author louis.gueye@gmail.com
 */
public class AnsibleInventoryWriterTest {

    private AnsibleInventoryWriter underTest;

    @Before
    public void before() {
        underTest = new AnsibleInventoryWriter();
    }

    @Test
    public void write() throws Exception {
        // Given
        final String inventoryAsString = "any inventory";

        // When
        underTest.write(inventoryAsString);

        final File file = new File(Paths.get(System.getProperty("user.home"), ".digitalocean", "vintagezerodowntime").toString());
        // will be deleted at the end of the build
        file.deleteOnExit();
        String actual = Files.toString(file, Charsets.UTF_8);

        // Then
        assertEquals(inventoryAsString, actual);
    }

}
