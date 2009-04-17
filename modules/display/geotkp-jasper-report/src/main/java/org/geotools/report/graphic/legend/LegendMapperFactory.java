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

package org.geotools.report.graphic.legend;

import java.awt.Image;
import javax.swing.ImageIcon;
import net.sf.jasperreports.engine.JRRenderable;

import org.geotools.map.MapContext;
import org.geotools.report.JRMapperFactory;
import org.geotoolkit.util.SimpleInternationalString;
import org.opengis.util.InternationalString;

/**
 * Factory to create java2d legend mappers.
 *
 * @author Johann Sorel (Geomatys)
 */
public class LegendMapperFactory implements JRMapperFactory<JRRenderable,MapContext>{

    private static final ImageIcon ICON = new ImageIcon(LegendMapperFactory.class.getResource("/org/geotools/report/legend.png"));
    private static final InternationalString TITLE = new SimpleInternationalString("Legend");
    private static final String[] FAVORITES = new String[]{"GO2-Legend"};

    /**
     * {@inheritDoc }
     */
    @Override
    public Class<JRRenderable> getValueClass() {
        return JRRenderable.class;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public LegendMapper createMapper() {
        return new LegendMapper(this);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Image getIcon(int type) {
        return ICON.getImage();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public InternationalString getTitle() {
        return TITLE;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Class getFieldClass() {
        return Object.class;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Class<MapContext> getRecordClass() {
        return MapContext.class;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String[] getFavoritesFieldName() {
        return FAVORITES;
    }

}
