/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2004-2012, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2009-2012, Geomatys
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
package org.geotoolkit.referencing.factory.epsg;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;
import java.util.LinkedHashMap;
import java.util.Map;
import net.jcip.annotations.ThreadSafe;

import org.geotoolkit.factory.Hints;


/**
 * An EPSG factory for the database generated by ANSI-compatible SQL scripts. This class overrides
 * {@link #adaptSQL(String)} in order to translate SQL statements from MS-Access dialect to ANSI
 * dialect. By default, the translated SQL statements use the table and field names in the Data
 * Description Language (DDL) scripts provided by EPSG to create the schema for the database.
 * Subclasses can changes this default behavior by modifying the {@link #toANSI} content in their
 * constructor.
 *
 * @author Rueben Schulz (UBC)
 * @author Martin Desruisseaux (IRD)
 * @author Didier Richard (IGN)
 * @author John Grange
 * @version 3.05
 *
 * @since 2.0
 * @module
 */
@ThreadSafe
public class AnsiDialectEpsgFactory extends DirectEpsgFactory {
    /**
     * Table names using as "sentinel value" for detecting the presence of an EPSG database.
     * This array lists different possible names for the same table. The first entry must be
     * the MS-Access name. Other names may be in any order. They will be tried in reverse order.
     */
    private static final String[] SENTINAL = {
        "Coordinate Reference System",
        "coordinatereferencesystem",
        "epsg_coordinatereferencesystem"
    };

    /**
     * The prefix in table names.
     */
    static final String TABLE_PREFIX = "epsg_";

    /**
     * Table names. The left side (elements at even index) contains the names in the MS-Access
     * database, while the right side (elements at odd index) contains the names in the SQL scripts.
     */
    static final String[] ACCESS_TO_ANSI = {
        "[Alias]",                                  "epsg_alias",
        "[Area]",                                   "epsg_area",
        "[Coordinate Axis]",                        "epsg_coordinateaxis",
        "[Coordinate Axis Name]",                   "epsg_coordinateaxisname",
        "[Coordinate_Operation]",                   "epsg_coordoperation",
        "[Coordinate_Operation Method]",            "epsg_coordoperationmethod",
        "[Coordinate_Operation Parameter]",         "epsg_coordoperationparam",
        "[Coordinate_Operation Parameter Usage]",   "epsg_coordoperationparamusage",
        "[Coordinate_Operation Parameter Value]",   "epsg_coordoperationparamvalue",
        "[Coordinate_Operation Path]",              "epsg_coordoperationpath",
        "[Coordinate Reference System]",            "epsg_coordinatereferencesystem",
        "[Coordinate System]",                      "epsg_coordinatesystem",
        "[Datum]",                                  "epsg_datum",
        "[Ellipsoid]",                              "epsg_ellipsoid",
        "[Naming System]",                          "epsg_namingsystem",
        "[Prime Meridian]",                         "epsg_primemeridian",
        "[Supersession]",                           "epsg_supersession",
        "[Unit of Measure]",                        "epsg_unitofmeasure",
        "[Version History]",                        "epsg_versionhistory",
        "[Change]",                                 "epsg_change",
        "[Deprecation]",                            "epsg_deprecation",
        "[ORDER]",                                  "coord_axis_order" // a field in epsg_coordinateaxis
    };

    /**
     * Maps the MS-Access names to ANSI names. Keys are MS-Access names including bracket.
     * Values are ANSI names. Keys and values are case-sensitive. The default content of
     * this map is:
     *
     * <blockquote><nobr><table cellspacing=0 cellpadding=0>
     * <tr><th>MS-Access name</th>                                        <th>ANSI name</th></tr>
     * <tr><td>{@code [Alias]}&nbsp;</td>                                 <td>{@code epsg_alias}</td></tr>
     * <tr><td>{@code [Area]}&nbsp;</td>                                  <td>{@code epsg_area}</td></tr>
     * <tr><td>{@code [Coordinate Axis]}&nbsp;</td>                       <td>{@code epsg_coordinateaxis}</td></tr>
     * <tr><td>{@code [Coordinate Axis Name]}&nbsp;</td>                  <td>{@code epsg_coordinateaxisname}</td></tr>
     * <tr><td>{@code [Coordinate_Operation]}&nbsp;</td>                  <td>{@code epsg_coordoperation}</td></tr>
     * <tr><td>{@code [Coordinate_Operation Method]}&nbsp;</td>           <td>{@code epsg_coordoperationmethod}</td></tr>
     * <tr><td>{@code [Coordinate_Operation Parameter]}&nbsp;</td>        <td>{@code epsg_coordoperationparam}</td></tr>
     * <tr><td>{@code [Coordinate_Operation Parameter Usage]}&nbsp;</td>  <td>{@code epsg_coordoperationparamusage}</td></tr>
     * <tr><td>{@code [Coordinate_Operation Parameter Value]}&nbsp;</td>  <td>{@code epsg_coordoperationparamvalue}</td></tr>
     * <tr><td>{@code [Coordinate_Operation Path]}&nbsp;</td>             <td>{@code epsg_coordoperationpath}</td></tr>
     * <tr><td>{@code [Coordinate Reference System]}&nbsp;</td>           <td>{@code epsg_coordinatereferencesystem}</td></tr>
     * <tr><td>{@code [Coordinate System]}&nbsp;</td>                     <td>{@code epsg_coordinatesystem}</td></tr>
     * <tr><td>{@code [Datum]}&nbsp;</td>                                 <td>{@code epsg_datum}</td></tr>
     * <tr><td>{@code [Naming System]}&nbsp;</td>                         <td>{@code epsg_namingsystem}</td></tr>
     * <tr><td>{@code [Ellipsoid]}&nbsp;</td>                             <td>{@code epsg_ellipsoid}</td></tr>
     * <tr><td>{@code [Prime Meridian]}&nbsp;</td>                        <td>{@code epsg_primemeridian}</td></tr>
     * <tr><td>{@code [Supersession]}&nbsp;</td>                          <td>{@code epsg_supersession}</td></tr>
     * <tr><td>{@code [Unit of Measure]}&nbsp;</td>                       <td>{@code epsg_unitofmeasure}</td></tr>
     * <tr><td>{@code [ORDER]}&nbsp;</td>                                 <td>{@code coord_axis_order}</td></tr>
     * </table></nobr></blockquote>
     *
     * Subclasses can modify this map in their constructor in order to provide a different
     * mapping.
     */
    protected final Map<String,String> toANSI;

