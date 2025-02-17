/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.processor.resequencer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SequenceTest {

    private TestObject e1;
    private TestObject e2;
    private TestObject e3;

    private Sequence<TestObject> set;

    @BeforeEach
    public void setUp() {
        e1 = new TestObject(3);
        e2 = new TestObject(4);
        e3 = new TestObject(7);
        set = new Sequence<>(new TestComparator());
        set.add(e3);
        set.add(e1);
        set.add(e2);
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testPredecessor() {
        assertEquals(e1, set.predecessor(e2));
        assertNull(set.predecessor(e1));
        assertNull(set.predecessor(e3));
    }

    @Test
    public void testSuccessor() {
        assertEquals(e2, set.successor(e1));
        assertNull(set.successor(e2));
        assertNull(set.successor(e3));
    }

}
