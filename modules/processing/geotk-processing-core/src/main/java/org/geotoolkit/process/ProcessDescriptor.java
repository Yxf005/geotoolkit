/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2009-2010, Geomatys
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

package org.geotoolkit.process;

import org.opengis.metadata.Identifier;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.util.InternationalString;

/**
 * Description of a process input and output parameters.
 *
 * @author Johann Sorel (Geomatys)
 * @module pending
 */
public interface ProcessDescriptor {

    /**
     * Hold the general information about this factory.
     * The authority property shall match the factory identification.
     */
    Identifier getName();

    /**
     * General description of this process.
     * @return InternationalString or null
     */
    InternationalString getAbstract();

    /**
     * Input parameters description.
     */
    ParameterDescriptorGroup getInputDescriptor();

    /**
     * Output parameters description.
     */
    ParameterDescriptorGroup getOutputDescriptor();

    /**
     * Create a process.
     * @return Process
     * @throws IllegalArgumentException if name is not part of this factory
     */
    Process createProcess();
}