    /**
     * The schema sets by the last call to {@code #setSchema}, or {@code null} if none.
     */
    private String schema;

    /**
     * Creates a factory using the given connection. The connection is
     * {@linkplain Connection#close() closed} when this factory is
     * {@linkplain #dispose(boolean) disposed}.
     * <p>
     * <b>Note:</b> we recommend to avoid keeping the connection open for a long time. An easy
     * way to get the connection created only when first needed and closed automatically after
     * a short timeout is to instantiate this {@code AnsiDialectEpsgFactory} class only in a
     * {@link org.geotoolkit.referencing.factory.ThreadedAuthorityFactory}. This approach also
     * gives concurrency and caching services in bonus.
     *
     * @param userHints The underlying factories used for objects creation,
     *        or {@code null} for the default ones.
     * @param connection The connection to the underlying EPSG database.
     */
    public AnsiDialectEpsgFactory(final Hints userHints, final Connection connection) {
        super(userHints, connection);
        toANSI = new LinkedHashMap<>();
        for (int i=0; i<ACCESS_TO_ANSI.length; i++) {
            toANSI.put(ACCESS_TO_ANSI[i], ACCESS_TO_ANSI[++i]);
        }
    }

    /**
     * Constructs an authority factory using an existing map.
     */
    AnsiDialectEpsgFactory(Hints userHints, Connection connection, Map<String,String> toANSI) {
        super(userHints, connection);
        this.toANSI = toANSI;
    }

