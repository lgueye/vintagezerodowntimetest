package org.diveintojee.poc.vintagezerodowntime.dbupgrader;

import com.google.common.collect.Lists;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;

/**
 * @author louis.gueye@gmail.com
 */
public class DbUpgraderTest {

    private DbUpgrader underTest;

    @Before
    public void before() {
        underTest = new DbUpgrader();
    }

    @Test
    public void runShouldThrowIllegalArgumentExceptionWithEmptyUrl() throws Exception {
        // When
        try {
            underTest.run();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertEquals("Please provide a datasource url with pattern [jdbc:<db>:<driver-specific-url-format>]", expected.getMessage());
        } catch (Exception unexpected) {
            fail("Expected IllegalArgumentException, got " + unexpected);
        }
    }

    @Test
    public void runShouldThrowIllegalArgumentExceptionWithMalformedUrl() throws Exception {
        // When
        underTest.setUrl("any-url");
        try {
            underTest.run();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertEquals("Expected datasource url pattern [jdbc:<db>:<driver-specific-url-format>] (at least 3 tokens , got 1)", expected.getMessage());
        } catch (Exception unexpected) {
            fail("Expected IllegalArgumentException, got " + unexpected);
        }
    }

    @Test
    public void runShouldThrowIllegalArgumentExceptionWithUnsupportedDb() throws Exception {
        // When
        underTest.setUrl("token:db:any-url");
        try {
            underTest.run();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertEquals("Supported databases are " + Lists.newArrayList(SupportedDb.values()) + ", got db", expected.getMessage());
        } catch (Exception unexpected) {
            fail("Expected IllegalArgumentException, got " + unexpected);
        }
    }

    @Test
    public void runShouldNotClean() throws Exception {
        // Given
        final String url = "token:h2:any-url";
        final Flyway flyway = Mockito.mock(Flyway.class);
        underTest.setFlyway(flyway);
        underTest.setUrl(url);

        // When
        underTest.run();

        // Then
        verify(flyway).setLocations("db.h2.migrations");
        verify(flyway).migrate();
        Mockito.verifyNoMoreInteractions(flyway);
    }

    @Test
    public void runShouldNotMigrate() throws Exception {
        // Given
        final String url = "token:h2:any-url";
        final Flyway flyway = Mockito.mock(Flyway.class);
        underTest.setFlyway(flyway);
        underTest.setUrl(url);
        underTest.setClean(true);
        underTest.setMigrate(false);

        // When
        underTest.run();

        // Then
        verify(flyway).setLocations("db.h2.migrations");
        verify(flyway).clean();
        Mockito.verifyNoMoreInteractions(flyway);


    }

    @Test
    public void runShouldCleanAndMigrate() throws Exception {
        // Given
        final String url = "token:h2:any-url";
        final Flyway flyway = Mockito.mock(Flyway.class);
        underTest.setFlyway(flyway);
        underTest.setUrl(url);
        underTest.setClean(true);

        // When
        underTest.run();

        // Then
        verify(flyway).setLocations("db.h2.migrations");
        verify(flyway).clean();
        verify(flyway).migrate();
    }

}
