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

package org.geotoolkit.data.wfs;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import org.geotoolkit.client.AbstractServerFactory;
import org.geotoolkit.client.ServerFactory;
import org.geotoolkit.data.AbstractDataStoreFactory;
import org.geotoolkit.data.DataStore;
import org.geotoolkit.metadata.iso.DefaultIdentifier;
import org.geotoolkit.metadata.iso.citation.DefaultCitation;
import org.geotoolkit.metadata.iso.identification.DefaultServiceIdentification;
import org.geotoolkit.parameter.DefaultParameterDescriptor;
import org.geotoolkit.parameter.DefaultParameterDescriptorGroup;
import org.geotoolkit.storage.DataStoreException;
import org.geotoolkit.wfs.xml.WFSVersion;
import org.opengis.metadata.Identifier;
import org.opengis.metadata.identification.Identification;
import org.opengis.parameter.*;

/**
 * Datastore factory for WFS client.
 *
 * @author Johann Sorel (Geomatys)
 * @module pending
 */
public class WFSDataStoreFactory extends AbstractDataStoreFactory implements ServerFactory{

    /** factory identification **/
    public static final String NAME = "wfs";
    public static final DefaultServiceIdentification IDENTIFICATION;
    static {
        IDENTIFICATION = new DefaultServiceIdentification();
        final Identifier id = new DefaultIdentifier(NAME);
        final DefaultCitation citation = new DefaultCitation(NAME);
        citation.setIdentifiers(Collections.singleton(id));
        IDENTIFICATION.setCitation(citation);
    }
    
    public static final ParameterDescriptor<String> IDENTIFIER = new DefaultParameterDescriptor<String>(
                    AbstractServerFactory.IDENTIFIER.getName().getCode(),
                    AbstractServerFactory.IDENTIFIER.getRemarks(), String.class,NAME,true);
    
    /**
     * Version, Mandatory.
     */
    public static final ParameterDescriptor<WFSVersion> VERSION =
            new DefaultParameterDescriptor<WFSVersion>("version","Server version",WFSVersion.class,WFSVersion.v110,true);
    /**
     * Optional -post request
     */
    public static final ParameterDescriptor<Boolean> POST_REQUEST =
            new DefaultParameterDescriptor<Boolean>("post request","post request",Boolean.class,false,false);    

    public static final ParameterDescriptorGroup PARAMETERS_DESCRIPTOR =
            new DefaultParameterDescriptorGroup("WFSParameters",
                IDENTIFIER, AbstractServerFactory.URL, VERSION, AbstractServerFactory.SECURITY, POST_REQUEST);

    @Override
    public Identification getIdentification() {
        return IDENTIFICATION;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public String getDescription() {
        return "OGC Web Feature Service datastore factory";
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ParameterDescriptorGroup getParametersDescriptor() {
        return PARAMETERS_DESCRIPTOR;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public DataStore createNew(final ParameterValueGroup params) throws DataStoreException {
        throw new DataStoreException("Can not create any new WFS DataStore");
    }

    @Override
    public WebFeatureServer create(Map<String, ? extends Serializable> params) throws DataStoreException {
        return (WebFeatureServer)super.create(params);
    }

    @Override
    public WebFeatureServer create(ParameterValueGroup params) throws DataStoreException {
        return new WebFeatureServer(params);
    }

}
