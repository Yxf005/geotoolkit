/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2004-2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2009-2011, Geomatys
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotoolkit.metadata.iso.extent;

import java.util.Date;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import net.jcip.annotations.ThreadSafe;

import org.opengis.geometry.Envelope;
import org.opengis.temporal.TemporalPrimitive;
import org.opengis.metadata.extent.TemporalExtent;
import org.opengis.referencing.operation.TransformException;

import org.geotoolkit.metadata.iso.MetadataEntity;
import org.geotoolkit.internal.referencing.ProxyForMetadata;


/**
 * Boundary enclosing the dataset, expressed as the closed set of
 * (<var>x</var>,<var>y</var>) coordinates of the polygon. The last
 * point replicates first point.
 *
 * @author Martin Desruisseaux (IRD, Geomatys)
 * @author Touraïvane (IRD)
 * @author Cédric Briançon (Geomatys)
 * @version 3.18
 *
 * @since 2.1
 * @module
 */
@ThreadSafe
@XmlType(name = "EX_TemporalExtent_Type")
@XmlRootElement(name = "EX_TemporalExtent")
@XmlSeeAlso(DefaultSpatialTemporalExtent.class)
public class DefaultTemporalExtent extends MetadataEntity implements TemporalExtent {
    /**
     * Serial number for inter-operability with different versions.
     */
    private static final long serialVersionUID = 3668140516657118045L;

    /**
     * The start date and time for the content of the dataset,
     * in milliseconds elapsed since January 1st, 1970. A value
     * of {@link Long#MIN_VALUE} means that this attribute is not set.
     */
    private long startTime = Long.MIN_VALUE;

    /**
     * The end date and time for the content of the dataset,
     * in milliseconds elapsed since January 1st, 1970. A value
     * of {@link Long#MIN_VALUE} means that this attribute is not set.
     */
    private long endTime = Long.MIN_VALUE;

    /**
     * The date and time for the content of the dataset.
     */
    private TemporalPrimitive extent;

    /**
     * Constructs an initially empty temporal extent.
     */
    public DefaultTemporalExtent() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @param source The metadata to copy, or {@code null} if none.
     *
     * @since 2.4
     */
    public DefaultTemporalExtent(final TemporalExtent source) {
        super(source);
        /*
         * The startTime and endTime attributes are not part of GeoAPI interfaces.
         * Consequently they are not copied by the above super-class constructor.
         */
    }

    /**
     * Constructs a temporal extent from the specified envelope. The envelope can be multi-dimensional,
     * in which case the {@linkplain Envelope#getCoordinateReferenceSystem() envelope CRS} must have
     * a vertical component.
     *
     * {@note This constructor is available only if the referencing module is on the classpath.}
     *
     * @param  envelope The envelope to use for initializing this temporal extent.
     * @throws UnsupportedOperationException if the referencing module is not on the classpath.
     * @throws TransformException if the envelope can't be transformed to a temporal extent.
     *
     * @see DefaultExtent#DefaultExtent(Envelope)
     * @see DefaultGeographicBoundingBox#DefaultGeographicBoundingBox(Envelope)
     * @see DefaultVerticalExtent#DefaultVerticalExtent(Envelope)
     *
     * @since 3.18
     */
    public DefaultTemporalExtent(final Envelope envelope) throws TransformException {
        ProxyForMetadata.getInstance().copy(envelope, this);
    }

    /**
     * Creates a temporal extent initialized to the specified values.
     *
     * @param startTime The start date and time for the content of the dataset.
     * @param endTime   The end date and time for the content of the dataset.
     */
    public DefaultTemporalExtent(final Date startTime, final Date endTime) {
        setStartTime(startTime);
        setEndTime  (endTime);
    }

    /**
     * The start date and time for the content of the dataset.
     *
     * @return The start time, or {@code null} if none.
     */
    public synchronized Date getStartTime() {
        final long time = startTime;
        return (time != Long.MIN_VALUE) ? new Date(time) : null;
    }

    /**
     * Sets the start date and time for the content of the dataset.
     *
     * @param newValue The new start time.
     */
    public synchronized void setStartTime(final Date newValue) {
        checkWritePermission();
        startTime = (newValue != null) ? newValue.getTime() : Long.MIN_VALUE;
    }

    /**
     * Returns the end date and time for the content of the dataset.
     *
     * @return The end time, or {@code null} if none.
     */
    public synchronized Date getEndTime() {
        final long time = endTime;
        return (time != Long.MIN_VALUE) ? new Date(time) : null;
    }

    /**
     * Sets the end date and time for the content of the dataset.
     *
     * @param newValue The new end time.
     */
    public synchronized void setEndTime(final Date newValue) {
        checkWritePermission();
        endTime = (newValue != null) ? newValue.getTime() : Long.MIN_VALUE;
    }

    /**
     * Returns the date and time for the content of the dataset.
     *
     * @since 2.4
     */
    @Override
    @XmlElement(name = "extent", required = true)
    public synchronized TemporalPrimitive getExtent() {
        return extent;
    }

    /**
     * Sets the date and time for the content of the dataset.
     *
     * @param newValue The new extent.
     *
     * @since 2.4
     */
    public synchronized void setExtent(final TemporalPrimitive newValue) {
        checkWritePermission();
        extent = newValue;
    }
}
