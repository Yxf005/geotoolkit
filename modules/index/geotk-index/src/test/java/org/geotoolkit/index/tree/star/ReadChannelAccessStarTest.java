/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2015, Geomatys
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
package org.geotoolkit.index.tree.star;

import java.io.File;
import java.io.IOException;
import org.geotoolkit.index.tree.AbstractTreeTest;
import org.geotoolkit.index.tree.FileTreeElementMapperTest;
import org.geotoolkit.index.tree.StoreIndexException;
import org.geotoolkit.internal.tree.TreeAccess;
import org.geotoolkit.internal.tree.TreeAccessByteArray;
import org.geotoolkit.internal.tree.TreeUtilities;
import org.geotoolkit.referencing.crs.PredefinedCRS;
import static org.junit.Assert.assertTrue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 *
 * @author rmarechal
 */
abstract class ReadChannelAccessStarTest extends AbstractTreeTest {
    
    /**
     * Create a Star RTree test suite adapted for Tree Access which use channel and byte array.
     * 
     * @param crs
     * @param insert
     * @throws IOException 
     */
    ReadChannelAccessStarTest(final CoordinateReferenceSystem crs, final boolean insert) throws IOException, StoreIndexException, ClassNotFoundException{
        super(crs);
        
//        final File inOutFile      = File.createTempFile("test", "tree", tempDir);
        final File treeMapperFile = File.createTempFile("test", "mapper", tempDir);
        
        // data insertion
        tEM  = new FileTreeElementMapperTest(crs, treeMapperFile);
        final TreeAccessByteArray ta = new TreeAccessByteArray(TreeUtilities.STAR_NUMBER, TreeUtilities.VERSION_NUMBER, 4, crs);
        tree = new StarRTree<double[]>(ta, tEM);
        
        // close 
        if (insert) insert();
        tree.close();
        tEM.close();
        assertTrue(tree.isClosed());
        assertTrue(tEM.isClosed());
        
        final byte[] data = ta.getData();
        
        // open Tree from already filled files.
        tEM  = new FileTreeElementMapperTest(treeMapperFile, crs);
        tree = new StarRTree(new TreeAccessByteArray(data, TreeUtilities.STAR_NUMBER, TreeUtilities.VERSION_NUMBER), tEM);
    }
}
