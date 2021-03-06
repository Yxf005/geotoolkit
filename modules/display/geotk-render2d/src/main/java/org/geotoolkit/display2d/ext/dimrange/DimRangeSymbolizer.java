/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2009, Geomatys
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

package org.geotoolkit.display2d.ext.dimrange;

import java.util.Collections;
import java.util.Map;
import org.geotoolkit.style.AbstractSymbolizer;
import org.apache.sis.measure.MeasurementRange;
import org.apache.sis.measure.Units;
import org.opengis.filter.expression.Expression;
import org.opengis.style.ExtensionSymbolizer;
import org.opengis.style.StyleVisitor;

/**
 *
 * @author Johann Sorel (Geomatys)
 * @module
 */
public class DimRangeSymbolizer extends AbstractSymbolizer implements ExtensionSymbolizer{

    public static final String NAME = "DimRange";

    private final MeasurementRange dimRange;

    public DimRangeSymbolizer(final MeasurementRange range) {
        super(Units.POINT, null, "", null);
        this.dimRange = range;
    }

    public MeasurementRange getDimRange(){
        return dimRange;
    }

    @Override
    public String getExtensionName() {
        return NAME;
    }

    @Override
    public Map<String, Expression> getParameters() {
        return Collections.emptyMap();
    }

    @Override
    public Object accept(final StyleVisitor visitor, final Object data) {
        return visitor.visit(this, data);
    }

}

