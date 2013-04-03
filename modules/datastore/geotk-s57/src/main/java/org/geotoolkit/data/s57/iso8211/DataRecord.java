/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2013, Geomatys
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
package org.geotoolkit.data.s57.iso8211;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.geotoolkit.data.s57.iso8211.ISO8211Constants.*;
import static org.geotoolkit.data.s57.iso8211.ISO8211Utilities.*;

/**
 *
 * @author Johann Sorel (Geomatys)
 */
public class DataRecord {
    
    private final DataRecord ddr;
    
    protected int recordLength;
    protected byte interchangeLevel;
    protected byte leaderidentifier;
    protected byte extensionIndicator;
    protected byte version;
    protected byte applicationIndicator;
    protected int fieldControlLength;
    protected int areaAddress;
    protected byte[] charsetIndicator;
    
    //fields table structure
    private int fieldLengthSize;
    private int fieldPositionSize;
    private int fieldReserved;
    private int fieldSizeTag;
    
    //descriptions are complete only in the DDR
    private final List<FieldDescription> fieldDescriptions = new ArrayList<FieldDescription>();
    //fields are only in DR
    private final List<Field> fields = new ArrayList<Field>();

    public DataRecord() {
        this.ddr = null;
    }

    public DataRecord(DataRecord ddr) {
        this.ddr = ddr;
    }

    public DataRecord getDescriptor() {
        return ddr;
    }
    
    /**
     * @return the recordLength
     */
    public int getRecordLength() {
        return recordLength;
    }

    /**
     * @param recordLength the recordLength to set
     */
    public void setRecordLength(int recordLength) {
        this.recordLength = recordLength;
    }

    /**
     * @return the interchangeLevel
     */
    public byte getInterchangeLevel() {
        return interchangeLevel;
    }

    /**
     * @param interchangeLevel the interchangeLevel to set
     */
    public void setInterchangeLevel(byte interchangeLevel) {
        this.interchangeLevel = interchangeLevel;
    }

    /**
     * @return the leaderidentifier
     */
    public byte getLeaderidentifier() {
        return leaderidentifier;
    }

    /**
     * @param leaderidentifier the leaderidentifier to set
     */
    public void setLeaderidentifier(byte leaderidentifier) {
        this.leaderidentifier = leaderidentifier;
    }

    /**
     * @return the extensionIndicator
     */
    public byte getExtensionIndicator() {
        return extensionIndicator;
    }

    /**
     * @param extensionIndicator the extensionIndicator to set
     */
    public void setExtensionIndicator(byte extensionIndicator) {
        this.extensionIndicator = extensionIndicator;
    }

    /**
     * @return the version
     */
    public byte getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(byte version) {
        this.version = version;
    }

    /**
     * @return the applicationIndicator
     */
    public byte getApplicationIndicator() {
        return applicationIndicator;
    }

    /**
     * @param applicationIndicator the applicationIndicator to set
     */
    public void setApplicationIndicator(byte applicationIndicator) {
        this.applicationIndicator = applicationIndicator;
    }

    /**
     * @return the fieldControlLength
     */
    public int getFieldControlLength() {
        return fieldControlLength;
    }

    /**
     * @param fieldControlLength the fieldControlLength to set
     */
    public void setFieldControlLength(int fieldControlLength) {
        this.fieldControlLength = fieldControlLength;
    }

    /**
     * @return the areaAddress
     */
    public int getAreaAddress() {
        return areaAddress;
    }

    /**
     * @param areaAddress the areaAddress to set
     */
    public void setAreaAddress(int areaAddress) {
        this.areaAddress = areaAddress;
    }

    /**
     * @return the charsetIndicator
     */
    public byte[] getCharsetIndicator() {
        return charsetIndicator;
    }

    /**
     * @param charsetIndicator the charsetIndicator to set
     */
    public void setCharsetIndicator(byte[] charsetIndicator) {
        this.charsetIndicator = charsetIndicator;
    }

    /**
     * @return the fieldLengthSize
     */
    public int getFieldLengthSize() {
        return fieldLengthSize;
    }

    /**
     * @return the fieldPositionSize
     */
    public int getFieldPositionSize() {
        return fieldPositionSize;
    }

    /**
     * @return the fieldReserved
     */
    public int getFieldReserved() {
        return fieldReserved;
    }

    /**
     * @return the fieldSizeTag
     */
    public int getFieldSizeTag() {
        return fieldSizeTag;
    }
    
    public List<Field> getFields() {
        return fields;
    }

    public Field getField(String tag){
        for(Field f : fields){
            if(tag.equalsIgnoreCase(f.getType().getTag())){
                return f;
            }
        }
        return null;
    }
    
