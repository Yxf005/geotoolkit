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
package org.geotoolkit.observation.xml.v100;

import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.geotoolkit.internal.sql.table.Entry;
import org.geotoolkit.observation.xml.Process;


/**
 * Implémentation d'une entrée représentant une {@linkplain Procedure procédure}.
 *
 * @version $Id: ProcessType.java 1559 2009-04-23 14:42:42Z glegal $
 * @author Antoine Hnawia
 * @module
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="Process")
public class ProcessType implements Process, Entry {
    /**
     * Pour compatibilités entre les enregistrements binaires de différentes versions.
     */
    private static final long serialVersionUID = -1370011712794916454L;
    
    /**
     * Le nom/identifiant du capteur.
     */
    @XmlAttribute(required= true, namespace = "http://www.w3.org/1999/xlink")
    private String href;
    
    /**
     * Constructeur vide utilisé par JAXB.
     */
    private ProcessType(){}
    
    /**
     * Construit une nouvelle procédure du nom spécifié.
     *
     * @param name Le nom de la procédure.
     */
    public ProcessType(final String name) {
        this.href = name;
        
    }

    /**
     * Retourne la reference du capteur.
     */
    @Override
    public String getHref() {
        return href;
    }

    /**
     * Retourne la reference du capteur.
     */
    public String getName() {
        return href;
    }

    @Override
    public String getIdentifier() {
        return href;
    }
    
     /**
     * Verifie si cette entree est identique a l'objet specifie.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof ProcessType) {
            final ProcessType that = (ProcessType) object;
            return Objects.equals(this.href, that.href);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.href != null ? this.href.hashCode() : 0);
        return hash;
    }
     /**
     * Retourne une chaine de charactere representant la procedure.
     */
    @Override
    public String toString() {
        return  " href=" + this.getHref();
    }
}
