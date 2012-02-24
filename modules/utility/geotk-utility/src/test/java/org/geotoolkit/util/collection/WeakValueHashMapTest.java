/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2001-2012, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2009-2012, Geomatys
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
package org.geotoolkit.util.collection;

import java.util.HashMap;
import java.util.Random;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link WeakHashSet}. A standard {@link HashMap} object is used for comparison purpose.
 *
 * @author Martin Desruisseaux (IRD)
 * @version 3.13
 *
 * @since 2.0
 */
public final strictfp class WeakValueHashMapTest {
    /**
     * The size of the test sets to be created.
     */
    private static final int SAMPLE_SIZE = 500;

    /**
     * Tests the {@link WeakValueHashMap} using strong references.
     * The tested {@link WeakValueHashMap} should behave like a
     * standard {@link Map} object.
     */
    @Test
    public void testStrongReferences() {
        final Random random = new Random();
        for (int pass=0; pass<4; pass++) {
            final WeakValueHashMap<Integer,Integer> weakMap = new WeakValueHashMap<>();
            final HashMap<Integer,Integer> strongMap = new HashMap<>();
            for (int i=0; i<SAMPLE_SIZE; i++) {
                final Integer key   = random.nextInt(SAMPLE_SIZE);
                final Integer value = random.nextInt(SAMPLE_SIZE);
                assertEquals("containsKey:", strongMap.containsKey(key), weakMap.containsKey(key));
                assertEquals("containsValue:", strongMap.containsValue(value), weakMap.containsValue(value));
                assertSame("get:", strongMap.get(key), weakMap.get(key));
                if (random.nextBoolean()) {
                    // Test addition.
                    assertSame("put:", strongMap.put(key, value), weakMap.put(key, value));
                } else {
                    // Test remove
                    assertSame("remove:", strongMap.remove(key), weakMap.remove(key));
                }
                assertEquals("equals:", strongMap, weakMap);
            }
        }
    }

    /**
     * Tests the {@link WeakValueHashMap} using weak references.
     * In this test, we have to keep in mind than some elements
     * in {@code weakMap} may disappear at any time.
     *
     * @throws InterruptedException If the test has been interrupted.
     */
    @Test
    public void testWeakReferences() throws InterruptedException {
        final Random random = new Random();
        for (int pass=0; pass<2; pass++) {
            final WeakValueHashMap<Integer,Integer> weakMap = new WeakValueHashMap<>();
            final HashMap<Integer,Integer> strongMap = new HashMap<>();
            for (int i=0; i<SAMPLE_SIZE; i++) {
                // We really want new instances here.
                final Integer key   = new Integer(random.nextInt(SAMPLE_SIZE));
                final Integer value = new Integer(random.nextInt(SAMPLE_SIZE));
                if (random.nextBoolean()) {
                    /*
                     * Tests addition.
                     */
                    final Integer   weakPrevious = weakMap  .put(key, value);
                    final Integer strongPrevious = strongMap.put(key, value);
                    if (weakPrevious == null) {
                        // If the element was not in the WeakValueHashMap (i.e. if the garbage
                        // collector has cleared it), then it can not been in HashMap neither
                        // (otherwise GC should not have cleared it).
                        assertNull("put:", strongPrevious);
                    } else {
                        assertNotSame(value, weakPrevious);
                    }
                    if (strongPrevious != null) {
                        // Note: If 'strongPrevious==null', 'weakPrevious' can not
                        //       be null since GC has not collected its entry yet.
                        assertSame("put:", strongPrevious, weakPrevious);
                    }
                } else {
                    /*
                     * Tests remove.
                     */
                    final Integer   weakPrevious = weakMap.get(key);
                    final Integer strongPrevious = strongMap.remove(key);
                    if (strongPrevious != null) {
                        assertSame("remove:", strongPrevious, weakPrevious);
                    }
                }
                assertTrue("containsAll:", weakMap.entrySet().containsAll(strongMap.entrySet()));
            }
            // Do our best to lets GC finish its work.
            for (int i=0; i<4; i++) {
                Thread.sleep(50);
                System.gc();
            }
            assertEquals("equals:", strongMap, weakMap);
        }
    }
}
