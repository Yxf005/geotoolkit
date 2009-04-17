/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.report;

import java.awt.Image;

import org.opengis.util.InternationalString;

/**
 * Factory to create JRMapper objects.
 * First generic is the value class return for jasper report.
 * Second generic is the record class that can handle the mapper object.
 *
 * @author Johann Sorel (Geomatys)
 */
public interface JRMapperFactory<V,C> {

    /**
     * Icon of this factory, used for user interfaces.
     */
    Image getIcon(int type);

    /**
     * Title of this factory, used for user interfaces.
     */
    InternationalString getTitle();

    /**
     * @return the JRField/JRParameter class that this mapper can handle.
     */
    Class getFieldClass();

    /**
     * @return the result class that those mapper will create.
     */
    Class<V> getValueClass();

    /**
     * @return the record class that those mapper can handle.
     */
    Class<C> getRecordClass();

    /**
     * Create a mapper.
     */
    JRMapper<V,C> createMapper();

    /**
     * @return an array of the favorite field names that this mapper is made for.
     * if a field has the correct class and his name is in this list then this mapping
     * solution should be the first one offerd in the user interface.
     */
    String[] getFavoritesFieldName();

}
