/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2011, Geomatys
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
package org.geotoolkit.wmts.model;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import org.geotoolkit.storage.coverage.GridMosaic;
import org.geotoolkit.storage.coverage.TileReference;
import org.apache.sis.geometry.GeneralDirectPosition;
import org.apache.sis.geometry.GeneralEnvelope;
import org.apache.sis.storage.DataStoreException;
import org.apache.sis.util.Classes;
import org.geotoolkit.storage.coverage.PyramidSet;
import org.geotoolkit.wmts.WMTSUtilities;
import org.geotoolkit.wmts.xml.v100.TileMatrix;
import org.geotoolkit.wmts.xml.v100.TileMatrixLimits;
import org.opengis.coverage.PointOutsideCoverageException;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;

/**
 *
 * @author Johann Sorel (Geomatys)
 * @module
 */
public class WMTSMosaic implements GridMosaic{
    
    private final String id = UUID.randomUUID().toString();
    private final WMTSPyramid pyramid;
    private final TileMatrix matrix;
    private final TileMatrixLimits limit;
    private final double scale;

    public WMTSMosaic(final WMTSPyramid pyramid, final TileMatrix matrix, final TileMatrixLimits limits) {
        this.pyramid = pyramid;
        this.matrix = matrix;
        this.limit = limits;
        this.scale = WMTSUtilities.unitsByPixel(pyramid.getMatrixset(), pyramid.getCoordinateReferenceSystem(), matrix);
    }
    
    public TileMatrix getMatrix() {
        return matrix;
    }

    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public WMTSPyramid getPyramid() {
        return pyramid;
    }

    @Override
    public DirectPosition getUpperLeftCorner() {
        final GeneralDirectPosition ul = new GeneralDirectPosition(getPyramid().getCoordinateReferenceSystem());
        ul.setOrdinate(0, matrix.getTopLeftCorner().get(0));
        ul.setOrdinate(1, matrix.getTopLeftCorner().get(1));
        return ul;
    }

    @Override
    public Dimension getGridSize() {
        return new Dimension(
                matrix.getMatrixWidth(),
                matrix.getMatrixHeight());
    }

    @Override
    public double getScale() {
        return scale;
    }

    @Override
    public Dimension getTileSize() {
        return new Dimension(
                matrix.getTileWidth(),
                matrix.getTileHeight());
    }
    
    @Override
    public Envelope getEnvelope(int row, int col) {
        final DirectPosition ul = getUpperLeftCorner();
        final double minX = ul.getOrdinate(0);
        final double maxY = ul.getOrdinate(1);
        final Dimension tileSize = getTileSize();
        final double scale = getScale();
        final double spanX = tileSize.width * scale;
        final double spanY = tileSize.height * scale;
        
        final GeneralEnvelope envelope = new GeneralEnvelope(
                getPyramid().getCoordinateReferenceSystem());
        envelope.setRange(0, minX + col*spanX, minX + (col+1)*spanX);
        envelope.setRange(1, maxY - (row+1)*spanY, maxY - row*spanY);
        
        return envelope;
    }
    
    @Override
    public Envelope getEnvelope() {
        final DirectPosition ul = getUpperLeftCorner();
        final double minX = ul.getOrdinate(0);
        final double maxY = ul.getOrdinate(1);
        final double spanX = getTileSize().width * getGridSize().width * getScale();
        final double spanY = getTileSize().height* getGridSize().height* getScale();
        
        final GeneralEnvelope envelope = new GeneralEnvelope(
                getPyramid().getCoordinateReferenceSystem());
        envelope.setRange(0, minX, minX + spanX);
        envelope.setRange(1, maxY - spanY, maxY );
        
        return envelope;
    }
    
    @Override
    public boolean isMissing(int col, int row) throws PointOutsideCoverageException {
        if (col < 0 || row < 0 || col > matrix.getMatrixWidth() || row > matrix.getMatrixHeight()) {
            throw new PointOutsideCoverageException("Queried tile position is outside matrix dimension.", new GeneralDirectPosition(col, row));
        }
        if(limit == null) return false;
        
        //limits are exclusive
        return  col < limit.getMinTileCol() 
             || col > limit.getMaxTileCol()
             || row < limit.getMinTileRow()
             || row > limit.getMaxTileRow();
    }

    @Override
    public TileReference getTile(int col, int row, Map hints) throws DataStoreException {
        if(hints==null) hints = new HashMap();
        if(!hints.containsKey(PyramidSet.HINT_FORMAT)) hints.put(PyramidSet.HINT_FORMAT,"image/png");
        
        return ((WMTSPyramidSet)getPyramid().getPyramidSet()).getTile(this, col, row, hints);
    }
 
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(Classes.getShortClassName(this));
        sb.append("   scale = ").append(getScale());
        sb.append("   gridSize[").append(getGridSize().width).append(',').append(getGridSize().height).append(']');
        sb.append("   tileSize[").append(getTileSize().width).append(',').append(getTileSize().height).append(']');
        sb.append("   ").append(getEnvelope());
        return sb.toString();
    }

    @Override
    public BlockingQueue<Object> getTiles(Collection<? extends Point> positions, Map hints) throws DataStoreException {
        if(hints==null) hints = new HashMap();
        if(!hints.containsKey(PyramidSet.HINT_FORMAT)){
            hints = new HashMap(hints);
            hints.put(PyramidSet.HINT_FORMAT,"image/png");
        }
        return ((WMTSPyramidSet)getPyramid().getPyramidSet()).getTiles(this, positions, hints);
    }

    @Override
    public Rectangle getDataArea() {
        return new Rectangle(0,0, getGridSize().width, getGridSize().height);
    }
}