    /**
     * Returns {@code true} if the EPSG database seems to exists. This method
     * looks for the sentinel table documented in the {@link #autoconfig} method.
     */
    static boolean exists(final DatabaseMetaData metadata, final String schema) throws SQLException {
        final ResultSet result = metadata.getTables(null, schema, null, new String[] {"TABLE"});
        while (result.next()) {
            final String table = result.getString("TABLE_NAME");
            for (final String candidate : SENTINAL) {
                if (candidate.equalsIgnoreCase(table)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Invokes {@link #setSchema setSchema(...)} and {@link #useOriginalTableNames useOriginalTableNames(...)}
     * automatically according the {@linkplain DatabaseMetaData database metadata}. The default
     * implementation searches for a schema containing one of the following table:
     * <p>
     * <ul>
     *   <li>{@code "epsg_coordinatereferencesystem"}</li>
     *   <li>{@code "coordinatereferencesystem"}</li>
     *   <li>{@code "Coordinate Reference System"}</li>
     * </ul>
     * <p>
     * If one is found, then {@link #setSchema setSchema(...)} is invoked with the schema of
     * that table. Otherwise {@code setSchema(...)} is not invoked.
     *
     * @param  metadata The database metadata.
     * @throws SQLException If an error occurred while querying the database metadata.
     *
     * @since 3.00
     */
    final void autoconfig(final DatabaseMetaData metadata) throws SQLException {
        final String quote = metadata.getIdentifierQuoteString();
        for (int i=SENTINAL.length; --i>=0;) {
            final String table = SENTINAL[i];
            final String found;
            try (ResultSet result = metadata.getTables(null, schema, table, null)) {
                if (!result.next()) {
                    continue;
                }
                found = result.getString("TABLE_SCHEM");
            }
            if (found != null && !found.equals(schema)) {
                setSchema(found, quote, !table.startsWith(TABLE_PREFIX));
            }
            if (i == 0) {
                useOriginalTableNames(quote);
            }
            break;
        }
    }

    /**
     * Sets the schema where to search for the tables, and optionally removes the "{@code epsg_}"
     * prefix. Those two operations (setting a schema and removing the prefix) are proposed
     * together because the prefix is redundant with the schema.
     * <p>
     * This method inserts "<code><var>schema</var>.</code>" in front of every table names,
     * where <var>schema</var> is the value provided in argument. The "{@code epsg_}" prefix
     * is removed from the table names only if {@code removePrefix} is {@code true}.
     * <p>
     * This method can be invoked at most once, at construction time only.
     *
     * @param schema The database schema in which the EPSG tables are stored.
     * @param removePrefix {@code true} if the "{@code epsg_}" prefix should be removed from
     *        the table names.
     * @throws SQLException If the schema can not be set.
     *
     * @since 3.05
     */
    protected void setSchema(final String schema, final boolean removePrefix) throws SQLException {
        setSchema(schema, connection.getMetaData().getIdentifierQuoteString(), removePrefix);
    }

    /**
     * Implementation of {@link #setSchema(String, boolean)}.
     *
     * @param schema The database schema in which the EPSG tables are stored.
     * @param quote  The identifier quotes.
     * @param removePrefix {@code true} if the "{@code epsg_}" prefix should be removed from
     *        the table names.
     */
    final void setSchema(String schema, final String quote, final boolean removePrefix) {
        schema = schema.trim();
        if (schema.isEmpty()) {
            throw new IllegalArgumentException(schema);
        }
        String old = this.schema;
        old = (old != null) ? quote + old + quote + '.' : "";
        final String prefix = removePrefix ? TABLE_PREFIX : "";
        final StringBuilder buffer = new StringBuilder(quote).append(schema).append(quote).append('.');
        final int base = buffer.length();
        for (final Map.Entry<String,String> entry : toANSI.entrySet()) {
            if (isTableName(entry.getKey())) {
                String name = entry.getValue();
                if (name.startsWith(old)) {
                    name = name.substring(old.length());
                }
                if (name.startsWith(prefix)) {
                    name = name.substring(prefix.length());
                }
                buffer.setLength(base);
                name = buffer.append(name).toString();
                entry.setValue(name);
            }
        }
        this.schema = schema;
    }

    /**
     * Replaces the table names by the ones originally used in the MS-Access database. The new
     * names are quoted in order to allow the usage of space and mixed-case characters. For
     * example this method replaces the {@code epsg_coordinatereferencesystem} table name by
     * {@code "Coordinate Reference System"} (including the quotes).
     * <p>
     * If a schema has been previously set by a call to {@link #setSchema setSchema},
     * it will be preserved. It is better to set the schema (if desired) before to
     * invoke this method.
     * <p>
     * This method can be invoked at most once, at construction time only.
     *
     * @throws SQLException If the name of the tables to query can not be changed.
     *
     * @since 3.05
     */
    protected void useOriginalTableNames() throws SQLException {
        useOriginalTableNames(connection.getMetaData().getIdentifierQuoteString());
    }

    /**
     * Implementation of {@link #useOriginalTableNames()}.
     *
     * @param open The identifier quote (usually {@code "}).
     */
    private void useOriginalTableNames(final String quote) {
        for (final Map.Entry<String,String> entry : toANSI.entrySet()) {
            String original = entry.getKey();
            if (!isTableName(original)) {
                // Special case to skip (a column name, not a table name).
                continue;
            }
            int begin = original.indexOf('[');
            if (begin >= 0) {
                int end = original.lastIndexOf(']');
                if (end > begin) {
                    original = original.substring(begin+1, end);
                }
            }
            String name = entry.getValue();
            name = name.substring(0, name.indexOf('.') + 1);
            name = name + quote + original + quote;
            entry.setValue(name);
        }
    }

    /**
     * Returns {@code true} if the given name is the name of a table. The current
     * implementation assumes that the column name are all upper-case.
     *
     * @param name The table name in MS-Access dialect (i.e. a key of the {@linkplain #toANSI}.
     * @return {@code true} if the given name seems to be a table name.
     */
    private static boolean isTableName(final String name) {
        for (int i=name.length(); --i>=0;) {
            final char c = name.charAt(i);
            if (c != Character.toUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Modifies the given SQL string to be suitable for non MS-Access databases.
     * This replaces table and field names in the SQL with the new names in the
     * SQL DDL scripts provided with EPSG database.
     *
     * @param  statement The statement in MS-Access syntax.
     * @return The SQL statement in ANSI syntax.
     */
    @Override
    protected String adaptSQL(final String statement) {
        final StringBuilder modified = new StringBuilder(statement);
        for (final Map.Entry<String,String> entry : toANSI.entrySet()) {
            final String oldName = entry.getKey();
            final String newName = entry.getValue();
            /*
             * Replaces all occurrences of 'oldName' by 'newName'.
             */
            int start = 0;
            while ((start=modified.indexOf(oldName, start)) >= 0) {
                modified.replace(start, start+oldName.length(), newName);
                start += newName.length();
            }
        }
        return modified.toString();
    }
}
