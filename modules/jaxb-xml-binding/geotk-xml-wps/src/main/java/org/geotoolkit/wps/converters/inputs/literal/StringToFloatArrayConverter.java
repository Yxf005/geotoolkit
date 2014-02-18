/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2012, Geomatys
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotoolkit.wps.converters.inputs.literal;

import java.util.LinkedList;
import java.util.List;
import org.geotoolkit.util.converter.NonconvertibleObjectException;
import org.geotoolkit.util.converter.ObjectConverter;

/**
 * Convert a String to an array of float. 
 * Double in String should be separated by a coma like this : "13.6, 5.4, 182.1, 88.0".
 * Return an empty array if source is null or empty.
 * 
 * @author Quentin Boileau
 */
public class StringToFloatArrayConverter implements ObjectConverter<String, float[]> {

    @Override
    public Class<? super String> getSourceClass() {
        return String.class;
    }

    @Override
    public Class<? extends float[]> getTargetClass() {
        return float[].class;
    }

    @Override
    public boolean hasRestrictions() {
        return false;
    }

    @Override
    public boolean isOrderPreserving() {
        return true;
    }

    @Override
    public boolean isOrderReversing() {
        return false;
    }

    @Override
    public float[] convert(final String source) throws NonconvertibleObjectException {
        
        if (source != null && !source.trim().isEmpty()) {
            
            final List<Float> integerList = new LinkedList<Float>();
            if (source.contains(",")) {
                final String[] sourceSplit = source.split(",");
                
                for (final String str : sourceSplit) {
                    try {
                        final Float f = Float.valueOf(str.trim());
                        if (f != null) {
                            integerList.add(f);
                        }
                    } catch (NumberFormatException ex) {
                        throw new NonconvertibleObjectException(ex.getMessage(), ex);
                    }
                }
            } else {
                 try {
                    final Float f = Float.valueOf(source.trim());
                    if (f != null) {
                        integerList.add(f);
                    }
                } catch (NumberFormatException ex) {
                    throw new NonconvertibleObjectException(ex.getMessage(), ex);
                }
            }
            
            if (!integerList.isEmpty()) {
                final float[] outArray = new float[integerList.size()];
                for (int i = 0; i < integerList.size(); i++) {
                    outArray[i] = integerList.get(i);
                }
                return outArray;
            } else {
                throw new NonconvertibleObjectException("Invalid source String : "+source);
            }
        }
        
        return new float[0];
    }
    
}