/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2007 - 2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008 - 2009, Johann Sorel
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
package org.geotoolkit.gui.swing.render2d.control;

import javax.swing.JButton;
import org.geotoolkit.gui.swing.render2d.JMap2D;

/**
 *
 * @author Johann Sorel (Geomatys)
 * @module
 */
public class JCRSButton extends JButton{

    private final CRSAction ACTION_CRS = new CRSAction();

    public JCRSButton(){
        super();
        setAction(ACTION_CRS);
    }

    /**
     * set the related Map2D
     * @param map2d : related Map2D
     */
    public void setMap(final JMap2D map2d) {
        ACTION_CRS.setMap(map2d);
    }

}
