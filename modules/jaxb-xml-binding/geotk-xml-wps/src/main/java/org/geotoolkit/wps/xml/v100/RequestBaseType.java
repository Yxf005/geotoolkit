/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2008 - 2009, Geomatys
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
package org.geotoolkit.wps.xml.v100;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.geotoolkit.ows.xml.RequestBase;
import org.apache.sis.util.Version;


/**
 * WPS operation request base, for all WPS operations except GetCapabilities. In this XML encoding, no "request" parameter is included, since the element name specifies the specific operation.
 * 
 * <p>Java class for RequestBaseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RequestBaseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="service" use="required" type="{http://www.w3.org/2001/XMLSchema}string" fixed="WPS" />
 *       &lt;attribute name="version" use="required" type="{http://www.opengis.net/ows/1.1}VersionType" fixed="1.0.0" />
 *       &lt;attribute name="language" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 * @module
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestBaseType")
@XmlSeeAlso({
    Execute.class,
    DescribeProcess.class
})
public class RequestBaseType implements RequestBase {

    @XmlAttribute(required = true)
    protected String service;
    @XmlAttribute(required = true)
    protected String version;
    @XmlAttribute
    protected String language;

    public RequestBaseType() {
        
    }
    
    public RequestBaseType(String language) {
        this.language = language;
    }
    
    public RequestBaseType(String service, String language) {
        this.language = language;
        this.service = service;
    }
    
    /**
     * Gets the value of the service property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Override
    public String getService() {
        if (service == null) {
            return "WPS";
        } else {
            return service;
        }
    }

    /**
     * Sets the value of the service property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Override
    public void setService(final String value) {
        this.service = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Override
    public Version getVersion() {
        if (version == null) {
            return new Version("1.0.0");
        } else {
            return new Version(version);
        }
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Override
    public void setVersion(final String value) {
        this.version = value;
    }

    /**
     * Gets the value of the language property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the value of the language property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLanguage(final String value) {
        this.language = value;
    }

}
