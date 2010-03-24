/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2007-2010, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2007-2010, Geomatys
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotoolkit.internal.sql.table;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.postgresql.ds.PGSimpleDataSource;

import org.geotoolkit.test.TestData;
import org.geotoolkit.internal.io.Installation;

import org.junit.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;


/**
 * Base classe for every tests requerying a connection to a coverage database.
 * This test requires a connection to a PostgreSQL database.
 *
 * @author Martin Desruisseaux (Geomatys)
 * @version 3.10
 *
 * @since 3.09 (derived from Seagis)
 */
public class CatalogTestBase {
    /**
     * The connection to the database.
     */
    private static SpatialDatabase database;

    /**
     * {@code true} if the {@code "rootDirectory"} property is defined.
     */
    private static boolean hasRootDirectory;

    /**
     * Creates the database when first needed.
     *
     * @return The database.
     */
    protected static synchronized SpatialDatabase getDatabase() {
        if (database == null) {
            final File pf = new File(Installation.TESTS.directory(true), "coverage-sql.properties");
            assumeTrue(pf.isFile()); // All tests will be skipped if the above resources is not found.
            final Properties properties;
            try {
                properties = TestData.readProperties(pf);
            } catch (IOException e) {
                throw new AssertionError(e); // This will cause a JUnit test failure.
            }
            final PGSimpleDataSource ds = new PGSimpleDataSource();
            ds.setServerName(properties.getProperty("server"));
            ds.setDatabaseName(properties.getProperty("database"));
            database = new SpatialDatabase(ds, properties);
            hasRootDirectory = properties.containsKey(ConfigurationKey.ROOT_DIRECTORY.key);
        }
        return database;
    }

    /**
     * Closes the connection to the database.
     *
     * @throws SQLException If an error occured while closing the connection.
     */
    @AfterClass
    public static synchronized void shutdown() throws SQLException {
        final Database db = database;
        database = null;
        if (db != null) {
            db.reset();
        }
    }

    /**
     * Subclasses shall invoke this method if the remaining code in a method requires
     * the image files. Typically, this method is invoked right before the first call
     * to {@link org.geotoolkit.coverage.sql.GridCoverageEntry#read}.
     *
     * @since 3.10
     */
    protected static synchronized void requireImageData() {
        assertNotNull("This method can be invoked only after getDatabase().", database);
        assumeTrue(hasRootDirectory);
    }

    /**
     * Tests the database connection.
     *
     * @throws SQLException if an SQL error occured.
     */
    @Test
    public void testConnection() throws SQLException {
        final LocalCache cache = getDatabase().getLocalCache();
        synchronized (cache) {
            final Connection connection = cache.connection();
            assertNotNull(connection);
            assertFalse(connection.isClosed());
            assertSame(connection, cache.connection());
        }
    }
}