    /**
     * the root field, is the field not contained in any other fields.
     * @return Field
     */
    public Field getRootField(){
        FieldDescription root = getRootFieldDescription();
        return getField(root.getTag());
    }
    
    public List<FieldDescription> getFieldDescriptions() {
        return fieldDescriptions;
    }

    public FieldDescription getFieldDescription(String tag){
        for(FieldDescription f : fieldDescriptions){
            if(tag.equalsIgnoreCase(f.getTag())){
                return f;
            }
        }
        return null;
    }
    
    /**
     * the root field, is the field not contained in any other fields.
     * @return Field
     */
    public FieldDescription getRootFieldDescription(){
        final Set<FieldDescription> allSubFields = new HashSet<FieldDescription>();
        allSubFields.add(getFieldDescription("0000")); //field control field is not the root 
        for(FieldDescription f : fieldDescriptions){
            allSubFields.addAll(f.getFields());
        }
        for(FieldDescription f : fieldDescriptions){
            if(!allSubFields.contains(f)){
                //found the root
                return f;
            }
        }
        return null;
    }
    
    public void readDescription(DataInput ds) throws IOException{
        byte[] buffer = new byte[24];
        ds.readFully(buffer);
        
        //read header informations
        setRecordLength(Integer.parseInt(trimZeros(buffer, 0, 5)));
        setInterchangeLevel(buffer[5]);
        setLeaderidentifier(buffer[6]);
        setExtensionIndicator(buffer[7]);
        setVersion(buffer[8]);
        setApplicationIndicator(buffer[9]);
        final String str = trimZeros(buffer, 10, 12);
        if(Character.isDigit(str.charAt(0))){
            setFieldControlLength(Integer.parseInt(str));
        }else{
            //use ddr value
            setFieldControlLength(ddr.getFieldControlLength());
        }
        setAreaAddress(Integer.parseInt(trimZeros(buffer, 12, 17)));
        setCharsetIndicator(Arrays.copyOfRange(buffer, 17, 20));
        
        //read field definition
        fieldLengthSize = Integer.parseInt(new String(Arrays.copyOfRange(buffer, 20, 21)));
        fieldPositionSize = Integer.parseInt(new String(Arrays.copyOfRange(buffer, 21, 22)));
        fieldReserved = Integer.parseInt(new String(Arrays.copyOfRange(buffer, 22, 23)));
        fieldSizeTag = Integer.parseInt(new String(Arrays.copyOfRange(buffer, 23, 24)));
        
        //count number of fields
        final int entryLength = fieldLengthSize + fieldPositionSize + fieldSizeTag;
        final int nbDirectory = (getAreaAddress()-24-1)/entryLength;
        
        //read directory definitions
        buffer = new byte[entryLength];
        for(int i=0;i<nbDirectory;i++){
            ds.readFully(buffer);
            final FieldDescription field = new FieldDescription();
            field.setTag(new String(Arrays.copyOfRange(buffer, 0, fieldSizeTag)));
            field.setLength(Integer.parseInt(new String(Arrays.copyOfRange(buffer, fieldSizeTag, fieldSizeTag+fieldLengthSize))));
            field.setPosition(Integer.parseInt(new String(Arrays.copyOfRange(buffer, fieldSizeTag+fieldLengthSize, entryLength))));
            fieldDescriptions.add(field);            
        }        
        expect(ds,SFEND);
        
        //sort field descriptions
        Collections.sort(fieldDescriptions, new Comparator<FieldDescription>() {
            @Override
            public int compare(FieldDescription o1, FieldDescription o2) {
                return o1.getPosition() - o2.getPosition();
            }
        });
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        
        if(ddr!=null){
            //we are on a record
            sb.append("DR Record\n");
            sb.append(getRootField());
        }else{
            //we are on the data descriptive record
            sb.append("DDR Header\n");
            sb.append("-recordLength:").append(recordLength).append("\n");
            sb.append("-interchangeLevel:").append(interchangeLevel).append("\n");
            sb.append("-leaderidentifier:").append(leaderidentifier).append("\n");
            sb.append("-extensionIndicator:").append(extensionIndicator).append("\n");
            sb.append("-version:").append(version).append("\n");
            sb.append("-applicationIndicator:").append(applicationIndicator).append("\n");
            sb.append("-fieldControlLength:").append(fieldControlLength).append("\n");
            sb.append("-areaAddress:").append(areaAddress).append("\n");
            sb.append("-charsetIndicator:").append(charsetIndicator).append("\n");
            sb.append(getRootFieldDescription());
        }
        
        return sb.toString();
    }

}
